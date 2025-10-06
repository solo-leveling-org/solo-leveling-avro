package com.sleepkqq.sololeveling.avro.config

import com.sleepkqq.sololeveling.avro.constants.KafkaHeaders.LOCALE_METADATA_KEY
import org.apache.kafka.clients.consumer.ConsumerInterceptor
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class LocaleKafkaConsumerInterceptor : ConsumerInterceptor<String, Any> {

	override fun onConsume(records: ConsumerRecords<String, Any>): ConsumerRecords<String, Any> =
		records.onEach { record ->
			val localeHeader = record.headers().lastHeader(LOCALE_METADATA_KEY)
			localeHeader?.value()?.let {
				val locale = String(it)
				LocaleContextHolder.setLocale(Locale.forLanguageTag(locale))
			}
		}

	override fun onCommit(offsets: MutableMap<org.apache.kafka.common.TopicPartition, OffsetAndMetadata>?) {
		LocaleContextHolder.resetLocaleContext()
	}

	override fun close() {
		LocaleContextHolder.resetLocaleContext()
	}

	override fun configure(configs: MutableMap<String, *>?) {
	}
}
