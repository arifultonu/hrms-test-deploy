import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateShortLeaveComponent } from './create-short-leave.component';

describe('CreateShortLeaveComponent', () => {
  let component: CreateShortLeaveComponent;
  let fixture: ComponentFixture<CreateShortLeaveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateShortLeaveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateShortLeaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
