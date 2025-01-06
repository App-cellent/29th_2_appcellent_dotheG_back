package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_INFO_ID")
    private Long userInfoId;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    @Column(columnDefinition = "integer default 20")
    private int userReward;

    private Long mainChar;
}
