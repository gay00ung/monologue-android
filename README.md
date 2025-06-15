# 📝 Monologue Android

모노로그(Monologue)는 감정 기반의 한 줄 다이어리 앱으로, 사용자가 매일 자신의 기분을 이모티콘으로 표현하고 짧은 글로 하루를 기록할 수 있도록 설계되었습니다. 이 프로젝트는 Android 앱과 Ktor 기반 서버로 구성된 풀스택 프로젝트이며, Docker 기반 배포 및 JWT 인증, REST API 설계 등 다양한 기술이 적용되었습니다.

---

## 📱 앱 개요

- **플랫폼**: Android (Kotlin, Jetpack Compose)
- **아키텍처**: MVVM + Hilt DI + Jetpack Navigation
- **주요 기능**:
  - 감정 이모티콘 선택
  - 한 줄 일기 작성 및 수정
  - 일기 리스트 및 상세 조회
  - 회원가입 / 로그인 / 인증 유지 (JWT)
  - 설정 및 오픈소스 라이선스 확인


---

## 🛠 기술 스택

### 📱 Android (Frontend)
- **Jetpack Compose**: 선언형 UI 프레임워크
- **Hilt**: DI(의존성 주입) 관리
- **Navigation Component**: 화면 이동
- **ViewModel + State**: 상태 기반 화면 관리
- **Retrofit + OkHttp**: REST API 통신
- **Postman**: API 테스트 및 문서화

### 🧠 Ktor Server (Backend)
> GitHub: [monologue-server](https://github.com/gay00ung/monologue-server)

- **Ktor**: Kotlin 기반 비동기 웹 프레임워크
- **JWT 인증**: 로그인 및 토큰 기반 인증 처리
- **MySQL + Exposed ORM**: 사용자, 일기, 감정 데이터 관리
- **Docker**: 서버 컨테이너 배포
- **Ktor Routing + Serialization**: RESTful API 설계
- **코틀린 DSL 기반 구조**: 직관적인 서버 코드 구성**
- **CORS 설정** 및 **환경변수 기반 설정 분리**


---

## ⚙️ 개발 & 배포 환경

### Android
- Android Studio Meerkat
- Kotlin 1.9+
- Gradle 8+

### Server
- JDK 17
- Docker / Docker Compose
- MySQL (Workbench로 관리)


---

## 📂 주요 폴더 구조 (서버)
```
monologue-server/
├── src/
│   ├── routes/           # API 라우터 (auth, diary 등)
│   ├── models/           # 데이터 모델 (User, Diary)
│   ├── db/               # DB 연결 및 스키마 정의
│   ├── auth/             # JWT 관련 유틸
│   └── Application.kt    # Main 진입점
├── resources/
│   └── application.conf  # 설정 파일
├── Dockerfile
├── docker-compose.yml
```

---

## 📡 주요 API 예시

### ✅ 회원가입
```
POST /auth/signup
Content-Type: application/json

{
  "email": "test@monologue.com",
  "password": "secure123",
  "username": "test"
}
```

### ✅ 로그인
```
POST /auth/login
Content-Type: application/json

{
  "email": "test@monologue.com",
  "password": "secure123"
}

Response → {
  "token": "<JWT_ACCESS_TOKEN>",
  "userId": "abc123"
}
```

### ✅ 일기 작성
```
POST /diary
Authorization: Bearer <JWT_ACCESS_TOKEN>
Content-Type: application/json

{
  "mood": "HAPPY",
  "text": "오늘은 정말 좋은 날이었다."
}
```

### ✅ 일기 목록 조회
```
GET /diary
Authorization: Bearer <JWT_ACCESS_TOKEN>
```

### ✅ 일기 수정
```
PUT /diary/{id}
Authorization: Bearer <JWT_ACCESS_TOKEN>
Content-Type: application/json

{
  "mood": "SAD",
  "text": "생각보다 힘든 하루였다."
}
```


---

## 📊 향후 개선 예정
- 감정 분석 기반 통계 페이지 시각화
- 감정 키워드 자동 추출 (ML 모델 연동)
- Firebase 연동 (Crashlytics / Analytics)
- Lottie 애니메이션 및 다크모드 UI 대응
- 위젯 추가


---

## 👩‍💻 개발자
- **신가영** ([GitHub](https://github.com/gay00ung))

> 본 프로젝트는 실제 배포용으로 개발되었으며, Google Play를 통해 출시되었습니다.
