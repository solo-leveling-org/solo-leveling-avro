package com.sleepkqq.sololeveling.avro.config

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import java.lang.Boolean
import java.util.Map
import kotlin.Any
import kotlin.String

@Suppress("unused")
abstract class DefaultKafkaConfig(
	private val bootstrapServers: String,
	private val schemaRegistryUrl: String
) {

	companion object {
		private const val SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url"
		private const val SPECIFIC_AVRO_READER_CONFIG = "specific.avro.reader"
	}

	fun <V> createProducerFactory(): ProducerFactory<String, V> {
		return DefaultKafkaProducerFactory<String, V>(
			Map.of(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer::class,
				SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl
			)
		)
	}

	fun <V> createKafkaTemplate(
		producerFactory: ProducerFactory<String, V>
	): KafkaTemplate<String, V> = KafkaTemplate<String, V>(producerFactory)

	fun <V> createConsumerFactory(groupId: String): ConsumerFactory<String, V> {
		return DefaultKafkaConsumerFactory<String, V>(
			Map.of<String, Any>(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ConsumerConfig.GROUP_ID_CONFIG, groupId,
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class,
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer::class,
				SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl,
				SPECIFIC_AVRO_READER_CONFIG, Boolean.TRUE.toString()
			)
		)
	}

	fun <V> createKafkaListenerContainerFactory(
		consumerFactory: ConsumerFactory<String, V>
	): ConcurrentKafkaListenerContainerFactory<String, V> {
		val factory = ConcurrentKafkaListenerContainerFactory<String, V>()
		factory.consumerFactory = consumerFactory
		return factory
	}
}
