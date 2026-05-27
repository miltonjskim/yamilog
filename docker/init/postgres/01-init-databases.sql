-- 야미로그 로컬 개발용 PostgreSQL 초기화
-- 서비스별 독립 DB 생성 (MSA 원칙: DB 분리)

CREATE DATABASE yamilog_user;
CREATE DATABASE yamilog_category;
CREATE DATABASE yamilog_place;
CREATE DATABASE yamilog_ad;
CREATE DATABASE yamilog_level;

-- PostGIS 확장 (place-service 위치 데이터용)
\c yamilog_place
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c yamilog_user
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c yamilog_category
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c yamilog_ad
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c yamilog_level
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
