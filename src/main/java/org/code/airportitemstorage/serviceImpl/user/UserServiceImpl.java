package org.code.airportitemstorage.serviceImpl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.code.airportitemstorage.library.OrderStorageStatus;
import org.code.airportitemstorage.library.RoleType;
import org.code.airportitemstorage.library.SourceSystem;
import org.code.airportitemstorage.library.dto.users.UserCommentDto;
import org.code.airportitemstorage.library.dto.users.UserDto;
import org.code.airportitemstorage.library.dto.users.UserForRegisterDto;
import org.code.airportitemstorage.library.entity.orders.Order;
import org.code.airportitemstorage.library.entity.orders.OrderPaySuccessRecord;
import org.code.airportitemstorage.library.entity.storageCabinet.UserVoucherNumber;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.library.entity.user.UserComment;
import org.code.airportitemstorage.library.entity.user.UserPoint;
import org.code.airportitemstorage.library.request.user.*;
import org.code.airportitemstorage.mapper.order.OrderMapper;
import org.code.airportitemstorage.mapper.order.OrderPaySuccessRecordMapper;
import org.code.airportitemstorage.mapper.users.UserCommentMapper;
import org.code.airportitemstorage.mapper.users.UserMapper;
import org.code.airportitemstorage.mapper.users.UserPointMapper;
import org.code.airportitemstorage.mapper.users.UserVoucherNumberMapper;
import org.code.airportitemstorage.service.OssService;
import org.code.airportitemstorage.service.user.UserService;
import org.code.airportitemstorage.util.EmailValidator;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserMapper userMapper;
    private final OssService ossService;
    private final UserPointMapper userPointMapper;
    private final UserCommentMapper userCommentMapper;
    private final HttpServletRequest _httpServletRequest;
    private final UserVoucherNumberMapper userVoucherNumberMapper;
    private final OrderMapper orderMapper;
    private final OrderPaySuccessRecordMapper orderPaySuccessRecordMapper;

    @Override
    public UserDto GetUserById(long id) {
        User user = userMapper.selectById(id);

        if (user == null) return null;

        var queryUserPointWrapper = new QueryWrapper<UserPoint>().eq("user_id", user.getId());
        UserPoint userPoint = userPointMapper.selectOne(queryUserPointWrapper);

        UserDto userDto = new UserDto(user);
        userDto.point = userPoint != null ? userPoint.getPoint() : 0;

        return userDto;
    }

    @Override
    public int UpdateUser(UpdateUserRequest request) {
        User user = userMapper.selectById(request.getUserId());

        if(user == null)return 0;

        String imageUrl = ossService.getImageUrl(request.avatar);

        user.setNickName(request.getNickName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setAvatar(imageUrl);

        return userMapper.updateById(user);
    }

    @Override
    public UserLoginResponse LoginUser(String username, String password) throws Exception {
        String sourceSystem = _httpServletRequest.getHeader("Source_System");
        UserLoginResponse response = new UserLoginResponse();

        var queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("account_name", username).eq(true, "password", password);

        User user = userMapper.selectList(queryWrapper).getFirst();

        if(user == null)throw new Exception("Please enter the correct username and password.");

        //如果是后台登录，检验用户是否是管理员
        if(sourceSystem.equals(SourceSystem.Backstage.toString()) && user.getRoleType() != RoleType.Admin) throw new Exception("The current user has no permission to login backend.");

        response.user = new UserDto(user);
        response.token = UUID.randomUUID().toString();

        return response;
    }

    @Override
    public int RegisterUser(UserForRegisterDto userForRegisterDto) throws Exception {
        if (userForRegisterDto == null)return 0;

        if(!EmailValidator.isValidEmail(userForRegisterDto.email))
            throw new Exception("Please enter the correct email.");

        var user = new User(userForRegisterDto);
        if(userMapper.insert(user) != 1) return 0;

        return 1;
    }

    @Override
    public GetAllUserResponse GetAllUser(GetAllUserRequest request) {
        var response = new GetAllUserResponse();
        List<UserDto> userList = new ArrayList<>();

        var page = new Page<User>(request.pageIndex, request.pageSize);

        var queryUserWrapper = new QueryWrapper<User>();

        if(request.getNickName() != null || request.getAccountName() != null) {
            queryUserWrapper
                    .eq("account_name", request.getAccountName()).or()
                    .eq("nick_name", request.getNickName());
        }

        Page<User> userPage = userMapper.selectPage(page, queryUserWrapper);

        if (userPage == null || userPage.getRecords().isEmpty())return response;

        var userIds = userPage.getRecords().stream().map(User::getId).toList();

        var queryUserPointWrapper = new QueryWrapper<UserPoint>();
        queryUserPointWrapper.in("user_id", userIds);

        List<UserPoint> userPoints = userPointMapper.selectList(queryUserPointWrapper);

        for (User user : userPage.getRecords()) {
            var userPoint = userPoints.stream()
                    .filter(x -> x.getUserId() == user.getId())
                    .findFirst().orElse(null);

            UserDto userDto = new UserDto(user);

            userDto.point = userPoint == null ? 0 : userPoint.getPoint();

            EnrichUserStorageInfo(userDto);

            userList.add(userDto);
        }

        response.users = userList;
        response.total = userPage.getTotal();

        return response;
    }

    @Override
    public int DeleteUser(long id) {
        User user = userMapper.selectById(id);

        var queryUserPointWrapper = new QueryWrapper<UserPoint>();
        queryUserPointWrapper.eq("user_id", id);
        UserPoint userPoint = userPointMapper.selectOne(queryUserPointWrapper);

        if(user == null)return 0;

        if(userPoint != null)
            userPointMapper.deleteById(userPoint.getId());

        return userMapper.deleteById(id);
    }

    @Override
    public int AddUserComment(AddUserCommentRequest request) {
        if(request.getComment().isEmpty() || request.getContactInfo().isEmpty())return 0;

        UserComment userComment = new UserComment();
        userComment.setUserId(request.getUserId());
        userComment.setComment(request.getComment());
        userComment.setContactInfo(request.getContactInfo());
        userComment.setDefaultCommentDate();

        return userCommentMapper.insert(userComment);
    }

    @Override
    public GetUserCommentsResponse GetUserCommentList(GetUserCommentsRequest request) {
        var response = new GetUserCommentsResponse();
        var userComments = new ArrayList<UserCommentDto>();

        var page = new Page<UserComment>(request.pageIndex, request.pageSize);

        var queryUserCommentWrapper = new QueryWrapper<UserComment>();
        if(request.id > 0){
            queryUserCommentWrapper.eq("id", request.getId());
        }

        Page<UserComment> userCommentPage = userCommentMapper.selectPage(page, queryUserCommentWrapper);

        var userIds = userCommentPage.getRecords().stream().map(UserComment::getUserId).toList();

        var queryUserWrapper = new QueryWrapper<User>();
        if(request.getAccountName() != null) {
            queryUserWrapper
                    .in("id", userIds)
                    .eq("account_name", request.getAccountName());
        }

        List<User> users = userMapper.selectList(queryUserWrapper);

        for (UserComment userComment : userCommentPage.getRecords()) {
            UserCommentDto userCommentDto = new UserCommentDto();

            User user = users.stream().filter(u -> u.getId() == userComment.getUserId()).findFirst().orElse(null);
            if(user == null)continue;

            userCommentDto.setId(userComment.getId());
            userCommentDto.setComment(userComment.getComment());
            userCommentDto.setCommentDate(userComment.getCommentDate());
            userCommentDto.setAccountName(user.getAccountName());
            userCommentDto.setPhone(user.getPhone());

            userComments.add(userCommentDto);
        }

        response.total = userCommentPage.getTotal();
        response.userComments = userComments;

        return response;
    }

    @Override
    public int DeleteUserComment(Long id) {
        UserComment userComment = userCommentMapper.selectById(id);

        if(userComment == null)return 0;

        return userCommentMapper.deleteById(id);
    }

    @Override
    public int UpdateUserLoginPassword(UpdateUserLoginPasswordRequest request) {
        User user = CheckUserAuthorization();

        if(!Objects.equals(user.getPassword(), request.getOldPassword()))return 0;

        user.setPassword(request.getNewPassword());

        return userMapper.updateById(user);
    }

    @Override
    public int UpdateUserPayPassword(UpdateUserPayPasswordRequest request) {
        User user = CheckUserAuthorization();

        if(!Objects.equals(user.getPayPassword(), request.getOldPassword()))return 0;

        user.setPayPassword(request.getNewPassword());

        return userMapper.updateById(user);
    }

    @Override
    public int RechargeUserPoint(double point) {
        User user = CheckUserAuthorization();

        var queryUserPointWrapper = new QueryWrapper<UserPoint>();
        queryUserPointWrapper.eq("user_id", user.getId());

        UserPoint userPoint = userPointMapper.selectOne(queryUserPointWrapper);

        if(userPoint == null){
            UserPoint newUserPoint = new UserPoint();
            newUserPoint.setUserId(user.getId());
            newUserPoint.setPoint(point);
            newUserPoint.setCreateDate(LocalDateTime.now());
            return userPointMapper.insert(newUserPoint);
        }

        userPoint.setPoint(userPoint.getPoint() + point);

        return userPointMapper.updateById(userPoint);
    }

    @Override
    public boolean CheckUserOrderVoucher(String voucher) {
        User user = CheckUserAuthorization();
        var queryWrapper = new QueryWrapper<UserVoucherNumber>();
        queryWrapper.eq("user_id", user.getId()).eq("voucher_number", voucher);
        UserVoucherNumber userVoucherNumber = userVoucherNumberMapper.selectOne(queryWrapper);


        return userVoucherNumber != null;
    }

    public User CheckUserAuthorization() {
        String userId = _httpServletRequest.getHeader("userId");
        String sourceSystem = _httpServletRequest.getHeader("Source_System");

        User user = userMapper.selectById(Long.valueOf(userId));

        if(user != null && (sourceSystem == null ||
                sourceSystem.equals(SourceSystem.WebClient.toString()) ||
                (sourceSystem.equals(SourceSystem.Backstage.toString()) && user.getRoleType() == RoleType.Admin)))
            return user;

        return null;
    }

    private void EnrichUserStorageInfo(UserDto user) {
        var queryOrderWrapper = new QueryWrapper<Order>();
        queryOrderWrapper.eq("user_id", user.id).eq("storage_status", OrderStorageStatus.Using);
        List<Order> orders = orderMapper.selectList(queryOrderWrapper);

        if(!orders.isEmpty()) user.isStored = true;

        var queryPaySuccessRecordWrapper = new QueryWrapper<OrderPaySuccessRecord>();
        queryPaySuccessRecordWrapper.eq("user_id", user.id);

        user.storageCount = orderPaySuccessRecordMapper.selectCount(queryPaySuccessRecordWrapper);
    }
}
