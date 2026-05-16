package exception;

/*
   Excepción lanzada cuando se intenta operar con una cuenta nula
   o inválida.
 */
public class CuentaInvalidaException extends Exception {
    public CuentaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
