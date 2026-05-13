# Smart Quiz Platform - Setup Guide

## Quick Start

### System Requirements
- **Java**: 17 or higher
- **Maven**: 3.6.0 or higher
- **Database**: MySQL 5.7+ or PostgreSQL 10+
- **RAM**: 4GB minimum
- **Storage**: 2GB for application and uploads

### Step 1: Install Dependencies

#### Windows/MacOS/Linux
1. Install Java 17+
```bash
# Check Java version
java -version
```

2. Install Maven
```bash
# Check Maven version
mvn -version
```

3. Install MySQL (or use existing database)
```bash
# Windows
choco install mysql

# MacOS
brew install mysql

# Linux (Ubuntu)
sudo apt-get install mysql-server
```

### Step 2: Clone and Setup Project

```bash
# Clone repository
git clone <repository-url>
cd examme

# Navigate to project
cd c:\Users\User\Desktop\examme
```

### Step 3: Configure Database

1. Create MySQL database:
```sql
CREATE DATABASE examme_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/examme_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
```

### Step 4: Configure Gemini API

1. Get your Gemini API key from [Google AI Studio](https://makersuite.google.com/app/apikey)

2. Set environment variable (choose one method):

**Windows (Command Prompt):**
```bash
set GEMINI_API_KEY=your_api_key_here
```

**Windows (PowerShell):**
```powershell
$env:GEMINI_API_KEY = "your_api_key_here"
```

**MacOS/Linux:**
```bash
export GEMINI_API_KEY=your_api_key_here
```

**Or edit application.properties:**
```properties
gemini.api.key=your_api_key_here
```

### Step 5: Build Project

```bash
# Navigate to project directory
cd c:\Users\User\Desktop\examme

# Build with Maven
mvn clean install

# If you want to skip tests
mvn clean install -DskipTests
```

### Step 6: Run Application

```bash
# Run Spring Boot application
mvn spring-boot:run

# Or run directly
java -jar target/examme-0.0.1-SNAPSHOT.jar
```

The application will start at: `http://localhost:8080`

## Testing the API

### Using Postman

1. Download and install Postman
2. Create a new collection
3. Add requests using endpoints from `API_DOCUMENTATION.md`

### Using curl

#### 1. Register as a Student
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT"
  }'
```

#### 2. Register as a Teacher
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teacher@example.com",
    "password": "password123",
    "firstName": "Jane",
    "lastName": "Smith",
    "role": "TEACHER"
  }'
```

#### 3. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teacher@example.com",
    "password": "password123"
  }'
```

Response will contain JWT token. Use it in subsequent requests:
```bash
TOKEN=your_token_here
```

#### 4. Create a Quiz (Teacher)
```bash
curl -X POST http://localhost:8080/api/quizzes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Java Fundamentals",
    "description": "Basic Java concepts",
    "difficultyLevel": "MEDIUM",
    "instructions": "Read questions carefully",
    "questions": [
      {
        "questionText": "What is Java?",
        "type": "MULTIPLE_CHOICE",
        "difficultyLevel": "EASY",
        "marks": 1,
        "order": 1,
        "correctAnswer": "Programming Language",
        "explanation": "Java is an object-oriented programming language",
        "options": ["Programming Language", "Database", "Framework", "OS"]
      }
    ]
  }'
```

#### 5. Upload File (Teacher)
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -H "Authorization: Bearer $TEACHER_TOKEN" \
  -F "file=@/path/to/file.pdf"
```

#### 6. Generate Questions from File
```bash
curl -X POST http://localhost:8080/api/files/generate-questions \
  -H "Authorization: Bearer $TEACHER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "fileUploadId": 1,
    "numberOfQuestions": 5,
    "difficultyLevel": "MEDIUM",
    "language": "en"
  }'
```

#### 7. Start Quiz (Student)
```bash
curl -X POST http://localhost:8080/api/student/quiz/1/start \
  -H "Authorization: Bearer $STUDENT_TOKEN"
```

#### 8. Submit Answer
```bash
curl -X POST http://localhost:8080/api/student/quiz/attempt/1/answer \
  -H "Authorization: Bearer $STUDENT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "questionId": 1,
    "answerText": "Programming Language"
  }'
```

#### 9. Submit Quiz
```bash
curl -X POST http://localhost:8080/api/student/quiz/attempt/1/submit \
  -H "Authorization: Bearer $STUDENT_TOKEN"
```

