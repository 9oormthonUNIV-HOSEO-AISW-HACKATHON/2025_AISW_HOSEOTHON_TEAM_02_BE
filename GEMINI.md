
# Gemini 프로젝트 분석: dosol

## 프로젝트 개요 (Project Overview)

`dosol`은 사용자 간에 음악 플레이리스트를 생성하고, 서로의 취향(세대, 장르)과 기분(Mood)에 맞춰 플레이리스트를 교환(매칭)하는 Spring Boot 기반 웹 애플리케이션입니다.
사용자는 복잡한 회원가입 없이 닉네임 설정 후 발급받은 \*\*고유 인증 코드(Auth Code)\*\*를 통해 익명으로 활동합니다. 작성된 플레이리스트는 YouTube API와 연동되어 각 노래의 유튜브 영상 URL 정보를 자동으로 확보합니다.

## 사용 기술 (Technologies Used)

  - **백엔드 (Backend):**
      - Java 17
      - Spring Boot 3
      - Spring Web
      - Spring Data JPA
      - Spring WebFlux (YouTube API 비동기 통신용)
  - **데이터베이스 (Database):**
      - H2 (개발/테스트용 인메모리 DB)
      - MySQL (배포/운영용)
  - **빌드 도구 (Build Tool):**
      - Gradle
  - **기타 라이브러리:**
      - Lombok (Getter/Setter 등 보일러플레이트 코드 감소)

## 프로젝트 구조 (Project Structure)

표준적인 Spring Boot 레이어드 아키텍처를 따릅니다.

  - `src/main/java/com/backend/dosol`
      - `config`: 설정 클래스 (`DataLoader` 등 초기 데이터 세팅 포함)
      - `controller`: API 요청을 처리하는 진입점 (`PlaylistController`, `ExchangeController` 등)
      - `dto`: 클라이언트와 데이터를 주고받기 위한 객체 (Request/Response)
      - `entity`: DB 테이블과 매핑되는 도메인 객체
      - `repository`: DB 접근을 위한 JpaRepository 인터페이스
      - `service`: 핵심 비즈니스 로직 (매칭, 유튜브 검색, 플리 생성 등)
  - `src/main/resources`
      - `application.yaml`: DB 설정, 로깅 레벨, API 키 관리

## 핵심 기능 (Key Features)

1.  **플레이리스트 생성 및 관리:** 사용자는 원하는 노래들을 담아 플레이리스트를 생성합니다.
2.  **YouTube 연동:** 플레이리스트 생성 시 또는 조회 시, `Song` 엔티티에 저장된 유튜브 URL을 통해 바로 음악을 감상할 수 있도록 지원합니다.
3.  **간편 인증 (User Auth):** ID/PW 방식 대신 랜덤하게 생성된 '인증 코드'를 사용하여 익명성과 접근 편의성을 높였습니다.
4.  **AI 매칭 및 교환 (Exchange):** 사용자의 기분(Mood)이나 선호 장르를 기반으로 타인의 플레이리스트를 추천받고 저장(교환)합니다.
5.  **감상평 (Review):** 교환받은 플레이리스트를 청취한 후 작성자에게 감상평을 남길 수 있습니다.

## 데이터베이스 스키마 (Database Schema)

### 1\. Users (유저)

사용자의 식별 정보 및 매칭을 위한 취향 정보를 저장합니다.

