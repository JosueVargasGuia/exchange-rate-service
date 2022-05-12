package com.nttdata.exchangerate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.exchangerate.entity.ExchangeRate;
import com.nttdata.exchangerate.service.ExchangeRateService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/v1/exchangerate")
public class ExchangeRateController {
	@Autowired
	ExchangeRateService exchangeRateService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<ExchangeRate> findAll() {
		return exchangeRateService.findAll();

	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ResponseEntity<ExchangeRate>> save(@RequestBody ExchangeRate exchangeRate) {
		return exchangeRateService.save(exchangeRate).map(_exchangeRate -> ResponseEntity.ok().body(_exchangeRate))
				.onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				});
	}

	@GetMapping("/{idExchangeRate}")
	public Mono<ResponseEntity<ExchangeRate>> findById(@PathVariable(name = "idExchangeRate") Long idExchangeRate) {
		return exchangeRateService.findById(idExchangeRate).map(exchangeRate -> ResponseEntity.ok().body(exchangeRate))
				.onErrorResume(e -> {
					log.info("Error:" + e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@PutMapping
	public Mono<ResponseEntity<ExchangeRate>> update(@RequestBody ExchangeRate exchangeRate) {
		Mono<ExchangeRate> mono = exchangeRateService.findById(exchangeRate.getIdExchangeRate())
				.flatMap(objExchangeRate -> {
					return exchangeRateService.update(exchangeRate);
				});
		return mono.map(_exchangeRate -> {
			return ResponseEntity.ok().body(_exchangeRate);
		}).onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@DeleteMapping("/{idExchangeRate}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable(name = "idExchangeRate") Long idExchangeRate) {
		Mono<ExchangeRate> _exchangeRate = exchangeRateService.findById(idExchangeRate);
		_exchangeRate.subscribe();
		ExchangeRate exchangeRate = _exchangeRate.toFuture().join();
		if (exchangeRate != null) {
			return exchangeRateService.delete(idExchangeRate).map(r -> ResponseEntity.ok().<Void>build());
		} else {
			return Mono.just(ResponseEntity.noContent().build());
		}

	}
}
