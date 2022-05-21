import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementValueEditComponent } from './payroll-element-value-edit.component';

describe('PayrollElementValueEditComponent', () => {
  let component: PayrollElementValueEditComponent;
  let fixture: ComponentFixture<PayrollElementValueEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementValueEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementValueEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
