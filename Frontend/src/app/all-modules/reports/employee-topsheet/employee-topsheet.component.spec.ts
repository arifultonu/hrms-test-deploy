import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeTopsheetComponent } from './employee-topsheet.component';

describe('EmployeeTopsheetComponent', () => {
  let component: EmployeeTopsheetComponent;
  let fixture: ComponentFixture<EmployeeTopsheetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeTopsheetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeTopsheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
