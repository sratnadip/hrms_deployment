import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../app/auths/auth.service';
import { ToastModule } from 'primeng/toast';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-leave-request',
  standalone: true,
  imports: [ToastModule, ReactiveFormsModule, CommonModule],
  templateUrl: './add-leave-request.component.html',
  styleUrls: ['./add-leave-request.component.css']
})
export class AddLeaveRequestComponent implements OnInit {
  leaveForm!: FormGroup;
  leaveTypes: any[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.fetchLeaveTypes();
  }

  initForm() {
    this.leaveForm = this.fb.group({
      leaveTypeId: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      noOfHalfDays: ['', [Validators.required, Validators.min(0.5)]],
      reason: ['', Validators.required],
    });
  }

  fetchLeaveTypes() {
    this.authService.getAllLeaveTypes().subscribe({
      next: (res: any) => {
        this.leaveTypes = res;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load leave types' });
      }
    });
  }

  submitLeaveRequest() {
    if (this.leaveForm.invalid) {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please fill all fields' });
      return;
    }

    const startDate = new Date(this.leaveForm.value.startDate);
    const year = startDate.getFullYear().toString();
    const month = (startDate.getMonth() + 1).toString().padStart(2, '0');

    const payload = {
      leaveType: {
        leaveTypeId: this.leaveForm.value.leaveTypeId
      },
      startDate: this.leaveForm.value.startDate,
      endDate: this.leaveForm.value.endDate,
      noOfHalfDays: this.leaveForm.value.noOfHalfDays,
      month: month,
      year: year,
      reason: this.leaveForm.value.reason,
    };

    this.authService.addLeaveRequest(payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Leave request submitted' });
        this.leaveForm.reset();
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Leave request failed' });
      }
    });
  }
}
