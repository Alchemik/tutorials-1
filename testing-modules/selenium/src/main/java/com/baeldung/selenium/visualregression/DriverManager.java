package com.baeldung.selenium.visualregression;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import static java.text.MessageFormat.format;

public class DriverManager {

    private WebDriver driver;

    public void startChromeInCloud()  {
        final String LT_USERNAME = System.getenv("LT_USERNAME");
        final String LT_ACCESS_KEY = System.getenv("LT_ACCESS_KEY");
        final String GRID_URL = "@hub.lambdatest.com/wd/hub";

        final ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("latest");
        browserOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        final HashMap<String, Object> ltOptions = getLambdaTestOptions();
        browserOptions.setCapability("LT:Options", ltOptions);

        try {
            this.driver = new RemoteWebDriver(new URL(format("https://{0}:{1}{2}", LT_USERNAME, LT_ACCESS_KEY, GRID_URL)), browserOptions);
        } catch (final MalformedURLException e) {
            throw new Error("Error in setting RemoteDriver's URL!");
        }
        this.driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(20));
    }

    private static HashMap<String, Object> getLambdaTestOptions() {
        final HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("resolution", "2560x1440");
        ltOptions.put("video", true);
        ltOptions.put("build", "smartui-demo");
        ltOptions.put("name", "visual regression with smartui");
        ltOptions.put("smartUI.project", "Visual Regression Selenium Demo");
        ltOptions.put("smartUI.baseline", false);
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");
        final HashMap<String, Object> smartOptions = new HashMap<>();
        smartOptions.put("largeImageThreshold", 1200);
        smartOptions.put("transparency", 0.3);
        smartOptions.put("errorType", "movement");
        ltOptions.put("smartUI.options", smartOptions);
        return ltOptions;
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public void quitDriver() {
        this.driver.quit();
    }

}
