import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TpvListComponent } from './tpv-list.component';

describe('TpvListComponent', () => {
  let component: TpvListComponent;
  let fixture: ComponentFixture<TpvListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TpvListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TpvListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
