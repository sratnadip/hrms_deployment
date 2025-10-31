import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-bank-details',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './bank-details.component.html',
  styleUrl: './bank-details.component.css'
})
export class BankDetailsComponent implements OnInit {
  bankForm!: FormGroup;
  isSubmitted = false;
  isVisible = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private location: Location // ✅ Inject Location
  ) {}

  ngOnInit(): void {
    this.bankForm = this.fb.group({
      accountHolderName: ['', [Validators.required, Validators.pattern(/^[A-Za-z ]{3,}$/)]],
      bankName: ['', [Validators.required, Validators.pattern(/^[A-Za-z ]{3,}$/)]],
      accountNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{8,20}$/)]],
      ifscCode: ['', [Validators.required, Validators.pattern(/^[A-Z]{4}0[A-Z0-9]{6}$/)]],
      branch: ['', [Validators.required, Validators.minLength(3)]],
      accountType: ['', Validators.required]
    });
  }

  get f() {
    return this.bankForm.controls;
  }

  onSubmit(): void {
    this.isSubmitted = true;

    if (this.bankForm.invalid) return;

    this.authService.createBankDetails(this.bankForm.value).subscribe({
      next: () => {
        window.alert('Bank details added successfully.');
        this.bankForm.reset();
        this.isSubmitted = false;
      },
      error: (err) => {
        window.alert('Failed to add bank details.');
        console.error(err);
      }
    });
  }

  onClear(): void {
    if (window.confirm('Are you sure you want to clear the form?')) {
      this.bankForm.reset();
      this.isSubmitted = false;
    }
  }

  closeForm(): void {
    this.location.back(); // ✅ Go to previous page
  }
}
