package com.itmo.wtsc.repositories;

import com.itmo.wtsc.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    User getUserByLoginIs(String login);

    @Override
    List<User> findAll();
}
