import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalStepApproverComponent } from './approval-step-approver.component';

describe('ApprovalStepApproverComponent', () => {
  let component: ApprovalStepApproverComponent;
  let fixture: ComponentFixture<ApprovalStepApproverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalStepApproverComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalStepApproverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
