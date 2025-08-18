import { Component } from '@angular/core';
import { EmployeeService } from '../../services/employee.service';
import { Router } from '@angular/router';
import { Employee, EmployeePost } from '../../models/employee.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUsers, faFileAlt, faPlusCircle, faSearch, faEnvelope, faFilter, faEdit, faTrashAlt, faFolderOpen } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-view-employees',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './view-employees.component.html',
  styleUrls: ['./view-employees.component.css']
})
export class ViewEmployeesComponent {

  faUsers = faUsers;
  faFileAlt = faFileAlt;
  faPlusCircle = faPlusCircle;
  faSearch = faSearch;
  faEnvelope = faEnvelope;
  faFilter = faFilter;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  faFolderOpen = faFolderOpen;

  employees: Employee[] = [];
  searchName: string = '';
  searchEmail: string = '';
  searchDepartment: string = '';
  departmentTypes: string[] = ['HR', 'IT', 'FINANCE', 'OPERATIONS'];

  constructor(
    private employeeService: EmployeeService,
    private router: Router
  ) { }

  ngOnInit() {
    this.getAllEmployees();
    this.configureSwalStyles();
  }

  configureSwalStyles() {
    Swal.mixin({
      customClass: {
        confirmButton: 'swal-confirm-btn',
        cancelButton: 'swal-cancel-btn',
        input: 'swal-input'
      },
      buttonsStyling: false
    });
  }

  getAllEmployees() {
    this.employeeService.getAllEmployees().subscribe({
      next: (response) => {
        this.employees = response;
      }
    });
  }

  getDepartmentClass(department: string): string {
    return `dept-${department.toLowerCase()}`;
  }

