import { ComponentFixture, TestBed } from '@angular/core/testing';

import {  ViewHrAdminComponent } from './view.component';

describe('ViewHrAdminComponent', () => {
  let component: ViewHrAdminComponent;
  let fixture: ComponentFixture<ViewHrAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewHrAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewHrAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
