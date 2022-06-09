package stepDefinitions;

import UtilitiesForProject.Base;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import pageObject.CityPage;
import pageObject.HomePage;

import java.io.IOException;

public class DataDrivenViaExamples extends Base {

    CityPage city;
    HomePage home;
    String response;
    float tempFromApiLayer;
    float tempFromUiLayer;
    boolean result;

    @Given("query parameters {string} and {string} to the API for identifying location")
    public void query_parameters_and_to_the_api_for_identifying_location(String lat, String lon) throws IOException {
        reqSpecBuilder = getReqSpecBuilderForWeatherApi().
                addQueryParam("lat", lat).addQueryParam("lon", lon);
    }

    @And("searched website for city name {string}")
    public void searched_website_for_city_name(String cityName) throws IOException, InterruptedException {

        driver = getWebDriver("chrome");
        setImplicitWait(5);
        setWindowToMaxSize();
        driver.get(getPropertyValue("url.of.application"));
        home = new HomePage(driver);
        home.getSearchBox().sendKeys(cityName);
        Thread.sleep(2000);
        home.getSearchBox().sendKeys(Keys.ARROW_DOWN, Keys.ENTER);

    }

    @When("API is triggered and temp is fetched")
    public void api_is_triggered() throws IOException {
        response = RestAssured.given().spec(reqSpecBuilder.build()).
                when().get(getPropertyValue("context.path.of.api")).
                then().spec(getResSpecBuilderForWeatherApi().build()).extract().response().asString();
        tempFromApiLayer = new JsonPath(response).getFloat("main.temp");
    }

    @And("website is searched and temp is fetched")
    public void website_is_searched() {
        city = new CityPage(driver);
        tempFromUiLayer = Float.parseFloat(city.getTempDiv().getText().substring(0, 2));
    }

    @Then("temperature data is matched as per variance value {string}")
    public void temperature_data_is_matched_as_per_variance_value(String tempVariance) {
        result = compareTwoTempValues(tempFromUiLayer, tempFromApiLayer, Float.parseFloat(tempVariance));
        //System.out.println(result);
    }

    @And("result is {string}")
    public void result_is(String match) {
        Assert.assertTrue(result == Boolean.parseBoolean(match));

    }

    @After()
    public void close_driver() {
        driver.close();
    }
}