```sql
CREATE TABLE Users (
    user_id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '유저 PK',
    nickname        VARCHAR(50) NOT NULL COMMENT '닉네임',
    auth_code       VARCHAR(100) COMMENT '인증코드 (로그인 대용)',
    generation      VARCHAR(20) COMMENT '유저 세대 (예: 90년대생)',
    favorite_genre  ENUM('EMOTIONAL', 'DANCE', 'HIPHOP', 'VOCAL') COMMENT '선호 장르',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 2\. Songs (노래)

노래 정보를 저장합니다. 앨범 커버 대신 유튜브 URL을 관리합니다.

```sql
CREATE TABLE Songs (
    song_id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '노래 PK',
    title       VARCHAR(200) NOT NULL COMMENT '노래 제목',
    artist      VARCHAR(100) NOT NULL COMMENT '가수',
    generation  VARCHAR(20) COMMENT '노래 발매 세대',
    genre       ENUM('EMOTIONAL', 'DANCE', 'HIPHOP', 'VOCAL') COMMENT '장르',
    youtube_url VARCHAR(500) COMMENT '유튜브 영상 URL'
);
```

### 3\. Playlists (플레이리스트 헤더)

플레이리스트의 메타 데이터(제목, 작성자)를 관리합니다.

```sql
CREATE TABLE Playlists (
    playlist_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '플리 PK',
    user_id     BIGINT NOT NULL COMMENT '플리 만든 유저 (FK)',
    title       VARCHAR(100) NOT NULL COMMENT '플리 제목',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
```

### 4\. Playlist\_Songs (플레이리스트 수록곡)

플레이리스트와 노래 간의 다대다(N:M) 관계를 연결하는 매핑 테이블입니다.

```sql
CREATE TABLE Playlist_Songs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    playlist_id BIGINT NOT NULL COMMENT '플리 PK (FK)',
    song_id     BIGINT NOT NULL COMMENT '노래 PK (FK)',
    FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES Songs(song_id)
);
```

### 5\. Exchanges (노래 교환/저장 기록)

추천받거나 교환하여 내 보관함에 저장된 플레이리스트 목록입니다.

```sql
CREATE TABLE Exchanges (
    exchange_id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '교환 PK',
    sender_id       BIGINT NOT NULL COMMENT '보낸 사람/원작자 (FK)',
    receiver_id     BIGINT NOT NULL COMMENT '받은 사람/저장한 사람 (FK)',
    playlist_id     BIGINT NOT NULL COMMENT '교환된 플리 (FK)',
    exchanged_at    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '저장 시점',
    FOREIGN KEY (sender_id) REFERENCES Users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES Users(user_id),
    FOREIGN KEY (playlist_id) REFERENCES Playlists(playlist_id)
);
```

### 6\. Reviews (리뷰)

교환받은 플레이리스트에 대한 한줄평/감상평을 저장합니다.

```sql
CREATE TABLE Reviews (
    review_id       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '리뷰 PK',
    writer_id       BIGINT NOT NULL COMMENT '작성자 (FK)',
    target_user_id  BIGINT NOT NULL COMMENT '대상자/플리주인 (FK)',
    content         TEXT NOT NULL COMMENT '리뷰 내용',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (writer_id) REFERENCES Users(user_id),
    FOREIGN KEY (target_user_id) REFERENCES Users(user_id)
);
```

## API 명세서 (API Specification)

**Base URL:** `http://localhost:8080/api/v1`
**응답 형식:** `status`, `message` 래퍼 없이 \*\*핵심 데이터(JSON)\*\*만 직접 반환합니다.

### 1\. 노래 및 플리 등록

| Method | URI | 설명 |
| :--- | :--- | :--- |
| `GET` | `/songs/candidates` | 유저 취향에 맞는 노래 후보 목록 조회 |
| `POST` | `/playlists/register` | 플리 등록 및 유저 고유 코드 발급 |

**Response Example (노래 후보 조회):**

```json
[
  { "songId": 1, "title": "APT.", "artist": "로제 브루노마스" },
  { "songId": 2, "title": "Supernova", "artist": "aespa" }
]
```

**Response Example (플리 등록):**

```json
{
  "userCode": "8X92A1",
  "playlistId": 2
}
```

### 2\. 매칭 및 저장 (Exchange)

| Method | URI | 설명 |
| :--- | :--- | :--- |
| `POST` | `/ai/recommendations` | 기분(Mood)에 따른 AI 플리 추천 요청 |
| `POST` | `/exchanges` | 추천받은 플리를 내 보관함에 저장(교환 확정) |

**Response Example (AI 추천):**

