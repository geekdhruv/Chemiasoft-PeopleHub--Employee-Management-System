package org.hrmanage.service;

import org.hrmanage.dto.UserDto;

public interface UserService {

    UserDto registerUser(UserDto userDTO);

    UserDto getUserByUsername(String username);
}
