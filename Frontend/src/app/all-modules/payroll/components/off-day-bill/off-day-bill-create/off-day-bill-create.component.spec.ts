import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OffDayBillCreateComponent } from './off-day-bill-create.component';

describe('OffDayBillCreateComponent', () => {
  let component: OffDayBillCreateComponent;
  let fixture: ComponentFixture<OffDayBillCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OffDayBillCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OffDayBillCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
