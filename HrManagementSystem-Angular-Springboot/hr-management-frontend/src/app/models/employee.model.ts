export enum DepartmentType {
    HR = 'HR',
    FINANCE = 'FINANCE',
    IT = 'IT',
    OPERATIONS = 'OPERATIONS'
}

export class Employee {
    id!: number;
    name!: string;
    email!: string;
    departmentType!: DepartmentType;
    createdAt!: Date;
    updatedAt!: Date;
}

export class EmployeePost {
    name!: string;
    email!: string;
    departmentType!: DepartmentType;
    username!: string;
    password!: string;
}
