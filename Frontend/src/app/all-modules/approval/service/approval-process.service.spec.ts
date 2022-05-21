import { TestBed } from '@angular/core/testing';

import { ApprovalProcessService } from './approval-process.service';

describe('ApprovalProcessService', () => {
  let service: ApprovalProcessService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApprovalProcessService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
