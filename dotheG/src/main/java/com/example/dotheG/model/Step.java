package com.example.dotheG.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STEP_ID")
    private Long stepId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    private int stepCount;

    private Date stepDate;

}
