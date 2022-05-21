import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementValueCreateComponent } from './payroll-element-value-create.component';

describe('PayrollElementValueCreateComponent', () => {
  let component: PayrollElementValueCreateComponent;
  let fixture: ComponentFixture<PayrollElementValueCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementValueCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementValueCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
