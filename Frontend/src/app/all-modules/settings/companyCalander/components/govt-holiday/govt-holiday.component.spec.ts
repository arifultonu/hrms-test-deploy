import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GovtHolidayComponent } from './govt-holiday.component';

describe('GovtHolidayComponent', () => {
  let component: GovtHolidayComponent;
  let fixture: ComponentFixture<GovtHolidayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GovtHolidayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GovtHolidayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
