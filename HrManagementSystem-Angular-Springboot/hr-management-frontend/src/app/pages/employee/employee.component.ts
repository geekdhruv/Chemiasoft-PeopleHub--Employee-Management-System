import { Component } from '@angular/core';
import { ViewEmployeesComponent } from "../../components/view-employees/view-employees.component";
import { NavBarComponent } from "../../components/nav-bar/nav-bar.component";

@Component({
  selector: 'app-employee',
  imports: [ViewEmployeesComponent, NavBarComponent],
  templateUrl: './employee.component.html',
  styleUrl: './employee.component.css'
})
export class EmployeeComponent {

}
