import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalaryProcessJobCreateComponent } from './salary-process-job-create.component';

describe('SalaryProcessJobCreateComponent', () => {
  let component: SalaryProcessJobCreateComponent;
  let fixture: ComponentFixture<SalaryProcessJobCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SalaryProcessJobCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SalaryProcessJobCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
