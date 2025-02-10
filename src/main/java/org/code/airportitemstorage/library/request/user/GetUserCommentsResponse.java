package org.code.airportitemstorage.library.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.code.airportitemstorage.library.dto.users.UserCommentDto;

import java.util.List;

public class GetUserCommentsResponse {
    @JsonProperty("userComments")
    public List<UserCommentDto> userComments;

    public long total;
}
