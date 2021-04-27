package org.marchenko.service;

import org.marchenko.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserServiceImpl implements UserService {
    @Override
    public List<User> findAllUsers(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public User createUser(String name, String phone) {
        return null;
    }

    @Override
    public User findUserById(Long id) {
        return null;
    }

    @Override
    public List<User> findUsersByName(String name, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public User deleteUser(Long id) {
        return null;
    }

    @Override
    public User updateUser(Long id, String name, String phone) {
        return null;
    }
}
