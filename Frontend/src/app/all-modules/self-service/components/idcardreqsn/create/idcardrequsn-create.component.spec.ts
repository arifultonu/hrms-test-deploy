import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IdcardrequsnCreateComponent } from './idcardrequsn-create.component';

describe('IdcardrequsnCreateComponent', () => {
  let component: IdcardrequsnCreateComponent;
  let fixture: ComponentFixture<IdcardrequsnCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IdcardrequsnCreateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IdcardrequsnCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