## Project Structure

```
examme/
├── src/main/java/com/examme/examme/
│   ├── controller/              # REST API endpoints
│   │   ├── AuthenticationController.java
│   │   ├── QuizController.java
│   │   ├── FileUploadController.java
│   │   ├── StudentQuizController.java
│   │   └── AdminController.java
│   ├── service/                 # Business logic
│   │   ├── UserService.java
│   │   ├── QuizService.java
│   │   ├── QuestionService.java
│   │   ├── StudentQuizService.java
│   │   ├── FileUploadService.java
│   │   └── GeminiQuestionGeneratorService.java
│   ├── entity/                  # JPA entities
│   │   ├── User.java
│   │   ├── Quiz.java
│   │   ├── Question.java
│   │   ├── QuestionOption.java
│   │   ├── StudentQuizAttempt.java
│   │   ├── StudentAnswer.java
│   │   └── FileUpload.java
│   ├── repository/              # Database access layer
│   ├── dto/                     # Data transfer objects
│   ├── config/                  # Configuration classes
│   ├── security/                # Security components
│   ├── util/                    # Utility classes
│   ├── enums/                   # Enum types
│   └── ExammeApplication.java   # Main application class
├── src/main/resources/
│   ├── application.properties   # Configuration file
│   ├── db/migration/            # Database migrations
│   └── static/                  # Static files
├── pom.xml                      # Maven configuration
├── API_DOCUMENTATION.md         # API documentation
└── README.md                    # This file
```

## Common Issues and Solutions

### Issue: Database connection failed
**Solution:**
- Check MySQL is running: `mysql -u root -p`
- Verify credentials in application.properties
- Ensure database exists: `CREATE DATABASE examme_db;`

### Issue: Gemini API key not working
**Solution:**
- Generate new key from Google AI Studio
- Verify environment variable is set: `echo $GEMINI_API_KEY`
- Check key format (should start with 'sk-' or similar)

### Issue: Port 8080 already in use
**Solution:**
- Change port in application.properties: `server.port=8081`
- Or kill process using port: `lsof -ti:8080 | xargs kill -9`

### Issue: Maven build fails
**Solution:**
```bash
# Clear cache and rebuild
mvn clean install -DskipTests

# Update dependencies
mvn dependency:resolve
```

### Issue: JWT token validation fails
**Solution:**
- Check token is included in request header: `Authorization: Bearer <token>`
- Verify token is not expired (default 24 hours)
- Check JWT secret matches configuration

## Configuration Files

### application.properties
Main configuration file for:
- Database connection
- Server port
- JWT settings
- Gemini API key
- File upload limits
- Logging levels

### Database Migrations
Located in `src/main/resources/db/migration/`
- Flyway manages database schema
- Migrations run automatically on startup

## Environment Variables

Required:
- `GEMINI_API_KEY`: Google Generative AI API key

Optional:
- `DB_URL`: Database URL
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: JWT signing key
- `JWT_EXPIRATION`: Token expiration time in ms

## Development Tips

1. **Enable DEBUG logging:**
   Edit `application.properties`:
   ```properties
   logging.level.com.examme.examme=DEBUG
   ```

2. **Run specific tests:**
   ```bash
   mvn test -Dtest=UserServiceTest
   ```

3. **Generate project documentation:**
   ```bash
   mvn javadoc:javadoc
   ```

4. **Check code quality:**
   ```bash
   mvn clean verify
   ```

## Performance Tips

1. Add database indexes for frequently queried fields
2. Implement caching for quiz and question data
3. Use pagination for large result sets
4. Optimize file upload size limits
5. Monitor database query performance

## Security Checklist

- [ ] Change default JWT secret in production
- [ ] Use strong database passwords
- [ ] Enable HTTPS/SSL in production
- [ ] Validate all user inputs
- [ ] Keep dependencies updated
- [ ] Use environment variables for sensitive data
- [ ] Implement rate limiting for API endpoints
- [ ] Regular security audits

## Deployment

### Docker Deployment (Optional)

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-slim
COPY target/examme-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Build and run:
```bash
docker build -t smart-quiz-platform .
docker run -p 8080:8080 smart-quiz-platform
```

## Support and Contribution

- Report issues in GitHub Issues
- Follow code style guidelines
- Write tests for new features
- Update documentation

## License

This project is licensed under the MIT License.

## Contact

For support, contact: support@smartquizplatform.com
