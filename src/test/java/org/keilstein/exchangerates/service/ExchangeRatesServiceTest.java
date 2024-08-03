package org.keilstein.exchangerates.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.keilstein.exchangerates.client.FxRatesClient;
import org.keilstein.exchangerates.client.model.FxRatesResponse;
import org.keilstein.exchangerates.mapper.ExchangeRatesServiceResultFactory;
import org.keilstein.exchangerates.service.model.ExchangeRatesServiceResult;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesServiceTest {

  @InjectMocks
  private ExchangeRatesService tested;

  @Mock
  private FxRatesClient fxRatesClient;

  @Mock
  private ExchangeRatesServiceResultFactory exchangeRatesServiceResultFactory;

  private static Stream<Arguments> provideTargets() {
    return Stream.of(
        Arguments.of(List.of("USD"), false),
        Arguments.of(List.of("USD"), true),
        Arguments.of(List.of("USD", "CNY"), false),
        Arguments.of(List.of("USD", "CNY"), true)
    );
  }

  @ParameterizedTest
  @MethodSource("provideTargets")
  void shouldGetExchangeRates(List<String> targets, boolean force) {
    var response = mock(FxRatesResponse.class);
    var expected = mock(ExchangeRatesServiceResult.class);

    when(fxRatesClient.getExchangeRates("EUR", force)).thenReturn(Mono.just(response));
    when(exchangeRatesServiceResultFactory.createExchangeRatesServiceResult(response, targets)).thenReturn(expected);

    var actual = tested.getExchangeRates("EUR", targets, force);

    StepVerifier.create(actual)
        .expectNext(expected)
        .verifyComplete();
  }
}