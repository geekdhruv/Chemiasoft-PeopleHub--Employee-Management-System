import { Component } from '@angular/core';
import { NavBarComponent } from "../../components/nav-bar/nav-bar.component";
import { ViewLeavesComponent } from "../../components/view-leaves/view-leaves.component";

@Component({
  selector: 'app-leave',
  imports: [NavBarComponent, ViewLeavesComponent],
  templateUrl: './leave.component.html',
  styleUrl: './leave.component.css'
})
export class LeaveComponent {

}
