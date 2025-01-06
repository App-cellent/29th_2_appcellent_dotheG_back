package com.example.dotheG.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class MemberActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ACTIVITY_ID")
    private Long userAcvityId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    @ManyToOne
    @JoinColumn(name = "ACTICITY_ID")
    private Activity activityId;

    private String activityName;

    // Todo : 이미지

    private Date activityDate;
}
