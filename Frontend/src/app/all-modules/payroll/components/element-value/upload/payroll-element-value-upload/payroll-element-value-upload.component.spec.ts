import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementValueUploadComponent } from './payroll-element-value-upload.component';

describe('PayrollElementValueUploadComponent', () => {
  let component: PayrollElementValueUploadComponent;
  let fixture: ComponentFixture<PayrollElementValueUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementValueUploadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementValueUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
