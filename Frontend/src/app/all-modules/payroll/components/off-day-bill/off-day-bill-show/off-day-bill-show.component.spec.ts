import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OffDayBillShowComponent } from './off-day-bill-show.component';

describe('OffDayBillShowComponent', () => {
  let component: OffDayBillShowComponent;
  let fixture: ComponentFixture<OffDayBillShowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OffDayBillShowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OffDayBillShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
