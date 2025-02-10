package org.code.airportitemstorage.library.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserCommentDto {
    private long id;

    @JsonProperty("accountName")
    private String accountName;

    private String comment;

    @JsonProperty("commentDate")
    private Date commentDate;

    private String phone;


}
