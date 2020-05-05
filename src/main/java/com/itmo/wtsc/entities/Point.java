package com.itmo.wtsc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double longitude;

    private Double latitude;

    @OneToOne(mappedBy = "point")
    private Request request;
}
