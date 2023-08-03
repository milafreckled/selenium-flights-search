import org.junit.Test;
import org.openqa.selenium.By;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;

public class TestBookFlights {
        public static void selectDate(WebDriver driver, WebElement dateBtn, WebElement pickDate){
            dateBtn.click();
            pickDate.click();
        };
        @Test
        public void main(){
            System.setProperty("webdriver.chrome.driver","src/main/chromedriver");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--start-maximized");
            options.addArguments("chrome.switches", "--disable-extensions");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            WebDriver driver= new ChromeDriver(options);
//            options.merge(capabilities);
//            driver.manage().window().maximize();
            try {
                driver.get("https://www.skyscanner.pl/");
                Assert.assertTrue(driver.getTitle().contains("Skyscanner"));
                synchronized (driver) {
                    try {
                        driver.wait((long) (Math.random() * 100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                WebElement cookiesBtn = new WebDriverWait(driver, 10)
//                        .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"user-preferences-dialog\"]/div/div/div[2]/button[1]")));
//                cookiesBtn.click();
                WebElement from = driver.findElement(By.cssSelector("#originInput-input"));

                from.sendKeys("Warszawa");

                WebElement to = driver.findElement(By.cssSelector("#destinationInput-input"));

                to.sendKeys("Paryż");
                WebElement departDateBtn = driver.findElement(By.cssSelector("[data-testid=depart-btn]"));
                departDateBtn.click();

                selectDate(driver, departDateBtn, driver.findElement(By.cssSelector(".CustomCalendar_today__MDBkM")));
                WebElement returnDateBtn = driver.findElement(By.cssSelector("[data-testid=return-btn]"));
                returnDateBtn.click();
                selectDate(driver, returnDateBtn, driver.findElement(By.cssSelector(".CustomCalendar_day__OWJhO[aria-hidden=false]")));
                WebElement searchBtn = driver.findElement(By.xpath("/html/body/div[1]/div/div[1]/div/main/div[1]/div/div/div/button"));
                searchBtn.click();
                String title = driver.getTitle();
                Assert.assertTrue(title.contains("transport"));
                WebElement searchSummary = driver.findElement(By.cssSelector("#flights-search-summary-root"))
//                Assert.assertThat(isDisplayed, searchSummary);
                driver.wait(2000);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }finally {
                driver.close();
            }
        }
}
