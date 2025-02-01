package com.example.dotheG.service;

import com.example.dotheG.dto.notification.MemberAlertListResponseDto;
import com.example.dotheG.dto.notification.MemberAlertRequestDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberAlert;
import com.example.dotheG.repository.MemberAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final MemberService memberService;
    private final MemberAlertRepository memberAlertRepository;

    // 알람 전체 목록 조회
    @Transactional
    public List<MemberAlertListResponseDto> getNotifications() {
        // 유저 정보 불러오기
        Member member = memberService.getCurrentMember();

        // 유저의 알람 목록 조회
        List<MemberAlert> memberAlertList = memberAlertRepository.findAllByUserIdOrderBySendTimeDesc(member);

        // dto 반환
        List<MemberAlertListResponseDto> memberAlertListResponseDtos = memberAlertList.stream()
                .map(memberAlert -> new MemberAlertListResponseDto(
                        memberAlert.getUserAlertId(),
                        memberAlert.getContent(),
                        memberAlert.isRead(), // 읽음 여부 같이 반환
                        memberAlert.getSendTime()))
                .collect(Collectors.toList());

        return memberAlertListResponseDtos;
    }

    // 알람 상세 목록 조회
    @Transactional
    public void getNotification(Long userAlertId) {
        Member member = memberService.getCurrentMember();
        MemberAlert memberAlert = memberAlertRepository.findByUserIdAndUserAlertId(member,userAlertId);
        if(memberAlert == null) {
            throw new CustomException(null);
        }

        if (!memberAlert.isRead()) {
            memberAlert.updateIsRead();
        }

        memberAlertRepository.save(memberAlert);
    }

    /// 알람 저장
    @Transactional
    public void saveMemberAlert(MemberAlertRequestDto requestDto) {

        Member member = memberService.getCurrentMember();

        MemberAlert memberAlert = new MemberAlert(
                member,
                requestDto.getTitle(),
                requestDto.getMessage(),
                LocalDateTime.now());
        memberAlertRepository.save(memberAlert);
    }







}
