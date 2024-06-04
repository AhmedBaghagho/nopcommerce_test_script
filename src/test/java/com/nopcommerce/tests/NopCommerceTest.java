package com.nopcommerce.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class NopCommerceTest {

    public static void main(String[] args) {
        // Set the system property for Edge WebDriver
        System.setProperty("webdriver.edge.driver", "D:\\edgedriver_win64\\msedgedriver.exe");

        // Create a new instance of EdgeDriver
        WebDriver driver = new EdgeDriver();

        try {
            // Open the website
            driver.get("https://demo.nopcommerce.com/");

            // Read data from JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode testData = objectMapper.readTree(new File("test-data/testdata.json"));

            // Extract registration data
            JsonNode registrationData = testData.get("registration");
            String firstName = registrationData.get("firstName").asText();
            String lastName = registrationData.get("lastName").asText();
            String email = registrationData.get("email").asText();
            String password = registrationData.get("password").asText();

            // Click on Register link
            WebElement registerLink = driver.findElement(By.className("ico-register"));
            registerLink.click();

            // Fill in registration form
            WebElement firstNameInput = driver.findElement(By.id("FirstName"));
            firstNameInput.sendKeys(firstName);

            WebElement lastNameInput = driver.findElement(By.id("LastName"));
            lastNameInput.sendKeys(lastName);

            WebElement emailInput = driver.findElement(By.id("Email"));
            emailInput.sendKeys(email);

            WebElement passwordInput = driver.findElement(By.id("Password"));
            passwordInput.sendKeys(password);

            WebElement confirmPasswordInput = driver.findElement(By.id("ConfirmPassword"));
            confirmPasswordInput.sendKeys(password);

            WebElement registerButton = driver.findElement(By.id("register-button"));
            registerButton.click();

            // Verify registration success message
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement registrationSuccessMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".result")));
            String registrationMessage = registrationSuccessMessage.getText();
            if (registrationMessage.contains("Your registration completed")) {
                System.out.println("Registration successful.");
            } else {
                System.out.println("Registration failed.");
            }

            // Navigate to login page
            driver.get("https://demo.nopcommerce.com/login");

            // Fill in login form with the newly created email
            WebElement emailLoginInput = driver.findElement(By.id("Email"));
            emailLoginInput.sendKeys(email);

            WebElement passwordLoginInput = driver.findElement(By.id("Password"));
            passwordLoginInput.sendKeys(password);

            // Move to the login button before clicking
            WebElement loginButton = driver.findElement(By.cssSelector(".login-button"));
            Actions actions = new Actions(driver);
            actions.moveToElement(loginButton).click().perform();

            // Verify login success
            driver.get("https://demo.nopcommerce.com/customer/info");
            WebElement firstNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FirstName")));
            String loggedinFirstName = firstNameElement.getAttribute("value");
            WebElement lastNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("LastName")));
            String loggedinLastName = lastNameElement.getAttribute("value");

            if (loggedinFirstName.equals(firstName) && loggedinLastName.equals(lastName)) {
                System.out.println("Login successful.");
            } else {
                System.out.println("Login failed.");
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}