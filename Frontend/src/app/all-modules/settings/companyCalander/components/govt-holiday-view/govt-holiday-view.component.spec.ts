import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GovtHolidayViewComponent } from './govt-holiday-view.component';

describe('GovtHolidayViewComponent', () => {
  let component: GovtHolidayViewComponent;
  let fixture: ComponentFixture<GovtHolidayViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GovtHolidayViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GovtHolidayViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
