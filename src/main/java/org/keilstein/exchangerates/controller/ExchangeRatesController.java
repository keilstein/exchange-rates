package org.keilstein.exchangerates.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keilstein.exchangerates.controller.model.ExchangeRatesWebResponse;
import org.keilstein.exchangerates.mapper.ExchangeRatesWebResponseFactory;
import org.keilstein.exchangerates.service.ExchangeRatesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1", produces = APPLICATION_JSON_VALUE)
public class ExchangeRatesController {

  private final ExchangeRatesService exchangeRatesService;
  private final ExchangeRatesWebResponseFactory exchangeRatesWebResponseFactory;

  @GetMapping("/exchange-rates/{base}")
  public Mono<ExchangeRatesWebResponse> getExchangeRates(@NotBlank @Valid @PathVariable("base") String base,
                                                         @RequestParam(value = "targets", required = false) List<String> targets,
                                                         @RequestParam(value = "force", required = false) boolean force) {
    return exchangeRatesService.getExchangeRates(base, targets, force)
        .map(exchangeRatesWebResponseFactory::createExchangeRatesWebResponse);
  }
}
