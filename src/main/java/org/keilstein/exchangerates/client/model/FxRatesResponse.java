package org.keilstein.exchangerates.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.Map;

/**
 * This holds a random quotes response from FXRatesAPI.
 * It is a subset of the full response defined in
 * <a href="https://fxratesapi.com/docs/endpoints/latest-exchange-rates">Latest Exchange Rates</a>
 */
@Value
@Builder
@Jacksonized
@JsonNaming(SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FxRatesResponse {

  String base;
  Date date;
  Map<String, Float> rates;
  Boolean success;
}