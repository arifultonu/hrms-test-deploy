import { TestBed } from '@angular/core/testing';

import { ShortLeaveService } from './short-leave.service';

describe('ShortLeaveService', () => {
  let service: ShortLeaveService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShortLeaveService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
