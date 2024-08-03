# exchange-rates

**Exchange Rates** is an auxiliary service that exposes a REST API to query currency exchange rates.

A third-party vendor, [FXRatesAPI](https://fxratesapi.com/docs/endpoints/latest-exchange-rates),
is used to fetch the latest rates. For faster retrieval the currencies are categorizes as
**hot**, **warm** and **cold**, and they are cached accordingly within **Exchange Rates**.

1. **Hot** and **warm** currencies are
    1. fetched and cached during start up;
    2. refreshed with changing frequency during
        1. business hours,
        2. non-business hours,
        3. weekends.
2. **Cold** currencies are fetched and cached on demand.

## Usage

The service can be reached via Web browser, CLI, or service-to-service REST API calls.
No Authorization is required.

### Browser

Open the following URL using your favorite Web browser.

```text
http://<exchange-rates-host>/v1/exchange-rates/USD?targets=EUR,GBP,CHF&force=true
```

### CLI

Open a terminal and send a request to the following URL using your favorite command line tool.
Below is an example using `curl`. Alternatives to `curl` are `fetch`, `wget`, etc.

```shell
curl http://<exchange-rates-host>/v1/exchange-rates/USD?targets=EUR,GBP,CHF&force=true
```

### REST

Implement a client application that makes a `GET` call to **Exchange Rates** server as follows.

```http request
GET http://<exchange-rates-host>/v1/exchange-rates/USD?targets=EUR,GBP,CHF&force=true
```

## API

### Endpoint

**Exchange Rates** API has a single endpoint `/v1/exchange-rates/{base}` with
two optional query parameters `targets` and `force`.

- Path variable `{base}` is the 3-char uppercase code of base currency, e.g. `EUR` (Euro) or `USD` (United States
  Dollar).
- Query parameter `targets` is the comma separated list of target currencies, e.g. `targets=USD,GBP`. By default, all
  target currencies are included.
- Query parameter `force` is a boolean to force the retrieval of the currencies from the third-party vendor,
  e.g. `force=true`. Default value is `false` which forces cache-first approach.

| Example                                                 | Description                                                                                     |
|:--------------------------------------------------------|:------------------------------------------------------------------------------------------------|
| `/v1/exchange-rates/USD`                                | Query **all** exchange rates **in cache** for the base currency                                 |
| `/v1/exchange-rates/USD?targets=EUR,GBP,CHF`            | Query **selected** exchange rates **in cache** for the base currency                            |
| `/v1/exchange-rates/USD?targets=EUR,GBP,CHF&force=true` | Query **selected** exchange rates for the base currency from the **3rd party service** directly |

### Response

The API response is in JSON format and has the following fields:

| Field    | Description                              |
|:---------|:-----------------------------------------|
| `base`   | Base currency                            |
| `date`   | Local date time the rates are from       |
| `rates`  | Exchange rates for the target currencies |
| `source` | Source of the currency rates             |

```json
{
  "base": "USD",
  "date": "2024-04-21T16:35:00",
  "rates": {
    "CHF": 0.90979016,
    "EUR": 0.9381702,
    "GBP": 0.8081701
  },
  "source": "FXRatesAPI"
}
```

### Examples

Please find example API calls in [requests](requests) directory.

## Local Deployment

You can deploy  **Exchange Rates** to local through IntelliJ IDE, CLI or Docker.
Please use profile [local](src/main/resources/application-local.yaml),
in order to make sure that the application starts on
port 8081 of localhost, i.e. **localhost:8081**.

### Prerequisites

#### IDE

Download and install your favourite IDE. Recommended IDE for this project is **IntelliJ IDEA Ultimate**.

| IDE                                                                        | Details                                                                                                                                                                                                                                                  |
|:---------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download) | _A free IDE built on open-source code that provides essential features for Java and Kotlin enthusiasts._ See also [IntelliJ IDEA Ultimate vs IntelliJ IDEA Community Edition](https://www.jetbrains.com/products/compare/?product=idea&product=idea-ce). |
| [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/download)          | There is 30 days of trial period. Check [Subscription Options and Pricing](https://www.jetbrains.com/idea/buy).                                                                                                                                          |
| [Visual Studio Code](https://code.visualstudio.com)                        | _Free. Built on open source. Runs everywhere._                                                                                                                                                                                                           |
| [Eclipse](https://www.eclipse.org/downloads)                               | _The Leading Open Platform for Professional Developers_                                                                                                                                                                                                  |

#### Docker

Download and install [Docker Desktop](https://www.docker.com/products/docker-desktop) and start.

#### Java

Install `Java` version **21**, and configure the project to use it.
Verify the version by running:

```shell
java --version
```

> You can use [sdkman](https://sdkman.io) for installing **Java**.
> It is also possible to install **Java** through IntelliJ IDE
> [SDKs](https://www.jetbrains.com/help/idea/sdk.html#change-project-sdk).

#### Gradle

Install the latest **Gradle**.
The current version in [gradle-wrapper.properties](./gradle/wrapper/gradle-wrapper.properties) is **8.9**.
Verify the version by running:

```shell
gradle --version
```

> You can use [sdkman](https://sdkman.io) for installing **Gradle**.
> It is also possible to use the built-in **Gradle** available in IntelliJ IDE.

### IntelliJ IDE

Local deployment through IntelliJ is as easy as running
[ExchangeRatesApplication](/src/main/java/org/keilstein/exchangerates/ExchangeRatesApplication.java).

### CLI

Alternatively, you can run the following CLI command in the project root.

```shell
./gradlew bootRun --args='--spring.profiles.active=local'
```

### Docker

You can also deploy to local by using [compose-local.yaml](compose-local.yaml)
either from the file itself via the IDE or using the following command:

```shell
docker compose --file compose-local.yaml up exchange-rates
```
