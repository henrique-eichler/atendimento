package br.com.estimular.atendimento.selenium;

import org.testng.TestNG;

/**
 * Utility class to run Selenium tests through the test suite.
 * This ensures that the tests run in the correct environment.
 */
public class RunSeleniumTests {

    /**
     * Run all Selenium tests through the test suite
     */
    public static void runAllTests() {
        System.out.println("Running all Selenium tests through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.run();
    }

    /**
     * Run ProfissionalCadastroTest through the test suite
     */
    public static void runProfissionalTests() {
        System.out.println("Running ProfissionalCadastroTest through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.setGroups("profissional");
        testNG.run();
    }

    /**
     * Run SalaCadastroTest through the test suite
     */
    public static void runSalaTests() {
        System.out.println("Running SalaCadastroTest through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.setGroups("sala");
        testNG.run();
    }

    /**
     * Run RecursoSalaTest through the test suite
     */
    public static void runRecursoTests() {
        System.out.println("Running RecursoSalaTest through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.setGroups("recurso");
        testNG.run();
    }

    /**
     * Run TerapiaProfissionalTest through the test suite
     */
    public static void runTerapiaTests() {
        System.out.println("Running TerapiaProfissionalTest through SeleniumTestSuite...");
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{SeleniumTestSuite.class});
        testNG.setGroups("terapia");
        testNG.run();
    }

    /**
     * Main method to run all tests
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            runAllTests();
        } else if (args[0].equalsIgnoreCase("profissional")) {
            runProfissionalTests();
        } else if (args[0].equalsIgnoreCase("sala")) {
            runSalaTests();
        } else if (args[0].equalsIgnoreCase("recurso")) {
            runRecursoTests();
        } else if (args[0].equalsIgnoreCase("terapia")) {
            runTerapiaTests();
        } else {
            System.out.println("Invalid argument. Use 'profissional', 'sala', 'recurso', 'terapia', or no argument to run all tests.");
        }
    }
}
