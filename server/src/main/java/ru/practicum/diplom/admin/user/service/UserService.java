package ru.practicum.diplom.admin.user.service;

import ru.practicum.diplom.admin.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto saveUser(UserDto userDto);

    void deleteUser(Long id);
}
