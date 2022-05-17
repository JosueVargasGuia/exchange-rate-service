package com.nttdata.exchangerate.consumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nttdata.exchangerate.entity.ExchangeRate;
import com.nttdata.exchangerate.service.ExchangeRateService;
 
import com.nttdata.purchaserequest.model.PurchaseRequestKafka;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class PurchaseConsumer {
	@Value("${api.kafka-uri.exchange-topic-respose}")
	String exchangeTopicResponse;
	@Autowired
	KafkaTemplate<String, PurchaseRequestKafka> kafkaTemplate;
	@Autowired
	ExchangeRateService exchangeRateService;

	@KafkaListener(topics = "${api.kafka-uri.exchange-topic}", groupId = "group_id")
	public void purchaseConsumer(PurchaseRequestKafka purchaseRequestKafka) throws ParseException {
		log.info("Reciviendo purchase para calcular el precio");
		SimpleDateFormat dmy = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dmyNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date _dateIni = dmyNow.parse(dmy.format(new Date()) + " 00:00:00");
		Date _dateEnd = dmyNow.parse(dmy.format(new Date()) + " 11:59:59");
		ExchangeRate exchangeRate = exchangeRateService.findAll()
				.filter(e -> e.getCreationDate().getTime() >= _dateIni.getTime()
						&& e.getCreationDate().getTime() <= _dateEnd.getTime())
				.collect(Collectors.maxBy(Comparator.comparing(ExchangeRate::getIdExchangeRate))).blockOptional().get()
				.get();
		if(exchangeRate!=null) {
		purchaseRequestKafka.setAmountInCurrency(purchaseRequestKafka.getAmountBitcoin()*exchangeRate.getExchangeRateSale());
		kafkaTemplate.send(exchangeTopicResponse,purchaseRequestKafka);
		}
	}
}
