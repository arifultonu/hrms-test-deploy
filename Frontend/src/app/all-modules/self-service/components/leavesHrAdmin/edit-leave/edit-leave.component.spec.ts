import { ComponentFixture, TestBed } from '@angular/core/testing';

import {  EditLeaveHrAdminComponent } from './edit-leave.component';

describe('EditLeaveHrAdminComponent', () => {
  let component: EditLeaveHrAdminComponent;
  let fixture: ComponentFixture<EditLeaveHrAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditLeaveHrAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditLeaveHrAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
