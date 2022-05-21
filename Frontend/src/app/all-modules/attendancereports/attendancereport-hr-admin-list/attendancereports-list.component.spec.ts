import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendancereportsHrAdminListComponent } from './attendancereports-list.component';

describe('AttendancereportsHrAdminListComponent', () => {
  let component: AttendancereportsHrAdminListComponent;
  let fixture: ComponentFixture<AttendancereportsHrAdminListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AttendancereportsHrAdminListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AttendancereportsHrAdminListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
