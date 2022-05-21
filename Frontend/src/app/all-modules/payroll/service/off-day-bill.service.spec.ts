import { TestBed } from '@angular/core/testing';

import { OffDayBillService } from './off-day-bill.service';

describe('OffDayBillService', () => {
  let service: OffDayBillService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OffDayBillService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
