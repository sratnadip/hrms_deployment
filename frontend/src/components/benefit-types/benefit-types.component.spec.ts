import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BenefitTypesComponent } from './benefit-types.component';

describe('BenefitTypesComponent', () => {
  let component: BenefitTypesComponent;
  let fixture: ComponentFixture<BenefitTypesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BenefitTypesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BenefitTypesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
