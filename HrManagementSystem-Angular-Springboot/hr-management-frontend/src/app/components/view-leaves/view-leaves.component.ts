import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import Swal from 'sweetalert2';
import { faUsers, faFileAlt, faPlusCircle, faSearch, faEnvelope, faFilter, faEdit, faTrashAlt, faFolderOpen } from '@fortawesome/free-solid-svg-icons';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee.model';
import { Leave, LeaveGet, LeaveStatus, LeaveType } from '../../models/leave.model';
import { LeaveService } from '../../services/leave.service';

@Component({
  selector: 'app-view-leaves',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './view-leaves.component.html',
  styleUrls: ['./view-leaves.component.css']
})
export class ViewLeavesComponent implements OnInit {
  faUsers = faUsers;
  faFileAlt = faFileAlt;
  faPlusCircle = faPlusCircle;
  faSearch = faSearch;
  faEnvelope = faEnvelope;
  faFilter = faFilter;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  faFolderOpen = faFolderOpen;

  leaves: LeaveGet[] = [];
  employees: Employee[] = [];
  searchEmployeeId = '';
  startDate = '';
  endDate = '';

  constructor(
    private leaveService: LeaveService,
    private employeeService: EmployeeService,
    private router: Router
  ) { }

  ngOnInit() {
    this.getAllLeaves();
    this.configureSwalStyles();
  }

  private processLeaves(leaves: LeaveGet[]): LeaveGet[] {
    return leaves.map(leave => {
      if (!leave.employee) {
        leave.employee = { id: 0, name: 'Unknown' } as Employee;
      }
      return {
        ...leave,
        startDate: leave.startDate || '',
        endDate: leave.endDate || '',
        reason: leave.reason || '',
        status: leave.status || LeaveStatus.PENDING,
        createdAt: leave.createdAt || '',
        updatedAt: leave.updatedAt || '',
      };
    });
  }

  configureSwalStyles() {
    Swal.mixin({
      customClass: {
        popup: 'swal-custom-popup swal-wide',
        confirmButton: 'swal-custom-confirm',
        cancelButton: 'swal-custom-cancel',
        input: 'swal-input'
      },
      buttonsStyling: false
    });
  }

  getAllLeaves() {
    this.leaveService.getAllLeaves().subscribe({
      next: (res) => {
        this.leaves = this.processLeaves(res);
      },
      error: () => Swal.fire('Error', 'Failed to fetch leaves', 'error')
    });
    this.employeeService.getAllEmployees().subscribe({
      next: (res) => this.employees = res,
      error: () => Swal.fire('Error', 'Failed to fetch employees', 'error')
    });
  }

  addLeave() {
    const employeeOptions = this.employees
      .map(emp => `<option value="${emp.id}">${emp.id} - ${emp.name}</option>`)
      .join('');

    const leaveTypeOptions = Object.values(LeaveType)
      .map(type => `<option value="${type}">${type}</option>`)
      .join('');

    const leaveStatusOptions = Object.values(LeaveStatus)
      .map(status => `<option value="${status}">${status}</option>`)
      .join('');

    Swal.fire({
      title: '<strong>Add New Leave</strong>',
      html: `
        <div class="swal-form-group">
          <label class="swal-label">Employee</label>
          <select id="swal-empId" class="swal-select">${employeeOptions}</select>
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Leave Type</label>
          <select id="swal-leaveType" class="swal-select">${leaveTypeOptions}</select>
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Start Date</label>
          <input type="date" id="swal-startDate" class="swal-input">
        </div>
        <div class="swal-form-group">
          <label class="swal-label">End Date</label>
          <input type="date" id="swal-endDate" class="swal-input">
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Reason</label>
          <textarea id="swal-reason" class="swal-input"></textarea>
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Status</label>
          <select id="swal-status" class="swal-select">${leaveStatusOptions}</select>
        </div>
      `,
      showCancelButton: true,
      confirmButtonText: 'Add Leave',
      preConfirm: () => {
        const employeeId = parseInt((document.getElementById('swal-empId') as HTMLSelectElement).value);
        const leaveType = (document.getElementById('swal-leaveType') as HTMLSelectElement).value as LeaveType;
        const startDate = (document.getElementById('swal-startDate') as HTMLInputElement).value;
        const endDate = (document.getElementById('swal-endDate') as HTMLInputElement).value;
        const reason = (document.getElementById('swal-reason') as HTMLTextAreaElement).value.trim();
        const status = (document.getElementById('swal-status') as HTMLSelectElement).value as LeaveStatus;

        if (!employeeId || !leaveType || !startDate || !endDate || !status) {
          Swal.showValidationMessage('Please fill all required fields');
          return false;
        }

        return {
          employeeId,
          leaveType,
          startDate,
          endDate,
          reason,
          status
        };
      }
    }).then(result => {
      if (result.isConfirmed && result.value) {
        this.leaveService.createLeave(result.value).subscribe({
          next: () => {
            Swal.fire('Success', 'Leave added successfully', 'success');
            this.getAllLeaves();
          },
          error: () => Swal.fire('Error', 'Failed to add leave', 'error')
        });
      }
    });
  }

