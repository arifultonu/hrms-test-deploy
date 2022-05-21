import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OnTourComponent } from './on-tour.component';

describe('OnTourComponent', () => {
  let component: OnTourComponent;
  let fixture: ComponentFixture<OnTourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OnTourComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OnTourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
