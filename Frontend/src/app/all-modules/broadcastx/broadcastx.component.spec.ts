import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BroadcastxComponent } from './broadcastx.component';

describe('BroadcastxComponent', () => {
  let component: BroadcastxComponent;
  let fixture: ComponentFixture<BroadcastxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BroadcastxComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BroadcastxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
