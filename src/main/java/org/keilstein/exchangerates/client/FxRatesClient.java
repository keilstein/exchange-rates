package org.keilstein.exchangerates.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keilstein.exchangerates.client.model.FxRatesResponse;
import org.keilstein.exchangerates.config.FxRatesProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class FxRatesClient {

  private static final ConcurrentHashMap<String, FxRatesResponse> CACHE = new ConcurrentHashMap<>();
  private final WebClient webClient;
  private final FxRatesProperties fxRatesProperties;

  private static String buildFxRatesUrl(String url, String base) {
    return format("%s/latest?amount=1&base=%s", url, base);
  }

  public Mono<FxRatesResponse> getExchangeRates(String base, boolean force) {
    var cached = CACHE.get(base);

    if (!force && cached != null) {
      return Mono.just(cached)
          .doOnNext(__ -> log.info("Using cached exchange rates for {}", base));
    }

    return getExchangeRates(base)
        .doOnNext(response -> CACHE.put(base, response));
  }

  private Mono<FxRatesResponse> getExchangeRates(String base) {
    var uri = buildFxRatesUrl(fxRatesProperties.getUrl(), base);

    return webClient
        .get()
        .uri(uri)
        .retrieve()
        .bodyToMono(FxRatesResponse.class)
        .doOnNext(response -> log.info("Successfully fetched from {}", uri))
        .doOnError(throwable -> log.error("Failed to fetch from {}", uri, throwable));
  }
}