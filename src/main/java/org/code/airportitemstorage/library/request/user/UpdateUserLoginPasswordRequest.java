package org.code.airportitemstorage.library.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateUserLoginPasswordRequest {
    @JsonProperty("oldPassword")
    public String oldPassword;

    @JsonProperty("newPassword")
    public String newPassword;
}
