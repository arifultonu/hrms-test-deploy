import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalProcessTnxHistoryComponent } from './approval-process-tnx-history.component';

describe('ApprovalProcessTnxHistoryComponent', () => {
  let component: ApprovalProcessTnxHistoryComponent;
  let fixture: ComponentFixture<ApprovalProcessTnxHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalProcessTnxHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalProcessTnxHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
