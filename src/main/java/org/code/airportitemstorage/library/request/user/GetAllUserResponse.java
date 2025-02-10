package org.code.airportitemstorage.library.request.user;

import org.code.airportitemstorage.library.dto.users.UserDto;

import java.util.List;

public class GetAllUserResponse {
    public List<UserDto> users;

    public long total;
}
