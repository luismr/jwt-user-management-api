# Spring Boot Login Application with Spring Data REST

A Spring Boot application that provides RESTful APIs for user management using Spring Data REST and MySQL database connectivity.

## Features

- **Spring Boot 3.2.0** with Java 17
- **Spring Data JPA** for database operations
- **Spring Data REST** for automatic REST API generation
- **MySQL Database** connectivity
- **User Management** with roles and permissions
- **Validation** using Bean Validation
- **Automatic API Documentation** via Spring Data REST

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- MySQL server running on `localhost:3306`

## Database Setup

1. Ensure MySQL is running on localhost:3306
2. Create a database (optional - application can create tables automatically):
   ```sql
   CREATE DATABASE login_app;
   ```
3. The application is configured to connect with:
   - **URL**: `jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true`
   - **Username**: `root`
   - **Password**: `test123`

## Configuration

The application is configured in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=test123

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Spring Data REST Configuration
spring.data.rest.base-path=/api
```

## Running the Application

1. Clone or navigate to the project directory
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. The application will start on `http://localhost:8080`

## API Endpoints

Spring Data REST automatically generates RESTful endpoints for your entities. Base path: `/api`

### Users API

- **GET** `/api/users` - Get all users (paginated)
- **POST** `/api/users` - Create a new user
- **GET** `/api/users/{id}` - Get user by ID
- **PUT** `/api/users/{id}` - Update user
- **PATCH** `/api/users/{id}` - Partial update user
- **DELETE** `/api/users/{id}` - Delete user

#### Custom User Endpoints

- **GET** `/api/users/search/by-username?username={username}` - Find user by username
- **GET** `/api/users/search/by-email?email={email}` - Find user by email
- **GET** `/api/users/search/by-enabled?enabled={true/false}` - Find users by enabled status
- **GET** `/api/users/search/by-first-name?firstName={name}` - Find users by first name
- **GET** `/api/users/search/by-last-name?lastName={name}` - Find users by last name
- **GET** `/api/users/search/search-by-name?name={pattern}` - Search users by name pattern

### Roles API

- **GET** `/api/roles` - Get all roles
- **POST** `/api/roles` - Create a new role
- **GET** `/api/roles/{id}` - Get role by ID
- **PUT** `/api/roles/{id}` - Update role
- **DELETE** `/api/roles/{id}` - Delete role

#### Custom Role Endpoints

- **GET** `/api/roles/search/by-name?name={roleName}` - Find role by name
- **GET** `/api/roles/search/by-name-containing?name={pattern}` - Find roles containing name

### User Roles API

- **GET** `/api/user-roles` - Get all user role assignments
- **POST** `/api/user-roles` - Create user role assignment
- **GET** `/api/user-roles/{id}` - Get user role by ID

#### Custom User Role Endpoints

- **GET** `/api/user-roles/search/by-user-id?userId={id}` - Find roles by user ID
- **GET** `/api/user-roles/search/by-role-name?roleName={name}` - Find users with specific role

## Example API Usage

### Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### Create a Role

```bash
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ADMIN",
    "description": "Administrator role"
  }'
```

### Get All Users

```bash
curl http://localhost:8080/api/users
```

### Search User by Username

```bash
curl "http://localhost:8080/api/users/search/by-username?username=johndoe"
```

## API Discovery

Spring Data REST provides automatic API discovery:

- **GET** `/api` - API root with available resources
- **GET** `/api/profile` - ALPS (Application-Level Profile Semantics) metadata

## Database Schema

The application creates the following tables:

- `users` - User information
- `roles` - Role definitions
- `user_roles` - User role assignments
- `user_role_mapping` - Many-to-many mapping between user_roles and roles

## Development

### Project Structure

