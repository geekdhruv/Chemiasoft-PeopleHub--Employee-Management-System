# Role-Based Access Control (RBAC) Implementation

This document describes the complete RBAC system implementation for the Employee Leave Management System.

## Overview

The system now supports two roles:
- **ADMIN**: Full access to all features
- **EMPLOYEE**: Limited access to leave management features

## Default Credentials

### Admin User
- **Username**: `admin`
- **Password**: `admin123`

### Sample Employee Users
- **Username**: `john.doe` | **Password**: `admin123`
- **Username**: `jane.smith` | **Password**: `admin123`
- **Username**: `mike.johnson` | **Password**: `admin123`

## Database Schema Changes

### Employee Table
Added new fields:
- `username` (VARCHAR(50), UNIQUE, NOT NULL)
- `password` (VARCHAR(255), NOT NULL)
- `role` (VARCHAR(20), NOT NULL, DEFAULT 'EMPLOYEE')

### User Table
Added new field:
- `role` (VARCHAR(20), NOT NULL, DEFAULT 'EMPLOYEE')

## Backend API Endpoints

### Authentication
- `POST /api/auth/login` - Login with role-based response

### Employee Management (Admin Only)
- `GET /api/employees/list` - Get all employees
- `POST /api/employees/add` - Add new employee with authentication

### Leave Management
- `GET /api/leave` - Get all leaves (Admin only)
- `POST /api/leave/apply` - Apply for leave (Employee only)
- `PUT /api/leave/edit/{id}` - Edit leave (Admin only)
- `DELETE /api/leave/delete/{id}` - Delete leave (Employee can delete own only)
- `PUT /api/leave/approve/{id}` - Approve leave (Admin only)
- `PUT /api/leave/reject/{id}` - Reject leave (Admin only)

## Role Permissions

### ADMIN Role
- ✅ View all employees
- ✅ Add new employees
- ✅ Edit any leave request
- ✅ Approve/reject leave requests
- ✅ View all leave requests
- ✅ Generate reports

### EMPLOYEE Role
- ✅ Apply for new leave requests
- ✅ View own leave requests
- ✅ Delete own leave requests
- ❌ Cannot edit leave requests
- ❌ Cannot add employees
- ❌ Cannot approve/reject leave requests

## Frontend Features

### Navigation
- Role-based navigation menu
- User info display with role badge
- Conditional button visibility based on role

### Leave Management
- **Admin View**: Full CRUD operations, approve/reject buttons
- **Employee View**: Apply leave, view own requests, delete own requests

### Employee Management
- **Admin Only**: Full employee management interface
- **Employee**: No access to employee management

## Security Features

1. **JWT Authentication**: Secure token-based authentication
2. **Role-based Authorization**: Backend validates user roles for each endpoint
3. **Password Hashing**: All passwords are hashed using BCrypt
4. **Frontend Guards**: UI elements are conditionally displayed based on user role
5. **Session Management**: User role is stored in JWT token and local storage

## Running the Application

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL/PostgreSQL database

### Backend Setup
1. Navigate to `hr-management-services/`
2. Update database configuration in `application.properties`
3. Run database migration: `V2__Add_RBAC_Fields.sql`
4. Start the application: `mvn spring-boot:run`

### Frontend Setup
1. Navigate to `hr-management-frontend/`
2. Install dependencies: `npm install`
3. Start the application: `ng serve`
4. Access the application at `http://localhost:4200`

## Database Migration

Run the following SQL script to update your database schema:

```sql
-- Add role field to employee table
ALTER TABLE employee ADD COLUMN username VARCHAR(50) UNIQUE NOT NULL;
ALTER TABLE employee ADD COLUMN password VARCHAR(255) NOT NULL;
ALTER TABLE employee ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE';

-- Add role field to user table
ALTER TABLE user ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE';

-- Create indexes for better performance
CREATE INDEX idx_employee_username ON employee(username);
CREATE INDEX idx_employee_role ON employee(role);
CREATE INDEX idx_user_role ON user(role);

-- Insert default admin user (password: admin123)
INSERT INTO user (username, password, role) VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN');

-- Insert sample employees with authentication credentials
INSERT INTO employee (name, email, username, password, department_type, role, created_at, updated_at) VALUES 
('John Doe', 'john.doe@company.com', 'john.doe', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'IT', 'EMPLOYEE', CURRENT_DATE, CURRENT_DATE),
('Jane Smith', 'jane.smith@company.com', 'jane.smith', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'HR', 'EMPLOYEE', CURRENT_DATE, CURRENT_DATE),
('Mike Johnson', 'mike.johnson@company.com', 'mike.johnson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'FINANCE', 'EMPLOYEE', CURRENT_DATE, CURRENT_DATE);
```

## Testing the System

1. **Login as Admin**:
   - Username: `admin`
   - Password: `admin123`
   - Verify full access to all features

2. **Login as Employee**:
   - Username: `john.doe`
   - Password: `admin123`
   - Verify limited access (only leave management)

3. **Test Role Restrictions**:
   - Try accessing admin features as employee
   - Verify proper error handling and access denial

## Key Implementation Files

### Backend
- `Role.java` - Role enum definition
- `SecurityConfig.java` - Security configuration with role-based access
- `CustomUserDetailsService.java` - User authentication with role support
- `AuthController.java` - Updated login with role response
- `EmployeeController.java` - Role-based employee management
- `LeaveController.java` - Role-based leave management

### Frontend
- `user.model.ts` - Updated user model with role support
- `auth.service.ts` - Enhanced authentication service with role methods
- `nav-bar.component.html` - Role-based navigation
- `view-leaves.component.html` - Role-based leave management UI
- `view-employees.component.html` - Admin-only employee management

## Security Notes

1. **Password Security**: All passwords are hashed using BCrypt
2. **Token Security**: JWT tokens include role information
3. **Backend Validation**: All endpoints validate user roles server-side
4. **Frontend Security**: UI elements are hidden based on role, but backend validation is primary
5. **Session Management**: User sessions include role information

## Troubleshooting

1. **Database Issues**: Ensure migration script is executed properly
2. **Authentication Issues**: Check JWT token configuration
3. **Role Issues**: Verify user has correct role in database
4. **Frontend Issues**: Clear browser cache and local storage

## Future Enhancements

1. **Role Management**: Admin interface to manage user roles
2. **Permission Granularity**: More detailed permissions per role
3. **Audit Logging**: Track role-based actions
4. **Password Policies**: Enforce password complexity rules
5. **Multi-factor Authentication**: Additional security layers
