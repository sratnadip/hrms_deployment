
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, map, of, switchMap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../components/environment';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';




export interface Department {
  departmentId?: number;
  departmentCode: string;
  name: string;
  description?: string;
  location?: string;
  departmentHeadId?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface User {
  userId: string;
  username: string;
  email: string;
  role: string;
  status?: string;
}

export interface LeaveType {
  leaveTypeId?: string;
  name: string;
  description: string;
  maxDaysPerYear: number;
  carryForward?: boolean;
  encashable?: boolean;
  approvalFlow?: string;
}

export interface Benefit {
  id: number;
  name: string;
  description: string;
  type: 'MONETARY' | 'NON_MONETARY';
  taxable: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface EmployeeBenefit {
  employeeBenefitId?: number;
  empId: string;
  benefitId: number;
  amount: number;
  notes?: string;
  status?: string;
  effectiveFrom?: number[];
  effectiveTo?: number[] | null;
  createdAt?: number[];
  updatedAt?: number[];
}

export interface Compliance {
  complianceId?: number;
  id?: number;
  name: string;
  description: string;
  type: 'STATUTORY' | 'INTERNAL';
  frequency: 'MONTHLY' | 'QUARTERLY' | 'YEARLY';
  dueDate: string;
  penalty: string;
  documentRequired: boolean;
  isActive: boolean;
}

export interface ComplianceRecord {
  complianceRecordId?: number;
  compliance: { complianceId: number };
  period: string; // Format: YYYY-MM
  status: 'PENDING' | 'COMPLETED' | 'LATE';
  filePath?: string | null;
  submittedOn?: string | null;
  remarks?: string;
  createdBy: string;
}
export interface Document {
  id: string;
  type: string;
  fileName: string;
  verified: boolean;
  fileType?: string;
  fileSize?: number;
  url?: string;
  version?: number;
  expiryDate?: string;
  remarks?: string;
  documentStatus?: string;
  uploadedAt?: string;
  verifiedAt?: string;
}
 export interface Announcement {
  id: string;
  title: string;
  message: string;
  visibility: 'ALL' | 'ROLE' | 'DEPARTMENT' | 'EMPLOYEE';
  visibleToValue?: string;
  createdAt: string;
  expiresOn?: string;
}
 

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiBaseUrl;
  private currentUserSubject: BehaviorSubject<any>;
  public currentUser: Observable<any>;
  private isBrowser: boolean;
  public announcements$ = new BehaviorSubject<Announcement[]>([]);
  private stompClient: Client | null = null;
 

  constructor(
    private http: HttpClient,
    private router: Router,
    private messageService: MessageService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
    const storedUser = this.isBrowser ? localStorage.getItem('currentUser') : null;
    this.currentUserSubject = new BehaviorSubject<any>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue() {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return this.isBrowser ? localStorage.getItem('token') : null;
  }

  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    const headersConfig: any = {};
    if (token) headersConfig['Authorization'] = `Bearer ${token}`;
    headersConfig['Content-Type'] = 'application/json';
    return new HttpHeaders(headersConfig);
  }

  getMultipartAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getUserRole(): string {
    return this.currentUserValue?.role || '';
  }

  isAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }

  isHR(): boolean {
    return this.getUserRole() === 'HR';
  }

  isManager(): boolean {
    return this.getUserRole() === 'MANAGER';
  }

  isEmployee(): boolean {
    return this.getUserRole() === 'EMPLOYEE';
  }

