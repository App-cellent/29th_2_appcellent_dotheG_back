# 프로젝트 소개
DotheG; do the Green

친환경 활동도 게임처럼, 캐릭터 수집으로 만드는 나만의 Eco-Life!

<img src="https://github.com/user-attachments/assets/0f2b0823-5b82-42a6-99c1-287ac3d733df" width="480"/>


# Backend

## ✨Main 기능
- 회원가입/로그인
- 튜토리얼 제공
- 만보기 측정
- 친환경 활동 인증
- 데일리 환경 퀴즈
- 캐릭터 뽑기 및 수집
- 주간/월간 성과보고서
- 마이페이지 


## 👩‍💻 역할 분담
|       이름         | 프로필                                                              |                                      역할분담                         |
| -------------------------------------- | ------------------------------------------------------------------- | --------------------------------------------------------------------- |
| 노영서 | <img src="https://github.com/user-attachments/assets/da4077ee-ea1c-4878-a67a-8c545668fb8a" width="160"/> | api 명세서, ERD, 초기 세팅, 튜토리얼, 데일리 환경 퀴즈, 친환경 활동 인증, 활동 인증 조회, 닉네임/비밀번호 변경, 알림 설정, 회원 탈퇴 |
| 이현진 | <img src="https://github.com/user-attachments/assets/3cd02d1e-7f5f-463d-bfec-f2b772ca8f29" width="160"/> | api 명세서, ERD, 회원가입/로그인, 네이버 로그인, 회원정보 조회 및 수정, 회원 탈퇴 |
| 이혜지 | <img src="https://github.com/user-attachments/assets/0b9d7fa2-99a4-4424-beb2-4d5e7fd96bb5" width="160"/> | api 명세서, ERD, 캐릭터 뽑기, 캐릭터 도감 조회, 대표 캐릭터 지정, 주간/월간 성과보고서 생성 및 조회 |
| 한은서 | <img src="" width="160"/> | api 명세서, ERD, 초기 세팅, 만보기, 알림 기능 구현 및 알림 조회, 백엔드 배포 |


## 🌳 프로젝트 구조
```
├─java
│  └─com
│      └─example
│          └─dotheG
│              ├─config
│              │  ├─nnnnnn
│              │  └─mmm
│              ├─controller
│              │  ├─nnnnnn
│              │  └─mmm
│              ├─dto
│              │  ├─nnnnnn
│              │  └─mmm
│              ├─exception
│              │  ├─nnnnnn
│              │  └─mmm
│              ├─model
│              │  ├─nnnnnn
│              │  └─mmm
│              ├─repository
│              │  ├─nnnnnn
│              │  └─mmm
│              ├─service
│              │  ├─nnnnnn
│              │  └─mmm
│              └─DotheGApplication.java
└─resources
    └─application.properties
```
