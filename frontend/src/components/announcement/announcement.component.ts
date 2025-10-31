import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
 
@Component({
  selector: 'app-announcement',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './announcement.component.html',
  styleUrls: ['./announcement.component.css']
})
export class AnnouncementComponent implements OnInit {
  form: FormGroup;
 
  employees: any[] = [];
  roles: string[] = ['ADMIN', 'HR', 'MANAGER', 'EMPLOYEE', 'FINANCE'];
  departments: string[] = [];
 
  constructor(
    private fb: FormBuilder,
    private auth: AuthService
  ) {
    this.form = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(150)]],
      message: ['', [Validators.required, Validators.maxLength(2000)]],
      visibility: ['ALL', Validators.required],
      visibleToValue: [''],
      expiresOn: ['']
    });
  }
 
  ngOnInit(): void {
    // Load employees
    this.auth.getAllEmployees().subscribe({
      next: (res) => this.employees = res,
      error: () => alert('❌ Failed to load employees')
    });
 
    // Load departments
    this.auth.getDepartments().subscribe({
      next: (res) => this.departments = res,
      error: () => alert('❌ Failed to load departments')
    });
 
    // Set or clear validator dynamically
    this.form.get('visibility')?.valueChanges.subscribe((vis) => {
      const visibleField = this.form.get('visibleToValue');
      visibleField?.reset();
 
      if (vis === 'ALL') {
        visibleField?.clearValidators();
      } else {
        visibleField?.setValidators([Validators.required]);
      }
 
      visibleField?.updateValueAndValidity();
    });
  }
 
  submit(): void {
    if (this.form.invalid) {
      alert('⚠️ Please fill all required fields correctly');
      return;
    }
 
    const payload = { ...this.form.value };
 
    // If visibility is ALL, don't send visibleToValue
    if (payload.visibility === 'ALL') {
      delete payload.visibleToValue;
    }
 
    console.log('📤 Submitting payload:', payload);
 
    this.auth.postAnnouncement(payload).subscribe({
      next: () => {
        alert('✅ Announcement posted!');
        this.form.reset({ visibility: 'ALL' });
      },
      error: (err: any) => {
        console.error('❌ Failed to post announcement:', err);
        alert('❌ Failed to post announcement.');
      }
    });
  }
}
 
 