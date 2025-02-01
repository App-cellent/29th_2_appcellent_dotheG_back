package com.example.dotheG.dto.oAuth2;
import java.util.Map;
public class NaverResponse implements OAuth2Response {
    //데이터 받을 Map 형식 변수 선언
    private final Map<String, Object> attributes;
    public NaverResponse(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }
    @Override
    public String getProvider() {
        return "naver";
    }
    //naver에서 Id라는 값으로 Id 제공하므로
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }
    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
    public boolean available;

    public boolean isSocial;

    public boolean noti;

    public boolean tutorial;
}