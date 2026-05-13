# Smart Quiz Platform - API Documentation

## Project Overview
Smart Quiz Platform is a comprehensive online examination and quiz management system that enables teachers to create and manage quizzes while allowing students to test their knowledge securely.

## Features

### Admin Capabilities
- Manage all users in the system
- Register and control teachers and students
- Monitor platform activity
- Track reports and quiz statistics
- Deactivate/Activate user accounts

### Teacher Capabilities
- Create and manage quizzes
- Add and organize questions
- Upload quiz content in multiple file formats (PDF, DOCX, Excel, TXT)
- Generate questions automatically using Gemini 2.5 Flash AI
- Set difficulty levels for quizzes and questions
- Add custom instructions and descriptions for students

### Student Capabilities
- Log into personal dashboard
- View available and active quizzes
- Take quizzes with automatic answer saving
- Track scores and quiz results
- View detailed result analysis

## Technical Stack

### Backend
- **Framework**: Spring Boot 4.0.6
- **Security**: Spring Security with JWT
- **Database**: MySQL/PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **AI Integration**: Gemini 2.5 Flash API
- **File Processing**: Apache POI, PDFBox

### Authentication
- JWT (JSON Web Tokens) for stateless authentication
- Role-based access control (RBAC)
- Token expiration: 24 hours (configurable)

## API Endpoints

### Authentication Endpoints

#### Register User
```
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT" // ADMIN, TEACHER, STUDENT
}

Response: 201 Created
{
  "message": "User registered successfully",
  "success": true
}
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT",
    "isActive": true
  },
  "message": "Login successful"
}
```

#### Get Profile
```
GET /api/auth/profile
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT",
  "isActive": true
}
```

### Quiz Management Endpoints (Teacher)

#### Create Quiz
```
POST /api/quizzes
Authorization: Bearer <teacher_token>
Content-Type: application/json

{
  "title": "Java Fundamentals",
  "description": "Basic Java concepts quiz",
  "difficultyLevel": "MEDIUM",
  "instructions": "Read all questions carefully...",
  "questions": [
    {
      "questionText": "What is JVM?",
      "type": "MULTIPLE_CHOICE",
      "difficultyLevel": "EASY",
      "marks": 1,
      "order": 1,
      "correctAnswer": "Java Virtual Machine",
      "explanation": "JVM executes Java bytecode",
      "options": ["Java Virtual Machine", "Java Version Manager", "Java Valid Module", "Java Virtual Monitor"]
    }
  ]
}

Response: 201 Created
{
  "id": 1,
  "title": "Java Fundamentals",
  "description": "Basic Java concepts quiz",
  "difficultyLevel": "MEDIUM",
  "totalQuestions": 1,
  "totalTime": 60,
  "isActive": true,
  "instructions": "Read all questions carefully...",
  "createdAt": "2026-05-12T10:30:00",
  "updatedAt": "2026-05-12T10:30:00",
  "teacher": {...},
  "questions": [...]
}
```

#### Get Quiz by ID
```
GET /api/quizzes/{quizId}

Response: 200 OK
{
  "id": 1,
  "title": "Java Fundamentals",
  "description": "Basic Java concepts quiz",
  ...
}
```

#### Get My Quizzes
```
GET /api/quizzes/teacher/all
Authorization: Bearer <teacher_token>

Response: 200 OK
[
  {
    "id": 1,
    "title": "Java Fundamentals",
    ...
  },
  {
    "id": 2,
    "title": "Spring Boot Basics",
    ...
  }
]
```

#### Get Active Quizzes
```
GET /api/quizzes/active/all

Response: 200 OK
[...]
```

#### Get Quizzes by Difficulty
```
GET /api/quizzes/difficulty/{difficulty}

Difficulty: EASY, MEDIUM, HARD

Response: 200 OK
[...]
```

#### Update Quiz
```
PUT /api/quizzes/{quizId}
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated description",
  "difficultyLevel": "HARD",
  "instructions": "Updated instructions",
  "questions": [...]
}

Response: 200 OK
{...}
```

#### Delete Quiz
```
DELETE /api/quizzes/{quizId}

Response: 200 OK
{
  "message": "Quiz deleted successfully",
  "success": true
}
```

### File Upload and Question Generation Endpoints

#### Upload File
```
POST /api/files/upload
Authorization: Bearer <teacher_token>
Content-Type: multipart/form-data

file: <PDF/DOCX/EXCEL/TXT file>

Response: 201 Created
{
  "id": 1,
  "fileName": "study_material.pdf",
  "fileType": "PDF",
  "fileSize": 1024000,
  "filePath": "uploads/1234567890_study_material.pdf",
  "extractedContent": "Content extracted from file...",
  "createdAt": "2026-05-12T10:30:00"
}
```

#### Generate Questions from File (Gemini 2.5 Flash)
```
POST /api/files/generate-questions
Authorization: Bearer <teacher_token>
Content-Type: application/json

{
  "fileUploadId": 1,
  "numberOfQuestions": 10,
  "difficultyLevel": "MEDIUM",
  "language": "en" // Optional
}

Response: 200 OK
{
  "questions": [
    {
      "questionText": "Generated question text...",
      "type": "MULTIPLE_CHOICE",
      "difficultyLevel": "MEDIUM",
      "marks": 1,
      "correctAnswer": "Answer A",
      "explanation": "Explanation...",
      "options": ["Option A", "Option B", "Option C", "Option D"]
    },
    ...
  ],
  "status": "success",
  "message": "Questions generated successfully"
}
```

