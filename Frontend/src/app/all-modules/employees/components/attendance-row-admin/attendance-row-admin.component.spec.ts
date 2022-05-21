import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendanceRowAdminComponent } from './attendance-row-admin.component';

describe('AttendanceRowAdminComponent', () => {
  let component: AttendanceRowAdminComponent;
  let fixture: ComponentFixture<AttendanceRowAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AttendanceRowAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AttendanceRowAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
