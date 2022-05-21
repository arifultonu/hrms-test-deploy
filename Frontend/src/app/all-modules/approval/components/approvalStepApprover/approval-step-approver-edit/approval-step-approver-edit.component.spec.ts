import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalStepApproverEditComponent } from './approval-step-approver-edit.component';

describe('ApprovalStepApproverEditComponent', () => {
  let component: ApprovalStepApproverEditComponent;
  let fixture: ComponentFixture<ApprovalStepApproverEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalStepApproverEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalStepApproverEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
