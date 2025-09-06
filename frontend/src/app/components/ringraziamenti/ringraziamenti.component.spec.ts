import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RingraziamentiComponent } from './ringraziamenti.component';

describe('RingraziamentiComponent', () => {
  let component: RingraziamentiComponent;
  let fixture: ComponentFixture<RingraziamentiComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RingraziamentiComponent]
    });
    fixture = TestBed.createComponent(RingraziamentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
