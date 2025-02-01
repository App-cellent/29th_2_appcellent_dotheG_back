# 프로젝트 소개
DotheG; do the Green

친환경 활동도 게임처럼, 캐릭터 수집으로 만드는 나만의 Eco-Life!

<img src="https://github.com/user-attachments/assets/0f2b0823-5b82-42a6-99c1-287ac3d733df" width="480"/>


# Backend

## ✨Main 기능
- 회원가입/로그인
- 목적에 맞는 중간지점 찾기
- 커뮤니티 게시판
- 즐겨찾기 장소/친구

## 👩‍💻 역할 분담
|       이름         | 프로필                                                              |                                      역할분담                         |
| -------------------------------------- | ------------------------------------------------------------------- | --------------------------------------------------------------------- |
| 노영서 | <img src="https://github.com/user-attachments/assets/da4077ee-ea1c-4878-a67a-8c545668fb8a" width="160"/> | api 명세서, 초기 세팅, 회원가입, 이메일 인증, (카카오 로그인), 로그아웃, 회원정보 조회 및 수정, 회원 탈퇴, 백엔드 배포 |
| 이현진 | <img src="https://github.com/user-attachments/assets/3cd02d1e-7f5f-463d-bfec-f2b772ca8f29" width="160"/> | 목적에 맞는 중간지점 찾기, 중간 지점 주변 장소 필터링하기, 특정 장소에 대한 구글 리뷰 끌어오기, 즐겨찾기 장소/친구 등록, 수정, 삭제, 상세보기, 즐겨찾기 리스트 보기 |
| 이혜지 | <img src="https://github.com/user-attachments/assets/0b9d7fa2-99a4-4424-beb2-4d5e7fd96bb5" width="160"/> | ERD, 커뮤니티 게시판 (리뷰 게시판)의 글 등록, 수정, 삭제, 상세보기, 좋아요, 해시태그 및 키워드 검색, 장소 검색 기록 저장 및 조회 |
| 한은서 | <img src="" width="160"/> | ERD, 커뮤니티 게시판 (리뷰 게시판)의 글 등록, 수정, 삭제, 상세보기, 좋아요, 해시태그 및 키워드 검색, 장소 검색 기록 저장 및 조회 |


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
