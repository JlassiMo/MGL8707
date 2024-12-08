# MGL8707
Here is the improved version of the `README.md` for your project, "Profile Management," focusing on the framework features, assertion validation, test execution with groups, managing secret data, and data-driven testing using JSON files:

---

# Profile Management - API Test Automation Framework

## Overview

The **Profile Management** API test automation framework is designed for efficiently testing APIs that require authentication with access tokens. Built using **RestAssured** and **TestNG**, this framework allows for automated API testing with advanced features like assertion validation, group-based test execution, secure data management, and data-driven tests with JSON files. The framework generates comprehensive reports, making it ideal for integration with CI/CD pipelines.

## Key Features

- **RestAssured Integration**: Simplifies the process of making API requests and handling responses.
- **Assertion Validation**: Robust assertions for validating HTTP status codes, response bodies, headers, and other response attributes.
- **Test Execution with Groups**: Organize and execute tests based on groups, making it easier to run specific subsets of tests (e.g., smoke tests, regression tests).
- **Secret Data Management**: Manage sensitive information like tokens, API keys, and credentials securely.
- **Data-Driven Testing**: Use JSON files to provide input data for your tests, enabling flexible and reusable test cases.
- **Comprehensive Reporting**: Generate detailed test reports with status (pass/fail), execution time, and log output.

## Project Structure

```bash
profile-management/
│
├── pom.xml                  # Maven project file with dependencies
├── README.md                # Project documentation (this file)
├── src/                     # Source code for the project
│   ├── main/
│   └── test/
│       └── java/
│           └── api/
│               ├── ApiTest.java        # Main test class for API tests
│       
├── data/                    # JSON files for test data
│   └── testdata.json        # Test data for data-driven tests
├── target/                  # Generated files after compilation
└── reports/                 # Test reports generated after execution
```

## Prerequisites

- **Java 11 or higher**
- **Maven** for dependency management
- **IntelliJ IDEA** or another IDE
- **Docker Desktop** (if using API mocks)

## Framework Setup

### 1. Clone the repository

```bash
git clone https://github.com/JlassiMo/MGL8707
cd profile-management
```

### 2. Install dependencies

Maven will automatically manage dependencies. Ensure you have Maven installed, and run:

```bash
mvn clean install
```

## Test Execution

### Grouped Test Execution

To execute tests by groups (e.g., smoke, regression), use the following Maven command:

```bash
mvn test -Dgroups="regression"
```

You can organize your tests into various groups by using the `@Test` annotation in TestNG:

```java
@Test(groups = { "regression" })
public validateGetUserProfile() {
    // Test code here
}
```

### Data-Driven Testing with JSON

This framework supports data-driven testing using external JSON files. Place your JSON data files in the `data/` directory, and they will be used to supply test data.

Example JSON (`testdata.json`):

```json
[
  {
    "userId": 1,
    "userName": "testuser1"
  },
  {
    "userId": 2,
    "userName": "testuser2"
  }
]
```

In your test class, use the `DataProvider` from TestNG to load data from the JSON file:

```java
@Test(dataProvider = "getUserData", dataProviderClass = DataProviderUtil.class)
public void testGetUserProfile(Map<String, Object> testData) {
    int userId = (int) testData.get("userId");
    int expectedStatusCode = (int) testData.get("expectedStatusCode");
    
    RestAssured.given()
        .header("Authorization", "Bearer " + accessToken)
        .pathParam("userId", userId)
        .when()
        .get("/users/{userId}")
        .then()
        .statusCode(expectedStatusCode);
}
```

And the `DataProviderUtil` class will handle loading the JSON data:

```java
public class DataProviderUtil {

    @DataProvider(name = "getUserData")
    public static Object[][] getUserData() {
        return JsonFileReader.readJsonData("data/testdata.json");
    }
}
```

### Assertion Validation

The framework uses **RestAssured** for making requests and validating responses. Assertions are built into the tests to verify status codes, response body contents, and headers.

Example of validating a `GET` request:

```java
@Test
public void testGetUserProfile() {
    RestAssured.given()
        .header("Authorization", "Bearer " + accessToken)
        .when()
        .get("/users/1")
        .then()
        .statusCode(200)
        .body("userName", equalTo("testuser1"));
}
```

### Reports and Logging

Upon test execution, the framework generates reports under the `reports/` directory. You can extend reporting with **Allure** or **TestNG** listeners for better visualization.

To generate reports:

```bash
mvn test
```

Test results and logs will be output in the `target/surefire-reports/` folder, and you can integrate them into your CI/CD pipeline for continuous monitoring of test outcomes.

## Technologies Used

- **Java**: Core programming language.
- **RestAssured**: Library for REST API testing.
- **TestNG**: Testing framework for organizing test cases.
- **Maven**: Dependency management and build tool.
- **JSON**: Data-driven testing input format.
- **Docker Desktop**: For running API mocks or external dependencies.

## Future Improvements

- **CI/CD Integration**: Automate test execution via Jenkins or GitLab CI.
- **Improved Secret Management**: Use external vaults like AWS Secrets Manager.
- **OAuth2 Integration**: Implement token refresh logic.
- **Performance Testing**: Integrate performance tests using tools like **Gatling** or **JMeter**.
