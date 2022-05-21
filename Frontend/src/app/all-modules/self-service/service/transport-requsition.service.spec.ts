import { TestBed } from '@angular/core/testing';

import { TransportRequsitionService } from './transport-requsition.service';

describe('TransportRequsitionService', () => {
  let service: TransportRequsitionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransportRequsitionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
