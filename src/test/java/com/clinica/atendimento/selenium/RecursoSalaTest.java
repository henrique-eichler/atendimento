package com.clinica.atendimento.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.TestNG;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Selenium tests for the Recursos functionality in the SalaCadastro component
 */
public class RecursoSalaTest extends BaseSeleniumTest {

    // Locators for SalaCadastro page elements
    private static final By NOVA_SALA_BUTTON = By.xpath("//button[contains(text(), 'Nova Sala')]");
    private static final By NUMERO_INPUT = By.xpath("//label[text()='Número']/following-sibling::input");
    private static final By SALVAR_BUTTON = By.xpath("//button[contains(text(), 'Salvar')]");
    private static final By RECURSOS_TAB = By.xpath("//button[contains(text(), 'Recursos')]");
    private static final By SALAS_TABLE = By.xpath("//table[contains(@class, 'data-table')]");
    private static final By SALAS_TABLE_ROWS = By.xpath("//table[contains(@class, 'data-table')]/tbody/tr");
    private static final By EDITAR_BUTTON = By.xpath("//span[contains(text(), '📝')]/..");
    private static final By REMOVER_BUTTON = By.xpath("//span[contains(text(), '🗑️')]/..");
    private static final By ATUALIZAR_BUTTON = By.xpath("//button[contains(text(), 'Atualizar')]");

    /**
     * Test adding resources to a room
     */
    @Test
    public void testAddRecursosToSala() {
        // Navigate to the application
        driver.get(baseUrl);
        waitForPageToLoad();

        // Navigate to the Sala page
        driver.get(baseUrl + "/salas");
        waitForPageToLoad();

        // Create a new room
        int testRoomNumber = (int) (Math.random() * 1000) + 100;
        createSala(testRoomNumber);

        // Edit the room to add resources
        WebElement row = findSalaRowByNumber(testRoomNumber);
        Assert.assertNotNull(row, "Room row should exist for editing");

        // Click the edit button in that row
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][1]"));
        editButton.click();

        // Click on Recursos tab
        waitAndClick(RECURSOS_TAB);

        // Check if there are any available resources
        List<WebElement> availableRecursos = driver.findElements(
                By.xpath("//div[contains(@class, 'list-box')][2]//div[contains(@class, 'list-item')]"));

        if (!availableRecursos.isEmpty()) {
            // Select the first available resource
            availableRecursos.get(0).click();

            // Save the changes
            waitAndClick(SALVAR_BUTTON);
            waitForPageToLoad();

            // Verify the resource was added to the room
            row = findSalaRowByNumber(testRoomNumber);
            Assert.assertNotNull(row, "Room row should exist after adding resource");

            // Check if the resource tag exists in the row
            boolean hasRecurso = !row.findElements(By.xpath(".//div[contains(@class, 'recurso-tag')]")).isEmpty();
            Assert.assertTrue(hasRecurso, "Room should have at least one resource associated");
        }

        // Clean up - delete the test room
        deleteSala(testRoomNumber);
    }

    /**
     * Test adding multiple resources to a room
     */
    @Test
    public void testAddMultipleRecursosToSala() {
        // Navigate to the application
        driver.get(baseUrl);
        waitForPageToLoad();

        // Navigate to the Sala page
        driver.get(baseUrl + "/salas");
        waitForPageToLoad();

        // Create a new room
        int testRoomNumber = (int) (Math.random() * 1000) + 100;
        createSala(testRoomNumber);

        // Edit the room to add resources
        WebElement row = findSalaRowByNumber(testRoomNumber);
        Assert.assertNotNull(row, "Room row should exist for editing");

        // Click the edit button in that row
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][1]"));
        editButton.click();

        // Click on Recursos tab
        waitAndClick(RECURSOS_TAB);

        // Check if there are at least two available resources
        List<WebElement> availableRecursos = driver.findElements(
                By.xpath("//div[contains(@class, 'list-box')][2]//div[contains(@class, 'list-item')]"));

        int resourcesAdded = 0;
        if (availableRecursos.size() >= 2) {
            // Select the first two available resources
            availableRecursos.get(0).click();
            resourcesAdded++;

            availableRecursos.get(1).click();
            resourcesAdded++;

            // Save the changes
            waitAndClick(SALVAR_BUTTON);
            waitForPageToLoad();

            // Verify the resources were added to the room
            row = findSalaRowByNumber(testRoomNumber);
            Assert.assertNotNull(row, "Room row should exist after adding resources");

            // Check if the resource tags exist in the row
            List<WebElement> resourceTags = row.findElements(By.xpath(".//div[contains(@class, 'recurso-tag')]"));
            Assert.assertEquals(resourceTags.size(), resourcesAdded,
                    "Room should have " + resourcesAdded + " resources associated");
        }

        // Clean up - delete the test room
        deleteSala(testRoomNumber);
    }

    /**
     * Create a new room with the given number
     */
    private void createSala(int numero) {
        // Click on "Nova Sala" button
        waitAndClick(NOVA_SALA_BUTTON);

        // Fill in the form
        waitAndSendKeys(NUMERO_INPUT, String.valueOf(numero));

        // Save the room
        waitAndClick(SALVAR_BUTTON);

        // Wait for the table to update
        waitForPageToLoad();
    }

    /**
     * Delete a room by number
     */
    private void deleteSala(int numero) {
        // Find the row with the number
        WebElement row = findSalaRowByNumber(numero);
        Assert.assertNotNull(row, "Room row should exist for deletion");

        // Click the delete button in that row
        WebElement deleteButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][2]"));
        deleteButton.click();

        // Accept the confirmation dialog
        driver.switchTo().alert().accept();

        // Wait for the table to update
        waitForPageToLoad();
    }

    /**
     * Find a room row by number
     */
    private WebElement findSalaRowByNumber(int numero) {
        // Refresh the table first
        waitAndClick(ATUALIZAR_BUTTON);
        waitForPageToLoad();

        List<WebElement> rows = driver.findElements(SALAS_TABLE_ROWS);
        for (WebElement row : rows) {
            WebElement numberCell = row.findElement(By.xpath("./td[1]"));
            if (numberCell.getText().equals(String.valueOf(numero))) {
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
        System.out.println("Running RecursoSalaTest through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.run();
    }
}