import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-employee-leaves-request',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './employee-leaves-request.component.html',
  styleUrls: ['./employee-leaves-request.component.css']
})
export class EmployeeLeavesRequestComponent implements OnInit {
  leaveRequests: any[] = [];
  loading = true;
  errorMessage = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.getPendingRequests();
  }

  getPendingRequests(): void {
    this.authService.getLeaveRequestsForManager().subscribe({
      next: (res: any[]) => {
        console.log('Leave Requests:', res);
        this.leaveRequests = res.filter(request => request.status === 'PENDING');
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching leave requests', err);
        this.errorMessage = 'Error fetching leave requests.';
        this.loading = false;
      }
    });
  }

  approveRequest(requestId: number): void {
    this.authService.approveLeaveRequest(requestId).subscribe(() => {
      this.leaveRequests = this.leaveRequests.filter(req => req.id !== requestId);
    });
  }

  rejectRequest(requestId: number): void {
    this.authService.rejectLeaveRequest(requestId).subscribe(() => {
      this.leaveRequests = this.leaveRequests.filter(req => req.id !== requestId);
    });
  }
}
