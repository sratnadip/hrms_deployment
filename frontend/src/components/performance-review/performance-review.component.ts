import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-performance-review',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TableModule, ToastModule, ButtonModule],
  providers: [MessageService],
  templateUrl: './performance-review.component.html',
  styleUrls: ['./performance-review.component.css']
})
export class PerformanceReviewComponent implements OnInit {
  cycleForm: FormGroup;
  activeCycles: any[] = [];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) {
    this.cycleForm = this.fb.group({
      cycleName: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadActiveCycles();
  }

  createCycle() {
    if (this.cycleForm.valid) {
      this.authService.createReviewCycle(this.cycleForm.value).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Cycle Created Successfully' });
          this.cycleForm.reset();
          this.loadActiveCycles();
        },
        error: () => {
          this.messageService.add({ severity: 'error', summary: 'Failed to Create Cycle' });
        }
      });
    }
  }

  closeCycle(id: number) {
    this.authService.closeReviewCycle(id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'info', summary: 'Cycle Closed' });
        this.loadActiveCycles();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Failed to Close Cycle' });
      }
    });
  }

  loadActiveCycles() {
    this.authService.getActiveReviewCycles().subscribe({
      next: (res) => this.activeCycles = res,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error Fetching Cycles' })
    });
  }
}
