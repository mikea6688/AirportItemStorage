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

    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.GetUserById(id);
    }

    @GetMapping("all")
    public GetAllUserResponse getAllUsers(GetAllUserRequest request) {
        return userService.GetAllUser(request);
    }

    @PostMapping("update")
    public int updateUser(@RequestBody UpdateUserRequest request) {
        return userService.UpdateUser(request);
    }

    @PostMapping("delete")
    public int deleteUser(@RequestBody Long id) {
        return userService.DeleteUser(id);
    }

    @PostMapping("register")
    public int register(@RequestBody UserForRegisterDto user) throws Exception {
        return userService.RegisterUser(user);
    }

    @PostMapping("login")
    public UserLoginResponse login(@RequestBody UserLoginRequest request) throws Exception {
        return userService.LoginUser(request.username, request.password);
    }

    @PostMapping("comment/add")
    public int addUserComment(@RequestBody AddUserCommentRequest request) {
        return userService.AddUserComment(request);
    }

    @PostMapping("comment/delete")
    public int deleteUserComment(@RequestBody Long id) {
        return userService.DeleteUserComment(id);
    }

    @GetMapping("comment/list")
    public GetUserCommentsResponse getUserCommentList(GetUserCommentsRequest request) {
        return userService.GetUserCommentList(request);
    }

    @PostMapping("loginPassword/update")
    public int updateUserLoginPassword(@RequestBody UpdateUserLoginPasswordRequest request) {
        return userService.UpdateUserLoginPassword(request);
    }

    @PostMapping("payPassword/update")
    public int updateUserPayPassword(@RequestBody UpdateUserPayPasswordRequest request) {
        return userService.UpdateUserPayPassword(request);
    }

    @PostMapping("recharge")
    public int rechargeUserPoint(@RequestBody Double point) {
        return userService.RechargeUserPoint(point);
    }

    @PostMapping("voucher/check")
    public boolean checkUserOrderVoucher(@RequestBody String voucher) {
        return userService.CheckUserOrderVoucher(voucher);
    }
}
