package org.keilstein.exchangerates.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "fx-rates")
public class FxRatesProperties {

  private String url;
}
