package ru.ibs.ui.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

/**
 * Класс для организации доступа к вебрайверу
 */
public class UiHooks {
    /**
     * Экземпляр Chrome Driver
     */
    static WebDriver chromeDriver;

    /**
     * Explicitly wait
     */
    public static WebDriverWait wait;

    /**
     * Стартовый URL
     */
    static String baseUrl;

    /**
     * Выполняется перед каждым тестом
     */
    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
        chromeDriver = new ChromeDriver();
        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
        chromeDriver.manage().window().maximize();
        chromeDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        Properties uiProperties = new Properties();
        try {
            uiProperties.load(getClass().getResourceAsStream("/ui.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Не удалось загрузить файл ui.properties",e);
        }
        baseUrl=uiProperties.getProperty("base.url");
    }

    /**
     * Выполняется после каждого теста
     */
    @After
    public void after() {
        chromeDriver.quit();
    }

}
