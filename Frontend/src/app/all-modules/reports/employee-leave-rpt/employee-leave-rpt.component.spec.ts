import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeLeaveRptComponent } from './employee-leave-rpt.component';

describe('EmployeeLeaveRptComponent', () => {
  let component: EmployeeLeaveRptComponent;
  let fixture: ComponentFixture<EmployeeLeaveRptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeLeaveRptComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeLeaveRptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
