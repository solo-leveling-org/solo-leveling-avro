package com.sleepkqq.sololeveling.avro.config

import com.sleepkqq.sololeveling.avro.constants.KafkaHeaders.LOCALE_METADATA_KEY
import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class LocaleKafkaProducerInterceptor : ProducerInterceptor<String, Any> {

	override fun onSend(record: ProducerRecord<String, Any>): ProducerRecord<String, Any> =
		record.apply {
			headers().add(LOCALE_METADATA_KEY, LocaleContextHolder.getLocale().language.toByteArray())
		}

	override fun onAcknowledgement(acknowledgement: RecordMetadata?, exception: Exception?) {
	}

	override fun close() {
	}

	override fun configure(configs: MutableMap<String, *>?) {
	}
}
