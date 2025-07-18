package br.com.estimular.atendimento.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Base class for Selenium tests providing common functionality
 */
public class BaseSeleniumTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl = "http://localhost:8080"; // Adjust as needed

    // Static flag to track if we're running in the test suite
    private static boolean isRunningInTestSuite = false;

    // Set this flag to true when running from SeleniumTestSuite
    public static void setRunningInTestSuite(boolean value) {
        isRunningInTestSuite = value;
    }

    @BeforeMethod
    public void checkRunningEnvironment() {
        // Check if we're running as part of the test suite
        if (!isRunningInTestSuite) {
            // Also check the stack trace as a fallback
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement element : stackTrace) {
                if (element.getClassName().contains("SeleniumTestSuite")) {
                    isRunningInTestSuite = true;
                    break;
                }
            }

            // If still not running in test suite, skip the test
            if (!isRunningInTestSuite) {
                System.out.println("=================================================================");
                System.out.println("IMPORTANT: Selenium tests should be run through SeleniumTestSuite");
                System.out.println("Run the tests using one of the following commands:");
                System.out.println("  mvn test -Dtest=selenium.br.com.estimular.atendimento.SeleniumTestSuite");
                System.out.println("  or");
                System.out.println("  java -cp <classpath> selenium.br.com.estimular.atendimento.SeleniumTestSuite");
                System.out.println("=================================================================");

                // Skip the test using SkipException
                throw new SkipException("Test skipped: must be run through SeleniumTestSuite");
            }
        }
    }

    @BeforeClass
    public void setUp() {

        boolean firefoxAvailable = false;
        Exception firefoxException = null;
        boolean chromeAvailable = false;
        Exception chromeException = null;

        // Try Firefox first
        try {
            WebDriverManager.firefoxdriver().setup();

            // Configure Firefox options
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--headless"); // Run in headless mode (no UI)
            firefoxOptions.addArguments("--window-size=1920,1080");

            // Initialize the WebDriver with Firefox
            driver = new FirefoxDriver(firefoxOptions);
            System.out.println("Using Firefox WebDriver");
            firefoxAvailable = true;
        } catch (Exception e) {
            firefoxException = e;
            System.out.println("Firefox WebDriver initialization failed: " + e.getMessage());
        }

        // If Firefox failed, try Chrome
        if (!firefoxAvailable) {
            try {
                WebDriverManager.chromedriver().setup();

                // Configure Chrome options
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless"); // Run in headless mode (no UI)
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--window-size=1920,1080");

                // Initialize the WebDriver with Chrome
                driver = new ChromeDriver(chromeOptions);
                System.out.println("Using Chrome WebDriver");
                chromeAvailable = true;
            } catch (Exception e) {
                chromeException = e;
                System.out.println("Chrome WebDriver initialization failed: " + e.getMessage());
            }
        }

        // If both browsers failed, throw a more informative exception
        if (!firefoxAvailable && !chromeAvailable) {
            String errorMessage = "Neither Firefox nor Chrome could be initialized. " + "Please ensure at least one of these browsers is installed.\n" +
                    "Firefox error: " + firefoxException.getMessage() + "\n" +
                    "Chrome error: " + chromeException.getMessage();
            throw new RuntimeException(errorMessage);
        }

        // Configure wait timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Wait for an element to be clickable and then click it
     */
    protected void waitAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    /**
     * Wait for an element to be visible and then enter text
     */
    protected void waitAndSendKeys(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Wait for an element to be visible and then get its text
     */
    protected String waitAndGetText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }

    /**
     * Check if an element exists on the page
     */
    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Execute JavaScript in the browser
     */
    protected Object executeJavaScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Wait for page to load completely
     */
    protected void waitForPageToLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }
}
