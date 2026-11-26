
package com.selenium;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class AppTest {

    static WebDriver driver;
    static WebDriverWait wait;

    static final String BASE_URL =
            "http://127.0.0.1:5500/web-app/";

    static final int TEST_PAUSE = 1800;
    static final int ACTION_PAUSE = 900;

    static String currentTestName = "";
    static int currentTestNumber = 0;

    @BeforeAll
    static void startBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("\n==================================");
        System.out.println(" SHOP EASE AUTOMATION TESTING");
        System.out.println(" Total Test Cases: 25");
        System.out.println("==================================\n");
    }

    @BeforeEach
    void startTest(TestInfo info) {
        currentTestName = info.getDisplayName();

        try {
            currentTestNumber = Integer.parseInt(
                    currentTestName.substring(4, 6));
        } catch (Exception e) {
            currentTestNumber++;
        }

        System.out.println("\n----------------------------------");
        System.out.println("TEST CASE: " + currentTestNumber + "/25");
        System.out.println("TEST NAME: " + currentTestName);
        System.out.println("----------------------------------");

        pause(TEST_PAUSE);
    }

    @AfterEach
    void finishTest() {
        try {
            showBanner("TEST CASE " + currentTestNumber
                    + "/25 COMPLETED: " + currentTestName);

            takeScreenshot(currentTestName);
            pause(TEST_PAUSE);

        } catch (Exception e) {
            System.out.println("Could not finish test display: "
                    + e.getMessage());
        }
    }

    @AfterAll
    static void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }

        System.out.println("\nALL TEST CASES FINISHED.");
        System.out.println("Check the test-screenshots folder.");
    }

    void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void openPage(String page) {
        driver.get(BASE_URL + page);

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.tagName("body")));

        showBanner("TEST CASE " + currentTestNumber
                + "/25: " + currentTestName);

        pause(ACTION_PAUSE);
    }

    void showBanner(String message) {
        ((JavascriptExecutor) driver).executeScript(
                "let old = document.getElementById('selenium-test-banner');" +
                "if(old) old.remove();" +
                "let b = document.createElement('div');" +
                "b.id = 'selenium-test-banner';" +
                "b.innerText = arguments[0];" +
                "b.style.position = 'fixed';" +
                "b.style.top = '0';" +
                "b.style.left = '0';" +
                "b.style.width = '100%';" +
                "b.style.padding = '15px';" +
                "b.style.background = '#172554';" +
                "b.style.color = 'white';" +
                "b.style.fontSize = '17px';" +
                "b.style.fontWeight = 'bold';" +
                "b.style.textAlign = 'center';" +
                "b.style.zIndex = '999999';" +
                "b.style.boxSizing = 'border-box';" +
                "document.body.appendChild(b);" +
                "document.body.style.paddingTop = '55px';",
                message);
    }

    void takeScreenshot(String name) {
        try {
            Path folder = Paths.get("test-screenshots");
            Files.createDirectories(folder);

            File image = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            Files.copy(
                    image.toPath(),
                    folder.resolve(name + ".png"),
                    StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot saved: "
                    + folder.toAbsolutePath().resolve(name + ".png"));

        } catch (Exception e) {
            System.out.println("Screenshot error: " + e.getMessage());
        }
    }

    JavascriptExecutor js() {
        return (JavascriptExecutor) driver;
    }

    // TEST 01: Home page opens
    @Test
    public void test01HomePageOpens() {
        openPage("index.html");

        assertTrue(driver.getCurrentUrl().contains("index.html"));
        System.out.println("PASS: Home page opened.");
    }

    // TEST 02: Home page title is correct
    @Test
    public void test02HomePageTitle() {
        openPage("index.html");

        assertTrue(driver.getTitle().contains("ShopEase"));
        System.out.println("PASS: Website title verified.");
    }

    // TEST 03: Home page has navigation links
    @Test
    public void test03NavigationLinks() {
        openPage("index.html");

        int count = driver.findElements(
                By.cssSelector("header a")).size();

        assertTrue(count >= 3);
        System.out.println("PASS: Navigation links found: " + count);
    }

    // TEST 04: Products page opens
    @Test
    public void test04ProductsPageOpens() {
        openPage("products.html");

        assertTrue(driver.getCurrentUrl().contains("products.html"));
        System.out.println("PASS: Products page opened.");
    }

    // TEST 05: Product cards are available
    @Test
    public void test05ProductCardsDisplayed() {
        openPage("products.html");

        int count = driver.findElements(
                By.cssSelector(".product-card")).size();

        assertTrue(count > 0);
        System.out.println("PASS: Product cards found: " + count);
    }

    // TEST 06: Product names and prices are available
    @Test
    public void test06ProductNamesAndPrices() {
        openPage("products.html");

        int names = driver.findElements(
                By.cssSelector(".product-name")).size();

        int prices = driver.findElements(
                By.cssSelector(".product-price")).size();

        assertTrue(names > 0);
        assertEquals(names, prices);

        System.out.println("PASS: Product names and prices verified.");
    }

    // TEST 07: Search finds an existing product
    @Test
    public void test07SearchExistingProduct() {
        openPage("products.html");

        WebElement search = driver.findElement(By.id("searchInput"));
        search.clear();
        search.sendKeys("Lipstick");

        driver.findElement(By.id("searchButton")).click();
        pause(ACTION_PAUSE);

        boolean found = false;

        for (WebElement p :
                driver.findElements(By.cssSelector(".product-card"))) {
            if (p.isDisplayed()
                    && p.getText().toLowerCase().contains("lipstick")) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Lipstick should appear in search results.");
        System.out.println("PASS: Existing product search works.");
    }

    // TEST 08: Search hides products that do not match
    @Test
    public void test08SearchNonExistingProduct() {
        openPage("products.html");

        WebElement search = driver.findElement(By.id("searchInput"));
        search.clear();
        search.sendKeys("xyzproductnotfound");

        driver.findElement(By.id("searchButton")).click();
        pause(ACTION_PAUSE);

        boolean found = false;

        for (WebElement p :
                driver.findElements(By.cssSelector(".product-card"))) {
            if (p.isDisplayed()) {
                found = true;
                break;
            }
        }

        assertFalse(found,
                "No product cards should match this search.");

        System.out.println("PASS: No-match search handled.");
    }

    // TEST 09: Search input can be cleared
    @Test
    public void test09ClearSearchInput() {
        openPage("products.html");

        WebElement search = driver.findElement(By.id("searchInput"));
        search.sendKeys("Lipstick");
        search.clear();

        assertEquals("", search.getAttribute("value"));
        System.out.println("PASS: Search input cleared.");
    }

    // TEST 10: Add one product to cart
    @Test
    public void test10AddOneProductToCart() {
        openPage("products.html");

        js().executeScript(
                "localStorage.removeItem('shopEaseCart');");
        driver.navigate().refresh();
        pause(ACTION_PAUSE);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".add-cart"))).click();

        Object count = js().executeScript(
                "return JSON.parse(localStorage.getItem('shopEaseCart') || '[]').length;");

        assertTrue(((Number) count).intValue() > 0);
        System.out.println("PASS: One product added to cart.");
    }

    // TEST 11: Add two products to cart
    @Test
    public void test11AddTwoProductsToCart() {
        openPage("products.html");

        js().executeScript(
                "localStorage.removeItem('shopEaseCart');");
        driver.navigate().refresh();
        pause(ACTION_PAUSE);

        driver.findElements(By.cssSelector(".add-cart")).get(0).click();
        pause(ACTION_PAUSE);

        driver.findElements(By.cssSelector(".add-cart")).get(1).click();
        pause(ACTION_PAUSE);

        Object count = js().executeScript(
                "return JSON.parse(localStorage.getItem('shopEaseCart') || '[]').length;");

        assertTrue(((Number) count).intValue() >= 2);
        System.out.println("PASS: Multiple products added to cart.");
    }

    // TEST 12: Cart page opens
    @Test
    public void test12CartPageOpens() {
        openPage("cart.html");

        assertTrue(driver.getCurrentUrl().contains("cart.html"));
        assertTrue(driver.findElement(By.id("cartItems")).isDisplayed());

        System.out.println("PASS: Cart page opened.");
    }

    // TEST 13: Cart subtotal is displayed
    @Test
    public void test13SubtotalDisplayed() {
        openPage("cart.html");

        assertTrue(driver.findElement(By.id("subtotal")).isDisplayed());
        System.out.println("PASS: Subtotal displayed.");
    }

    // TEST 14: Delivery charge is displayed
    @Test
    public void test14DeliveryChargeDisplayed() {
        openPage("cart.html");

        assertTrue(driver.findElement(By.id("delivery")).isDisplayed());
        System.out.println("PASS: Delivery charge displayed.");
    }

    // TEST 15: Final total is displayed
    @Test
    public void test15FinalTotalDisplayed() {
        openPage("cart.html");

        assertTrue(driver.findElement(By.id("total")).isDisplayed());
        System.out.println("PASS: Final total displayed.");
    }

    // TEST 16: Checkout button is enabled
    @Test
    public void test16CheckoutButtonEnabled() {
        openPage("cart.html");

        WebElement button = driver.findElement(By.id("checkoutButton"));

        assertTrue(button.isDisplayed());
        assertTrue(button.isEnabled());

        System.out.println("PASS: Checkout button is enabled.");
    }

    // TEST 17: Checkout page opens
    @Test
    public void test17CheckoutPageOpens() {
        openPage("checkout.html");

        assertTrue(driver.getCurrentUrl().contains("checkout.html"));
        System.out.println("PASS: Checkout page opened.");
    }

    // TEST 18: Login page opens
    @Test
    public void test18LoginPageOpens() {
        openPage("login.html");

        assertTrue(driver.getCurrentUrl().contains("login.html"));
        System.out.println("PASS: Login page opened.");
    }

    // TEST 19: Login fields are visible
    @Test
    public void test19LoginFieldsDisplayed() {
        openPage("login.html");

        assertTrue(driver.findElement(By.id("username")).isDisplayed());
        assertTrue(driver.findElement(By.id("password")).isDisplayed());
        assertTrue(driver.findElement(By.id("loginButton")).isDisplayed());

        System.out.println("PASS: Login fields displayed.");
    }

    // TEST 20: Invalid login should not open account
    @Test
    public void test20InvalidLoginRejected() {
        openPage("login.html");

        driver.findElement(By.id("username")).sendKeys("wronguser");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.id("loginButton")).click();

        pause(1800);

        assertFalse(driver.getCurrentUrl().contains("my-account.html"));
        System.out.println("PASS: Invalid login did not open account.");
    }

    // TEST 21: Valid login should open account
    @Test
    public void test21ValidLogin() {
        openPage("index.html");

        js().executeScript(
                "let users = JSON.parse(localStorage.getItem('shopEaseUsers') || '[]');" +
                "users = users.filter(u => u.username !== 'seleniumtest');" +
                "users.push({username:'seleniumtest'," +
                "email:'seleniumtest@example.com'," +
                "password:'TestPass123'});" +
                "localStorage.setItem('shopEaseUsers', JSON.stringify(users));" +
                "localStorage.removeItem('shopEaseCurrentUser');");

        openPage("login.html");

        driver.findElement(By.id("username")).sendKeys("seleniumtest");
        driver.findElement(By.id("password")).sendKeys("TestPass123");
        driver.findElement(By.id("loginButton")).click();

        wait.until(ExpectedConditions.urlContains("my-account.html"));

        assertTrue(driver.getCurrentUrl().contains("my-account.html"));
        System.out.println("PASS: Valid login opened account.");
    }

    // TEST 22: My Account page opens
    @Test
    public void test22MyAccountPageOpens() {
        openPage("my-account.html");

        assertTrue(driver.getCurrentUrl().contains("my-account.html"));
        System.out.println("PASS: My Account page opened.");
    }

    // TEST 23: My Orders page opens
    @Test
    public void test23MyOrdersPageOpens() {
        openPage("my-orders.html");

        assertTrue(driver.getCurrentUrl().contains("my-orders.html"));
        System.out.println("PASS: My Orders page opened.");
    }

    // TEST 24: Order confirmation page opens
    @Test
    public void test24OrderConfirmationOpens() {
        openPage("order-confirmation.html");

        assertTrue(driver.getCurrentUrl().contains(
                "order-confirmation.html"));

        System.out.println("PASS: Order confirmation page opened.");
    }

    // TEST 25: Cart data remains after refresh
    @Test
    public void test25CartDataPersistsAfterRefresh() {
        openPage("products.html");

        js().executeScript(
                "localStorage.setItem('shopEaseCart', JSON.stringify([" +
                "{id:'1',name:'Lipstick',price:499,quantity:1}]));");

        driver.navigate().refresh();
        pause(ACTION_PAUSE);

        Object name = js().executeScript(
                "let c = JSON.parse(localStorage.getItem('shopEaseCart') || '[]');" +
                "return c.length ? c[0].name : null;");

        assertEquals("Lipstick", name);
        System.out.println("PASS: Cart data persisted after refresh.");
    }
}
