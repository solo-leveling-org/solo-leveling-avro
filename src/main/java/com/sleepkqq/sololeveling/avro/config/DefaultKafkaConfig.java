package com.sleepkqq.sololeveling.avro.config;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@RequiredArgsConstructor
public class DefaultKafkaConfig {

  private static final String SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url";
  private static final String SPECIFIC_AVRO_READER_CONFIG = "specific.avro.reader";

  private final String bootstrapServers;
  private final String schemaRegistryUrl;

  public <V> ProducerFactory<String, V> createProducerFactory() {
    return new DefaultKafkaProducerFactory<>(Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class,
        SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl
    ));
  }

  public <V> KafkaTemplate<String, V> createKafkaTemplate(
      ProducerFactory<String, V> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  public <V> ConsumerFactory<String, V> createConsumerFactory(String groupId) {
    return new DefaultKafkaConsumerFactory<>(Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
        ConsumerConfig.GROUP_ID_CONFIG, groupId,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class,
        SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl,
        SPECIFIC_AVRO_READER_CONFIG, Boolean.TRUE.toString()
    ));
  }

  public <V> ConcurrentKafkaListenerContainerFactory<String, V> createKafkaListenerContainerFactory(
      ConsumerFactory<String, V> consumerFactory
  ) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, V>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }
}