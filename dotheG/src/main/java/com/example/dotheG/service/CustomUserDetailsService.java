package com.example.dotheG.service;

import com.example.dotheG.dto.CustomUserDetails;
import com.example.dotheG.model.Member;
import com.example.dotheG.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserLogin(userLogin);

        if (member != null) {
            return new CustomUserDetails(member);
        }
        throw new UsernameNotFoundException("User not found with ID: " + userLogin);
    }
}