# KBTU University Research System - Test Accounts

## Compilation & Running

**Compile:**
```bash
javac -encoding UTF-8 -d . WspClassDiagram/**/*.java Main.java
```

**Run:**
```bash
java Main
```

## Test Accounts (from DataStore seed data)

| Role | Email | Password | Description |
|------|-------|----------|-------------|
| Admin | admin@uni.kz | admin123 | System administrator |
| Teacher | aibek@uni.kz | teacher123 | Professor with research papers |
| Student | ainur@uni.kz | student123 | 2nd year CS student |
| GraduateStudent | daniyar@uni.kz | grad123 | 1st year grad student |
| Manager | gulnara@uni.kz | manager123 | OR Manager |
| TechSupport | yerlan@uni.kz | tech123 | Tech support specialist |

## Features Implemented

### Core Models ✅
- User hierarchy (Admin, Teacher, Student, GraduateStudent, Manager, TechSupport)
- Course management with enrollment
- Mark/Grade system with GPA calculation
- Research paper management
- Journal with Observer pattern
- Student organizations

### Controllers ✅
- AuthController - login/logout
- AdminController - user management, logs
- StudentController - course registration, transcript
- TeacherController - grading, complaints
- ManagerController - course management, reports
- ResearcherController - papers, projects
- TechSupportController - request handling

### Views ✅
- Console-based menus for all roles
- AdminView - user CRUD, logs
- StudentView - courses, registration, transcript
- TeacherView - grading, student management
- ManagerView - academic management
- ResearcherView - research capabilities
- TechSupportView - request management
- GraduateStudentView - supervisor, diploma

### Design Patterns
1. **Singleton** - DataStore
2. **Factory** - UserFactory
3. **Decorator** - ResearcherDecorator
4. **Observer** - Journal/Subscriber
5. **Strategy** - Comparators (Citations, Date, Length)

### Exception Handling
- CreditLimitException
- MaxFailedReachedException
- LowHIndexException
- NotAResearcherException
- InvalidCredentialsException

### Serialization
- DataStore saves/loads from data.ser
- All models implement Serializable

## Quick Test Workflow

1. Login as admin@uni.kz / admin123
2. View all users
3. View logs
4. Create new user
5. Logout and login as different role
6. Test role-specific features

## Project Structure
```
finalooooooo/
├── Main.java
├── WspClassDiagram/
│   ├── exceptions/
│   ├── enums/
│   ├── models/
│   ├── comparators/
│   ├── core/
│   ├── utils/
│   ├── controllers/
│   └── views/
└── data.ser (created on first run)
```

## Notes
- H-index calculation implemented for researchers
- Teacher can be promoted to Researcher by Manager
- Graduate students require supervisor with H-index >= 3
- Credit limit: 21 credits maximum
- Maximum 3 failed courses allowed
- News with "Research" topic are auto-pinned
