# KBTU University Management System

Complete university management system with both Console and JavaFX GUI interfaces.

## Features

- **User Management**: Admin, Teacher, Student, Graduate Student, Manager, Tech Support
- **Course Management**: Registration, enrollment, prerequisites
- **Grade System**: AT1, AT2, Final exams with GPA calculation
- **Research System**: Papers, journals, citations, H-index
- **News System**: University news with pinned items
- **Support System**: Tech support requests and orders
- **Complaint System**: Student complaints to Dean

## Project Structure

```
finalooooooo/
├── Main.java                          # Console application entry point
├── MainApp.java                       # JavaFX GUI entry point
├── pom.xml                            # Maven configuration
├── WspClassDiagram/
│   ├── core/
│   │   ├── DataStore.java            # Singleton data store with serialization
│   │   └── UserFactory.java          # Factory pattern for user creation
│   ├── models/                       # 25+ model classes
│   │   ├── User.java (abstract)
│   │   ├── Student.java
│   │   ├── Teacher.java
│   │   ├── Admin.java
│   │   ├── Course.java
│   │   ├── Mark.java
│   │   └── ...
│   ├── controllers/                  # Business logic controllers
│   │   ├── AuthController.java
│   │   ├── StudentController.java
│   │   ├── TeacherController.java
│   │   └── ...
│   ├── views/                        # Console views
│   │   ├── StudentView.java
│   │   ├── TeacherView.java
│   │   └── ...
│   ├── gui/                          # JavaFX GUI (NEW)
│   │   ├── LoginScreen.java
│   │   ├── BaseDashboard.java
│   │   ├── StudentDashboard.java
│   │   ├── TeacherDashboard.java
│   │   ├── AdminDashboard.java
│   │   ├── ManagerDashboard.java
│   │   └── TechSupportDashboard.java
│   ├── exceptions/                   # Custom exceptions
│   │   ├── CreditLimitException.java
│   │   ├── MaxFailedReachedException.java
│   │   └── ...
│   ├── enums/                        # Enumerations
│   │   ├── TeacherTitle.java
│   │   ├── UrgencyLevel.java
│   │   └── ...
│   └── comparators/                  # Sorting strategies
│       ├── StudentGPAComparator.java
│       └── ...
└── data.ser                          # Serialized data (auto-generated)
```

## Test Accounts

| Role               | Email                | Password     |
|--------------------|---------------------|--------------|
| Admin              | admin@uni.kz        | admin123     |
| Teacher            | aibek@uni.kz        | teacher123   |
| Student            | ainur@uni.kz        | student123   |
| Graduate Student   | daniyar@uni.kz      | grad123      |
| Manager            | gulnara@uni.kz      | manager123   |
| Tech Support       | yerlan@uni.kz       | tech123      |

## Running the Console Application

The console version works without any additional dependencies.

### Compile:
```bash
cd "C:\Users\Bagdat\OneDrive\Desktop\finalooooooo"

# PowerShell:
$files = Get-ChildItem -Recurse -Filter "*.java" -Exclude "MainApp.java","gui\*.java" | Select-Object -ExpandProperty FullName
javac -encoding UTF-8 @files Main.java
```

### Run:
```bash
java Main
```

The console application will start and show a login menu. Use one of the test accounts above.

## Running the JavaFX GUI Application

The JavaFX version requires JavaFX libraries. **Two options:**

### Option 1: Using Maven (Recommended)

1. **Install Maven**: Download from https://maven.apache.org/download.cgi
   - Extract to `C:\Program Files\Maven`
   - Add `C:\Program Files\Maven\bin` to PATH
   - Verify: `mvn --version`

2. **Run with Maven**:
```bash
cd "C:\Users\Bagdat\OneDrive\Desktop\finalooooooo"
mvn clean javafx:run
```

This will automatically download JavaFX and all dependencies, then launch the GUI.

### Option 2: Manual JavaFX Setup

1. **Download JavaFX SDK**: 
   - Go to https://gluonhq.com/products/javafx/
   - Download JavaFX SDK 17 or later for Windows
   - Extract to `C:\javafx-sdk-17`

2. **Compile**:
```bash
cd "C:\Users\Bagdat\OneDrive\Desktop\finalooooooo"

# PowerShell:
$files = Get-ChildItem -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
javac --module-path "C:\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml @files
```

3. **Run**:
```bash
java --module-path "C:\javafx-sdk-17\lib" --add-modules javafx.controls,javafx.fxml MainApp
```

## Using the Application

### JavaFX GUI:
1. Launch the application
2. Login with test credentials (e.g., `ainur@uni.kz` / `student123`)
3. Navigate using the sidebar menu
4. All data is automatically saved to `data.ser` on logout

### Console:
1. Run `java Main`
2. Choose login option and enter credentials
3. Use numeric menu options to navigate
4. Type `0` to go back or logout

## Design Patterns Used

- **Singleton**: DataStore for centralized data management
- **Factory**: UserFactory for creating different user types
- **Decorator**: ResearcherDecorator for adding research capabilities
- **Observer**: Journal/Subscriber for research paper notifications
- **Strategy**: Comparators for different sorting strategies
- **MVC**: Separation of Models, Views, and Controllers

## Key Features by Role

### Student
- View available courses and register
- View enrolled courses and transcript
- See marks (AT1, AT2, Final) and GPA
- Read university news
- Exception handling for credit limits and fail counts

### Teacher
- View assigned courses and enrolled students
- Put marks for students (AT1, AT2, Final)
- Send complaints about students to Dean
- View personal rating

### Admin
- Manage all users (view, create, remove)
- View system logs
- Monitor system statistics

### Manager
- View all courses and enrollments
- Generate student academic reports
- Manage university news (create, pin)
- View statistics

### Tech Support
- View and manage support requests
- Accept or reject orders
- View all users in the system

## Data Persistence

All data is stored in `data.ser` using Java serialization:
- Users and their credentials
- Courses and enrollments
- Marks and transcripts
- News items
- Support requests
- System logs

The file is automatically created on first run with seed data and updated on each save.

## Development Notes

- **Java Version**: Java 11 or later required
- **JavaFX Version**: 17.0.2 (configured in pom.xml)
- **Encoding**: UTF-8
- **Serialization**: All model classes implement `Serializable`
- **No external database**: Uses file-based serialization

## Troubleshooting

### "package javafx.application does not exist"
- JavaFX is not in the classpath
- Use Maven (Option 1) or set up JavaFX manually (Option 2)

### "data.ser" not found
- Normal on first run
- Will be auto-created with seed data
- Don't delete unless you want to reset all data

### Compilation errors with console version
- Make sure to exclude JavaFX files when compiling console-only:
  ```
  -Exclude "MainApp.java","gui\*.java"
  ```

### Display issues on Windows
- Ensure Windows display scaling is 100% or 125%
- If text is cut off, try resizing the window

## License

Educational project for KBTU OOP Final Project.
