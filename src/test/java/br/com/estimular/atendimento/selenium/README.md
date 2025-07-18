# Selenium Tests for Atendimento Application

This directory contains Selenium tests for the frontend components of the Atendimento application.

## Test Structure

- `BaseSeleniumTest.java`: Base class with common functionality for all Selenium tests
- `ProfissionalCadastroTest.java`: Tests for the professional registration component
- `SalaCadastroTest.java`: Tests for the room registration component
- `SeleniumTestSuite.java`: Test suite to run all Selenium tests together

## Prerequisites

Before running the tests, ensure you have:

1. Java 21 installed
2. Maven installed
3. Firefox browser installed (preferred) or Chrome browser installed
4. The application running locally on port 8080 (or update the `baseUrl` in `BaseSeleniumTest.java`)

## Running the Tests

### Important Note

The Selenium tests must be run through the SeleniumTestSuite or the RunSeleniumTests utility class to ensure they run in the correct environment. Running the test classes directly may result in errors if the required browsers are not installed.

### Using the RunSeleniumTests Utility

The easiest way to run the tests is using the RunSeleniumTests utility class:

```bash
# Run all tests
java -cp target/test-classes:target/classes:path/to/dependencies selenium.br.com.estimular.atendimento.RunSeleniumTests

# Run only ProfissionalCadastroTest
java -cp target/test-classes:target/classes:path/to/dependencies selenium.br.com.estimular.atendimento.RunSeleniumTests profissional

# Run only SalaCadastroTest
java -cp target/test-classes:target/classes:path/to/dependencies selenium.br.com.estimular.atendimento.RunSeleniumTests sala
```

### Using Maven

To run all Selenium tests:

```bash
mvn test -Dtest=selenium.br.com.estimular.atendimento.SeleniumTestSuite
```

To run specific test groups:

```bash
# Run only ProfissionalCadastroTest
mvn test -Dtest=selenium.br.com.estimular.atendimento.SeleniumTestSuite -Dgroups=profissional

# Run only SalaCadastroTest
mvn test -Dtest=selenium.br.com.estimular.atendimento.SeleniumTestSuite -Dgroups=sala
```

### From an IDE

You can also run the tests from your IDE:

1. Open the SeleniumTestSuite class or the RunSeleniumTests class
2. Right-click on the class or method
3. Select "Run" or "Debug"

Do not run the individual test classes directly, as they may fail if the required browsers are not installed.

## Test Configuration

The tests are configured to run in headless mode by default. If you want to see the browser UI during test execution, modify the `setUp()` method in `BaseSeleniumTest.java` and remove the `--headless` option from the Firefox or Chrome options (depending on which browser is being used).

## Troubleshooting

If the tests fail, check the following:

1. Make sure the application is running and accessible at the URL specified in `baseUrl`
2. Verify that Firefox (preferred) or Chrome browser is installed and compatible with the WebDriver version
3. Check the console output to see which browser is being used (Firefox or Chrome)
4. Check if the XPath locators in the test classes match the actual elements in the application
5. Increase the wait timeouts if the application is slow to respond

## Adding New Tests

To add new tests:

1. Create a new test class that extends `BaseSeleniumTest`
2. Define locators for the elements you want to interact with
3. Implement test methods using the helper methods from the base class
4. Add assertions to verify the expected behavior
