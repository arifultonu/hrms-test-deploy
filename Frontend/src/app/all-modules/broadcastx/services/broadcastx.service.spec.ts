import { TestBed } from '@angular/core/testing';

import { BroadcastxService } from './broadcastx.service';

describe('BroadcastxService', () => {
  let service: BroadcastxService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BroadcastxService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
