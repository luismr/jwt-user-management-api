# Test Profiles Configuration

This directory contains Spring Boot profile configurations for different test scenarios.

## Available Test Profiles

### 1. Integration Profile (`application-integration.properties`)
- **Purpose**: Used for integration tests that require a real database connection
- **Database**: MySQL database at `localhost:3306/b2bapp`
- **Credentials**: 
  - Username: `root`
  - Password: `test123`
- **Usage**: Applied automatically to integration tests with `@ActiveProfiles("integration")`

### 2. Integration with Environment Variables (`application-integration-env.properties`)
- **Purpose**: Used for integration tests with environment-based credentials (more secure)
- **Database**: MySQL database (configurable via env vars)
- **Credentials**: Set via environment variables:
  - `DB_URL` (default: `jdbc:mysql://localhost:3306/b2bapp?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC`)
  - `DB_USERNAME` (default: `root`)
  - `DB_PASSWORD` (default: `test123`)
- **Usage**: Apply with `@ActiveProfiles("integration-env")`

### 3. Test Profile (`application-test.properties`)
- **Purpose**: Used for unit tests with in-memory database
- **Database**: H2 in-memory database
- **Credentials**: Default H2 credentials (sa/empty password)
- **Usage**: Can be applied with `@ActiveProfiles("test")`

## How to Use

### Integration Tests
```java
@SpringBootTest
@ActiveProfiles("integration")
class MyIntegrationTest {
    // Tests that connect to real MySQL database
}
```

### Integration Tests with Environment Variables
```java
@SpringBootTest
@ActiveProfiles("integration-env")
class MySecureIntegrationTest {
    // Tests that connect to database using env vars
}
```

### Unit Tests with H2
```java
@SpringBootTest
@ActiveProfiles("test")
class MyUnitTest {
    // Tests that use in-memory H2 database
}
```

### Running Tests with Profiles

#### Run only integration tests:
```bash
mvn test -Dtest="*IntegrationTest"
```

#### Run with specific profile:
```bash
mvn test -Dspring.profiles.active=integration
```

#### Run with environment variables:
```bash
export DB_URL=jdbc:mysql://localhost:3306/b2bapp?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
export DB_USERNAME=root
export DB_PASSWORD=test123
mvn test -Dspring.profiles.active=integration-env -Dtest="*IntegrationTest"
```

## Security Notes

- Integration test credentials are stored in test resources only
- These profiles are separate from production configurations
- Never commit production credentials to version control
- Consider using environment variables for sensitive data in CI/CD pipelines

## Database Requirements

### For Integration Tests
- MySQL server running on localhost:3306
- Database `b2bapp` must exist
- User `root` with password `test123` must have access
- Can be run using Docker: `docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=test123 mysql:8.0`

### For Unit Tests
- No external database required
- H2 dependency must be included in test scope (already configured in pom.xml)
