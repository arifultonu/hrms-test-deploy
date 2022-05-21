import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OffDayBillEditComponent } from './off-day-bill-edit.component';

describe('OffDayBillEditComponent', () => {
  let component: OffDayBillEditComponent;
  let fixture: ComponentFixture<OffDayBillEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OffDayBillEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OffDayBillEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
