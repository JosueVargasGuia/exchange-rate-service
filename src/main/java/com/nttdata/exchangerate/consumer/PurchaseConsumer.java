package com.nttdata.exchangerate.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nttdata.purchaserequest.model.PurchaseRequestKafka;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PurchaseConsumer {
	@Value("${api.kafka-uri.exchange-topic-respose}")
	String exchangeTopicResponse;
	@Autowired
	KafkaTemplate<String, PurchaseRequestKafka> kafkaTemplate;

	@KafkaListener(topics = "${api.kafka-uri.exchange-topic}", groupId = "group_id")
	public void purchaseConsumer(PurchaseRequestKafka purchaseRequestKafka) {
		log.info("Reciviendo purchase para calcular el pricio");
	}
}
