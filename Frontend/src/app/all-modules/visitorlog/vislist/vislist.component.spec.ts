import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VislistComponent } from './vislist.component';

describe('VislistComponent', () => {
  let component: VislistComponent;
  let fixture: ComponentFixture<VislistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VislistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VislistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
