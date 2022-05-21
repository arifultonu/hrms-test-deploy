import { ComponentFixture, TestBed } from '@angular/core/testing';

import {  CreateLeaveHrAdminComponent } from './create-leave.component';

describe('CreateLeaveHrAdminComponent', () => {
  let component: CreateLeaveHrAdminComponent;
  let fixture: ComponentFixture<CreateLeaveHrAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateLeaveHrAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateLeaveHrAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
