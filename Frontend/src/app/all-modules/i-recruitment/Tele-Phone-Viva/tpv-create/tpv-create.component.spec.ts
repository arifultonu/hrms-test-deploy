import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TpvCreateComponent } from './tpv-create.component';

describe('TpvCreateComponent', () => {
  let component: TpvCreateComponent;
  let fixture: ComponentFixture<TpvCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TpvCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TpvCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
