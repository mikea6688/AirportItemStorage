package org.code.airportitemstorage.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.code.airportitemstorage.library.dto.users.UserDto;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.mapper.users.UserMapper;
import org.code.airportitemstorage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Operation(summary = "测试接口")
    @GetMapping("/h")
    public String h() {
        UserDto user = userService.GetUserById(1);
        System.out.println(user);
        return "获取用户信息";
    }

    @PostMapping("add")
    public int add(User user) {
        return userMapper.insert(user);
    }
}
