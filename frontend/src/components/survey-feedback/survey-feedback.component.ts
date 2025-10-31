import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators, ReactiveFormsModule, FormArray } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { formatDate } from '@angular/common';
import { AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-survey-feedback',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ToastModule],
  providers: [MessageService],
  templateUrl: './survey-feedback.component.html',
  styleUrls: ['./survey-feedback.component.css']
})
export class SurveyFeedbackComponent implements OnInit {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  messageService = inject(MessageService);

  surveyForm!: FormGroup;
  roles = ['EMPLOYEE', 'HR', 'MANAGER', 'FINANCE'];
  surveyTypes = ['PERFORMANCE', 'ENGAGEMENT', 'EXIT'];
  surveys: any[] = [];

  ngOnInit(): void {
    this.initForm();
    this.loadSurveys();
  }

  initForm() {
    this.surveyForm = this.fb.group({
      title: ['', Validators.required],
      type: ['', Validators.required],
      targetRoles: [[], Validators.required],
      deadline: [''],
      questions: this.fb.array([this.fb.control('', Validators.required)])
    });
  }

  get questions(): FormArray {
    return this.surveyForm.get('questions') as FormArray;
  }

  addQuestion() {
    this.questions.push(this.fb.control('', Validators.required));
  }

  removeQuestion(index: number) {
    this.questions.removeAt(index);
  }

  createSurvey() {
    if (this.surveyForm.invalid) return;

    const formData = {
      ...this.surveyForm.value,
      questionsJson: JSON.stringify(this.questions.value),
      deadline: this.surveyForm.value.deadline || null
    };
    delete formData.questions;

    this.authService.createSurvey(formData).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Survey Created' });
        this.surveyForm.reset();
        this.questions.clear();
        this.questions.push(this.fb.control('', Validators.required));
        this.loadSurveys();
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Creation Failed' })
    });
  }

  loadSurveys() {
    this.authService.getSurveys().subscribe({
      next: (res) => (this.surveys = res),
      error: () => this.messageService.add({ severity: 'error', summary: 'Load Failed' })
    });
  }

  formatDateTime(date: string): string {
    return formatDate(date, 'dd/MM/yyyy HH:mm', 'en-IN');
  }
}
