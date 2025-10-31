import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../app/auths/auth.service';
 
@Component({
  selector: 'app-document',
  standalone: true,
  templateUrl: './document.component.html',
  styleUrl: './document.component.css',
  imports: [CommonModule, ReactiveFormsModule],
})
export class DocumentComponent implements OnInit {
  uploadForm!: FormGroup;
  documents: any[] = [];
  expiringDocs: any[] = [];
  employees: any[] = [];
  selectedFile: File | null = null;
 
  constructor(private fb: FormBuilder, private auth: AuthService) {}
 
  ngOnInit(): void {
    this.uploadForm = this.fb.group({
      type: ['', Validators.required],
      file: [null, Validators.required],
      empId: ['', Validators.required],
    });
 
    this.fetchEmployees();
    this.fetchExpiringDocuments();
  }
 
  fetchEmployees(): void {
    this.auth.getAllEmployees().subscribe({
      next: (res) => (this.employees = res),
      error: () => alert('Failed to load employees.'),
    });
  }
 
  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.uploadForm.patchValue({ file });
    }
  }
 
  uploadDocument(): void {
    if (this.uploadForm.invalid || !this.selectedFile) {
      alert('⚠️ Please complete all fields and choose a file.');
      return;
    }
 
    const { type, empId } = this.uploadForm.value;
    this.auth.uploadDocument(empId, type, this.selectedFile).subscribe({
      next: () => {
        alert('✅ Document uploaded successfully!');
        this.uploadForm.reset();
        this.selectedFile = null;
        this.fetchDocuments(empId);
      },
      error: () => alert('❌ Upload failed. Please try again.'),
    });
  }
 
  fetchDocuments(empId: string): void {
    this.auth.getDocumentsByEmployee(empId).subscribe({
      next: (res) => {
        console.log('📄 Documents:', res);
        this.documents = res;
      },
      error: () => alert('⚠️ Unable to fetch documents.'),
    });
  }
 
  onEmployeeChange(): void {
    const empId = this.uploadForm.get('empId')?.value;
    if (empId) {
      this.fetchDocuments(empId);
    }
  }
 
  // ✅ Updated verify method with uploadedBy.userId
  verify(docId: string): void {
    if (!docId) {
      alert('⚠️ Invalid document ID.');
      return;
    }
 
    const remarks = prompt('Enter verification remarks:')?.trim();
    if (!remarks) {
      alert('⚠️ Remarks are required.');
      return;
    }
 
    const doc = this.documents.find((d) => d.id === docId);
    const userId = doc?.uploadedBy?.userId;
 
    if (!userId) {
      alert('❌ Cannot verify — UploadedBy user ID not found.');
      return;
    }
 
    this.auth.verifyDocument(docId, userId, remarks).subscribe({
      next: () => {
        alert('✅ Document verified successfully.');
        const empId = this.uploadForm.get('empId')?.value;
        if (empId) this.fetchDocuments(empId);
      },
      error: (err) => {
        console.error('❌ Verification error:', err);
        if (err.status === 401) {
          alert('🔒 Unauthorized - please log in again.');
        } else {
          alert('❌ Verification failed.');
        }
      },
    });
  }
 
  deleteDocument(docId: string): void {
    if (!docId) {
      alert('⚠️ Invalid document ID.');
      return;
    }
 
    if (confirm('Are you sure you want to delete this document?')) {
      this.auth.deleteDocument(docId).subscribe({
        next: () => {
          alert('🗑️ Document deleted.');
          const empId = this.uploadForm.get('empId')?.value;
          if (empId) this.fetchDocuments(empId);
        },
        error: () => alert('❌ Deletion failed.'),
      });
    }
  }
 
  fetchExpiringDocuments(): void {
    this.auth.getExpiringDocuments().subscribe({
      next: (res) => (this.expiringDocs = res),
      error: () => console.error('Failed to fetch expiring documents'),
    });
  }
}
 
 