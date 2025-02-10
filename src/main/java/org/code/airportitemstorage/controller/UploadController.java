package org.code.airportitemstorage.controller;

import org.code.airportitemstorage.mapper.users.UserMapper;
import org.code.airportitemstorage.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/upload")
public class UploadController {

    @Autowired
    private OssService ossService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("avatar/{userId}")
    public String uploadProfileImage(@PathVariable Long userId, @RequestParam MultipartFile file) {
        return ossService.uploadImage(file);
    }

}
