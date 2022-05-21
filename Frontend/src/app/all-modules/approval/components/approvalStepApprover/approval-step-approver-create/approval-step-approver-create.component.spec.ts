import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalStepApproverCreateComponent } from './approval-step-approver-create.component';

describe('ApprovalStepApproverCreateComponent', () => {
  let component: ApprovalStepApproverCreateComponent;
  let fixture: ComponentFixture<ApprovalStepApproverCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalStepApproverCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalStepApproverCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
