package org.code.airportitemstorage.library.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetUserCommentsRequest {
    public long id;

    @JsonProperty("accountName")
    public String accountName;

    public long pageIndex = 1;

    public long pageSize = 10;
}
