package org.keilstein.exchangerates.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder
@Jacksonized
@JsonNaming(SnakeCaseStrategy.class)
public class ExchangeRatesWebResponse {

  String base;
  LocalDateTime date;
  Map<String, Float> rates;
  String source;
}
