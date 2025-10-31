import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, Compliance, ComplianceRecord } from '../../app/auths/auth.service';


@Component({
  selector: 'app-compliance-record',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './compliance-records.component.html',
  styleUrls: ['./compliance-records.component.css']
})
export class ComplianceRecordsComponent implements OnInit {
  compliances: Compliance[] = [];
  records: ComplianceRecord[] = [];
  selectedPeriod: string = '';

  record: ComplianceRecord = {
    compliance: { complianceId: 0 },
    period: '',
    status: 'PENDING',
    remarks: '',
    createdBy: ''
  };

  selectedRecord: ComplianceRecord | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.fetchCompliances();
  }

  // ✅ Fetch all compliances
  fetchCompliances() {
    this.authService.getAllCompliances().subscribe({
      next: (data) => this.compliances = data,
      error: (err) => console.error('Error fetching compliances', err)
    });
  }

  // ✅ Fetch records for selected period
  fetchRecords() {
    if (!this.selectedPeriod) return;
    this.authService.getComplianceRecordsByPeriod(this.selectedPeriod).subscribe({
      next: (data) => this.records = data,
      error: (err) => console.error('Error fetching records', err)
    });
  }

  // ✅ Get compliance name
  getComplianceName(complianceId: number): string {
    const compliance = this.compliances.find(c => c.complianceId === complianceId);
    return compliance ? compliance.name : 'N/A';
  }

  // ✅ Submit new record
  onSubmit() {
    if (this.record.compliance.complianceId === 0) {
      alert('Please select a valid Compliance');
      return;
    }

    this.record.createdBy = this.authService.currentUserValue?.username || 'admin';

    this.authService.createComplianceRecord(this.record).subscribe({
      next: () => {
        alert('Compliance record added successfully!');
        this.resetForm();
        this.fetchRecords();
      },
      error: (err) => {
        console.error('Error saving record', err);
      }
    });
  }

  // ✅ Edit record
  editRecord(item: ComplianceRecord) {
    const formattedItem = { ...item };
    if (formattedItem.submittedOn) {
      formattedItem.submittedOn = formattedItem.submittedOn.slice(0, 16); // format for datetime-local input
    }
    this.selectedRecord = formattedItem;
  }

  // ✅ Update record
  updateRecord() {
    if (!this.selectedRecord?.complianceRecordId) return;

    this.authService.updateComplianceRecord(this.selectedRecord.complianceRecordId, this.selectedRecord).subscribe({
      next: () => {
        alert('Compliance record updated successfully!');
        this.selectedRecord = null;
        this.fetchRecords();
      },
      error: (err) => {
        console.error('Error updating record', err);
      }
    });
  }

  // ✅ Reset form
  resetForm() {
    this.record = {
      compliance: { complianceId: 0 },
      period: '',
      status: 'PENDING',
      remarks: '',
      createdBy: ''
    };
  }
}
