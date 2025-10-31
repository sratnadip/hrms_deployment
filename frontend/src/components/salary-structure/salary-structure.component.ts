import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
 
@Component({
  selector: 'app-salary-structure',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './salary-structure.component.html',
  styleUrl: './salary-structure.component.css'
})
export class SalaryStructureComponent implements OnInit {
 
  salaryForm!: FormGroup;
  employees: any[] = [];
  structures: any[] = [];
 
  selectedStructureId: string = '';
  isEditMode = false;
  modalTitle = 'Assign Salary Structure';
 
  constructor(private fb: FormBuilder, private authService: AuthService) {}
 
  ngOnInit(): void {
    this.initForm();
    this.loadEmployees();
    this.loadStructures();
  }
 
  initForm(): void {
    this.salaryForm = this.fb.group({
      empId: ['', Validators.required],
      basicSalary: ['', Validators.required],
      hra: ['', Validators.required],
      specialAllowance: [''],
      bonus: [''],
      pfDeduction: [''],
      taxDeduction: [''],
      effectiveFrom: ['', Validators.required]
    });
  }
 
  loadEmployees(): void {
    this.authService.getAllEmployees().subscribe({
      next: res => this.employees = res,
      error: () => alert('Failed to load employees')
    });
  }
 
loadStructures(): void {
  this.authService.getAllSalaryStructures().subscribe({
    next: res => {
      console.log('Fetched structures:', res); // for debugging
      this.structures = res;
    },
    error: err => {
      console.error('Failed to load structures:', err);
      alert('Failed to load structures');
    }
  });
}
 
 
  async openAddModal(): Promise<void> {
    this.modalTitle = 'Assign Salary Structure';
    this.isEditMode = false;
    this.salaryForm.reset();
 
    if (typeof window !== 'undefined') {
      const bootstrap = await import('bootstrap');
      const modal = document.getElementById('salaryModal');
      if (modal) new bootstrap.Modal(modal).show();
    }
  }
 
  async openEditModal(structure: any): Promise<void> {
    this.modalTitle = 'Edit Salary Structure';
    this.isEditMode = true;
    this.selectedStructureId = structure.salaryStructureId;
 
    this.salaryForm.patchValue({
      empId: structure.employee.empId,
      basicSalary: structure.basicSalary,
      hra: structure.hra,
      specialAllowance: structure.specialAllowance,
      bonus: structure.bonus,
      pfDeduction: structure.pfDeduction,
      taxDeduction: structure.taxDeduction,
      effectiveFrom: structure.effectiveFrom
    });
 
    if (typeof window !== 'undefined') {
      const bootstrap = await import('bootstrap');
      const modal = document.getElementById('salaryModal');
      if (modal) new bootstrap.Modal(modal).show();
    }
  }
 
  submitForm(): void {
    if (this.salaryForm.invalid) return;
 
    const payload = {
      employee: { empId: this.salaryForm.value.empId },
      basicSalary: this.salaryForm.value.basicSalary,
      hra: this.salaryForm.value.hra,
      specialAllowance: this.salaryForm.value.specialAllowance,
      bonus: this.salaryForm.value.bonus,
      pfDeduction: this.salaryForm.value.pfDeduction,
      taxDeduction: this.salaryForm.value.taxDeduction,
      effectiveFrom: this.salaryForm.value.effectiveFrom
    };
 
    if (this.isEditMode) {
      this.authService.updateSalaryStructure(this.selectedStructureId, payload).subscribe(() => {
        alert('Updated!');
        this.loadStructures();
      });
    } else {
      this.authService.assignSalaryStructure(payload).subscribe(() => {
        alert('Assigned!');
        this.loadStructures();
      });
    }
 
    const modal = document.getElementById('closeModalBtn') as HTMLElement;
    modal?.click();
  }
 
  deleteStructure(id: string): void {
    if (!confirm('Are you sure?')) return;
    this.authService.deleteSalaryStructure(id).subscribe(() => {
      alert('Deleted!');
      this.loadStructures();
    });
  }
 
 
}
 
 