import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { NavBarComponent } from "../../components/nav-bar/nav-bar.component";

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, NavBarComponent, RouterModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent {

  constructor(
      public authService: AuthService,
      private router: Router) { }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}
