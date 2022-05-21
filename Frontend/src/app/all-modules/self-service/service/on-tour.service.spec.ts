import { TestBed } from '@angular/core/testing';

import { OnTourService } from './on-tour.service';

describe('OnTourService', () => {
  let service: OnTourService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OnTourService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
