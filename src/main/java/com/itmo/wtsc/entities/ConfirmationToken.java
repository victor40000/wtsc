package com.itmo.wtsc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private Integer id;

    @Column(name="confirmation_token")
    private String confirmationToken;

    private boolean activated;

    private LocalDateTime createdWhen;

    @OneToOne(fetch = FetchType.EAGER)
    private User user;
}
