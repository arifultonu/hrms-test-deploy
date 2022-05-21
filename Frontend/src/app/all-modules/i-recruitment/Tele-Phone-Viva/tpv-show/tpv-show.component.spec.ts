import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TpvShowComponent } from './tpv-show.component';

describe('TpvShowComponent', () => {
  let component: TpvShowComponent;
  let fixture: ComponentFixture<TpvShowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TpvShowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TpvShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
