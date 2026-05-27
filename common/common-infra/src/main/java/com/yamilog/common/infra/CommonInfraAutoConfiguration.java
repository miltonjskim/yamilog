package com.yamilog.common.infra;

import com.yamilog.common.infra.kafka.KafkaConsumerConfig;
import com.yamilog.common.infra.kafka.KafkaProducerConfig;
import com.yamilog.common.infra.kafka.KafkaTopicConfig;
import com.yamilog.common.infra.security.RequireLevelAspect;
import com.yamilog.common.infra.security.jwt.JwtProperties;
import com.yamilog.common.infra.security.jwt.JwtProvider;
import com.yamilog.common.infra.web.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(JwtProperties.class)
@Import({
    JwtProvider.class,
    RequireLevelAspect.class,
    GlobalExceptionHandler.class,
    KafkaTopicConfig.class,
    KafkaProducerConfig.class,
    KafkaConsumerConfig.class
})
public class CommonInfraAutoConfiguration {}
