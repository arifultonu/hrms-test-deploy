import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditShortLeaveComponent } from './edit-short-leave.component';

describe('EditShortLeaveComponent', () => {
  let component: EditShortLeaveComponent;
  let fixture: ComponentFixture<EditShortLeaveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditShortLeaveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditShortLeaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
