import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-leave-type',
  standalone: true,
  templateUrl: './leave-type.component.html',
  styleUrls: ['./leave-type.component.css'],
  providers: [MessageService],
  imports: [CommonModule, ReactiveFormsModule, ToastModule]
})
export class LeaveTypeComponent implements OnInit {
  leaveTypeForm!: FormGroup;
  leaveNames = ['SICK_LEAVE', 'CASUAL_LEAVE', 'ANNUAL_LEAVE', 'MATERNITY_LEAVE', 'PATERNITY_LEAVE'];
  leaveTypes: any[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.leaveTypeForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', [Validators.required, Validators.maxLength(200)]],
      maxDaysPerYear: ['', [Validators.required, Validators.min(1)]]
    });

    this.loadLeaveTypes();
  }

  loadLeaveTypes(): void {
    this.authService.getAllLeaveTypes().subscribe({
      next: (data) => this.leaveTypes = data,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load leave types.' })
    });
  }

  onSubmit(): void {
    if (this.leaveTypeForm.invalid) {
      this.messageService.add({ severity: 'warn', summary: 'Validation Error', detail: 'Please complete the form.' });
      return;
    }

    this.authService.addLeaveType(this.leaveTypeForm.value).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Leave type created successfully!' });
        this.leaveTypeForm.reset();
        this.loadLeaveTypes();
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to create leave type.' })
    });
  }

  deleteLeaveType(id: string): void {
    if (confirm("Are you sure you want to delete this leave type?")) {
      this.authService.deleteLeaveType(id).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Leave type deleted successfully!' });
          this.loadLeaveTypes();
        },
        error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Delete failed!' })
      });
    }
  }
}
