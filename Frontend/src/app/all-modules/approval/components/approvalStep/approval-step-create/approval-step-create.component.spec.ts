import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalStepCreateComponent } from './approval-step-create.component';

describe('ApprovalStepCreateComponent', () => {
  let component: ApprovalStepCreateComponent;
  let fixture: ComponentFixture<ApprovalStepCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalStepCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalStepCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