```
src/
├── main/
│   ├── java/com/example/login/
│   │   ├── LoginApplication.java       # Main application class
│   │   ├── entity/                     # JPA entities
│   │   │   ├── User.java
│   │   │   ├── Role.java
│   │   │   └── UserRole.java
│   │   └── repository/                 # Spring Data repositories
│   │       ├── UserRepository.java
│   │       ├── RoleRepository.java
│   │       └── UserRoleRepository.java
│   └── resources/
│       └── application.properties      # Application configuration
└── test/                              # Test classes
```

### Adding New Entities

1. Create entity class in `entity` package
2. Create repository interface extending `JpaRepository`
3. Add `@RepositoryRestResource` annotation for REST exposure
4. Restart application - endpoints are automatically available

## Troubleshooting

### Database Connection Issues

- Verify MySQL is running on localhost:3306
- Check username/password (root/test123)
- Ensure `allowPublicKeyRetrieval=true` parameter is included

### Common Errors

- **Access denied**: Check MySQL credentials
- **Table doesn't exist**: Verify `spring.jpa.hibernate.ddl-auto=update`
- **Port already in use**: Change `server.port` in application.properties

## Testing

This project includes comprehensive unit and integration tests to ensure code quality and functionality.

### Test Structure

The project uses a multi-layered testing approach:

```
src/test/
├── java/com/example/login/
│   ├── entity/                     # Entity unit tests
│   │   ├── UserTest.java
│   │   ├── ClientTest.java
│   │   └── ...
│   ├── repository/                 # Repository unit tests
│   │   ├── UserRepositoryTest.java
│   │   ├── ClientRepositoryTest.java
│   │   └── ...
│   ├── integration/                # Integration tests
│   │   └── RestEndpointsIntegrationTest.java
│   └── LoginApplicationTest.java   # Application context test
└── resources/
    ├── application-test.properties           # H2 in-memory DB for unit tests
    ├── application-integration.properties    # MySQL DB for integration tests
    ├── application-integration-env.properties # MySQL with env vars
    └── README-TEST-PROFILES.md              # Test profiles documentation
```

### Test Profiles

The application uses different Spring profiles for different testing scenarios:

#### 1. Unit Tests (`test` profile)
- **Database**: H2 in-memory database
- **Purpose**: Fast, isolated unit tests
- **Configuration**: `application-test.properties`

#### 2. Integration Tests (`integration` profile)
- **Database**: Real MySQL database
- **Purpose**: End-to-end testing with actual database
- **Configuration**: `application-integration.properties`

#### 3. Secure Integration Tests (`integration-env` profile)
- **Database**: Real MySQL database with environment variables
- **Purpose**: Integration tests with secure credential management
- **Configuration**: `application-integration-env.properties`

### Running Tests

#### Run All Tests
```bash
# Run all tests (unit + integration)
mvn test

# Clean build with all tests
mvn clean test
```

#### Run Only Unit Tests
```bash
# Run specific unit test categories
mvn test -Dtest="UserTest,ClientTest,*RepositoryTest"

# Run all tests except integration tests (using Maven profiles)
mvn test -Dskip.integration.tests=true

# Run tests from specific packages
mvn test -Dtest="com.example.login.entity.*Test,com.example.login.repository.*Test"
```

#### Run Only Integration Tests
```bash
# Run only integration tests with integration profile (MySQL database)
mvn test -Dtest="*IntegrationTest" -Dspring.profiles.active=integration

# Run with environment variable profile for secure credentials
mvn test -Dtest="*IntegrationTest" -Dspring.profiles.active=integration-env

# Alternative: Run all integration tests (they automatically use integration profile)
mvn test -Dtest="*IntegrationTest"
```

#### Run Tests with Specific Profile
```bash
# Run unit tests with test profile (H2 in-memory database)
mvn test -Dtest="UserTest,ClientTest,*RepositoryTest" -Dspring.profiles.active=test

# Run integration tests with integration profile (MySQL database)
mvn test -Dtest="*IntegrationTest" -Dspring.profiles.active=integration

# Run integration tests with environment variable profile (secure credentials)
mvn test -Dtest="*IntegrationTest" -Dspring.profiles.active=integration-env

# Run all tests with default profile (uses application.properties)
mvn test
```

