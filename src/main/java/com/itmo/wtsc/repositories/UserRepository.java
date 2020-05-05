package com.itmo.wtsc.repositories;

import com.itmo.wtsc.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User getUserByLoginIs(String login);
}
