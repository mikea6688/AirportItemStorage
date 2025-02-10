package org.code.airportitemstorage.library.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.code.airportitemstorage.library.RoleType;

@Data
public class UpdateUserRequest {
    @JsonProperty("userId")
    public long userId;

    @JsonProperty("nickName")
    public String nickName;

    public String phone;

    public String email;

    public String address;

    @JsonProperty("avatar")
    public String avatar;
}
