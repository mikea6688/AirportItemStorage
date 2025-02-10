package org.code.airportitemstorage.library.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetAllUserRequest {
    @JsonProperty("accountName")
    public String accountName;

    @JsonProperty("nickName")
    public String nickName;

    public long pageIndex = 1;

    public long pageSize = 15;
}
