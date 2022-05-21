import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IrregularAttendanceComponent } from './irregular-attendance.component';

describe('IrregularAttendanceComponent', () => {
  let component: IrregularAttendanceComponent;
  let fixture: ComponentFixture<IrregularAttendanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IrregularAttendanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IrregularAttendanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