### Test Categories

#### Entity Tests
Test entity validation, constructors, and Lombok-generated methods:
```bash
mvn test -Dtest="UserTest,ClientTest"

# Or test all entity tests using package pattern
mvn test -Dtest="com.example.login.entity.*Test"
```

**Example tests:**
- Field validation (@NotNull, @Email, etc.)
- Custom constructors
- Builder pattern functionality
- toString(), equals(), hashCode() methods
- Enum handling

#### Repository Tests
Test data access layer with mocked dependencies:
```bash
mvn test -Dtest="*RepositoryTest"
```

**Example tests:**
- CRUD operations
- Custom query methods
- Relationship mappings
- Search functionality
- Pagination

#### Integration Tests
Test complete REST API endpoints with real database:
```bash
mvn test -Dtest="*IntegrationTest"
```

**Example tests:**
- GET endpoints for all entities
- Relationship navigation endpoints
- Custom search endpoints
- Error handling
- JSON response validation

### Setting Up Integration Tests

#### Prerequisites for Integration Tests
1. **MySQL Database**: Ensure MySQL is running on localhost:3306
2. **Database**: Create `b2bapp` database with existing data
3. **Credentials**: Default credentials (root/test123) or environment variables
4. **Profile Configuration**: Integration tests automatically use `@ActiveProfiles("integration")`

#### Using Environment Variables (Recommended for CI/CD)
```bash
# Set environment variables
export DB_URL="jdbc:mysql://localhost:3306/b2bapp?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="test123"

# Run integration tests with environment variables
mvn test -Dtest="*IntegrationTest" -Dspring.profiles.active=integration-env
```

### Test Results and Coverage

#### View Test Results
```bash
# Run tests with detailed output
mvn test -Dtest.verbose=true

# Generate test report
mvn surefire-report:report
```

#### Current Test Coverage
- **Total Tests**: 161
- **Entity Tests**: 27 tests across 6 entities
- **Repository Tests**: 105 tests across 6 repositories
- **Integration Tests**: 28 REST endpoint tests
- **Application Tests**: 1 context loading test

### Continuous Integration

#### GitHub Actions / Jenkins
```yaml
# Example CI configuration
- name: Run Unit Tests
  run: mvn test -Dtest="!*IntegrationTest"

- name: Run Integration Tests
  run: mvn test -Dtest="*IntegrationTest"
  env:
    DB_URL: ${{ secrets.DB_URL }}
    DB_USERNAME: ${{ secrets.DB_USERNAME }}
    DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
```

### Test Best Practices

#### Writing New Tests
1. **Unit Tests**: Use H2 in-memory database for fast execution
2. **Integration Tests**: Use real MySQL database for realistic scenarios
3. **Isolation**: Each test should be independent
4. **Cleanup**: Use `@Transactional` for automatic rollback
5. **Naming**: Use descriptive test method names

#### Test Data Management
- **Unit Tests**: Use test data builders or fixtures
- **Integration Tests**: Rely on existing database data
- **Cleanup**: Tests automatically rollback transactions

### Troubleshooting Tests

#### Common Issues

**MySQL Connection Failed (Integration Tests)**
```bash
# Check MySQL is running
mysql -u root -p -e "SELECT 1;"

# Verify database exists
mysql -u root -p -e "SHOW DATABASES LIKE 'b2bapp';"
```

**H2 Database Issues (Unit Tests)**
```bash
# Clear Maven cache and retry
mvn clean test
```

**Test Profile Not Applied**
```bash
# Explicitly specify profile
mvn test -Dspring.profiles.active=integration -Dtest="*IntegrationTest"
```

### Performance

- **Unit Tests**: ~3-5 seconds (H2 in-memory)
- **Integration Tests**: ~8-12 seconds (MySQL database)
- **All Tests**: ~15-20 seconds total

### Quick Reference - Common Test Commands

