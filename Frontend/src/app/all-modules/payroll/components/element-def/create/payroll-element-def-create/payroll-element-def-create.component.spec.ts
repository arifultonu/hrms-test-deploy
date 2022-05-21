import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementDefCreateComponent } from './payroll-element-def-create.component';

describe('PayrollElementDefCreateComponent', () => {
  let component: PayrollElementDefCreateComponent;
  let fixture: ComponentFixture<PayrollElementDefCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementDefCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementDefCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
