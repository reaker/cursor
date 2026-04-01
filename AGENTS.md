# AGENTS.md

## Cursor Cloud specific instructions

### Project overview
This is a Java 21 Maven project (group: `pl.telly`, artifact: `cursor`). It has no external runtime dependencies; JUnit 5 is used for testing.

### Prerequisites
- **Java 21** (OpenJDK) — pre-installed in the Cloud VM
- **Maven 3.8+** — installed via `sudo apt-get install -y maven`

### Key commands
| Task | Command |
|------|---------|
| Compile | `mvn compile` |
| Run tests | `mvn test` |
| Package (JAR) | `mvn package` |
| Run app | `java -cp target/classes pl.telly.cursor.App` |

### Notes
- The default `maven-surefire-plugin` bundled with the system Maven is too old for JUnit 5. The `pom.xml` pins `maven-surefire-plugin` 3.5.2 to ensure JUnit Jupiter tests are discovered and executed.
- There is no web UI or long-running service — this is a CLI application.
- No Docker, databases, or external services are required.
