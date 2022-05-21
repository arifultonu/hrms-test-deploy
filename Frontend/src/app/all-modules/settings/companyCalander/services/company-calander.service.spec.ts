import { TestBed } from '@angular/core/testing';

import { CompanyCalanderService } from './company-calander.service';

describe('CompanyCalanderService', () => {
  let service: CompanyCalanderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompanyCalanderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
