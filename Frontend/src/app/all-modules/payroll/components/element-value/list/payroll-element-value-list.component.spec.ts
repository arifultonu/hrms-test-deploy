import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementValueListComponent } from './payroll-element-value-list.component';

describe('PayrollElementValueListComponent', () => {
  let component: PayrollElementValueListComponent;
  let fixture: ComponentFixture<PayrollElementValueListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementValueListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementValueListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
