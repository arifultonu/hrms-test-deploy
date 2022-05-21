import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalStepActionEditComponent } from './approval-step-action-edit.component';

describe('ApprovalStepActionEditComponent', () => {
  let component: ApprovalStepActionEditComponent;
  let fixture: ComponentFixture<ApprovalStepActionEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalStepActionEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalStepActionEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
