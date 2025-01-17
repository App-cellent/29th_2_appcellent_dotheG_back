package com.example.dotheG.dto;

public interface OAuth2Response {

    //제공 서비스
    String getProvider();

    //제공 서비스에서 발급해주는 ID
    String getProviderId();

    String getEmail();

    String getName();

}
