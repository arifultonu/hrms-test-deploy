import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalStepActionCreateComponent } from './approval-step-action-create.component';

describe('ApprovalStepActionCreateComponent', () => {
  let component: ApprovalStepActionCreateComponent;
  let fixture: ComponentFixture<ApprovalStepActionCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalStepActionCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalStepActionCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
