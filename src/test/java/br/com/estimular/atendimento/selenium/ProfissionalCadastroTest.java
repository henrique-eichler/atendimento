package br.com.estimular.atendimento.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.TestNG;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Selenium tests for the ProfissionalCadastro component
 */
public class ProfissionalCadastroTest extends BaseSeleniumTest {

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
     * Test the complete CRUD flow for a professional
     */
    @Test
    public void testProfissionalCRUD() {
        // Navigate to the application
        driver.get(baseUrl);
        waitForPageToLoad();

        // Navigate to the Profissional page (assuming it's at /profissionais)
        // This might need to be adjusted based on your application's routing
        driver.get(baseUrl + "/profissionais");
        waitForPageToLoad();

        // Test Create: Add a new professional
        String testName = "Test Professional " + System.currentTimeMillis();
        String testEmail = "test" + System.currentTimeMillis() + "@example.com";
        createProfissional(testName, testEmail, LocalDate.now().minusYears(30), "M");

        // Verify the professional was added to the table
        Assert.assertTrue(isProfissionalInTable(testName),
                "The newly created professional should appear in the table");

        // Test Read: Verify professional details in the table
        WebElement row = findProfissionalRowByName(testName);
        Assert.assertNotNull(row, "Should find the professional row");

        // Test Update: Edit the professional
        String updatedName = testName + " Updated";
        editProfissional(testName, updatedName, testEmail, LocalDate.now().minusYears(35), "F");

        // Verify the professional was updated
        Assert.assertTrue(isProfissionalInTable(updatedName),
                "The updated professional name should appear in the table");

        // Test Delete: Remove the professional
        deleteProfissional(updatedName);

        // Verify the professional was removed
        Assert.assertFalse(isProfissionalInTable(updatedName),
                "The professional should be removed from the table");
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
     * Edit an existing professional
     */
    private void editProfissional(String currentName, String newName, String email, LocalDate dataNascimento, String sexo) {
        // Find the row with the current name
        WebElement row = findProfissionalRowByName(currentName);
        Assert.assertNotNull(row, "Professional row should exist for editing");

        // Click the edit button in that row
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][1]"));
        editButton.click();

        // Update the form
        waitAndSendKeys(NOME_INPUT, newName);

        // Click on "Cadastro" tab
        waitAndClick(By.xpath("//button[contains(text(), 'Cadastro')]"));

        // Update the rest of the form
        waitAndSendKeys(EMAIL_INPUT, email);
        waitAndSendKeys(DATA_NASCIMENTO_INPUT, dataNascimento.format(DateTimeFormatter.ISO_DATE));
        waitAndSendKeys(SEXO_INPUT, sexo);

        // Save the changes
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
     * Check if a professional with the given name exists in the table
     */
    private boolean isProfissionalInTable(String name) {
        // Refresh the table first
        waitAndClick(ATUALIZAR_BUTTON);
        waitForPageToLoad();

        // Check if the professional exists in the table
        List<WebElement> rows = driver.findElements(PROFISSIONAIS_TABLE_ROWS);
        for (WebElement row : rows) {
            WebElement nameCell = row.findElement(By.xpath("./td[1]"));
            if (nameCell.getText().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find a professional row by name
     */
    private WebElement findProfissionalRowByName(String name) {
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
        System.out.println("Running ProfissionalCadastroTest through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.run();
    }
}
