import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeProfileRptComponent } from './employee-profile-rpt.component';

describe('EmployeeProfileRptComponent', () => {
  let component: EmployeeProfileRptComponent;
  let fixture: ComponentFixture<EmployeeProfileRptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeProfileRptComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeProfileRptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
