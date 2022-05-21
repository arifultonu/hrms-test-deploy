import { ComponentFixture, TestBed } from '@angular/core/testing';

import {  ViewLeaveHrAdminComponent } from './view-leave.component';

describe('ViewLeaveHrAdminComponent', () => {
  let component: ViewLeaveHrAdminComponent;
  let fixture: ComponentFixture<ViewLeaveHrAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewLeaveHrAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewLeaveHrAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
