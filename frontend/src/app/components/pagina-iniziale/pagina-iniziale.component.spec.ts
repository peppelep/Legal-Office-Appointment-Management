import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaInizialeComponent } from './pagina-iniziale.component';

describe('PaginaInizialeComponent', () => {
  let component: PaginaInizialeComponent;
  let fixture: ComponentFixture<PaginaInizialeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaInizialeComponent]
    });
    fixture = TestBed.createComponent(PaginaInizialeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
