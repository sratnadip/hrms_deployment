import { Component, OnInit } from '@angular/core';
import { AuthService, Compliance } from '../../app/auths/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-compliance',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule,FormsModule],
  templateUrl: './compliance.component.html',
  styleUrl: './compliance.component.css'
})
export class ComplianceComponent implements OnInit {

  compliance: Compliance = {
    name: '',
    description: '',
    type: 'STATUTORY',
    frequency: 'MONTHLY',
    dueDate: '',
    penalty: '',
    documentRequired: false,
    isActive: true,
    complianceId: undefined
  };

  compliances: Compliance[] = [];   
  selectedRecord: Compliance | null = null; 

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.loadCompliances();
  }

 
  loadCompliances(): void {
    this.authService.getAllCompliances().subscribe({
      next: (data) => {
        this.compliances = data;
      },
      error: (err) => {
        console.error('Failed to load compliances:', err);
      }
    });
  }

  
  onSubmit(): void {
    if (this.selectedRecord) {
      // update mode
      this.authService.updateCompliance(this.selectedRecord.id!, this.compliance).subscribe({
        next: () => {
          this.resetForm();
          this.loadCompliances();
        },
        error: (err) => console.error('Update failed:', err)
      });
    } else {
      // add mode
      this.authService.createCompliance(this.compliance).subscribe({
        next: () => {
          this.resetForm();
          this.loadCompliances();
        },
        error: (err) => console.error('Create failed:', err)
      });
    }
  }

 
  editRecord(record: Compliance): void {
    this.selectedRecord = { ...record };
    this.compliance = { ...record };
  }


  resetForm(): void {
    this.compliance = {
      name: '',
      description: '',
      type: 'STATUTORY',
      frequency: 'MONTHLY',
      dueDate: '',
      penalty: '',
      documentRequired: false,
      isActive: true
    };
    this.selectedRecord = null;
  }
}
