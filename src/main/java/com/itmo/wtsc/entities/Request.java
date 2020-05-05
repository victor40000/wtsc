package com.itmo.wtsc.entities;

import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private DumpType dumpType;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private Integer size;

    private String description;

    @ManyToOne
    private User user;

    @OneToOne
    private Point point;
}
