import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalaryProcessJobEditComponent } from './salary-process-job-edit.component';

describe('SalaryProcessJobEditComponent', () => {
  let component: SalaryProcessJobEditComponent;
  let fixture: ComponentFixture<SalaryProcessJobEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SalaryProcessJobEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SalaryProcessJobEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
