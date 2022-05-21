import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendanceMailxComponent } from './attendance-mailx.component';

describe('AttendanceMailxComponent', () => {
  let component: AttendanceMailxComponent;
  let fixture: ComponentFixture<AttendanceMailxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AttendanceMailxComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AttendanceMailxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
