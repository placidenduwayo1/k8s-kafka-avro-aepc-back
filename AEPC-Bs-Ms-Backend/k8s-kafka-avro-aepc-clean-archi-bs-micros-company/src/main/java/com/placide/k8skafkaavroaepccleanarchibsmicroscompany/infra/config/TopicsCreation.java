package com.placide.k8skafkaavroaepccleanarchibsmicroscompany.infra.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicsCreation {
    @Value("${topics.names}")
    private String [] topics;
    private static final int NB_PARTITIONS = 1;
    private static final int NB_REPLICAS = 1;

    @Bean
    public NewTopic createTopicCompanyAdd() {
        return TopicBuilder
                .name(topics[0])
                .partitions(NB_PARTITIONS)
                .replicas(NB_REPLICAS)
                .build();
    }

    @Bean
    public NewTopic createTopicCompanyDelete() {
        return TopicBuilder
                .name(topics[1])
                .partitions(NB_PARTITIONS)
                .replicas(NB_REPLICAS)
                .build();
    }

    @Bean
    public NewTopic createTopicCompanyEdit() {
        return TopicBuilder
                .name(topics[2])
                .partitions(NB_PARTITIONS)
                .replicas(NB_REPLICAS)
                .build();
    }
}
