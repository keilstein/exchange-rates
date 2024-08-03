package org.keilstein.exchangerates.service.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder
public class ExchangeRatesServiceResult {

  String base;
  LocalDateTime date;
  Map<String, Float> rates;
  String source;
}
