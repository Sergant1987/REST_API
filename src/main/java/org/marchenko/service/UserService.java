package org.marchenko.service;

import org.marchenko.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers(Integer pageNumber, Integer pageSize);

    User createUser(String name, String phone);

    User findUserById(Long id);

    List<User> findUsersByName(String name, Integer pageNumber, Integer pageSize);

    User deleteUser(Long id);

    User updateUser(Long id, String name, String phone);
}
