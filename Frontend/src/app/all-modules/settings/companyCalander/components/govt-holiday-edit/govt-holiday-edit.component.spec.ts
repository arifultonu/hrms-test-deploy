import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GovtHolidayEditComponent } from './govt-holiday-edit.component';

describe('GovtHolidayEditComponent', () => {
  let component: GovtHolidayEditComponent;
  let fixture: ComponentFixture<GovtHolidayEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GovtHolidayEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GovtHolidayEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
