package com.nttdata.exchangerate.service.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.exchangerate.entity.ExchangeRate;
import com.nttdata.exchangerate.repository.ExchangeRateRepository;
import com.nttdata.exchangerate.service.ExchangeRateService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
	@Autowired
	ExchangeRateRepository rateRepository;

	@Override
	public Flux<ExchangeRate> findAll() {
		return rateRepository.findAll()
				.sort((objA, objB) -> objA.getIdExchangeRate().compareTo(objB.getIdExchangeRate()));
	}

	@Override
	public Mono<ExchangeRate> findById(Long idExchangeRate) {
		return rateRepository.findById(idExchangeRate);

	}

	@Override
	public Mono<ExchangeRate> save(ExchangeRate exchangeRate) {
		Long count = this.findAll().collect(Collectors.counting()).blockOptional().get();
		Long idExchangeRate;
		if (count != null) {
			if (count <= 0) {
				idExchangeRate = Long.valueOf(0);
			} else {
				idExchangeRate = this.findAll()
						.collect(Collectors.maxBy(Comparator.comparing(ExchangeRate::getIdExchangeRate)))
						.blockOptional().get().get().getIdExchangeRate();
			}
		} else {
			idExchangeRate = Long.valueOf(0);

		}
		exchangeRate.setIdExchangeRate(idExchangeRate + 1);
		exchangeRate.setCreationDate(Calendar.getInstance().getTime());
		return rateRepository.insert(exchangeRate);
	}

	@Override
	public Mono<com.nttdata.exchangerate.entity.ExchangeRate> update(ExchangeRate exchangeRate) {
		return rateRepository.save(exchangeRate);
	}

	@Override
	public Mono<Void> delete(Long idExchangeRate) {
		return rateRepository.deleteById(idExchangeRate);
	}

}
