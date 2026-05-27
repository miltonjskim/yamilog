// 야미로그 MongoDB 초기화 — 서비스별 DB 생성
// review-service, feed-service 각각 독립 DB 사용

db = db.getSiblingDB('yamilog_review');
db.createCollection('reviews');
db.reviews.createIndex({ placeId: 1, createdAt: -1 });
db.reviews.createIndex({ userId: 1, createdAt: -1 });
db.reviews.createIndex({ categoryId: 1, visibilityLevel: 1, createdAt: -1 });

db = db.getSiblingDB('yamilog_feed');
db.createCollection('feed_items');
db.feed_items.createIndex({ userId: 1, createdAt: -1 });
db.feed_items.createIndex({ categoryId: 1, score: -1, createdAt: -1 });
db.feed_items.createIndex({ createdAt: 1 }, { expireAfterSeconds: 2592000 }); // 30일 TTL

print('MongoDB 초기화 완료: yamilog_review, yamilog_feed');
