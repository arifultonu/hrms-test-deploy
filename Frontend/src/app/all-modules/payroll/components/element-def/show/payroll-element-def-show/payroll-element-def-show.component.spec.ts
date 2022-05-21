import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayrollElementDefShowComponent } from './payroll-element-def-show.component';

describe('PayrollElementDefShowComponent', () => {
  let component: PayrollElementDefShowComponent;
  let fixture: ComponentFixture<PayrollElementDefShowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayrollElementDefShowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PayrollElementDefShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
