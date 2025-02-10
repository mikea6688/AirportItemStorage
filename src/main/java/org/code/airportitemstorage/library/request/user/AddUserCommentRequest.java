package org.code.airportitemstorage.library.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddUserCommentRequest {
    public long userId;

    public String comment;

    public String contactInfo;
}
