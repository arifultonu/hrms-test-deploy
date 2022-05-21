import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalStepEditComponent } from './approval-step-edit.component';

describe('ApprovalStepEditComponent', () => {
  let component: ApprovalStepEditComponent;
  let fixture: ComponentFixture<ApprovalStepEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalStepEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalStepEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
