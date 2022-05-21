import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalaryProcessJobShowComponent } from './salary-process-job-show.component';

describe('SalaryProcessJobShowComponent', () => {
  let component: SalaryProcessJobShowComponent;
  let fixture: ComponentFixture<SalaryProcessJobShowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SalaryProcessJobShowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SalaryProcessJobShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
