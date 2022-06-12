Feature: Compare weather data from accuweather site and an open source API

  Scenario Outline: Weather data comparison within allowed variance
    Given query parameters "<lat>" and "<lon>" to the API for identifying location
    And searched website for city name "<cityName>"
    When API is triggered and temp is fetched
    And website is searched and temp is fetched
    Then temperature data is matched as per variance value "<tempVariance>"
    And result is "<match>"

    Examples:
      | lat     | lon     | cityName | tempVariance | match |
      | 28.7041 | 77.1025 | delhi    | 5            | true  |
      | 28.7041 | 77.1025 | delhi    | 0            | false |
      | 22.5726 | 88.3639 | kolkata  | 5            | true  |
      | 22.5726 | 88.3639 | kolkata  | 0            | false |
      | 19.0760 | 72.8777 | mumbai   | 5            | true  |
      | 19.0760 | 72.8777 | mumbai   | 0            | false |

