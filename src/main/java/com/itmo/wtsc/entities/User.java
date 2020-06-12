package com.itmo.wtsc.entities;

import com.itmo.wtsc.utils.enums.RequestStatus;
import com.itmo.wtsc.utils.enums.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Collection;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String login;

    private String password;

    private String email;

    private boolean active;

    @OneToMany(mappedBy = "user")
    private Collection<Request> requests;

    @OneToOne(mappedBy = "user")
    private ConfirmationToken token;
}
