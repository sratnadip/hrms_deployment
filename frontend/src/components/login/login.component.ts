import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { AuthService } from '../../app/auths/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ToastModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [MessageService]
})
export class LoginComponent {
  username = '';
  password = '';
  email = '';
  otp = '';
  newPassword = '';
  isAdmin = true;
  showForgot = false;
  showReset = false;
  isLoading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) { }

  login(): void {
    if (!this.username || !this.password) {
      this.showError('Username and password are required');
      return;
    }

    this.isLoading = true;
    const loginMethod = this.isAdmin
      ? this.authService.adminLogin
      : this.authService.userLogin;

    loginMethod.call(this.authService, this.username, this.password).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.showSuccess(`Welcome, ${this.username}!`);

        setTimeout(() => {
          this.navigateToDashboard();
        }, 1500);
      },
      error: (err) => {
        this.isLoading = false;
        this.handleLoginError(err);
      }
    });
  }




  private navigateToDashboard(): void {
    const role = this.authService.getUserRole();
    let route = '';

    if (role === 'ADMIN') {
      route = '/dashboard';
    } else if (role === 'HR') {
      route = '/hr-dashboard';
    } else if (role === 'EMPLOYEE') {
      route = '/employees-dashboard';
    } else if (role === 'MANAGER') {
      route = '/manager-dashboard';
    } else if (role === 'FINANCE') {
      route = '/finance-dashboard';
    }



    this.router.navigate([route]);
  }

  private handleLoginError(err: any): void {
    if (err?.error?.includes('reset the password')) {
      this.email = this.username;
      this.showForgot = false;
      this.showReset = true;
      this.showInfo('Please reset your password before login');
    } else {
      const errorMsg = err.error?.message || 'Invalid username or password';
      this.showError(errorMsg);
      this.triggerShakeAnimation();
    }
  }

  toggleUserType(): void {
    this.isAdmin = !this.isAdmin;
    this.clearForm();
  }

  private clearForm(): void {
    this.username = '';
    this.password = '';
  }

  sendOtp(): void {
    if (!this.email) {
      this.showError('Email is required');
      return;
    }

    this.isLoading = true;
    this.authService.forgotPassword(this.email).subscribe({
      next: () => {
        this.isLoading = false;
        this.showSuccess('OTP sent to your email');
        this.showForgot = false;
        this.showReset = true;
      },
      error: (err) => {
        this.isLoading = false;
        this.showError(err.error?.message || 'Failed to send OTP');
      }
    });
  }

  resetPassword(): void {
    if (!this.email || !this.otp || !this.newPassword) {
      this.showError('All fields are required');
      return;
    }

    this.isLoading = true;

    this.authService.resetPassword(this.email, this.otp, this.newPassword).subscribe({
      next: () => {
        this.isLoading = false;
        this.showSuccess('Password reset successful');
        this.showReset = false;
        this.username = this.email;
        this.password = '';
      },
      error: (err) => {
        this.isLoading = false;
        this.showError(err.error?.message || 'Failed to reset password');
      }
    });
  }

  private showSuccess(message: string): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: message,
      life: 3000
    });
  }

  private showError(message: string): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: message,
      life: 5000
    });
  }

  private showInfo(message: string): void {
    this.messageService.add({
      severity: 'info',
      summary: 'Info',
      detail: message,
      life: 5000
    });
  }

  private triggerShakeAnimation(): void {
    const form = document.querySelector('.login-form') as HTMLElement;
    if (form) {
      form.classList.add('shake');
      setTimeout(() => form.classList.remove('shake'), 500);
    }
  }
}

