package org.code.airportitemstorage.controller;

import org.code.airportitemstorage.library.dto.users.UserDto;
import org.code.airportitemstorage.library.dto.users.UserForRegisterDto;
import org.code.airportitemstorage.library.request.user.*;
import org.code.airportitemstorage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;
//用户获取用户信息
    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.GetUserById(id);
    }
//后台管理员查看所有用户
    @GetMapping("all")
    public GetAllUserResponse getAllUsers(GetAllUserRequest request) {
        return userService.GetAllUser(request);
    }
//用户更新用户信息
    @PostMapping("update")
    public int updateUser(@RequestBody UpdateUserRequest request) {
        return userService.UpdateUser(request);
    }
//管理员删除用户信息
    @PostMapping("delete")
    public int deleteUser(@RequestBody Long id) {
        return userService.DeleteUser(id);
    }
//用户注册
    @PostMapping("register")
    public int register(@RequestBody UserForRegisterDto user) throws Exception {
        return userService.RegisterUser(user);
    }
//登录
    @PostMapping("login")
    public UserLoginResponse login(@RequestBody UserLoginRequest request) throws Exception {
        return userService.LoginUser(request.username, request.password);
    }
//用户创建意见反馈
    @PostMapping("comment/add")
    public int addUserComment(@RequestBody AddUserCommentRequest request) {
        return userService.AddUserComment(request);
    }
//管理删除用户意见反馈
    @PostMapping("comment/delete")
    public int deleteUserComment(@RequestBody Long id) {
        return userService.DeleteUserComment(id);
    }
//管理员查看用户反馈列表
    @GetMapping("comment/list")
    public GetUserCommentsResponse getUserCommentList(GetUserCommentsRequest request) {
        return userService.GetUserCommentList(request);
    }
//用户登陆密码更新
    @PostMapping("loginPassword/update")
    public int updateUserLoginPassword(@RequestBody UpdateUserLoginPasswordRequest request) {
        return userService.UpdateUserLoginPassword(request);
    }
//用户支付密码更新
    @PostMapping("payPassword/update")
    public int updateUserPayPassword(@RequestBody UpdateUserPayPasswordRequest request) {
        return userService.UpdateUserPayPassword(request);
    }
//用户充值积分
    @PostMapping("recharge")
    public int rechargeUserPoint(@RequestBody Double point) {
        return userService.RechargeUserPoint(point);
    }
//凭证检查
    @PostMapping("voucher/check")
    public boolean checkUserOrderVoucher(@RequestBody String voucher) {
        return userService.CheckUserOrderVoucher(voucher);
    }
}
