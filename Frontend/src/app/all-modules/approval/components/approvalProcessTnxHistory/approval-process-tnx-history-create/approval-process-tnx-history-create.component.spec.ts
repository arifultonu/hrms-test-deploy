import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalProcessTnxHistoryCreateComponent } from './approval-process-tnx-history-create.component';

describe('ApprovalProcessTnxHistoryCreateComponent', () => {
  let component: ApprovalProcessTnxHistoryCreateComponent;
  let fixture: ComponentFixture<ApprovalProcessTnxHistoryCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalProcessTnxHistoryCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalProcessTnxHistoryCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
