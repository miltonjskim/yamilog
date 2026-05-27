새 마이크로서비스를 헥사고날 구조로 스캐폴딩한다.

## 사용법
/new-service [서비스명] [DB타입] [포트]
예: /new-service notification-service postgresql 8089

## 생성 항목
1. `services/{서비스명}/build.gradle.kts` — DB 타입에 맞는 의존성 포함
2. `services/{서비스명}/src/main/resources/application.yml` — 환경변수 기반 설정
3. 헥사고날 패키지 구조 (domain/application/adapter)
4. Spring Boot Main 클래스
5. `settings.gradle.kts`에 include 추가
6. `.idea/runConfigurations/{서비스명}_local.xml`
7. `docker/init/` 에 DB 초기화 스크립트 (필요시)

## DB 타입별 의존성
- `postgresql`: spring-boot-starter-jpa + postgresql driver
- `mongodb`: spring-boot-starter-data-mongodb
- `elasticsearch`: spring-boot-starter-data-elasticsearch
- `none`: 저장소 없음 (이벤트 처리 전용)
