package com.yamilog.common.infra.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic reviewCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.REVIEW_CREATED).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic reviewUpdatedTopic() {
        return TopicBuilder.name(KafkaTopics.REVIEW_UPDATED).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic levelChangedTopic() {
        return TopicBuilder.name(KafkaTopics.LEVEL_CHANGED).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic placeCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.PLACE_CREATED).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic userFollowedTopic() {
        return TopicBuilder.name(KafkaTopics.USER_FOLLOWED).partitions(3).replicas(1).build();
    }
}
