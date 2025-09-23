package com.artostapyshyn.data.retrival.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KafkaConfigTest {

    private final KafkaConfig config = new KafkaConfig();

    @Test
    void financialDataTopicIsCreated() {
        NewTopic topic = config.financialDataTopic();
        assertNotNull(topic, "Topic should not be null");
        assertEquals(KafkaConfig.FINANCIAL_DATA_TOPIC, topic.name(), "Topic name should match");

        Integer partitions = topic.numPartitions();
        assertNotNull(partitions, "Partitions should be set");
        assertEquals(3, partitions.intValue(), "Topic should have 3 partitions");

        Short replication = topic.replicationFactor();
        assertNotNull(replication, "Replication factor should be set");
        assertEquals(1, replication.shortValue(), "Replication factor should be 1");
    }
}
