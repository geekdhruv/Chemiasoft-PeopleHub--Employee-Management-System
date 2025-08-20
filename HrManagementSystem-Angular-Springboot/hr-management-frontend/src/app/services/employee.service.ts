import { Injectable } from "@angular/core";
import { environment } from "../../environment/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Employee, EmployeePost } from "../models/employee.model";

@Injectable({
    providedIn: 'root'
})

export class EmployeeService {
    private apiUrl = `${environment.baseUrl}/employees`;

    constructor(private http: HttpClient) { }

    getAllEmployees(): Observable<Employee[]> {
        return this.http.get<Employee[]>(`${this.apiUrl}/list`);
    }

    getEmployeeById(employeeId: number): Observable<Employee> {
        return this.http.get<Employee>(`${this.apiUrl}/${employeeId}`);
    }

    createEmployee(employee: EmployeePost): Observable<Employee> {
        return this.http.post<Employee>(`${this.apiUrl}/add`, employee);
    }

    updateEmployee(employee: any): Observable<Employee> {
        return this.http.put<Employee>(`${this.apiUrl}/${employee.id}`, employee);
    }

    deleteEmployee(employeeId: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.apiUrl}/${employeeId}`);
    }

    exportEmployeeReport(): Observable<Blob> {
        return this.http.get(`${this.apiUrl}/report`, { responseType: 'blob' });
    }
}