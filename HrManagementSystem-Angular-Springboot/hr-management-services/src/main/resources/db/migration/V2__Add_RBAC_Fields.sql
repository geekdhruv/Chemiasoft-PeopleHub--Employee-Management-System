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
