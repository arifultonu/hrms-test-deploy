import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalaryProcessJobListComponent } from './salary-process-job-list.component';

describe('SalaryProcessJobListComponent', () => {
  let component: SalaryProcessJobListComponent;
  let fixture: ComponentFixture<SalaryProcessJobListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SalaryProcessJobListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SalaryProcessJobListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
