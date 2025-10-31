import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './employee-list.component.html',
  styleUrl: './employee-list.component.css'
})
export class EmployeeListComponent implements OnInit {
  employees: any[] = [];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.loadEmployeesWithBankDetails();
  }

  loadEmployeesWithBankDetails(): void {
    this.authService.getAllEmployees().subscribe((res: any[]) => {
      this.employees = res;

      this.employees.forEach(emp => {
        if (emp.bankDetailsId) {
          this.authService.getBankDetailsById(emp.bankDetailsId).subscribe(
            (bankData) => emp.bankDetails = bankData,
            () => emp.bankDetails = null
          );
        }
      });
    });
  }
}