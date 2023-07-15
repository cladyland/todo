package kovalenko.vika.service.impl;

import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.mapper.UserMapper;
import kovalenko.vika.utils.Hashing;

public abstract class AbstractUserService {
    protected final UserDAO userDAO;
    protected final UserMapper userMapper;
    protected final Hashing hashing;

    public AbstractUserService(UserDAO userDAO, Hashing hashing) {
        this.userDAO = userDAO;
        this.userMapper = UserMapper.INSTANCE;
        this.hashing = hashing;
    }
}
