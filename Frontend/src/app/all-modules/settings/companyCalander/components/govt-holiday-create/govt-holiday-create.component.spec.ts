import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GovtHolidayCreateComponent } from './govt-holiday-create.component';

describe('GovtHolidayCreateComponent', () => {
  let component: GovtHolidayCreateComponent;
  let fixture: ComponentFixture<GovtHolidayCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GovtHolidayCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GovtHolidayCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
