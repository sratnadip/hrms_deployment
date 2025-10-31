// dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
import { ToastModule } from 'primeng/toast';
import { HrSidebarComponent } from '../hr-sidebar/hr-sidebar.component';
import { FinanceSidebarComponent } from '../finance-sidebar/finance-sidebar.component';
import { ManagerSidebarComponent } from '../manager-sidebar/manager-sidebar.component';
import { StatsCardsComponent } from '../stats-cards/stats-cards.component';
import { AttendanceChartComponent } from '../attendance-chart/attendance-chart.component';
import { DepartmentChartComponent } from '../department-chart/department-chart.component';
import { QuickActionsComponent } from '../quick-actions/quick-actions.component';
import { UpcomingEventsComponent } from '../upcoming-events/upcoming-events.component';
import { PendingApprovalsComponent } from '../pending-approvals/pending-approvals.component';
import { SidebarComponent } from "../sidebar/sidebar.component";
import { AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterModule,
    ToastModule,
    HrSidebarComponent,
    FinanceSidebarComponent,
    ManagerSidebarComponent,
    StatsCardsComponent,
    AttendanceChartComponent,
    DepartmentChartComponent,
    QuickActionsComponent,
    UpcomingEventsComponent,
    PendingApprovalsComponent,
    SidebarComponent
],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  viewMode = 'ADMIN';
  department = 'All';
  username = '';
  role = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const user = this.authService.getUserInfo();
    if (user) {
      this.username = user.username;
      this.role = user.role;
      this.viewMode = this.mapRoleToViewMode(user.role);
    }
  }

  private mapRoleToViewMode(role: string): string {
    switch (role.toUpperCase()) {
      case 'ADMIN': return 'ADMIN';
      case 'HR': return 'HR';
      case 'FINANCE': return 'Finance';
      case 'MANAGER': return 'Management';
      case 'EMPLOYEE': return 'Employee';
      default: return 'ADMIN';
    }
  }
}