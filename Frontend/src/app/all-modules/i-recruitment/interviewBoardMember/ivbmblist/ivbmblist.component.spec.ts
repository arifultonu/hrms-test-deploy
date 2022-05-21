import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IvbmblistComponent } from './ivbmblist.component';

describe('IvbmblistComponent', () => {
  let component: IvbmblistComponent;
  let fixture: ComponentFixture<IvbmblistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IvbmblistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IvbmblistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
