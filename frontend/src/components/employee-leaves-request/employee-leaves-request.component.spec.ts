import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeLeavesRequestComponent } from './employee-leaves-request.component';

describe('EmployeeLeavesRequestComponent', () => {
  let component: EmployeeLeavesRequestComponent;
  let fixture: ComponentFixture<EmployeeLeavesRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeLeavesRequestComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EmployeeLeavesRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
