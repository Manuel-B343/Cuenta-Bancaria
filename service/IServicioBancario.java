package service;

import model.Cuenta;

/**
 * Interfaz que define el contrato base de los servicios bancarios.
 * Aplica el principio de Inversión de Dependencias (DIP):
 * los módulos de alto nivel dependen de abstracciones, no de implementaciones concretas.
 */
public interface IServicioBancario {
    String getNombreServicio();
}
