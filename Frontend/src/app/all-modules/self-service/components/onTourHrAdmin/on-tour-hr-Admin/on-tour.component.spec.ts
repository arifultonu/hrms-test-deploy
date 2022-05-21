import { ComponentFixture, TestBed } from '@angular/core/testing';

import {  OnTourHrAdminComponent } from './on-tour.component';

describe('OnTourHrAdminComponent', () => {
  let component: OnTourHrAdminComponent;
  let fixture: ComponentFixture<OnTourHrAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OnTourHrAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OnTourHrAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
