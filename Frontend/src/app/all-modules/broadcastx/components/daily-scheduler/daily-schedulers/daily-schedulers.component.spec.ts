import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DailySchedulersComponent } from './daily-schedulers.component';

describe('DailySchedulersComponent', () => {
  let component: DailySchedulersComponent;
  let fixture: ComponentFixture<DailySchedulersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DailySchedulersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DailySchedulersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
