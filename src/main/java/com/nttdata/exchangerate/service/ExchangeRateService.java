package com.nttdata.exchangerate.service;



import com.nttdata.exchangerate.entity.ExchangeRate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {
	Flux<ExchangeRate> findAll();

	Mono<ExchangeRate> findById(Long idExchangeRate);

	Mono<ExchangeRate> save(ExchangeRate exchangeRate);

	Mono<ExchangeRate> update(ExchangeRate exchangeRate);

	Mono<Void> delete(Long idExchangeRate);
}
