import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../app/auths/auth.service';  // Assuming auth.service is needed for token header

@Injectable({
  providedIn: 'root'
})
export class LeaveService {
  private baseUrl = '/api/leave'; // Update to match your backend if needed

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getLeaveTypes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/types`, {
      headers: this.getAuthHeaders()
    });
  }

  getLeaveBalance(employeeId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/balance/${employeeId}`, {
      headers: this.getAuthHeaders()
    });
  }

  getLeaveRequests(employeeId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/requests/${employeeId}`, {
      headers: this.getAuthHeaders()
    });
  }

  getAllEmployeeLeaveRequests(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/requests/all`, {
      headers: this.getAuthHeaders()
    });
  }

  getOptionalHolidays(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/optional-holidays`, {
      headers: this.getAuthHeaders()
    });
  }

  createLeaveRequest(payload: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/request`, payload, {
      headers: this.getAuthHeaders()
    });
  }

  selectOptionalHolidays(employeeId: string, holidayIds: string[]): Observable<any> {
    return this.http.post<any>(
      `${this.baseUrl}/optional-holidays/${employeeId}`,
      { holidayIds },
      { headers: this.getAuthHeaders() }
    );
  }

  approveRejectLeave(leaveRequestId: string, status: string): Observable<any> {
    return this.http.put<any>(
      `${this.baseUrl}/approval/${leaveRequestId}`,
      { status },
      { headers: this.getAuthHeaders() }
    );
  }
}
