package com.nttdata.exchangerate.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.exchangerate.entity.ExchangeRate;

@Repository
public interface ExchangeRateRepository extends ReactiveMongoRepository<ExchangeRate,Long>{

}
