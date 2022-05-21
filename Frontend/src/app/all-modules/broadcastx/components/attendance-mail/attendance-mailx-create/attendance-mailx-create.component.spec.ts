import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendanceMailxCreateComponent } from './attendance-mailx-create.component';

describe('AttendanceMailxCreateComponent', () => {
  let component: AttendanceMailxCreateComponent;
  let fixture: ComponentFixture<AttendanceMailxCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AttendanceMailxCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AttendanceMailxCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
