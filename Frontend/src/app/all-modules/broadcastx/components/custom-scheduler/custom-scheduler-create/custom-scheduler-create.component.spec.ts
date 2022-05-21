import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomSchedulerCreateComponent } from './custom-scheduler-create.component';

describe('CustomSchedulerCreateComponent', () => {
  let component: CustomSchedulerCreateComponent;
  let fixture: ComponentFixture<CustomSchedulerCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomSchedulerCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomSchedulerCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
