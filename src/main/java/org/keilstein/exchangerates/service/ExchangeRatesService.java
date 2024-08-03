package org.keilstein.exchangerates.service;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keilstein.exchangerates.client.FxRatesClient;
import org.keilstein.exchangerates.mapper.ExchangeRatesServiceResultFactory;
import org.keilstein.exchangerates.service.model.ExchangeRatesServiceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRatesService {

  private final FxRatesClient fxRatesClient;
  private final ExchangeRatesServiceResultFactory exchangeRatesServiceResultFactory;

  @NotEmpty
  @Value("${currency.hot.currencies}")
  private List<String> hotCurrencies;

  @NotEmpty
  @Value("${currency.warm.currencies}")
  private List<String> warmCurrencies;

  public Mono<ExchangeRatesServiceResult> getExchangeRates(String base, List<String> targets, boolean force) {
    return fxRatesClient.getExchangeRates(base, force)
        .map(response -> exchangeRatesServiceResultFactory.createExchangeRatesServiceResult(response, targets));
  }

  @Schedules({
      @Scheduled(cron = "${currency.hot.refresh-rate-before-business-hours}"),
      @Scheduled(cron = "${currency.hot.refresh-rate-during-business-hours}"),
      @Scheduled(cron = "${currency.hot.refresh-rate-after-business-hours}"),
      @Scheduled(cron = "${currency.hot.refresh-rate-during-weekends}"),
  })
  private void refreshCacheForHotCurrencies() {
    refreshCache(hotCurrencies);
  }

  @Schedules({
      @Scheduled(cron = "${currency.warm.refresh-rate-during-business-hours}"),
      @Scheduled(cron = "${currency.warm.refresh-rate-during-weekends}"),
  })
  private void refreshCacheForWarmCurrencies() {
    refreshCache(warmCurrencies);
  }

  private void refreshCache(List<String> currencies) {
    currencies.forEach(base -> fxRatesClient.getExchangeRates(base, true)
        .doOnNext(rates -> log.info("Refreshing cache for {}", base))
        .doOnError(throwable -> log.warn("Failed to refresh cache for {}", base, throwable))
        .subscribe());
  }
}
