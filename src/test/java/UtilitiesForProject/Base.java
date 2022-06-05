package UtilitiesForProject;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.*;
import java.time.Duration;
import java.util.Properties;

public class Base {

    private final String PROPERTIES_PATH = "src/test/java/properties/application.properties";
    protected WebDriver driver;
    RequestSpecBuilder reqSpecBuilder;
    ResponseSpecBuilder resSpecBuilder;

    /*
   1. method to get RequestSpecBuilder with base URI and two static query Parameters
   2. also add request logging functionality to log request in a file
   3. set content type to json
    */
    public RequestSpecBuilder getReqSpecBuilderForWeatherApi() throws IOException {
        PrintStream printReqLogs = new PrintStream(new FileOutputStream("src/test/java/reqResLogs/ApiReqLogs.txt"));
        PrintStream printResLogs = new PrintStream(new FileOutputStream("src/test/java/reqResLogs/ApiResLogs.txt"));
        return reqSpecBuilder.setBaseUri(getPropertyValue("base.uri.of.application")).
                addQueryParam("appid", getPropertyValue("value.for.app.id")).
                addQueryParam("units", getPropertyValue("value.for.units")).setContentType(ContentType.JSON).
                addFilter(RequestLoggingFilter.logRequestTo(printReqLogs)).
                addFilter(RequestLoggingFilter.logRequestTo(printResLogs));
    }

    /*
    1. method to get ResponseSpecBuilder with specified status code as 200
    2. content type as Json
    */
    public ResponseSpecBuilder getResSpecBuilderForWeatherApi() {
        return resSpecBuilder.expectStatusCode(200).expectContentType(ContentType.JSON);
    }

    //method to pull values from the properties file
    public String getPropertyValue(String propertyName) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(PROPERTIES_PATH));
        return prop.getProperty(propertyName);
    }

    //method to get the WebDriver Instance using WebDriverManager
    public WebDriver getWebDriver(String browserName) {
        if (browserName.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("safari")) {
            WebDriverManager.safaridriver().setup();
            return new SafariDriver();
        } else if (browserName.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            return new EdgeDriver();
        } else {
            System.out.println("Browser name incorrect or not available - type chrome or safari or edge");
            return null;
        }
    }

    //method to set implicit wait in seconds
    public void setImplicitWait(long durationInSeconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(durationInSeconds));
    }

    //method to compare two values - UI Layer and API Layer
    public boolean compareTwoTempValues(float tempFromUiLayer, float tempFromApiLayer, float allowedVariance) {
        float diff = Math.abs(tempFromApiLayer - tempFromUiLayer);
        if (diff > allowedVariance) {
            System.out.println("The variance in temperature is more than the allowed variance");
            throw new RuntimeException("The variance in temperature is: " + diff + " which is more than the allowed variance value of: " + allowedVariance);
        } else
            return true;
    }


}
