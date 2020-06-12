package com.itmo.wtsc.repositories;

import com.itmo.wtsc.entities.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<ConfirmationToken, Integer> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
