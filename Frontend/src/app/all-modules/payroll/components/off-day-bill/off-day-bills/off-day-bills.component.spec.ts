import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OffDayBillsComponent } from './off-day-bills.component';

describe('OffDayBillsComponent', () => {
  let component: OffDayBillsComponent;
  let fixture: ComponentFixture<OffDayBillsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OffDayBillsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OffDayBillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
