# Password Hashing Functionality

This document describes the password hashing and validation functionality added to the Spring Boot login application.

## Overview

The application now includes comprehensive password hashing capabilities with support for multiple algorithms and a Spring service bean for easy integration.

## Components

### 1. PasswordType Enum (Enhanced)

The `User.PasswordType` enum now includes a `fromString()` method for case-insensitive parsing:

```java
// Get type from string (case-insensitive)
PasswordType type = PasswordType.fromString("MD5");
```

**Supported Types:**
- `BCRYPT` - Uses Spring Security's BCrypt implementation
- `SHA256` - SHA-256 hashing algorithm
- `MD5` - MD5 hashing algorithm (default)
- `SHA512` - SHA-512 hashing algorithm

### 2. PasswordHashUtil Utility Class

A utility class providing static methods for password hashing operations:

```java
// Generate salt
String salt = PasswordHashUtil.generateRandomSaltHex(16);

// Create hash
String hash = PasswordHashUtil.generateHash(type, salt, "P@ssw0rd!");

// Validate later
boolean ok = PasswordHashUtil.isValid(type, salt, "P@ssw0rd!", hash);
```

**Key Methods:**
- `generateRandomSaltHex(int length)` - Generate random salt as hex string
- `generateHash(PasswordType, String salt, String password)` - Create hash with salt
- `isValid(PasswordType, String salt, String password, String hash)` - Validate password
- `generateSalt(PasswordType)` - Generate appropriate salt for algorithm
- `generateHashWithSalt(PasswordType, String password)` - Generate both salt and hash

### 3. PasswordService Spring Bean

A Spring service bean providing high-level password operations:

```java
@Autowired
private PasswordService passwordService;

// Generate hash with salt
var result = passwordService.generateHash("SHA256", "mypassword");
String salt = result.getSalt();
String hash = result.getHash();

// Validate password
boolean isValid = passwordService.isValid("SHA256", salt, "mypassword", hash);

// Check password security
boolean isSecure = passwordService.isPasswordSecure("MySecure123!");
```

**Key Methods:**
- `generateHash(String typeString, String password)` - Generate hash with auto-generated salt
- `generateHash(PasswordType type, String password)` - Generate hash with enum type
- `generateHash(String typeString, String salt, String password)` - Generate hash with provided salt
- `isValid(...)` - Validate password against hash
- `generateSalt(...)` - Generate salt for specific algorithm
- `isPasswordSecure(String password)` - Check password security requirements

## Usage Examples

### Basic Usage (as requested)

```java
// Get type from string (case-insensitive)
PasswordType type = PasswordType.fromString("MD5");

// Generate salt
String salt = PasswordHashUtil.generateRandomSaltHex(16);

// Create hash
String hash = PasswordHashUtil.generateHash(type, salt, "P@ssw0rd!");

// Validate later
boolean ok = PasswordHashUtil.isValid(type, salt, "P@ssw0rd!", hash);
```

### Using Spring Service Bean

```java
@Service
public class UserService {
    
    @Autowired
    private PasswordService passwordService;
    
    public void createUser(String username, String password) {
        // Generate hash with salt
        var result = passwordService.generateHash("SHA256", password);
        
        // Create user with hashed password
        User user = User.builder()
            .username(username)
            .passwordHash(result.getHash())
            .passwordSalt(result.getSalt())
            .passwordType(PasswordType.SHA256)
            .build();
            
        // Save user...
    }
    
    public boolean validatePassword(String username, String password) {
        // Get user from database...
        User user = userRepository.findByUsername(username);
        
        // Validate password
        return passwordService.isValid(
            user.getPasswordType().name(),
            user.getPasswordSalt(),
            password,
            user.getPasswordHash()
        );
    }
}
```

### Different Password Types

```java
// BCrypt (recommended for production)
var bcryptResult = passwordService.generateHash("BCRYPT", "password");
boolean bcryptValid = passwordService.isValid("BCRYPT", bcryptResult.getSalt(), "password", bcryptResult.getHash());

// SHA-256
var sha256Result = passwordService.generateHash("SHA256", "password");
boolean sha256Valid = passwordService.isValid("SHA256", sha256Result.getSalt(), "password", sha256Result.getHash());

// MD5 (less secure, not recommended for production)
var md5Result = passwordService.generateHash("MD5", "password");
boolean md5Valid = passwordService.isValid("MD5", md5Result.getSalt(), "password", md5Result.getHash());
```

## Security Features

### Password Security Validation

The `PasswordService` includes a method to validate password strength:

```java
boolean isSecure = passwordService.isPasswordSecure("MySecure123!");
```

**Requirements for secure passwords:**
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- At least one special character (!@#$%^&*()_+-=[]{}|;:,.<>?)

### Algorithm Recommendations

1. **BCRYPT** - Recommended for production use
   - Built-in salt generation
   - Configurable cost factor
   - Resistant to rainbow table attacks

2. **SHA256** - Good for general use
   - Requires external salt
   - Fast hashing
   - Widely supported

3. **SHA512** - Higher security
   - Requires external salt
   - Slower than SHA256
   - More secure against brute force

4. **MD5** - Not recommended for production
   - Vulnerable to collision attacks
   - Fast but insecure
   - Only for legacy compatibility

## Testing

The implementation includes comprehensive tests:

- `PasswordTypeTest` - Tests enum functionality
- `PasswordHashUtilTest` - Tests utility class methods
- `PasswordServiceTest` - Tests Spring service bean

Run tests with:
```bash
mvn test -Dtest="PasswordTypeTest,PasswordHashUtilTest,PasswordServiceTest"
```

## Dependencies

The functionality requires Spring Security for BCrypt support:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## Error Handling

The implementation includes proper error handling:

- `IllegalArgumentException` for invalid password types
- `IllegalArgumentException` for null/empty passwords or salts
- Graceful handling of validation failures
- Clear error messages with supported types

## Integration with Existing Code

The password functionality integrates seamlessly with the existing `User` entity:

```java
@Entity
public class User {
    // ... existing fields ...
    
    @Enumerated(EnumType.STRING)
    private PasswordType passwordType = PasswordType.MD5;
    
    private String passwordHash;
    private String passwordSalt;
    
    // ... rest of entity ...
}
```

The `PasswordType` enum is already defined in the `User` entity and has been enhanced with the `fromString()` method.
