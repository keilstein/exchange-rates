package org.keilstein.exchangerates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class ExchangeRatesApplicationConfig {

  @Bean
  WebClient webClient() {
    return WebClient.builder()
        .build();
  }
}
