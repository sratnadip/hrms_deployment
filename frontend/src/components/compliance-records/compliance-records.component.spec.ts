import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComplianceRecordsComponent } from './compliance-records.component';

describe('ComplianceRecordsComponent', () => {
  let component: ComplianceRecordsComponent;
  let fixture: ComponentFixture<ComplianceRecordsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComplianceRecordsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ComplianceRecordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