```json
{
  "recommendedPlaylistId": 1,
  "playlistTitle": "테스트유저의 플레이리스트",
  "ownerCode": "TEST01",
  "summary": "신나는 힙합 모음입니다."
}
```

### 3\. 조회 (보관함 및 상세)

| Method | URI | 설명 |
| :--- | :--- | :--- |
| `GET` | `/exchanges/{userCode}` | 내 코드(`userCode`)로 저장된 플리 목록 조회 |
| `GET` | `/playlists/{playlistId}` | 플리 상세 조회 (**유튜브 URL 포함**) |

**Response Example (플리 상세 조회):**

```json
{
  "playlistId": 1,
  "title": "테스트유저의 플레이리스트",
  "songs": [
    { "title": "APT.", "artist": "로제", "youtubeUrl": "https://youtu.be/..." },
    { "title": "Supernova", "artist": "aespa", "youtubeUrl": "https://youtu.be/..." }
  ]
}
```

### 4\. 감상평 (Review)

| Method | URI | 설명 |
| :--- | :--- | :--- |
| `POST` | `/reviews` | 상대방 코드(`targetUserCode`)로 감상평 작성 |
| `GET` | `/reviews/{userCode}` | 내게 달린 감상평 목록 조회 |

**Response Example (감상평 목록):**

```json
{
  "ownerCode": "TEST01",
  "reviews": [
    { "reviewId": 1, "content": "노래 좋아요!", "createdAt": "2025-11-22" }
  ]
}
```

