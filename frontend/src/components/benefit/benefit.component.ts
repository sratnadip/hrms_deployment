import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService, Benefit } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-benefit',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './benefit.component.html',
  styleUrl: './benefit.component.css'
})
export class BenefitComponent implements OnInit {
  fb = inject(FormBuilder);
  authService = inject(AuthService);

  benefitForm!: FormGroup;
  benefits: Benefit[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  ngOnInit(): void {
    this.benefitForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      type: ['MONETARY', Validators.required],
      taxable: [false, Validators.required],
    });

    this.loadBenefits();
  }

  loadBenefits(): void {
    this.loading = true;
    this.authService.getAllBenefits().subscribe({
      next: (res) => {
        this.benefits = res;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error loading benefits.';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.benefitForm.invalid) return;

    const payload = this.benefitForm.value as Benefit;

    this.authService.addBenefit(payload).subscribe({
      next: (res) => {
        this.successMessage = '✅ Benefit added successfully.';
        this.benefitForm.reset({ type: 'MONETARY', taxable: false });
        this.loadBenefits();
      },
      error: (err) => {
        this.errorMessage = '❌ Failed to add benefit.';
      }
    });
  }
}
