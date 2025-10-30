# Spring AOP Logging and Exception Handling

This project implements simple logging and exception handling using Spring AOP (Aspect-Oriented Programming). The implementation provides centralized logging and exception handling across the application.

## Features

### 1. Logging Aspect
- **LoggingAspect**: Simple logging for method entry, exit, and execution time for all service methods

### 2. Exception Handling
- **ExceptionAspect**: Single exception handling class for all compile-time and runtime errors
- **Controller Advice**: Global exception handling for REST endpoints

## Usage

### Automatic Logging
All service methods are automatically logged:

```java
@Service
public class UserAuthServiceImpl implements UserAuthService {
    
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Method entry, exit, and execution time are automatically logged
        return new RegisterResponse();
    }
}
```

The LoggingAspect will automatically:
- Log method entry with class and method name
- Log method completion with execution time
- Log method failures with error details

### Exception Handling
Throw standard Java exceptions and they will be automatically handled:

```java
public RegisterResponse login(LoginRequest request) {
    if (!isValidUser(request)) {
        throw new RuntimeException("Invalid credentials");
    }
    
    if (request.getEmail() == null) {
        throw new RuntimeException("Email is required");
    }
    
    return new RegisterResponse();
}
```

## Exception Handling

### ExceptionAspect
Single exception handling class that catches all compile-time and runtime errors:

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
    log.error("Error occurred: {}", ex.getMessage(), ex);
    
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("error", "Internal Server Error");
    response.put("message", ex.getMessage());
    
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
}
```

**Handles:**
- `RuntimeException` - All runtime errors
- `Exception` - All other exceptions
- Compile-time errors that become runtime exceptions
- Any unexpected errors

## Logging Output

The LoggingAspect provides simple, clear logging output:

```
INFO  - Starting method: UserAuthServiceImpl.register
INFO  - Completed method: UserAuthServiceImpl.register in 45 ms
ERROR - Failed method: UserAuthServiceImpl.login after 12 ms - Error: Invalid email or password
```

## Configuration

### AOP Configuration
The `AopConfig` class enables AspectJ auto-proxy:

```java
@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    // AOP is automatically configured
}
```

### Logging Configuration
Configure logging levels in `application.properties`:

```properties
# Logging levels
logging.level.com.mta.core=INFO
logging.level.com.mta.core.logging=DEBUG

# Logging pattern
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

## Example API Endpoints

### Authentication Endpoints
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Token refresh


## Benefits

1. **Centralized Logging**: All logging logic is centralized in one aspect
2. **Exception Handling**: Consistent error responses across the application
3. **Non-intrusive**: Business logic remains clean and focused
4. **Simple**: Easy to understand and maintain
5. **Automatic**: No annotations or configuration needed

## Dependencies

The following dependency is required in `build.gradle`:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-aop'
```

## Best Practices

1. Use appropriate log levels (DEBUG, INFO, WARN, ERROR)
2. Don't log sensitive information (passwords, tokens)
3. Use custom exceptions for different error scenarios
4. Keep business logic clean and focused
5. Let AOP handle cross-cutting concerns automatically