  isLoggedIn(): boolean {
    return this.isBrowser && localStorage.getItem('token') !== null;
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem('token');
      localStorage.removeItem('currentUser');
      localStorage.removeItem('employeeId');
    }
    this.currentUserSubject.next(null);
    this.messageService.add({
      severity: 'info',
      summary: 'Logout',
      detail: 'You have been logged out',
    });
    this.router.navigate(['/login']);
  }

  private handleError(error: any, message: string): Observable<never> {
    const errMsg = error?.error || error?.message || 'Unknown error';
    this.messageService.add({
      severity: 'error',
      summary: message,
      detail: errMsg,
    });
    return throwError(() => error);
  }

  adminLogin(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post(`${this.apiUrl}/admin/test-login`, { username, password }, {
      headers,
      responseType: 'text'  // you can keep this if your backend returns plain token
    }).pipe(
      map((token: string) => {
        if (this.isBrowser) {
          localStorage.setItem('token', token);
          const user = { username, role: 'ADMIN' };
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
        }

        this.messageService.add({
          severity: 'success',
          summary: 'Admin Login',
          detail: 'Login successful',
        });

        this.router.navigate(['/admin-dashboard']);
        return { token };
      }),
      catchError((error) => this.handleError(error, 'Admin login failed'))
    );
  }


  userLogin(username: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/user/login`, { username, password }, {
      responseType: 'text'
    }).pipe(
      switchMap((token: string) => {
        if (this.isBrowser) {
          localStorage.setItem('token', token);

          const payload = JSON.parse(atob(token.split('.')[1]));
          const rawRole = payload.Role || payload.role || '';
          const normalizedRole = rawRole.replace('ROLE_', '').toUpperCase();

          const user = {
            username: payload.sub,
            role: normalizedRole
          };

          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);

          if (normalizedRole !== 'ADMIN') {
            return this.fetchEmployeeId().pipe(
              map(() => {
                this.messageService.add({
                  severity: 'success',
                  summary: 'Login',
                  detail: 'Login successful',
                });

                if (normalizedRole === 'HR') {
                  this.router.navigate(['/hr-dashboard']);
                } else if (normalizedRole === 'MANAGER') {
                  this.router.navigate(['/manager-dashboard']);
                } else if (normalizedRole === 'EMPLOYEE') {
                  this.router.navigate(['/employees-dashboard']);
                } else {
                  this.router.navigate(['/dashboard']);
                }

                return { token };
              }),
              catchError((err) => {
                console.warn('Failed to fetch employeeId during login', err);
                return of({ token });
              })
            );
          }

          this.messageService.add({
            severity: 'success',
            summary: 'Login',
            detail: 'Login successful',
          });
          this.router.navigate(['/dashboard']);
          return of({ token });
        }

        return of({ token });
      }),
      catchError((error) => {
        const errMsg = error.status === 406 ? error.error : 'Login failed';
        return this.handleError({ ...error, error: errMsg }, errMsg);
      })
    );
  }

  getUserInfo() {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const payload = JSON.parse(atob(token.split('.')[1]));
          return {
            username: payload.sub || '',
            role: payload.role || '',
          };
        } catch (error) {
          console.error('Invalid token format', error);
        }
      }
    }

    return null;
  }

  fetchEmployeeId(): Observable<any> {
    return this.http.get(`${this.apiUrl}/employees/myProfile`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((res: any) => {
        if (this.isBrowser) {
          localStorage.setItem('employeeId', res?.empId || '');
        }
        return res;
      }),
      catchError((err) => this.handleError(err, 'Fetching employee profile failed'))
    );
  }

  getEmployeeId(): string {
    if (!this.isBrowser) return '';
    const token = localStorage.getItem('token');
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.empId || payload.employeeId || '';
    } catch {
      return '';
    }
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/user/forgotPwd`, {
      params: { email },
      responseType: 'text'
    }).pipe(
      map((res) => res),
      catchError((error) => this.handleError(error, 'Failed to send OTP'))
    );
  }

  resetPassword(email: string, otp: string, newPassword: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/user/resetPwd`, null, {
      params: { email, otp, newPassword },
      responseType: 'text'
    }).pipe(
      map((res) => res),
      catchError((error) => this.handleError(error, 'Password reset failed'))
    );
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/user/register`, user, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    }).pipe(
      map((res) => res),
      catchError((error) => this.handleError(error, 'Registration failed'))
    );
  }

  createEmployeeFormData(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/employees`, formData, {
      headers: this.getMultipartAuthHeaders()
    });
  }

  getAllEmployees(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/employees/all`).pipe(
      catchError((error) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to fetch employees' });
        return throwError(() => error);
      })
    );
  }



  getAllDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(`${this.apiUrl}/departments`, {
      headers: this.getAuthHeaders(),
    });
  }

  getDepartmentById(id: number): Observable<Department> {
    return this.http.get<Department>(`${this.apiUrl}/departments/${id}`, {
      headers: this.getAuthHeaders(),
    });
  }

  createDepartment(dept: Department): Observable<Department> {
    return this.http.post<Department>(`${this.apiUrl}/departments`, dept, {
      headers: this.getAuthHeaders(),
    });
  }

  updateDepartment(id: number, dept: Department): Observable<Department> {
    return this.http.put<Department>(`${this.apiUrl}/departments/${id}`, dept, {
      headers: this.getAuthHeaders(),
    });
  }

  deleteDepartment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/departments/${id}`, {
      headers: this.getAuthHeaders(),
    });
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/user/users`, {
      headers: this.getAuthHeaders(),
    });
  }

  getUsersByRole(role: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/user/role/${role}`, {
      headers: this.getAuthHeaders()
    });
  }

  getManagersByDepartment(departmentId: number): Observable<User[]> {
    return this.http.get<User[]>(`/api/user/managers/by-department/${departmentId}`);
  }

  getUserId(): string {
    return this.isBrowser ? localStorage.getItem('employeeId') || '' : '';
  }

  hasRole(role: string): boolean {
    if (!this.isBrowser) return false;
    const roles = JSON.parse(localStorage.getItem('roles') || '[]');
    return roles.includes(role);
  }

  clockInAttendance(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/attendance/clockIn`, payload, {
      headers: this.getAuthHeaders()
    });
  }

  clockOutAttendance(): Observable<any> {
    return this.http.patch(`${this.apiUrl}/attendance/clockOut`, {}, {
      headers: this.getAuthHeaders()
    });
  }

  downloadAttendancePdf(data: any): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/attendance/pdf`, data, {
      headers: this.getAuthHeaders(),
      responseType: 'blob'
    });
  }

  downloadAttendanceCsv(data: any): Observable<Blob> {
    return this.http.post(`${this.apiUrl}/attendance/csv`, data, {
      headers: this.getAuthHeaders(),
      responseType: 'blob'
    });
  }

  getMonthlyAttendanceStatus(params: { fromDate: string, toDate: string, employeeId: string }): Observable<any> {
    const httpParams = new HttpParams()
      .set('fromDate', params.fromDate)
      .set('toDate', params.toDate)
      .set('employeeId', params.employeeId);

    return this.http.get(`${this.apiUrl}/attendance/monthlyStatus`, {
      headers: this.getAuthHeaders(),
      params: httpParams
    });
  }
  //=========================LeaveType========================
  addLeaveType(leaveType: LeaveType): Observable<any> {
    const headers = this.getAuthHeaders().set('Content-Type', 'application/json');
    return this.http.post(`${this.apiUrl}/leaveType/add`, leaveType, {
      headers,
      responseType: 'text',
    });
  }

  updateLeaveType(id: string, leaveType: LeaveType): Observable<any> {
    return this.http.put(`${this.apiUrl}/leaveType/update/${id}`, leaveType, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Updating leave type failed'))
    );
  }

  getLeaveTypeById(id: string): Observable<LeaveType> {
    return this.http.get<LeaveType>(`${this.apiUrl}/leaveType/get/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching leave type failed'))
    );
  }

  getAllLeaveTypes(): Observable<LeaveType[]> {
    return this.http.get<LeaveType[]>(`${this.apiUrl}/leaveType/get`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching all leave types failed'))
    );
  }

  deleteLeaveType(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/leaveType/delete/${id}`, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError((error) => this.handleError(error, 'Deleting leave type failed'))
    );
  }

  getLeaveBalance(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/leaveBalance/get`, {
      headers: this.getAuthHeaders(),
    });
  }

  addLeaveRequest(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/leaveRequest/add`, payload, {
      headers: this.getAuthHeaders(),
      responseType: 'text',
    });
  }

  getEmployeeLeaveRequests(empId: string, status: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/leaveRequest/trackLeave/${empId}?status=${status}`, {
      headers: this.getAuthHeaders(),
    });
  }

  updateLeaveRequestStatus(leaveRequestId: string, status: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/leaveRequest/approveReject/${leaveRequestId}?status=${status}`, {}, {
      headers: this.getAuthHeaders(),
      responseType: 'text',
    });
  }
  getLeaveRequestsForManager(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/leave-requests/manager`);
  }


  approveLeaveRequest(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/leave-requests/${id}/approve`, {});
  }

  rejectLeaveRequest(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/leave-requests/${id}/reject`, {});
  }
  getAllEmployeeLeaveRequests(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/leave-requests/manager`, {
      headers: this.getAuthHeaders()
    });
  }




  //==============Salary Structure=========================
  assignSalaryStructure(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/salaryStructure/assign`, payload, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Assigning salary structure failed'))
    );
  }

  getSalaryStructureById(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/salaryStructure/get/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Fetching salary structure failed'))
    );
  }

  updateSalaryStructure(id: string, payload: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/salaryStructure/update/${id}`, payload, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError((error) => this.handleError(error, 'Updating salary structure failed'))
    );
  }

  deleteSalaryStructure(id: string): Observable<any> {

    return this.http.delete(`${this.apiUrl}/salaryStructure/delete/${id}`, {

      headers: this.getAuthHeaders()

    }).pipe(

      catchError((error) => this.handleError(error, 'Deleting salary structure failed'))

    );

  }



  fetchSalaryStructure(empId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/salaryStructure/${empId}`);
  }

  getAllSalaryStructures(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/salaryStructure/all`, {
      headers: this.getAuthHeaders()
    });

  }


  getSalaryStructure(empId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/payroll/salaryStructure/${empId}`, {
      headers: this.getAuthHeaders(),
    });
  }



    //==========PAYROLL====================================
 
 addPayroll(payload: any): Observable<string> {
  return this.http.post(`${this.apiUrl}/payroll/addPayroll`, payload, {
    headers: this.getAuthHeaders(),
    responseType: 'text'
  });
}
 
 
generatePayslip(empId: string, month: string): Observable<any> {
  return this.http.patch(`${this.apiUrl}/payroll/generatePayslip/${empId}?month=${month}`, {}, {
    headers: this.getAuthHeaders(),
    responseType: 'text'
  });
}
 
 
  downloadPayslip(empId: string, month: string): Observable<Blob> {
  return this.http.get(`${this.apiUrl}/payroll/downloadPayslip/${empId}?month=${month}`, {
    headers: this.getAuthHeaders(),
    responseType: 'blob'
  });
}
 
  viewPayslip(empId: string, month: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/payroll/viewPayslip/${empId}?month=${month}`, {
      headers: this.getAuthHeaders(),
    });
  }
 
  getAllPayrolls(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/payroll/admin/allPayrolls`, {
      headers: this.getAuthHeaders(),
    });
  }
 
  viewPayslipSelf(month: string): Observable<any> {
  return this.http.get(`${this.apiUrl}/payroll/viewPayslip?month=${month}`, {
    headers: this.getAuthHeaders(),
  });
}
 // Download payslip for self (EMPLOYEE)
