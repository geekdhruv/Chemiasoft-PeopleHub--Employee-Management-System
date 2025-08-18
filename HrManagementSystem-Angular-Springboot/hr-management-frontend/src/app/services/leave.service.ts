import { Injectable } from "@angular/core";
import { environment } from "../../environment/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { LeaveGet, LeavePost } from "../models/leave.model";

@Injectable({
    providedIn: "root"
})

export class LeaveService {
    private apiUrl = `${environment.baseUrl}/leave`;

    constructor(private http: HttpClient) { }

    getAllLeaves(): Observable<LeaveGet[]> {
        return this.http.get<LeaveGet[]>(this.apiUrl);
    }

    getLeaveById(leaveId: number): Observable<LeaveGet> {
        return this.http.get<LeaveGet>(`${this.apiUrl}/${leaveId}`);
    }

    createLeave(leave: LeavePost): Observable<LeaveGet> {
        return this.http.post<LeaveGet>(this.apiUrl, leave);
    }

    updateLeave(leave: LeavePost, id: number): Observable<LeaveGet> {
        return this.http.put<LeaveGet>(`${this.apiUrl}/${id}`, leave);
    }

    deleteLeave(id: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.apiUrl}/${id}`);
    }

    exportLeaveReport(): Observable<Blob> {
        return this.http.get(`${this.apiUrl}/report`, { responseType: 'blob' });
    }
}