  updateLeave(leave: LeaveGet) {
    if (!leave || !leave.employee) {
      Swal.fire('Error', 'Invalid leave record', 'error');
      return;
    }
  
    const leaveTypeOptions = Object.values(LeaveType)
      .map(type => `<option value="${type}" ${leave.leaveType === type ? 'selected' : ''}>${type}</option>`)
      .join('');
  
    const leaveStatusOptions = Object.values(LeaveStatus)
      .map(status => `<option value="${status}" ${leave.status === status ? 'selected' : ''}>${status}</option>`)
      .join('');
  
    const employeeId = leave.employee.id;
    const employeeName = leave.employee.name;
  
    Swal.fire({
      title: '<strong>Update Leave</strong>',
      html: `
        <div class="swal-form-group">
          <label class="swal-label">Employee</label>
          <select id="swal-empId" class="swal-select" disabled>
            <option value="${employeeId}">${employeeId} - ${employeeName}</option>
          </select>
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Leave Type</label>
          <select id="swal-leaveType" class="swal-select">${leaveTypeOptions}</select>
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Start Date</label>
          <input type="date" id="swal-startDate" class="swal-input" value="${leave.startDate}">
        </div>
        <div class="swal-form-group">
          <label class="swal-label">End Date</label>
          <input type="date" id="swal-endDate" class="swal-input" value="${leave.endDate}">
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Reason</label>
          <textarea id="swal-reason" class="swal-input">${leave.reason || ''}</textarea>
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Status</label>
          <select id="swal-status" class="swal-select">${leaveStatusOptions}</select>
        </div>
      `,
      showCancelButton: true,
      confirmButtonText: 'Update Leave',
      preConfirm: () => {
        const leaveType = (document.getElementById('swal-leaveType') as HTMLSelectElement).value as LeaveType;
        const startDate = (document.getElementById('swal-startDate') as HTMLInputElement).value;
        const endDate = (document.getElementById('swal-endDate') as HTMLInputElement).value;
        const reason = (document.getElementById('swal-reason') as HTMLTextAreaElement).value.trim();
        const status = (document.getElementById('swal-status') as HTMLSelectElement).value as LeaveStatus;
  
        if (!leaveType || !startDate || !endDate || !status) {
          Swal.showValidationMessage('Please fill all required fields');
          return false;
        }
  
        return {
          id: leave.id,
          employeeId: employeeId,
          leaveType,
          startDate,
          endDate,
          reason,
          status
        };
      }
    }).then(result => {
      if (result.isConfirmed && result.value) {
        this.leaveService.updateLeave(result.value, leave.id!).subscribe({
          next: () => {
            Swal.fire('Success', 'Leave updated successfully', 'success');
            this.getAllLeaves();
          },
          error: () => Swal.fire('Error', 'Failed to update leave', 'error')
        });
      }
    });
  }  

  deleteLeave(leave: LeaveGet) {
    if (!leave || !leave.employee) {
      Swal.fire('Error', 'Invalid leave record', 'error');
      return;
    }

    Swal.fire({
      title: 'Delete this leave?',
      html: `
        <p><strong>Employee:</strong> ${leave.employee?.name || 'Unknown'}</p>
        <p><strong>Start Date:</strong> ${leave.startDate || 'Not specified'}</p>
        <p><strong>End Date:</strong> ${leave.endDate || 'Not specified'}</p>
      `,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed && leave.id) {
        this.leaveService.deleteLeave(leave.id).subscribe({
          next: () => {
            Swal.fire('Deleted', 'Leave record deleted', 'success');
            this.getAllLeaves();
          },
          error: () => Swal.fire('Error', 'Failed to delete leave', 'error')
        });
      }
    });
  }

  filteredLeaves(): LeaveGet[] {
    const validLeaves = this.leaves.map(leave => {
      if (!leave.employee) {
        return {
          ...leave,
          employee: { id: 0, name: 'Unknown' } as Employee
        };
      }
      return leave;
    });

    return validLeaves.filter(leave => {
      const employeeIdMatch = this.searchEmployeeId === '' ||
        (leave.employee && leave.employee.id &&
          leave.employee.id.toString().includes(this.searchEmployeeId));

      const startDateMatch = this.startDate === '' ||
        (leave.startDate && new Date(leave.startDate).toISOString().includes(this.startDate));

      const endDateMatch = this.endDate === '' ||
        (leave.endDate && new Date(leave.endDate).toISOString().includes(this.endDate));

      return employeeIdMatch && startDateMatch && endDateMatch;
    });
  }

  exportReports(): void {
    this.leaveService.exportLeaveReport().subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'leave_report.csv';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
}