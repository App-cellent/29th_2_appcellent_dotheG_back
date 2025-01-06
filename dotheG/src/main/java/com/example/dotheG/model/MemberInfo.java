package com.example.dotheG.model;

import jakarta.persistence.*;

@Entity
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

    protected MemberInfo() {}

    public MemberInfo(Long userInfoId, Member userId, int userReward,Long mainChar) {
        this.userInfoId = userInfoId;
        this.userId = userId;
        this.userReward = userReward;
        this.mainChar = mainChar;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public Member getUserId() {
        return userId;
    }

    public int getUserReward() {
        return userReward;
    }

    public Long getMainChar() {
        return mainChar;
    }

    // 대표 캐릭터 업데이트 메서드
    public void updateMainChar(Long characterId) {
        this.mainChar = characterId;
    }
}
