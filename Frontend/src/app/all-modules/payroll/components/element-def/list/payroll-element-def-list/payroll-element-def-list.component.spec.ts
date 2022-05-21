import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementDefListComponent } from './payroll-element-def-list.component';

describe('PayrollElementDefListComponent', () => {
  let component: PayrollElementDefListComponent;
  let fixture: ComponentFixture<PayrollElementDefListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementDefListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementDefListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
