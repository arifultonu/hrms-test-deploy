import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TpvEditComponent } from './tpv-edit.component';

describe('TpvEditComponent', () => {
  let component: TpvEditComponent;
  let fixture: ComponentFixture<TpvEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TpvEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TpvEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
