package com.yamilog.common.infra.kafka;

public final class KafkaTopics {

    private KafkaTopics() {}

    // review-service 발행
    public static final String REVIEW_CREATED = "yamilog.review.created";
    public static final String REVIEW_UPDATED = "yamilog.review.updated";

    // level-engine 발행
    public static final String LEVEL_CHANGED  = "yamilog.level.changed";

    // place-service 발행
    public static final String PLACE_CREATED  = "yamilog.place.created";

    // user-service 발행
    public static final String USER_FOLLOWED  = "yamilog.user.followed";
}
