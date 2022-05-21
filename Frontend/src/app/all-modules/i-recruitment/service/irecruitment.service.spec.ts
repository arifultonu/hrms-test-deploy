import { TestBed } from '@angular/core/testing';

import { IrecruitmentService } from './irecruitment.service';

describe('IrecruitmentService', () => {
  let service: IrecruitmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IrecruitmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
