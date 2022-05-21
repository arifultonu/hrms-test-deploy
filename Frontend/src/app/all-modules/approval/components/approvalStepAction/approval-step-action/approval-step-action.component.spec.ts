import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalStepActionComponent } from './approval-step-action.component';

describe('ApprovalStepActionComponent', () => {
  let component: ApprovalStepActionComponent;
  let fixture: ComponentFixture<ApprovalStepActionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalStepActionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalStepActionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
