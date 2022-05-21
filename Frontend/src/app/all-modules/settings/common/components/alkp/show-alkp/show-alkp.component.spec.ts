import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAlkpComponent } from './show-alkp.component';

describe('ShowAlkpComponent', () => {
  let component: ShowAlkpComponent;
  let fixture: ComponentFixture<ShowAlkpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAlkpComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAlkpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