```bash
# Run all tests (unit + integration)
mvn test

# Run only unit tests (entity + repository tests)
mvn test -Dtest="UserTest,ClientTest,*RepositoryTest"

# Run only integration tests with MySQL database
mvn test -Dtest="*IntegrationTest" -Dspring.profiles.active=integration

# Run integration tests with environment variables
mvn test -Dtest="*IntegrationTest" -Dspring.profiles.active=integration-env

# Run specific entity tests
mvn test -Dtest="UserTest,ClientTest"

# Run all repository tests
mvn test -Dtest="*RepositoryTest"

# Clean build and run all tests
mvn clean test

# Run tests with verbose output
mvn test -Dtest.verbose=true

# Generate coverage reports
mvn clean install

# View coverage report
open target/site/jacoco/index.html
```

## Code Coverage

This project includes JaCoCo code coverage reporting that generates detailed coverage metrics during the build process.

### Coverage Reports

JaCoCo generates coverage reports automatically when you run:

```bash
# Generate coverage reports with clean install
mvn clean install

# Generate coverage reports with test execution
mvn clean test

# View coverage reports
open target/site/jacoco/index.html
```

### Coverage Report Formats

JaCoCo generates multiple report formats:

1. **HTML Report**: `target/site/jacoco/index.html` - Interactive web report
2. **XML Report**: `target/site/jacoco/jacoco.xml` - For CI/CD integration
3. **CSV Report**: `target/site/jacoco/jacoco.csv` - For data analysis

### Coverage Configuration

- **Minimum Coverage**: 50% line coverage required
- **Exclusions**: 
  - Lombok generated code
  - Main application class (`LoginApplication.class`)
  - Configuration classes (`config/*.class`)
  - Serialization classes

### Coverage Metrics

Current coverage includes:
- **Line Coverage**: Percentage of executed lines
- **Branch Coverage**: Percentage of executed branches
- **Method Coverage**: Percentage of executed methods
- **Class Coverage**: Percentage of executed classes

### CI/CD Integration

For continuous integration, use the XML report:

```yaml
# Example GitHub Actions
- name: Generate Coverage Report
  run: mvn clean test
  
- name: Upload Coverage to Codecov
  uses: codecov/codecov-action@v3
  with:
    file: ./target/site/jacoco/jacoco.xml
```

## API Documentation

This project includes comprehensive API documentation using SpringDoc OpenAPI 3 (Swagger).

### Accessing the Documentation

Once the application is running, you can access the API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml

### Features

- **Interactive API Explorer**: Test endpoints directly from the browser
- **Schema Documentation**: Detailed entity and response models
- **Authentication Support**: Future integration with Spring Security
- **Multiple Server Environments**: Development and production configurations

### Configuration

All OpenAPI settings are configured in `application.properties`:

```properties
# OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
springdoc.packages-to-scan=com.example.login
springdoc.paths-to-match=/api/**

# OpenAPI Info Configuration
springdoc.info.title=Login System API
springdoc.info.description=Spring Boot REST API for user authentication and management system with Spring Data REST
springdoc.info.version=1.0.0
springdoc.info.contact.name=API Support
springdoc.info.contact.email=support@example.com
springdoc.info.license.name=MIT License
springdoc.info.license.url=https://opensource.org/licenses/MIT
```

### Available Endpoints

The API automatically documents all Spring Data REST endpoints:

- **Users**: `/api/users` - User management operations
- **Clients**: `/api/clients` - Client management operations  
- **Roles**: `/api/roles` - Role management operations
- **Client Roles**: `/api/clientRoles` - Client-role associations
- **User Roles**: `/api/userRoles` - User-role assignments
- **Login Logs**: `/api/logsLogins` - Authentication audit logs

## Next Steps

- Add Spring Security for authentication
- Implement password encryption
- Add custom controllers for complex business logic
- Configure CORS for frontend integration
- Add performance testing with JMeter
- Integrate coverage reports with CI/CD pipeline
