import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../app/auths/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-employee-benefit',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './employee-benefit.component.html',
  styleUrl: './employee-benefit.component.css'
})
export class EmployeeBenefitComponent implements OnInit {
  benefitForm!: FormGroup;
  employees: any[] = [];
  alertMessage = '';
  alertType: 'success' | 'danger' | '' = '';
  showAlert = false;
 
  constructor(private fb: FormBuilder, private authService: AuthService) {}
 
  ngOnInit(): void {
    this.benefitForm = this.fb.group({
      empId: ['', Validators.required],
      benefitId: ['', Validators.required],
      amount: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
      effectiveFrom: ['', Validators.required],
      effectiveTo: ['', Validators.required],
      notes: ['']
    });
 
    this.loadEmployees();
  }
 
  loadEmployees() {
  this.authService.getAllEmployees().subscribe({
    next: (res) => {
      console.log('Employees loaded:', res); // ✅ Add this
      this.employees = res;
    },
    error: (err) => {
      console.error('Error loading employees:', err);
      this.showError('Failed to load employees.');
    }
  });
}
  closeAlert() {
    this.showAlert = false;
  }
 
  onSubmit() {
    if (this.benefitForm.invalid) {
      this.showError('Please correct the form errors.');
      return;
    }
 
    const form = this.benefitForm.value;
 
    const payload = {
      employee: {
        empId: form.empId
      },
      benefit: {
        benefitId: form.benefitId
      },
      amount: form.amount,
      effectiveFrom: form.effectiveFrom,
      effectiveTo: form.effectiveTo,
      notes: form.notes,
      status: 'ACTIVE',
      createdBy: 'hrmanager',
      updatedBy: 'hrmanager'
    };
 
    this.authService.assignBenefit(payload).subscribe({
      next: () => {
        this.showSuccess('Benefit assigned successfully!');
        this.benefitForm.reset();
      },
      error: (err: HttpErrorResponse) => {
        this.showError(err.error?.message || 'Something went wrong.');
      }
    });
  }
 
  showSuccess(msg: string) {
    this.alertMessage = msg;
    this.alertType = 'success';
    this.showAlert = true;
  }
 
  showError(msg: string) {
    this.alertMessage = msg;
    this.alertType = 'danger';
    this.showAlert = true;
  }
}