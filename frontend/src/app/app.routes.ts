import { Routes } from '@angular/router';

// 🔓 Public Components
import { LoginComponent } from '../components/login/login.component';
import { RegisterComponent } from '../components/register/register.component';

// 🧭 Dashboards
import { DashboardComponent } from '../components/dashboard/dashboard.component';
import { HomeComponent } from '../components/home/home.component';
import { HrDashboardComponent } from '../components/hr-dashboard/hr-dashboard.component';
import { FinanceDashboardComponent } from '../components/finance-dashboard/finance-dashboard.component';
import { ManagerDashboardComponent } from '../components/manager-dashboard/manager-dashboard.component';
import { EmployeesDashboardComponent } from '../components/employees-dashboard/employees-dashboard.component';

// 📁 Feature Components
import { AttendanceComponent } from '../components/attendance/attendance.component';
import { AnnouncementComponent } from '../components/announcement/announcement.component';
import { AppreciationComponent } from '../components/appreciation/appreciation.component';
import { AuditLogComponent } from '../components/audit-log/audit-log.component';
import { BenefitComponent } from '../components/benefit/benefit.component';
import { ComplianceComponent } from '../components/compliance/compliance.component';
import { DEIComponent } from '../components/dei/dei.component';
import { DepartmentComponent } from '../components/department/department.component';
import { DocumentComponent } from '../components/document/document.component';
import { EmployeeBenefitComponent } from '../components/employee-benefit/employee-benefit.component';
import { LeaveComponent } from '../components/leave/leave.component';
import { LeaveTypeComponent } from '../components/leave-type/leave-type.component';
import { PayrollComponent } from '../components/payroll/payroll.component';
import { PerformanceReviewComponent } from '../components/performance-review/performance-review.component';
import { SalaryStructureComponent } from '../components/salary-structure/salary-structure.component';
import { TrainingComponent } from '../components/training/training.component';
import { UserComponent } from '../components/user/user.component';
import { AttendanceChartComponent } from '../components/attendance-chart/attendance-chart.component';
import { LeaveCalendarComponent } from '../components/leave-calendar/leave-calendar.component';
import { QuickActionsComponent } from '../components/quick-actions/quick-actions.component';
import { DepartmentChartComponent } from '../components/department-chart/department-chart.component';
import { UpcomingEventsComponent } from '../components/upcoming-events/upcoming-events.component';
import { PendingApprovalsComponent } from '../components/pending-approvals/pending-approvals.component';
import { TicketComponent } from '../components/ticket/ticket.component';
import { BankDetailsComponent } from '../components/bank-details/bank-details.component';
import { ComplianceRecordsComponent } from '../components/compliance-records/compliance-records.component';
import { EmployeeListComponent } from '../components/employee-list/employee-list.component';
import { LeaveBalanceComponent } from '../components/leave-balance/leave-balance.component';
import { AddLeaveRequestComponent } from '../components/add-leave-request/add-leave-request.component';
import { EmployeeLeavesRequestComponent } from '../components/employee-leaves-request/employee-leaves-request.component';

// 🧍‍♂️ Standalone Component
import { AddEmployeeComponent } from '../components/add-employee/add-employee.component';

// 🔐 Auth Guard
import { AuthGuard } from './auths/auth.guard';
import { SurveyFeedbackComponent } from '../components/survey-feedback/survey-feedback.component';

export const routes: Routes = [
  // Default Route
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Public Routes
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // Dashboards
  { path: 'hr-dashboard', component: HrDashboardComponent },
  { path: 'employees-dashboard', component: EmployeesDashboardComponent },
  { path: 'finance-dashboard', component: FinanceDashboardComponent },
  { path: 'manager-dashboard', component: ManagerDashboardComponent },

  // Manager Specific
  { path: 'employee-leave-requests', component: EmployeeLeavesRequestComponent },
  { path: 'employee-list', component: EmployeeListComponent },
  { path: 'attendance', component: AttendanceComponent },
  { path: 'leave', component: LeaveComponent },
  { path: 'performance-review', component: PerformanceReviewComponent },
  { path: 'ticket', component: TicketComponent },
  { path: 'training', component: TrainingComponent },

  // General Feature Routes
  { path: 'announcement', component: AnnouncementComponent },
  { path: 'appriciation', component: AppreciationComponent },
  { path: 'auditlog', component: AuditLogComponent },
  { path: 'benifit', component: BenefitComponent },
  { path: 'compliance', component: ComplianceComponent },
  { path: 'dei', component: DEIComponent },
  { path: 'department', component: DepartmentComponent },
  { path: 'document', component: DocumentComponent },
  { path: 'employeebenifit', component: EmployeeBenefitComponent },
  { path: 'leavetype', component: LeaveTypeComponent },
  { path: 'payroll', component: PayrollComponent },
  { path: 'salary-structure', component: SalaryStructureComponent },
  { path: 'user', component: UserComponent },
  { path: 'leave-calendar', component: LeaveCalendarComponent },
  { path: 'attendance-chart', component: AttendanceChartComponent },
  { path: 'department-chart', component: DepartmentChartComponent },
  { path: 'quick-action', component: QuickActionsComponent },
  { path: 'upcoming-events', component: UpcomingEventsComponent },
  { path: 'pending-approvals', component: PendingApprovalsComponent },
  { path: 'bank-details', component: BankDetailsComponent },
  { path: 'compliance-records', component: ComplianceRecordsComponent },
  { path: 'leave-balance', component: LeaveBalanceComponent },
  { path: 'apply-leave', component: AddLeaveRequestComponent },
  { path: 'survey-feedback', component: SurveyFeedbackComponent },

  // Standalone Component
  { path: 'add-employee', component: AddEmployeeComponent },

  // Protected Dashboard Container with Child
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
        children: [
          { path: '', component: HomeComponent }
        ]
      }
    ]
  }

  // Optional Wildcard Redirect
  // { path: '**', redirectTo: 'login' }
];
