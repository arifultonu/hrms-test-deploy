import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateAlkpComponent } from './create-alkp.component';

describe('CreateAlkpComponent', () => {
  let component: CreateAlkpComponent;
  let fixture: ComponentFixture<CreateAlkpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateAlkpComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateAlkpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
