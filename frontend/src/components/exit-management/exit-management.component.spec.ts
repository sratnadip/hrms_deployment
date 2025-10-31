import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExitManagementComponent } from './exit-management.component';

describe('ExitManagementComponent', () => {
  let component: ExitManagementComponent;
  let fixture: ComponentFixture<ExitManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExitManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExitManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
