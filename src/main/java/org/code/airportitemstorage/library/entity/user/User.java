package org.code.airportitemstorage.library.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.code.airportitemstorage.library.RoleType;
import org.code.airportitemstorage.library.dto.users.UserForRegisterDto;

@Data
@TableName("user")
public class User {
    public User() {}

    @TableId
    private long id;

    @TableField("account_name")
    private String accountName;

    @TableField("nick_name")
    private String nickName;

    private String password;

    @TableField("pay_password")
    private String payPassword;

    private String phone;

    private String email;

    private String address;

    @TableField("avatar_picture")
    private String avatar;

    @TableField("role_type")
    private RoleType roleType;

    public User(UserForRegisterDto userForRegisterDto) {
        this.nickName = userForRegisterDto.nickName;
        this.accountName = userForRegisterDto.accountName;
        this.email = userForRegisterDto.email;
        this.password = userForRegisterDto.password;
        this.phone = userForRegisterDto.phone;
        this.address = userForRegisterDto.address;
        this.payPassword = userForRegisterDto.payPassword;
        this.roleType = RoleType.Ordinary;
    }
}

