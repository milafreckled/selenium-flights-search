import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.manager.SeleniumManagerOutput;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.service.DriverFinder;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariDriverService;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Driver  {
    DriverService service = new DriverService.Builder<GeckoDriverService, GeckoDriverService.Builder>() {
        @Override
        public int score(Capabilities capabilities) {
            return 0;
        }

        @Override
        protected void loadSystemProperties() {

        }

        @Override
        protected List<String> createArgs() {
            return null;
        }

        @Override
        protected GeckoDriverService createDriverService(File exe, int port, Duration timeout, List<String> args, Map<String, String> environment) {
            return null;
        }
    }.build();
    SeleniumManagerOutput.Result driverLocation = DriverFinder.getPath(service, new Capabilities() {
        @Override
        public String getBrowserName() {
            return null;
        }

        @Override
        public Platform getPlatform() {
            return null;
        }

        @Override
        public String getVersion() {
            return null;
        }

        @Override
        public boolean isJavascriptEnabled() {
            return false;
        }

        @Override
        public Map<String, ?> asMap() {
            return null;
        }

        @Override
        public Object getCapability(String capabilityName) {
            return null;
        }

        @Override
        public boolean is(String capabilityName) {
            return false;
        }
    });
    File driverFile = new File(driverLocation.driverPath);
    WebDriver driver;
    public Driver(String browsername, String[] optionsList) throws IOException {
        switch(browsername){
            case("chrome"):{
                System.setProperty("webdriver.chrome.driver","src/main/chromedriver");
                ChromeOptions options = new ChromeOptions();
                for (String option:optionsList) {
                    options.addArguments(option);
                }
                this.driver = new ChromeDriver();
                break;
            }
            case("safari"):{
                System.setProperty("webdriver.safari.driver", "src/main/safaridriver");
                SafariOptions options = new SafariOptions();
//                for (String option:optionsList) {
//                    options.
//                }
                SafariDriverService safariService = new SafariDriverService.Builder()
                        .withLogging(true)
                        .build();
                this.driver = new SafariDriver(options);
                break;
            }
            case("firefox"):{
                System.setProperty("webdriver.firefox.driver", "src/main/geckodriver");
                FirefoxOptions options = new FirefoxOptions();
                for (String option:optionsList) {
                    options.addArguments(option);
                }
                this.driver = new FirefoxDriver(options);
                options.setImplicitWaitTimeout(Duration.ofSeconds(1));
                break;
            }
        }
        System.setProperty("webdriver.chrome.driver","src/main/chromedriver");
        ChromeOptions options = new ChromeOptions();
        for (String option:optionsList) {
            options.addArguments(option);
        }
        WebDriver driver = new ChromeDriver();
    }
}
