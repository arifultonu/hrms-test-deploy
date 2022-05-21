import { TestBed } from '@angular/core/testing';

import { LeaveConfigService } from './leave-config.service';

describe('LeaveConfigService', () => {
  let service: LeaveConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeaveConfigService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
