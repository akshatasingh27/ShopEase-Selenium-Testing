
package org.example;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTut {

    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();

        try {
            // 1. Open the Selenium web form ONCE
            driver.manage().window().maximize();
            driver.get("https://www.selenium.dev/selenium/web/web-form.html");

            WebDriverWait wait = new WebDriverWait(
                    driver, Duration.ofSeconds(15));

            Thread.sleep(2000);

            // 2. Retrieve page information
            System.out.println("Title: " + driver.getTitle());
            System.out.println("Page Source Length: "
                    + driver.getPageSource().length());

            // 3. Locate and operate the text box
            WebElement textBox =
                    driver.findElement(By.name("my-text"));

            textBox.sendKeys("Hello Selenium");
            System.out.println("Text: " + textBox.getAttribute("value"));

            textBox.clear();
            textBox.sendKeys("Testing with Java");
            System.out.println("Updated Text: "
                    + textBox.getAttribute("value"));

            // 4. Operate the password field
            WebElement password =
                    driver.findElement(By.name("my-password"));

            password.sendKeys("Test123");
            System.out.println("Password field filled.");

            // 5. Operate the text area
            WebElement textArea =
                    driver.findElement(By.name("my-textarea"));

            textArea.sendKeys("This is a Selenium practical.");
            textArea.clear();
            textArea.sendKeys("Learning WebDriver in Java.");

            System.out.println("Text area filled.");

            // 6. Select the checkbox
            WebElement checkbox =
                    driver.findElement(By.name("my-check"));

            checkbox.click();

            System.out.println("Checkbox selected: "
                    + checkbox.isSelected());

            // 7. Select the radio button
            WebElement radio =
                    driver.findElement(By.id("my-radio-2"));

            radio.click();

            System.out.println("Radio selected: "
                    + radio.isSelected());

            // 8. Select an option from the dropdown
            WebElement dropdown =
                    driver.findElement(By.name("my-select"));

            Select select = new Select(dropdown);
            select.selectByVisibleText("Two");

            System.out.println("Selected option: "
                    + select.getFirstSelectedOption().getText());

            // 9. Check text box properties
            System.out.println("Text box displayed: "
                    + textBox.isDisplayed());

            System.out.println("Text box enabled: "
                    + textBox.isEnabled());

            System.out.println("Text box tag: "
                    + textBox.getTagName());

            // 10. Submit the form using Enter
            System.out.println(
                    "--- 7.16 Submitting the Form Using Enter ---");

            textBox.sendKeys(Keys.ENTER);

            // 11. Read the submission result
            System.out.println(
                    "--- 7.17 Reading the Submission Result ---");

            WebElement result = wait.until(
                    d -> d.findElement(By.id("message")));

            System.out.println("Result: " + result.getText());

        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // 12. Close the browser
            System.out.println("Closing the browser...");
            driver.quit();
            System.out.println("Done.");
        }
    }
}