```aiexclude
{
  "gen1": {
    "emo": [
  "S.E.S – Just A Feeling",
  "젝스키스 – 커플",
  "핑클 – 영원한 사랑",
  "god – 촛불하나",
  "신화 – 너의 결혼식",
  "S.E.S – Dreams Come True(원곡)",
  "H.O.T – 행복",
  "Turbo – 회상",
  "핑클 – Now(발라드 ver.)",
  "유피 – 바다",
  "김현정 – 그녀와의 이별",
  "박지윤 – 성인식(슬로우 ver.)",
  "god – 사랑해 그리고 기억해",
  "H.O.T – 아이야",
  "신화 – First Love",
  "김범수 – 약속",
  "신승훈 – I Believe",
  "박효신 – 좋은 사람",
  "임창정 – 결혼해줘",
  "조성모 – 아시나요",
  "박정현 – 꿈에",
  "박정현 – 편지할게요",
  "브라운아이즈 – 벌써 일년",
  "성시경 – 내게 오는 길",
  "휘성 – 안되나요",
  "김범수 – 슬픔 활용법",
  "SG워너비 – 내 사람",
  "에메랄드 캐슬 – 발걸음",
  "라이너스 – 연(緣)",
  "박효신 – 해줄 수 없는 일",
  "왁스 – 부탁해요",
  "이승환 – 덩크슛",
  "윤도현 – 사랑했나봐",
  "조성모 – 불멸의 사랑",
  "박정현 – 미아",
  "이정현 – 너(슬로우 ver.)",
  "포지션 – I Love You",
  "박효신 – 그곳에 서서",
  "김동률 – 감사",
  "린 – 사랑했잖아"
]
,
    "dance": [
  "쿨 – 해변의 여인",
  "H.O.T – Candy",
  "핑클 – Now",
  "S.E.S – I'm Your Girl",
  "터보 – Twist King",
  "엄정화 – Poison",
  "샵 – Sweety",
  "베이비복스 – Killer",
  "H.O.T – We Are The Future",
  "NRG – Hit Song",
  "코요태 – 순정",
  "코요태 – 패션",
  "박진영 – 허니",
  "DJ DOC – Run To You",
  "박지윤 – 성인식",
  "이정현 – 와",
  "이정현 – 끈",
  "터보 – Black Cat",
  "룰라 – 3!4!",
  "김현정 – 멍",
  "쥬얼리 – One More Time (초기)",
  "신화 – Wild Eyes",
  "샵 – 내 입술 따뜻한 커피처럼",
  "박미경 – 이브의 경고",
  "엄정화 – 몰라",
  "유피 – 뿌요뿌요",
  "핑클 – 영원",
  "쥬비 – 눈부신 날",
  "R.ef – 이별공식",
  "울랄라세션 – 나쁜 여자야",
  "코요태 – 비몽",
  "듀스 – 런 투 유(초기 ver.)",
  "철이와 미애 – 너는 왜",
  "젝스키스 – 아프지 마요",
  "NRG – 티파니에서 아침을",
  "티티마 – 로미오",
  "박지윤 – 난 사랑에 빠졌죠",
  "핑클 – To My Prince",
  "박진영 – 날 떠나지마",
  "채연 – 둘이서"
]
,
    "hip": [
  "DJ DOC – DOC와 춤을",
  "1TYM – One Love",
  "지누션 – How Deep Is Your Love",
  "Drunken Tiger – 난 너에게",
  "업타운 – 다시 만나줘",
  "DJ DOC – 머피의 법칙",
  "1TYM – Hot",
  "지누션 – 말해줘",
  "DEUX – 여우야",
  "DEUX – 난 알아요",
  "DEUX – Turn Around And Away",
  "CB Mass – 진짜",
  "버벌진트 – 다음번엔",
  "DJ DOC – 스트리트 라이프",
  "1TYM – Without You",
  "업타운 – 올라",
  "리쌍 – 내가 웃는게 아니야(초기)",
  "김진표 – 로맨틱 겨울",
  "Soul Company – Breakdown",
  "T – 시간을 달려",
  "CB MASS – Beautiful Life",
  "DJ DOC – Dance With DOC",
  "드렁큰타이거 – Good Life",
  "1TYM – 더 원",
  "지누션 – A-Yo!",
  "부가킹즈 – 성냥팔이 소녀",
  "가리온 – 무투",
  "타이거JK – Reset",
  "디기리 – Street Life",
  "업타운 – 나를 돌아봐",
  "거북이 – 왜이래",
  "이정현 – 반(랩 ver.)",
  "테크노보이즈 – Break The Wall",
  "듀스 – 나를 돌아봐(Remix)",
  "Double K – Clap",
  "가리온 – 영순위",
  "DJ DOC – 모두가 웃었다",
  "드렁큰타이거 – 소외된 사람들",
  "버벌진트 – 좋겠다",
  "프리스타일 – Y"
],
    "vocal": [
  "박정현 – 꿈에",
  "김범수 – 보고싶다",
  "조성모 – 다짐",
  "임재범 – 너를 위해",
  "박효신 – 좋은 사람",
  "김경호 – 금지된 사랑",
  "박정현 – 편지할게요",
  "신승훈 – 미소 속에 비친 그대",
  "성시경 – 내게 오는 길",
  "김조한 – 사랑에 빠지고 싶다",
  "휘성 – With Me",
  "김범수 – 나타나",
  "임창정 – 소주 한잔",
  "박효신 – 야생화(초기 ver.)",
  "부활 – Never Ending Story",
  "이승철 – 그런 사람 또 없습니다",
  "박정현 – 미아",
  "김동률 – 다시 사랑한다 말할까",
  "성시경 – 거리에서",
  "윤하 – 비밀번호 486",
  "브라운아이즈 – 점점",
  "박효신 – 추억은 사랑을 닮아",
  "김범수 – 보고싶다",
  "KCM – 흑백사진",
  "아이비 – 유혹의 소나타(발라드 ver.)",
  "이기찬 – 감기",
  "휘성 – 가슴 시린 이야기",
  "임창정 – 결혼해줘",
  "린 – 섬머타임",
  "바이브 – 미워도 다시 한번",
  "박정현 – PS I Love You",
  "김범수 – 하루",
  "박효신 – 빛",
  "이수영 – 휠릴리",
  "김경호 – 비정",
  "이승기 – 삭제",
  "김종국 – 사랑스러워(발라드 ver.)",
  "최재훈 – 비의 랩소디",
  "포맨 – Baby Baby",
  "성시경 – 난 좋을 텐데"
]}
}

```