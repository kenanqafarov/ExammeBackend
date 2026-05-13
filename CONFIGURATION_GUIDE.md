# Example Configuration for Different Environments

## Development Environment (application-dev.properties)

```properties
# Server
server.port=8080
server.servlet.context-path=/

# Database (Local)
spring.datasource.url=jdbc:mysql://localhost:3306/examme_db_dev?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.root=INFO
logging.level.com.examme.examme=DEBUG
logging.level.org.springframework.web=DEBUG

# JWT
app.jwtSecret=dev-secret-key-change-in-production
app.jwtExpirationMs=86400000

# Gemini API (Development)
gemini.api.key=${GEMINI_API_KEY:dev-key-placeholder}

# File Upload
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB
```

## Production Environment (application-prod.properties)

```properties
# Server
server.port=8080
server.servlet.context-path=/

# Database (Production)
spring.datasource.url=jdbc:mysql://prod-db-host:3306/examme_db?useSSL=true&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Logging
logging.level.root=WARN
logging.level.com.examme.examme=INFO
logging.file.name=/var/log/examme/application.log
logging.file.max-size=10MB
logging.file.max-history=10

# JWT
app.jwtSecret=${JWT_SECRET}
app.jwtExpirationMs=${JWT_EXPIRATION:86400000}

# Gemini API (Production)
gemini.api.key=${GEMINI_API_KEY}

# File Upload
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# Security
server.ssl.key-store=${SSL_KEYSTORE_PATH}
server.ssl.key-store-password=${SSL_KEYSTORE_PASSWORD}
server.ssl.key-store-type=PKCS12
```

## Testing Environment (application-test.properties)

```properties
# Server
server.port=8081
server.servlet.context-path=/

# Database (H2 In-Memory)
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Logging
logging.level.root=WARN
logging.level.com.examme.examme=DEBUG

# JWT
app.jwtSecret=test-secret-key
app.jwtExpirationMs=3600000

# Gemini API (Mock)
gemini.api.key=test-api-key

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Switching Between Environments

### Using Command Line
```bash
# Run with development profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Run with production profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Run with test profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"
```

### Using application.yml (Alternative)

If using YAML instead of properties:

```yaml
# application.yml
spring:
  profiles:
    active: dev

---
# application-dev.yml
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:mysql://localhost:3306/examme_db_dev
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

---
# application-prod.yml
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:mysql://prod-host:3306/examme_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

## Docker Compose for Full Stack

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: examme_db
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  examme-app:
    build: .
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/examme_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      SPRING_PROFILES_ACTIVE: prod
      GEMINI_API_KEY: ${GEMINI_API_KEY}
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    volumes:
      - ./uploads:/app/uploads

volumes:
  mysql_data:
```

Run with:
```bash
docker-compose up -d
```
