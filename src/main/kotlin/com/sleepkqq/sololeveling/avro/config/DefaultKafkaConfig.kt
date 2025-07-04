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
			mapOf(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to KafkaAvroSerializer::class.java,
				SCHEMA_REGISTRY_URL_CONFIG to schemaRegistryUrl
			)
		)
	}

	fun <V> createKafkaTemplate(
		producerFactory: ProducerFactory<String, V>
	): KafkaTemplate<String, V> = KafkaTemplate<String, V>(producerFactory)

	fun <V> createConsumerFactory(groupId: String): ConsumerFactory<String, V> {
		return DefaultKafkaConsumerFactory<String, V>(
			mapOf(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
				ConsumerConfig.GROUP_ID_CONFIG to groupId,
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to KafkaAvroDeserializer::class.java,
				SCHEMA_REGISTRY_URL_CONFIG to schemaRegistryUrl,
				SPECIFIC_AVRO_READER_CONFIG to Boolean.TRUE.toString()
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
