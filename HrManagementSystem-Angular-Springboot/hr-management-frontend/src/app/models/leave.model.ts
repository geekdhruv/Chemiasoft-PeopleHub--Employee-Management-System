import { Employee } from "./employee.model";

export enum LeaveType {
    ANNUAL = 'ANNUAL',
    CASSUAL = 'CASUAL',
    SICK = 'SICK',
    MATERNITY = 'MATERNITY',
    PATERNITY = 'PATERNITY',
    UNPAID = 'UNPAID',
  }
  
  export enum LeaveStatus {
    PENDING = 'PENDING',
    APPROVED = 'APPROVED',
    REJECTED = 'REJECTED',
  }
  
  export interface Leave {
    id?: number;
    employeeId: number;
    leaveType: LeaveType;
    startDate: string;
    endDate: string;
    reason?: string;
    status: LeaveStatus;
    createdAt?: string;
    updatedAt?: string;
  }
  
  export interface LeaveGet {
    id?: number;
    employee: Employee;
    leaveType: LeaveType;
    startDate: string;
    endDate: string;
    reason?: string;
    status: LeaveStatus;
    createdAt?: string;
    updatedAt?: string;
  }

  export interface LeavePost {
    employeeId: number;
    leaveType: LeaveType;
    startDate: string;
    endDate: string;
    reason?: string;
    status?: LeaveStatus;
  }

//   {
//     "employee": { "id": 1 },
//     "leaveType": "ANNUAL",
//     "startDate": "2025-05-03",
//     "endDate": "2025-05-05",
//     "reason": "Personal matters",
//     "status": "PENDING"
//   }
  