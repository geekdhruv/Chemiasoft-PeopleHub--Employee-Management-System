# **Employee Leave Management System**

## **Overview**
The HR Management System is a comprehensive web application designed to streamline human resource operations. Built with Angular frontend and Spring Boot backend, it offers secure authentication via JWT and Spring Security, while providing a modern UI experience through Angular Material and Bootstrap CSS.

---

## **Features**

### **Employee Management**:
- Add, update, delete, and view employee records
- Department and role assignment
- Employee search and filtering capabilities
- Document management for employee files
- Generate employee reports


### **Leave Management**:
- Leave request submission and approval workflow
- Multiple leave type support (sick, vacation, personal)
- Leave balance tracking
- Calendar view of team availability
- Generate leave reports

### **Security Features**:
- JWT-based authentication and authorisation
- Role-based access control
- Secure password handling with encryption
- Session management
- API security with Spring Security

---

## **Technologies Used**

### **Backend**:
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT Authentication
- RESTful API design

### **Frontend**:
- Angular
- Angular Material
- Bootstrap CSS
- SweetAlert
- Responsive design

### **Database**:
- MySQL Database
- JPA/Hibernate ORM

---

## **System Architecture**

The application follows a modern microservices architecture:

1. **Frontend Layer**: Angular-based SPA with responsive design
2. **API Gateway**: Manages authentication and routes requests
3. **Service Layer**: RESTful APIs for different modules
4. **Data Access Layer**: JPA repositories for database operations
5. **Security Layer**: JWT token validation and role-based authorization

---

## **API Documentation**

The system provides RESTful APIs for all operations:

- **Authentication APIs**: `/api/auth/*`
- **Employee APIs**: `/api/employees/*`

- **Leave Management APIs**: `/api/leaves/*`

Detailed API documentation is available via Swagger UI at `/swagger-ui.html` when the application runs.

---


