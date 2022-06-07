Feature: Compare weather data from accuweather site and an open source API

  Scenario Outline: Weather data comparison within allowed variance
    Given query parameters "<lat>" and "<lon>" to the API for identifying location
    And searched website for city name "<cityName>"
    When API is triggered and temp is fetched
    And website is searched and temp is fetched
    Then temperature data is matched as per variance value "<tempVariance>"
    And result is "<finalResult>"

    Examples:
      | lat     | lon     | cityName | tempVariance | finalResult |
      | 28.7041 | 77.1025 | delhi    | 5            | true        |
      | 28.7041 | 77.1025 | delhi    | 0            | false       |
