# Spribe Player API Test Framework

## Prerequisites
- Java 17+
- Maven 3.8+

## Stack
- Java 17
- TestNG 7.9
- REST Assured 6.0
- Allure 2.17
- Log4j2
- Jackson
- DataFaker

## Project Structure
```
src/
├── main/java/
│   ├── api/player/        # API clients
│   ├── config/            # Framework configuration
│   ├── core/              # Base classes, filters
│   ├── models/            # DTOs and enums
│   └── utils/             # Helpers (PlayerFactory, PasswordGenerator)
├── test/java/
│   ├── core/              # BaseTest
│   ├── dataprovider/      # TestNG data providers
│   ├── listeners/         # TestLogListener
│   └── player/            # Test classes
└── test/resources/
    └── testng.xml
```

## How to Run

Run all tests with default settings:
```bash
mvn clean test
```

Run with custom parameters:
```bash
mvn clean test -DbaseUrl=http://3.68.165.45 -DthreadCount=3
```

Tests run in **3 parallel threads** by default.

## Allure Report

Generate and open report in browser:
```bash
mvn allure:serve
```

### Report Example
<img width="1728" height="729" alt="Screenshot 2026-02-27 at 00 58 21" src="https://github.com/user-attachments/assets/6312c34f-000e-414f-adda-e6d8c9d8f604" />


## Logging

Each test generates its own log file under `logs/` directory:
```
logs/PlayerCreatePositiveTest_shouldCreatePlayerAsSupervisor.log
logs/PlayerUpdateNegativeTest_shouldFailWhenEditorIsRegularUser.log
```

## Known Bugs

Many tests are disabled (`enabled = false`) because they cover confirmed bugs in the application. These tests are marked with the `@Issue` annotation describing the bug.

## API Design Issues

The application has incorrect HTTP method usage across endpoints:

| Endpoint | Current Method | Expected Method |
|---|---|---|
| `/player/create/{editor}` | GET | POST |
| `/player/delete/{editor}` | DELETE | DELETE ✅ |
| `/player/get` | POST | GET |
| `/player/get/all` | GET | GET ✅ |
| `/player/update/{editor}/{id}` | PATCH | PATCH ✅ |

Using `GET` for resource creation (`/player/create`) violates REST principles — `GET` requests should be read-only and must not change server state. Similarly, using `POST` for `/player/get` is semantically incorrect as it is a read operation.

## Security Issue

`/player/create/{editor}` sends all player fields including `password` as **query parameters** in the URL. This means:
- Password is visible in server access logs
- Password is stored in browser history

Sensitive data like passwords should always be sent in the **request body**, never in the URL.
