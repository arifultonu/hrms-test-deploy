import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalProcessTnxHistoryEditComponent } from './approval-process-tnx-history-edit.component';

describe('ApprovalProcessTnxHistoryEditComponent', () => {
  let component: ApprovalProcessTnxHistoryEditComponent;
  let fixture: ComponentFixture<ApprovalProcessTnxHistoryEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalProcessTnxHistoryEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalProcessTnxHistoryEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
