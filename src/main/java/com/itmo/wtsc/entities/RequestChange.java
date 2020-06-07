package com.itmo.wtsc.entities;

import com.itmo.wtsc.utils.enums.DumpType;
import com.itmo.wtsc.utils.enums.RequestStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class RequestChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "from_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus from;

    @Column(name = "to_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus to;

    private LocalDateTime updatedWhen;

    @ManyToOne
    private Request request;
}
