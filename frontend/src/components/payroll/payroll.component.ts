import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
import FileSaver from 'file-saver';
import { HttpClientModule } from '@angular/common/http';
 
@Component({
  selector: 'app-payroll',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './payroll.component.html',
  styleUrls: ['./payroll.component.css']
})
export class PayrollComponent implements OnInit {
  payrollForm!: FormGroup;
  employees: any[] = [];
  payrolls: any[] = [];
  payslip: any;
  isPayslipAvailable = false;
  userRole: string | null = null;
 
  constructor(private fb: FormBuilder, private authService: AuthService) {}
 
  ngOnInit(): void {
  this.userRole = this.authService.getUserRole();
  this.initForm();

  if (this.userRole === 'ADMIN') {
    this.loadEmployees();
    this.loadPayrolls();
  } else if (['HR', 'MANAGER', 'FINANCE'].includes(this.userRole)) {
    this.loadEmployees(); // to show dropdown
  }
}

 
  initForm(): void {
    this.payrollForm = this.fb.group({
      empId: ['', Validators.required],
      fromDate: ['', Validators.required],
      toDate: ['', Validators.required],
      month: ['', Validators.required]
    });
  }
 
  loadEmployees(): void {
    this.authService.getAllEmployees().subscribe({
      next: (res) => (this.employees = res),
      error: () => alert('Failed to load employees')
    });
  }
 
  loadPayrolls(): void {
    this.authService.getAllPayrolls().subscribe({
      next: (res) => (this.payrolls = res),
      error: () => alert('Failed to load payrolls')
    });
  }
 
  addPayroll(): void {
    if (this.payrollForm.invalid) return;
 
    const payload = {
      empId: this.payrollForm.value.empId,
      fromDate: this.payrollForm.value.fromDate,
      toDate: this.payrollForm.value.toDate,
      month: this.payrollForm.value.month.trim().toUpperCase(),
      year: new Date().getFullYear().toString(),
      workingDays: 22,
      workingHoursInMinutes: 14400
    };
 
    console.log('🧾 Payroll Payload:', payload);
 
    this.authService.addPayroll(payload).subscribe({
      next: () => {
        alert('✅ Payroll added');
        this.loadPayrolls();
      },
      error: (err) => alert(err?.error || '❌ Failed to add payroll')
    });
  }
 
  generatePayslip(): void {
    const { empId, month } = this.payrollForm.value;
    if (!empId || !month) return;
 
    this.authService.generatePayslip(empId, month.trim().toUpperCase()).subscribe({
      next: (res) => alert(`✅ ${res}`),
      error: (err) => alert(err?.error || '❌ Failed to generate payslip')
    });
  }
 
downloadPayslip(): void {
  const { empId, month } = this.payrollForm.value;
  if (!month) return;

  const selectedEmpId = this.userRole === 'EMPLOYEE' ? null : empId;
  const monthUpper = month.trim().toUpperCase();

  const method = this.userRole === 'EMPLOYEE'
    ? this.authService.downloadPayslipSelf(monthUpper)
    : this.authService.downloadPayslip(selectedEmpId, monthUpper);

  method.subscribe({
    next: (res) => {
      const file = new Blob([res], { type: 'application/pdf' });
      FileSaver.saveAs(file, `Payslip-${monthUpper}.pdf`);
    },
    error: () => alert('❌ Failed to download payslip')
  });
}

 
 
  viewPayslip(): void {
  const { empId, month } = this.payrollForm.value;
  if (!month) return;

  const selectedEmpId = this.userRole === 'EMPLOYEE' ? null : empId;
  const monthUpper = month.trim().toUpperCase();

  const method = this.userRole === 'EMPLOYEE'
    ? this.authService.viewPayslipSelf(monthUpper)
    : this.authService.viewPayslip(selectedEmpId, monthUpper);

  method.subscribe({
    next: (res) => {
      this.payslip = res;
      this.isPayslipAvailable = true;
    },
    error: () => alert('❌ No payslip found')
  });
}

  
}
 
 