#### Get My Files
```
GET /api/files/my-files
Authorization: Bearer <teacher_token>

Response: 200 OK
[
  {
    "id": 1,
    "fileName": "study_material.pdf",
    ...
  }
]
```

#### Get File by ID
```
GET /api/files/{fileId}

Response: 200 OK
{
  "id": 1,
  "fileName": "study_material.pdf",
  ...
}
```

#### Delete File
```
DELETE /api/files/{fileId}

Response: 200 OK
{
  "message": "File deleted successfully",
  "success": true
}
```

### Student Quiz Endpoints

#### Start Quiz
```
POST /api/student/quiz/{quizId}/start
Authorization: Bearer <student_token>

Response: 200 OK
{
  "id": 1,
  "student": {...},
  "quiz": {...},
  "startTime": "2026-05-12T10:30:00",
  "endTime": null,
  "totalScore": null,
  "maxScore": null,
  "isCompleted": false
}
```

#### Save Answer
```
POST /api/student/quiz/attempt/{attemptId}/answer
Authorization: Bearer <student_token>
Content-Type: application/json

{
  "questionId": 1,
  "answerText": "The correct answer"
}

Response: 200 OK
{
  "message": "Answer saved successfully",
  "success": true
}
```

#### Submit Quiz
```
POST /api/student/quiz/attempt/{attemptId}/submit
Authorization: Bearer <student_token>

Response: 200 OK
{
  "attemptId": 1,
  "totalScore": 8,
  "maxScore": 10,
  "percentage": 80.0,
  "startTime": "2026-05-12T10:30:00",
  "endTime": "2026-05-12T10:45:00"
}
```

#### Get My Attempts
```
GET /api/student/attempts
Authorization: Bearer <student_token>

Response: 200 OK
[
  {
    "id": 1,
    "student": {...},
    "quiz": {...},
    "startTime": "2026-05-12T10:30:00",
    "endTime": "2026-05-12T10:45:00",
    "totalScore": 8,
    "maxScore": 10,
    "isCompleted": true
  }
]
```

#### Get Quiz Result
```
GET /api/student/result/{attemptId}

Response: 200 OK
{
  "attemptId": 1,
  "totalScore": 8,
  "maxScore": 10,
  "percentage": 80.0,
  "startTime": "2026-05-12T10:30:00",
  "endTime": "2026-05-12T10:45:00"
}
```

### Admin Endpoints

#### Get All Teachers
```
GET /api/admin/teachers
Authorization: Bearer <admin_token>

Response: 200 OK
[
  {
    "id": 1,
    "email": "teacher@example.com",
    "firstName": "Jane",
    "lastName": "Smith",
    "role": "TEACHER",
    "isActive": true
  }
]
```

#### Get All Students
```
GET /api/admin/students
Authorization: Bearer <admin_token>

Response: 200 OK
[...]
```

#### Get User by ID
```
GET /api/admin/user/{userId}

Response: 200 OK
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "STUDENT",
  "isActive": true
}
```

#### Deactivate User
```
PUT /api/admin/user/{userId}/deactivate

Response: 200 OK
{
  "message": "User deactivated successfully",
  "success": true
}
```

#### Activate User
```
PUT /api/admin/user/{userId}/activate

Response: 200 OK
{
  "message": "User activated successfully",
  "success": true
}
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- MySQL 5.7+ or PostgreSQL 10+
- Maven 3.6+
- Gemini API Key (for AI-based question generation)

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd examme
```

2. **Configure Database**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/examme_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Configure Gemini API**
Set your Gemini API key as an environment variable:
```bash
export GEMINI_API_KEY=your_api_key_here
```

Or add to `application.properties`:
```properties
gemini.api.key=your_api_key_here
```

4. **Build the project**
```bash
mvn clean install
```

5. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Database Schema

### Tables
- **users**: User accounts with roles (ADMIN, TEACHER, STUDENT)
- **quizzes**: Quiz configurations and metadata
- **questions**: Individual quiz questions
- **question_options**: Multiple choice options for questions
- **student_quiz_attempts**: Quiz attempt records
- **student_answers**: Individual student answers
- **file_uploads**: Uploaded study materials

## Enum Types

### UserRole
- `ADMIN`: System administrator
- `TEACHER`: Quiz creator and manager
- `STUDENT`: Quiz participant

### DifficultyLevel
- `EASY`
- `MEDIUM`
- `HARD`

### QuestionType
- `MULTIPLE_CHOICE`
- `SHORT_ANSWER`
- `TRUE_FALSE`
- `ESSAY`

### FileFormat
- `PDF`
- `DOCX`
- `EXCEL`
- `TEXT`

## Security Features

1. **JWT Authentication**: All endpoints except auth require valid JWT token
2. **Role-Based Access Control**: Different permissions for Admin, Teacher, Student
3. **Password Encryption**: BCrypt password hashing
4. **CORS Support**: Configured for cross-origin requests
5. **Request Validation**: Input validation on all endpoints

## Error Handling

All error responses follow this format:
```json
{
  "message": "Error description",
  "success": false
}
```

HTTP Status Codes:
- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request
- `401 Unauthorized`: Missing or invalid authentication
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Future Enhancements

- Advanced analytics and reporting
- Detailed performance tracking
- Plagiarism detection for essay questions
- Question bank management
- Randomized question orders
- Time-based notifications
- Mobile app integration
- Video/Audio question support
- Collaborative quiz creation

## Support

For issues or questions, please contact the development team or create an issue in the repository.
