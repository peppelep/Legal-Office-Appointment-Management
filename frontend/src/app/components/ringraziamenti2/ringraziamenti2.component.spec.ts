import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Ringraziamenti2Component } from './ringraziamenti2.component';

describe('Ringraziamenti2Component', () => {
  let component: Ringraziamenti2Component;
  let fixture: ComponentFixture<Ringraziamenti2Component>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [Ringraziamenti2Component]
    });
    fixture = TestBed.createComponent(Ringraziamenti2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
