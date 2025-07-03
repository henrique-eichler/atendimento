package com.clinica.atendimento.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.TestNG;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Selenium tests for the SalaCadastro component
 */
public class SalaCadastroTest extends BaseSeleniumTest {

    // Locators for SalaCadastro page elements
    private static final By NOVA_SALA_BUTTON = By.xpath("//button[contains(text(), 'Nova Sala')]");
    private static final By NUMERO_INPUT = By.xpath("//label[text()='Número']/following-sibling::input");
    private static final By SALVAR_BUTTON = By.xpath("//button[contains(text(), 'Salvar')]");
    private static final By TERAPIAS_TAB = By.xpath("//button[contains(text(), 'Terapias')]");
    private static final By RECURSOS_TAB = By.xpath("//button[contains(text(), 'Recursos')]");
    private static final By CRONOGRAMA_TAB = By.xpath("//button[contains(text(), 'Cronograma')]");
    private static final By SALAS_TABLE = By.xpath("//table[contains(@class, 'data-table')]");
    private static final By SALAS_TABLE_ROWS = By.xpath("//table[contains(@class, 'data-table')]/tbody/tr");
    private static final By EDITAR_BUTTON = By.xpath("//span[contains(text(), '📝')]/..");
    private static final By REMOVER_BUTTON = By.xpath("//span[contains(text(), '🗑️')]/..");
    private static final By ATUALIZAR_BUTTON = By.xpath("//button[contains(text(), 'Atualizar')]");

    /**
     * Test the complete CRUD flow for a room
     */
    @Test
    public void testSalaCRUD() {
        // Navigate to the application
        driver.get(baseUrl);
        waitForPageToLoad();

        // Navigate to the Sala page (assuming it's at /salas)
        // This might need to be adjusted based on your application's routing
        driver.get(baseUrl + "/salas");
        waitForPageToLoad();

        // Test Create: Add a new room
        int testRoomNumber = (int) (Math.random() * 1000) + 100; // Random room number between 100-1099
        createSala(testRoomNumber);

        // Verify the room was added to the table
        Assert.assertTrue(isSalaInTable(testRoomNumber),
                "The newly created room should appear in the table");

        // Test Read: Verify room details in the table
        WebElement row = findSalaRowByNumber(testRoomNumber);
        Assert.assertNotNull(row, "Should find the room row");

        // Test Update: Edit the room
        int updatedRoomNumber = testRoomNumber + 1000;
        editSala(testRoomNumber, updatedRoomNumber);

        // Verify the room was updated
        Assert.assertTrue(isSalaInTable(updatedRoomNumber),
                "The updated room number should appear in the table");

        // Test Delete: Remove the room
        deleteSala(updatedRoomNumber);

        // Verify the room was removed
        Assert.assertFalse(isSalaInTable(updatedRoomNumber),
                "The room should be removed from the table");
    }

    /**
     * Test adding therapies to a room
     */
    @Test
    public void testAddTerapiasToSala() {
        // Navigate to the application
        driver.get(baseUrl);
        waitForPageToLoad();

        // Navigate to the Sala page
        driver.get(baseUrl + "/salas");
        waitForPageToLoad();

        // Create a new room
        int testRoomNumber = (int) (Math.random() * 1000) + 100;
        createSala(testRoomNumber);

        // Edit the room to add therapies
        WebElement row = findSalaRowByNumber(testRoomNumber);
        Assert.assertNotNull(row, "Room row should exist for editing");

        // Click the edit button in that row
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][1]"));
        editButton.click();

        // Click on Terapias tab
        waitAndClick(TERAPIAS_TAB);

        // Check if there are any available therapies
        List<WebElement> availableTherapies = driver.findElements(
                By.xpath("//div[contains(@class, 'list-box')][2]//div[contains(@class, 'list-item')]"));

        if (!availableTherapies.isEmpty()) {
            // Select the first available therapy
            availableTherapies.get(0).click();

            // Save the changes
            waitAndClick(SALVAR_BUTTON);
            waitForPageToLoad();

            // Verify the therapy was added to the room
            row = findSalaRowByNumber(testRoomNumber);
            Assert.assertNotNull(row, "Room row should exist after adding therapy");

            // Check if the therapy tag exists in the row
            boolean hasTerapia = !row.findElements(By.xpath(".//div[contains(@class, 'terapia-tag')]")).isEmpty();
            Assert.assertTrue(hasTerapia, "Room should have at least one therapy associated");
        }

        // Clean up - delete the test room
        deleteSala(testRoomNumber);
    }

    /**
     * Test setting up a room schedule
     */
    @Test
    public void testSetupRoomSchedule() {
        // Navigate to the application
        driver.get(baseUrl);
        waitForPageToLoad();

        // Navigate to the Sala page
        driver.get(baseUrl + "/salas");
        waitForPageToLoad();

        // Create a new room
        int testRoomNumber = (int) (Math.random() * 1000) + 100;
        createSala(testRoomNumber);

        // Edit the room to set up schedule
        WebElement row = findSalaRowByNumber(testRoomNumber);
        Assert.assertNotNull(row, "Room row should exist for editing");

        // Click the edit button in that row
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][1]"));
        editButton.click();

        // Click on Cronograma tab
        waitAndClick(CRONOGRAMA_TAB);

        // Select a time slot (Monday at 10:00)
        WebElement timeSlot = driver.findElement(
                By.xpath("//tr[contains(.,'10:00')]/td[contains(@class, '') and not(contains(@class, 'selected'))][2]"));
        timeSlot.click();

        // Save the changes
        waitAndClick(SALVAR_BUTTON);
        waitForPageToLoad();

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
     * Edit an existing room
     */
    private void editSala(int currentNumero, int newNumero) {
        // Find the row with the current number
        WebElement row = findSalaRowByNumber(currentNumero);
        Assert.assertNotNull(row, "Room row should exist for editing");

        // Click the edit button in that row
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'btn-icon')][1]"));
        editButton.click();

        // Update the form
        waitAndSendKeys(NUMERO_INPUT, String.valueOf(newNumero));

        // Save the changes
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
     * Check if a room with the given number exists in the table
     */
    private boolean isSalaInTable(int numero) {
        // Refresh the table first
        waitAndClick(ATUALIZAR_BUTTON);
        waitForPageToLoad();

        // Check if the room exists in the table
        List<WebElement> rows = driver.findElements(SALAS_TABLE_ROWS);
        for (WebElement row : rows) {
            WebElement numberCell = row.findElement(By.xpath("./td[1]"));
            if (numberCell.getText().equals(String.valueOf(numero))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find a room row by number
     */
    private WebElement findSalaRowByNumber(int numero) {
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
        System.out.println("Running SalaCadastroTest through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.run();
    }
}
