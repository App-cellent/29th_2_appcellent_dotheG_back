package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void addReward(int reward) {
        userReward += reward;
    }


    // 대표 캐릭터 업데이트 메서드
    public void updateMainChar(Long characterId) {
        this.mainChar = characterId;
    }

    @Builder
    public MemberInfo(Member userId) {
        this.userId = userId;
        this.userReward = 20;
        this.mainChar = null;
    }
}
