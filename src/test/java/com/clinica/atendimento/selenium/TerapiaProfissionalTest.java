package com.clinica.atendimento.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.TestNG;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Selenium tests for the Terapias functionality with Profissionais
 */
public class TerapiaProfissionalTest extends BaseSeleniumTest {

    // Locators for ProfissionalCadastro page elements
    private static final By NOVO_PROFISSIONAL_BUTTON = By.xpath("//button[contains(text(), 'Novo Profissional')]");
    private static final By NOME_INPUT = By.xpath("//label[text()='Nome']/following-sibling::input");
    private static final By EMAIL_INPUT = By.xpath("//label[text()='Email']/following-sibling::input");
    private static final By DATA_NASCIMENTO_INPUT = By.xpath("//label[text()='Data de nascimento']/following-sibling::input");
    private static final By SEXO_INPUT = By.xpath("//label[text()='Sexo']/following-sibling::input");
    private static final By SALVAR_BUTTON = By.xpath("//button[contains(text(), 'Salvar')]");
    private static final By TERAPIAS_TAB = By.xpath("//button[contains(text(), 'Terapias')]");
    private static final By PROFISSIONAIS_TABLE = By.xpath("//table[contains(@class, 'data-table')]");
    private static final By PROFISSIONAIS_TABLE_ROWS = By.xpath("//table[contains(@class, 'data-table')]/tbody/tr");
    private static final By EDITAR_BUTTON = By.xpath("//span[contains(text(), '📝')]/..");
    private static final By REMOVER_BUTTON = By.xpath("//span[contains(text(), '🗑️')]/..");
    private static final By ATUALIZAR_BUTTON = By.xpath("//button[contains(text(), 'Atualizar')]");

    /**
     * Test adding therapies to a professional
     */
    @Test
    public void testAddTerapiasToProfissional() {
        // Navigate to the application
        driver.get(baseUrl);
        waitForPageToLoad();

        // Navigate to the Profissional page
        driver.get(baseUrl + "/profissionais");
        waitForPageToLoad();

        // Create a new professional
        String testName = "Test Professional " + System.currentTimeMillis();
        String testEmail = "test" + System.currentTimeMillis() + "@example.com";
        createProfissional(testName, testEmail, LocalDate.now().minusYears(30), "M");

        // Edit the professional to add therapies
        WebElement row = findProfissionalRowByName(testName);
        Assert.assertNotNull(row, "Professional row should exist for editing");

        // Click the edit button in that row
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][1]"));
        editButton.click();

        // Click on Terapias tab
        waitAndClick(TERAPIAS_TAB);

        // Check if there are any available therapies
        List<WebElement> availableTerapias = driver.findElements(
                By.xpath("//div[contains(@class, 'list-box')][2]//div[contains(@class, 'list-item')]"));

        if (!availableTerapias.isEmpty()) {
            // Select the first available therapy
            availableTerapias.get(0).click();

            // Save the changes
            waitAndClick(SALVAR_BUTTON);
            waitForPageToLoad();

            // Verify the therapy was added to the professional
            row = findProfissionalRowByName(testName);
            Assert.assertNotNull(row, "Professional row should exist after adding therapy");

            // Check if the therapy tag exists in the row
            boolean hasTerapia = !row.findElements(By.xpath(".//div[contains(@class, 'terapia-tag')]")).isEmpty();
            Assert.assertTrue(hasTerapia, "Professional should have at least one therapy associated");
        }

        // Clean up - delete the test professional
        deleteProfissional(testName);
    }

    /**
     * Test adding multiple therapies to a professional
     */
    @Test
    public void testAddMultipleTerapiasToProfissional() {
        // Navigate to the application
        driver.get(baseUrl);
        waitForPageToLoad();

        // Navigate to the Profissional page
        driver.get(baseUrl + "/profissionais");
        waitForPageToLoad();

        // Create a new professional
        String testName = "Test Professional " + System.currentTimeMillis();
        String testEmail = "test" + System.currentTimeMillis() + "@example.com";
        createProfissional(testName, testEmail, LocalDate.now().minusYears(30), "M");

        // Edit the professional to add therapies
        WebElement row = findProfissionalRowByName(testName);
        Assert.assertNotNull(row, "Professional row should exist for editing");

        // Click the edit button in that row
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][1]"));
        editButton.click();

        // Click on Terapias tab
        waitAndClick(TERAPIAS_TAB);

        // Check if there are at least two available therapies
        List<WebElement> availableTerapias = driver.findElements(
                By.xpath("//div[contains(@class, 'list-box')][2]//div[contains(@class, 'list-item')]"));

        int therapiesAdded = 0;
        if (availableTerapias.size() >= 2) {
            // Select the first two available therapies
            availableTerapias.get(0).click();
            therapiesAdded++;

            availableTerapias.get(1).click();
            therapiesAdded++;

            // Save the changes
            waitAndClick(SALVAR_BUTTON);
            waitForPageToLoad();

            // Verify the therapies were added to the professional
            row = findProfissionalRowByName(testName);
            Assert.assertNotNull(row, "Professional row should exist after adding therapies");

            // Check if the therapy tags exist in the row
            List<WebElement> therapyTags = row.findElements(By.xpath(".//div[contains(@class, 'terapia-tag')]"));
            Assert.assertEquals(therapyTags.size(), therapiesAdded,
                    "Professional should have " + therapiesAdded + " therapies associated");
        }

        // Clean up - delete the test professional
        deleteProfissional(testName);
    }

    /**
     * Create a new professional with the given details
     */
    private void createProfissional(String nome, String email, LocalDate dataNascimento, String sexo) {
        // Click on "Novo Profissional" button
        waitAndClick(NOVO_PROFISSIONAL_BUTTON);

        // Fill in the form
        waitAndSendKeys(NOME_INPUT, nome);

        // Click on "Cadastro" tab (it should be active by default, but just to be sure)
        waitAndClick(By.xpath("//button[contains(text(), 'Cadastro')]"));

        // Fill in the rest of the form
        waitAndSendKeys(EMAIL_INPUT, email);
        waitAndSendKeys(DATA_NASCIMENTO_INPUT, dataNascimento.format(DateTimeFormatter.ISO_DATE));
        waitAndSendKeys(SEXO_INPUT, sexo);

        // Save the professional
        waitAndClick(SALVAR_BUTTON);

        // Wait for the table to update
        waitForPageToLoad();
    }

    /**
     * Delete a professional by name
     */
    private void deleteProfissional(String name) {
        // Find the row with the name
        WebElement row = findProfissionalRowByName(name);
        Assert.assertNotNull(row, "Professional row should exist for deletion");

        // Click the delete button in that row
        WebElement deleteButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][2]"));
        deleteButton.click();

        // Accept the confirmation dialog
        driver.switchTo().alert().accept();

        // Wait for the table to update
        waitForPageToLoad();
    }

    /**
     * Find a professional row by name
     */
    private WebElement findProfissionalRowByName(String name) {
        // Refresh the table first
        waitAndClick(ATUALIZAR_BUTTON);
        waitForPageToLoad();

        List<WebElement> rows = driver.findElements(PROFISSIONAIS_TABLE_ROWS);
        for (WebElement row : rows) {
            WebElement nameCell = row.findElement(By.xpath("./td[1]"));
            if (nameCell.getText().equals(name)) {
                return row;
            }
        }
        return null;
    }

    /**
     * Main method to run this test class through the SeleniumTestSuite
     * This ensures that the test runs in the same environment as when run through the test suite
     */
    public static void main(String[] args) {
        System.out.println("Running TerapiaProfissionalTest through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.run();
    }
}