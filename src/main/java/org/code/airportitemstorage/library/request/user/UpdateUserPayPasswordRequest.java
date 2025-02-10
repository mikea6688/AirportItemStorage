package org.code.airportitemstorage.library.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateUserPayPasswordRequest {
    public String oldPassword;

    public String newPassword;
}
