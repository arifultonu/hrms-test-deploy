import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementDefEditComponent } from './payroll-element-def-edit.component';

describe('PayrollElementDefEditComponent', () => {
  let component: PayrollElementDefEditComponent;
  let fixture: ComponentFixture<PayrollElementDefEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementDefEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementDefEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
