import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewShortLeaveComponent } from './view-short-leave.component';

describe('ViewShortLeaveComponent', () => {
  let component: ViewShortLeaveComponent;
  let fixture: ComponentFixture<ViewShortLeaveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewShortLeaveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewShortLeaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
