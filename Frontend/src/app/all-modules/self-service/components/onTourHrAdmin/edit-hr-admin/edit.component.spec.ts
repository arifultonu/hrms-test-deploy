import { ComponentFixture, TestBed } from '@angular/core/testing';

import {  EditHrAdminComponent } from './edit.component';

describe('EditHrAdminComponent', () => {
  let component: EditHrAdminComponent;
  let fixture: ComponentFixture<EditHrAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditHrAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditHrAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
