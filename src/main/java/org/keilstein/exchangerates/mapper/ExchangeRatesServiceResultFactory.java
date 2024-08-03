package org.keilstein.exchangerates.mapper;

import lombok.RequiredArgsConstructor;
import org.keilstein.exchangerates.client.model.FxRatesResponse;
import org.keilstein.exchangerates.service.model.ExchangeRatesServiceResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class ExchangeRatesServiceResultFactory {

  private static Map<String, Float> getTargetedRates(Map<String, Float> rates, List<String> targets) {
    if (targets == null) {
      return rates;
    }

    return rates.entrySet().stream()
        .filter(entry -> targets.contains(entry.getKey()))
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static LocalDateTime getLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public ExchangeRatesServiceResult createExchangeRatesServiceResult(FxRatesResponse response, List<String> targets) {
    var date = getLocalDateTime(response.getDate());
    var rates = getTargetedRates(response.getRates(), targets);

    return ExchangeRatesServiceResult.builder()
        .base(response.getBase())
        .date(date)
        .rates(rates)
        .source("FXRatesAPI")
        .build();
  }
}
