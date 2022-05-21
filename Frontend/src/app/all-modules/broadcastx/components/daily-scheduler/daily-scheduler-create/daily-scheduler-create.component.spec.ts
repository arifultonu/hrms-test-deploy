import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DailySchedulerCreateComponent } from './daily-scheduler-create.component';

describe('DailySchedulerCreateComponent', () => {
  let component: DailySchedulerCreateComponent;
  let fixture: ComponentFixture<DailySchedulerCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DailySchedulerCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DailySchedulerCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
