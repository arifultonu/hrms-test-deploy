import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementValueShowComponent } from './payroll-element-value-show.component';

describe('PayrollElementValueShowComponent', () => {
  let component: PayrollElementValueShowComponent;
  let fixture: ComponentFixture<PayrollElementValueShowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementValueShowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementValueShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
