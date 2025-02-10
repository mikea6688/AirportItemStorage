package org.code.airportitemstorage.service.user;

import org.code.airportitemstorage.library.dto.users.UserDto;
import org.code.airportitemstorage.library.dto.users.UserForRegisterDto;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.library.request.user.*;

import java.util.Optional;

public interface UserService {
    UserDto GetUserById(long id);

    int UpdateUser(UpdateUserRequest request);

    UserLoginResponse LoginUser(String username, String password) throws Exception;

    int RegisterUser(UserForRegisterDto user);

    GetAllUserResponse GetAllUser(GetAllUserRequest request);

    int DeleteUser(long id);

    int AddUserComment(AddUserCommentRequest request);

    GetUserCommentsResponse GetUserCommentList(GetUserCommentsRequest request);

    int DeleteUserComment(Long id);

    int UpdateUserLoginPassword(UpdateUserLoginPasswordRequest request);

    User CheckUserAuthorization();

    int UpdateUserPayPassword(UpdateUserPayPasswordRequest request);

    int RechargeUserPoint(double point);

    boolean CheckUserOrderVoucher(String voucher);
}

