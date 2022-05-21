import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeMonthlyRptComponent } from './employee-monthly-rpt.component';

describe('EmployeeMonthlyRptComponent', () => {
  let component: EmployeeMonthlyRptComponent;
  let fixture: ComponentFixture<EmployeeMonthlyRptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeMonthlyRptComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeMonthlyRptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
