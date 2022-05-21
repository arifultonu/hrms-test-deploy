import { TestBed } from '@angular/core/testing';

import { VisitorlogService } from './visitorlog.service';

describe('VisitorlogService', () => {
  let service: VisitorlogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VisitorlogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
