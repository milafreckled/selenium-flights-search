import org.junit.Test;
import org.openqa.selenium.*;
import org.junit.Assert;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class TestBookFlights {
        public static void selectDate(WebDriver driver, WebElement dateBtn,  int index){
            dateBtn.click();
            List<WebElement> dates = driver.findElement(By.cssSelector("[data-testid='DatePickerDesktop']"))
                    .findElements(By.tagName("button"));
            dates.get(index).click();
        };

        public static void driverWait(WebDriver driver){
            synchronized (driver) {
                try {
                    driver.wait((long) (Math.random() * 3000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public static void main(String[] args) {
            System.setProperty("webdriver.chrome.driver","src/main/chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
//            options.addArguments("--headless");
//            options.addArguments("--disable-web-security");
//            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--start-maximized");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");
//            options.addArguments("chrome.switches", "--disable-extensions");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            WebDriver driver= new ChromeDriver(options);
            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.DRIVER, Level.ALL);
            driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
            try {
                driver.get("https://www.skyscanner.pl/");
                Thread.sleep(3000);
                Assert.assertTrue(driver.getTitle().contains("Skyscanner"));
                driverWait(driver);
                WebElement consentButton = driver.findElement(By.cssSelector("button.BpkButtonBase_bpk-button__NmRiZ.UserPreferencesContent_buttons__YTQ4Y.UserPreferencesContent_acceptButton__NjQxZ"));
                Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                        .withTimeout(Duration.ofSeconds(2))
                        .pollingEvery(Duration.ofMillis(300))
                        .ignoring(ElementNotVisibleException.class);
                fluentWait.until(d -> {
                        consentButton.isDisplayed();
                        return true;
                });
                consentButton.click();

                WebElement from = driver.findElement(By.id("originInput-input"));
                ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", from);

                from.sendKeys("Warszawa");

                WebElement to = driver.findElement(By.id("destinationInput-input"));

                to.sendKeys("Paryż");
                WebElement departDateBtn = driver.findElement(By.cssSelector("[data-testid=depart-btn]"));
                selectDate(driver, departDateBtn, 1);
                WebElement returnDateBtn = driver.findElement(By.cssSelector("[data-testid=return-btn]"));

                selectDate(driver, returnDateBtn, 2);
                WebElement searchBtn = driver.findElement(By.cssSelector("[data-testid=desktop-cta]"));
                searchBtn.click();
                driverWait(driver);
                Thread.sleep(2000);
                String title = driver.getTitle();
//                Assert.assertTrue(title.contains("transport"));
                WebElement searchSummary = driver.findElement(By.cssSelector("#flights-search-summary-root"));
                fluentWait.until(d -> {
                    searchSummary.isDisplayed();
                    return true;
                });
                System.out.printf("Page title: %s, search summary: %s", title, searchSummary.getCssValue("id"));
//                Assert.assertThat(isDisplayed, searchSummary);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }finally {
                driver.close();
            }
        }
}
