package org.keilstein.exchangerates.mapper;

import lombok.RequiredArgsConstructor;
import org.keilstein.exchangerates.controller.model.ExchangeRatesWebResponse;
import org.keilstein.exchangerates.service.model.ExchangeRatesServiceResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeRatesWebResponseFactory {

  public ExchangeRatesWebResponse createExchangeRatesWebResponse(ExchangeRatesServiceResult result) {
    return ExchangeRatesWebResponse.builder()
        .base(result.getBase())
        .date(result.getDate())
        .rates(result.getRates())
        .source(result.getSource())
        .build();
  }
}
