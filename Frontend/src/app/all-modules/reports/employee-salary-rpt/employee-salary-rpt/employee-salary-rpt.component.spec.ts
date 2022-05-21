import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeSalaryRptComponent } from './employee-salary-rpt.component';

describe('EmployeeSalaryRptComponent', () => {
  let component: EmployeeSalaryRptComponent;
  let fixture: ComponentFixture<EmployeeSalaryRptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeSalaryRptComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeSalaryRptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
