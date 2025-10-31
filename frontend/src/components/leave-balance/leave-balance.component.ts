import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../app/auths/auth.service';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-leave-balance',
  standalone: true,
  templateUrl: './leave-balance.component.html',
  styleUrls: ['./leave-balance.component.css'],
  providers: [MessageService],
  imports: [CommonModule, ToastModule],
})
export class LeaveBalanceComponent implements OnInit {
  leaveBalance: any;

  constructor(
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.fetchLeaveBalance();
  }

  fetchLeaveBalance(): void {
    this.authService.getLeaveBalance().subscribe({
      next: (data) => {
        this.leaveBalance = data;
      },
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to fetch leave balance',
        });
      },
    });
  }
}
