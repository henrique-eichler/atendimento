package br.com.estimular.atendimento.selenium;

import org.testng.TestNG;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * Test suite to run all Selenium tests
 */
public class SeleniumTestSuite {

    /**
     * Setup before running the test suite
     */
    @BeforeSuite
    public void setUp() {
        System.out.println("Starting Selenium Test Suite for Atendimento Application");

        // Set the flag in BaseSeleniumTest to indicate we're running in the test suite
        BaseSeleniumTest.setRunningInTestSuite(true);
    }

    /**
     * Run the ProfissionalCadastroTest
     */
    @Test(groups = {"profissional"})
    public void runProfissionalTests() {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{ProfissionalCadastroTest.class});
        testNG.run();
    }

    /**
     * Run the SalaCadastroTest
     */
    @Test(groups = {"sala"})
    public void runSalaTests() {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SalaCadastroTest.class});
        testNG.run();
    }

    /**
     * Run the RecursoSalaTest
     */
    @Test(groups = {"recurso"})
    public void runRecursoTests() {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{RecursoSalaTest.class});
        testNG.run();
    }

    /**
     * Run the TerapiaProfissionalTest
     */
    @Test(groups = {"terapia"})
    public void runTerapiaTests() {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{TerapiaProfissionalTest.class});
        testNG.run();
    }

    /**
     * Cleanup after running the test suite
     */
    @AfterSuite
    public void tearDown() {
        System.out.println("Completed Selenium Test Suite for Atendimento Application");
    }

    /**
     * Main method to run the test suite from command line
     */
    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.run();
    }
}
