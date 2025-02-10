package org.code.airportitemstorage.library.dto.users;

import org.code.airportitemstorage.library.RoleType;
import org.code.airportitemstorage.library.entity.user.User;

public class UserDto {
    public long id;

    public String accountName;

    public String nickName;

    public String phone;

    public String email;

    public String address;

    public String avatar;

    public RoleType roleType;

    public double point;

    public long storageCount;

    public boolean isStored;

    public UserDto(User user) {
        this.id = user.getId();
        this.accountName = user.getAccountName();
        this.nickName = user.getNickName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.roleType = user.getRoleType();
        this.avatar = user.getAvatar();
    }
}