downloadPayslipSelf(month: string): Observable<Blob> {
  return this.http.get(`${this.apiUrl}/payroll/downloadPayslip?month=${month}`, {
    headers: this.getAuthHeaders(),
    responseType: 'blob'
  });
}

 



  // exportPayrollsToExcel(): Observable<Blob> {
  //   return this.http.get(`${this.apiUrl}/admin/exportPayrolls`, {
  //     headers: this.getAuthHeaders(),
  //     responseType: 'blob',
  //   });
  // }


  getRole(): string {
    if (!this.isBrowser) return '';
    const token = localStorage.getItem('token');
    if (!token) return '';
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role || payload.Role || '';
    } catch {
      return '';
    }
  }
  // 🧾 Add Bank Details
  createBankDetails(bankData: any): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.http.post(`${this.apiUrl}/bank-details`, bankData, { headers }).pipe(
      catchError((error) => {
        console.error('Bank Details API Error:', error);
        return throwError(() => error);
      })
    );
  }
  getBankDetailsById(id: string) {
    return this.http.get(`${this.apiUrl}/bank-details/${id}`);
  }


  // --------------------- MASTER BENEFITS METHODS ---------------------

  // Fetch all master benefits (for Admin/HR)
  getAllBenefits(): Observable<Benefit[]> {
    return this.http.get<Benefit[]>(`${this.apiUrl}/benefits`, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(err => this.handleError(err, 'Failed to fetch benefits')));
  }

  // Add a new benefit to the master list
  addBenefit(benefit: Benefit): Observable<Benefit> {
    return this.http.post<Benefit>(`${this.apiUrl}/benefits`, benefit, {
      headers: this.getAuthHeaders()
    }).pipe(catchError(err => this.handleError(err, 'Failed to add benefit')));
  }


  // --------------------- COMPLIANCE METHODS ---------------------


  getAllCompliances(): Observable<Compliance[]> {
    return this.http.get<Compliance[]>(`${this.apiUrl}/compliances`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(err => this.handleError(err, 'Failed to fetch compliances'))
    );
  }

  createCompliance(data: Compliance): Observable<Compliance> {
    return this.http.post<Compliance>(`${this.apiUrl}/compliances`, data, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(err => this.handleError(err, 'Failed to create compliance'))
    );
  }


  updateCompliance(id: number, data: Compliance): Observable<Compliance> {
    return this.http.put<Compliance>(`${this.apiUrl}/compliances/${id}`, data, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(err => this.handleError(err, 'Failed to update compliance'))
    );
  }


  deleteCompliance(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/compliances/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(err => this.handleError(err, 'Failed to delete compliance'))
    );
  }


  getComplianceById(id: number): Observable<Compliance> {
    return this.http.get<Compliance>(`${this.apiUrl}/compliances/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(err => this.handleError(err, 'Failed to fetch compliance'))
    );
  }

  // ------------------ COMPLIANCE RECORD METHODS ------------------

  // ✅ Create new compliance record
  createComplianceRecord(record: ComplianceRecord): Observable<ComplianceRecord> {
    return this.http.post<ComplianceRecord>(`${this.apiUrl}/compliance-records`, record, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(err => this.handleError(err, 'Failed to create compliance record'))
    );
  }

  // ✅ Get records by period (YYYY-MM)
  getComplianceRecordsByPeriod(period: string): Observable<ComplianceRecord[]> {
    return this.http.get<ComplianceRecord[]>(`${this.apiUrl}/compliance-records/${period}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(err => this.handleError(err, 'Failed to fetch compliance records'))
    );
  }

  // ✅ Update a specific compliance record
  updateComplianceRecord(id: number, payload: ComplianceRecord): Observable<ComplianceRecord> {
    return this.http.put<ComplianceRecord>(`${this.apiUrl}/compliance-records/${id}`, payload, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(err => this.handleError(err, 'Failed to update compliance record'))
    );
  }

  //==========EMPLOYEE BENEFIT====================================

  assignBenefit(payload: any): Observable<any> {

    return this.http.post(`${this.apiUrl}/employee-benefits`, payload, {

      headers: this.getAuthHeaders()

    });

  }





  getEmployeeBenefits(empId: string): Observable<any[]> {

    return this.http.get<any[]>(`${this.apiUrl}/employee-benefits/${empId}`, {

      headers: this.getAuthHeaders()

    });

  }

  updateBenefit(benefitId: number, payload: any): Observable<any> {

    return this.http.put(`${this.apiUrl}/employee-benefits/${benefitId}`, payload, {

      headers: this.getAuthHeaders()

    });

  }

  deleteBenefit(benefitId: number): Observable<void> {

    return this.http.delete<void>(`${this.apiUrl}/employee-benefits/${benefitId}`, {

      headers: this.getAuthHeaders()

    });

  }

  //==========Performance Review====================================

  createReviewCycle(data: any) {
    return this.http.post(`${this.apiUrl}/review-cycles`, data);
  }

  closeReviewCycle(id: number) {
    return this.http.patch(`${this.apiUrl}/review-cycles/${id}/close`, {});
  }

  getActiveReviewCycles() {
    return this.http.get<any[]>(`${this.apiUrl}/review-cycles/active`);
  }




  //==========Survey Feedback====================================
  createSurvey(data: any): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.post(`${this.apiUrl}/surveys`, data, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

  getSurveys(): Observable<any[]> {
    const token = localStorage.getItem('token');
    return this.http.get<any[]>(`${this.apiUrl}/surveys`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }


    //============================Document=====================================
 
 
 
 
 
 
// ✅ Get current user ID from localStorage
  getCurrentUserId(): string {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    // return user?.userId || '';
    return user?.id || '';
  }
 
 
 
 
  // getToken(): string {
  //   return localStorage.getItem('token') || '';
  // }
 
  // ✅ Get all employees
  // getAllEmployees(): Observable<any[]> {
  //   return this.http.get<any[]>(`${this.apiUrl}/employees`, {
  //     headers: {
  //       Authorization: `Bearer ${this.getToken()}`
  //     }
  //   });
  // }
 
  // ✅ Upload document
  uploadDocument(empId: string, type: string, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('type', type);
    formData.append('file', file);
 
    return this.http.post(`${this.apiUrl}/documents/upload/${empId}`, formData, {
      headers: {
        Authorization: `Bearer ${this.getToken()}`
      }
    });
  }
 
  // ✅ Get documents by employee
  getDocumentsByEmployee(empId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/documents/employee/${empId}`, {
      headers: {
        Authorization: `Bearer ${this.getToken()}`
      }
    });
  }
 
 
  // ✅ Verify document (without userId)
  verifyDocument(docId: string, userId: string, remarks: string): Observable<any> {
    const body = new URLSearchParams();
    body.set('remarks', remarks);
 
    return this.http.post(
      `${this.apiUrl}/documents/verify/${docId}/${userId}`,
      body.toString(),
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          Authorization: `Bearer ${this.getToken()}`
        }
      }
    );
  }
 
 
 
 
 
 
 
 
  // ✅ Delete document
  deleteDocument(docId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/documents/${docId}`, {
      headers: {
        Authorization: `Bearer ${this.getToken()}`
      }
    });
  }
 
  // ✅ Get expiring documents
  getExpiringDocuments(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/documents/expiring`, {
      headers: {
        Authorization: `Bearer ${this.getToken()}`
      }
    });
  }
 
  //===============Web Socket=================
 
  connectWebSocket(): void {
    const userId = this.getCurrentUserId();
    const token = sessionStorage.getItem('token') || localStorage.getItem('token');
 
    const socket = new SockJS(`${this.apiUrl}/ws-notifications`);
    this.stompClient = new Client({
      webSocketFactory: () => socket as any,
      reconnectDelay: 5000,
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      debug: str => console.log('WebSocket:', str),
      onConnect: () => {
        console.log('✅ WebSocket connected');
 
        this.stompClient?.subscribe(`/topic/notifications/${userId}`, (message: IMessage) => {
          const payload = JSON.parse(message.body);
          console.log('🔔 Notification received:', payload);
 
          // 1. Show toast
          this.messageService.add({
            severity: 'info',
            summary: payload.title,
            detail: payload.message,
            life: 5000
          });
 
          // 2. Refresh list via fetchAnnouncements
          this.fetchAnnouncements();
        });
      },
      onStompError: frame => {
        console.error('❌ STOMP error:', frame.headers['message'], frame.body);
      }
    });
 
    this.stompClient.activate();
  }
  //=============Announcement=======================
 
postAnnouncement(payload: any): Observable<any> {
  const token = localStorage.getItem('token');
  const headers = new HttpHeaders({
    Authorization: `Bearer ${token}`,
    'Content-Type': 'application/json'
  });
 
  return this.http.post(`${this.apiUrl}/announcements`, payload, { headers });
}
 
 
fetchAnnouncements(): void {
  const role = this.getUserRole();
  const department = this.currentUserValue?.department || '';
  const empId = this.getUserId(); // ✅ use correct method
 
  if (!empId) {
    console.error('❌ empId is null or empty. Cannot fetch announcements.');
    return;
  }
 
  console.log('🔍 Fetching announcements with:', { role, department, empId });
 
  this.http.get<Announcement[]>(`${this.apiUrl}/announcements/visible`, {
    headers: this.getAuthHeaders(),
    params: new HttpParams()
      .set('role', role)
      .set('department', department)
      .set('empId', empId)
  }).subscribe({
    next: list => this.announcements$.next(list),
    error: err => console.error('❌ Failed to fetch announcements', err)
  });
}

getDepartments(): Observable<string[]> {
  const token = localStorage.getItem('token');
  return this.http.get<any[]>(`${this.apiUrl}/departments`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  }).pipe(
    map(res => res.map(dep => dep.name || dep.departmentName)) // Adjust as per backend
  );
}
 
 
 createAnnouncement(data: any): Observable<any> {
  return this.http.post(`${this.apiUrl}/announcements`, data, {
    headers: this.getAuthHeaders()
  });
}

 
 

}