  addEmployee() {
    Swal.fire({
      title: '<strong>Add New Employee</strong>',
      html: `
        <div class="swal-form-group">
          <label class="swal-label">Full Name</label>
          <input type="text" id="swal-name" class="swal-input" placeholder="Enter full name">
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Email Address</label>
          <input type="email" id="swal-email" class="swal-input" placeholder="Enter email address">
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Department</label>
          <select id="swal-dept" class="swal-select">
            <option value="" disabled selected>Select Department</option>
            <option value="HR">HR</option>
            <option value="IT">IT</option>
            <option value="FINANCE">FINANCE</option>
            <option value="OPERATIONS">OPERATIONS</option>
          </select>
        </div>
      `,
      customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-secondary'
      },
      buttonsStyling: false,
      showCancelButton: true,
      confirmButtonText: '<i class="fas fa-user-plus me-2"></i>Add Employee',
      cancelButtonText: 'Cancel',
      focusConfirm: false,
      preConfirm: () => {
        const name = (document.getElementById('swal-name') as HTMLInputElement).value.trim();
        const email = (document.getElementById('swal-email') as HTMLInputElement).value.trim();
        const department = (document.getElementById('swal-dept') as HTMLSelectElement).value;
  
        if (!name || !email || !department) {
          Swal.showValidationMessage('Please fill all fields');
          return;
        }
  
        return { name, email, departmentType: department };
      }
    }).then((result) => {
      if (result.isConfirmed && result.value) {
        if (this.validateEmployee(result.value)) {
          const employee: EmployeePost = result.value;
          this.employeeService.createEmployee(employee).subscribe({
            next: (response) => {
              if (response != null) {
                Swal.fire({
                  title: 'Success!',
                  text: 'Employee has been added.',
                  icon: 'success',
                  confirmButtonText: 'OK',
                  customClass: { confirmButton: 'btn btn-primary' },
                  buttonsStyling: false
                });
                this.getAllEmployees();
              }
            }
          });
        } else {
          Swal.fire({
            title: 'Error!',
            text: 'Invalid employee data.',
            icon: 'error',
            confirmButtonText: 'OK',
            customClass: { confirmButton: 'btn btn-danger' },
            buttonsStyling: false
          });
        }
      }
    });
  }  

  validateEmployee(employee: EmployeePost): boolean {

    if (employee.name.length > 100) {
      return false;
    }

    const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    if (!emailRegex.test(employee.email)) {
      return false;
    }

    const validDepartments = ['HR', 'IT', 'FINANCE', 'OPERATIONS'];
    if (!validDepartments.includes(employee.departmentType)) {
      return false;
    }

    return true;
  }

  updateEmployee(employee: Employee) {
    Swal.fire({
      title: '<strong>Update Employee</strong>',
      html: `
        <div class="swal-form-group">
          <label class="swal-label">Full Name</label>
          <input type="text" id="swal-name" class="swal-input" value="${employee.name}">
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Email Address</label>
          <input type="email" id="swal-email" class="swal-input" value="${employee.email}">
        </div>
        <div class="swal-form-group">
          <label class="swal-label">Department</label>
          <select id="swal-dept" class="swal-select">
            <option value="" disabled>Select Department</option>
            <option value="HR" ${employee.departmentType === 'HR' ? 'selected' : ''}>HR</option>
            <option value="IT" ${employee.departmentType === 'IT' ? 'selected' : ''}>IT</option>
            <option value="FINANCE" ${employee.departmentType === 'FINANCE' ? 'selected' : ''}>FINANCE</option>
            <option value="OPERATIONS" ${employee.departmentType === 'OPERATIONS' ? 'selected' : ''}>OPERATIONS</option>
          </select>
        </div>
      `,
      showCancelButton: true,
      confirmButtonText: 'Update Employee',
      cancelButtonText: 'Cancel',
      customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-secondary'
      },
      buttonsStyling: false,
      preConfirm: () => {
        const name = (document.getElementById('swal-name') as HTMLInputElement).value.trim();
        const email = (document.getElementById('swal-email') as HTMLInputElement).value.trim();
        const department = (document.getElementById('swal-dept') as HTMLSelectElement).value;
  
        if (!name || !email || !department) {
          Swal.showValidationMessage('Please fill all fields');
          return;
        }
  
        return { id: employee.id, name, email, departmentType: department };
      }
    }).then((result) => {
      if (result.isConfirmed && result.value) {
        if (this.validateEmployee(result.value)) {
          const updated: Employee = result.value;
          this.employeeService.updateEmployee(updated).subscribe({
            next: () => {
              Swal.fire({
                title: 'Success!',
                text: 'Employee updated successfully.',
                icon: 'success',
                confirmButtonText: 'OK',
                customClass: { confirmButton: 'btn btn-primary' },
                buttonsStyling: false
              });
              this.getAllEmployees();
            },
            error: () => {
              Swal.fire('Error!', 'Failed to update employee.', 'error');
            }
          });
        } else {
          Swal.fire('Error!', 'Invalid employee data.', 'error');
        }
      }
    });
  }    

  deleteEmployee(employee: Employee) {
    Swal.fire({
      title: 'Are you sure to delete this employee?',
      html: `
        <p><strong>Name:</strong> ${employee.name}</p>
        <p><strong>Email:</strong> ${employee.email}</p>
        <p><strong>Department:</strong> ${employee.departmentType}</p>
      `,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.employeeService.deleteEmployee(employee.id).subscribe(() => {
          Swal.fire('Deleted!', 'Employee has been deleted.', 'success');
          this.getAllEmployees();
        });
      }
    });
  }

  filteredEmployees(): Employee[] {
    return this.employees.filter(emp =>
      (this.searchName === '' || emp.name.toLowerCase().includes(this.searchName.toLowerCase())) &&
      (this.searchEmail === '' || emp.email.toLowerCase().includes(this.searchEmail.toLowerCase())) &&
      (this.searchDepartment === '' || emp.departmentType === this.searchDepartment)
    );
  }

  exportReposrts(): void {
    this.employeeService.exportEmployeeReport().subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'employees_report.csv';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
}
