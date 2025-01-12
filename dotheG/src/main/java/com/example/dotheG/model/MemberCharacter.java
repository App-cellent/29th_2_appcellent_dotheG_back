package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class MemberCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_CHAR_ID")
    private Long userCharId;

    @ManyToOne
    @JoinColumn(name="USER_INFO_ID")
    private MemberInfo userInfoId;

    @ManyToOne
    @JoinColumn(name="CHAR_ID")
    private Character charId;

    protected MemberCharacter() {}

    public MemberCharacter(MemberInfo userInfo, Character character) {
        this.userInfoId = userInfo;
        this.charId = character;
    }
}