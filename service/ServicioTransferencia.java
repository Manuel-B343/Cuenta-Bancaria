package service;

import exception.CuentaInvalidaException;
import exception.SaldoInsuficienteException;
import model.Cuenta;
import model.TipoTransaccion;

/**
 * FUNCIONALIDAD 1: Transferencias entre cuentas.
 *
 * Responsabilidad única: gestionar la lógica de transferencias.
 * Aplica validaciones claras y registra la operación en ambas cuentas.
 */
public class ServicioTransferencia implements IServicioBancario {

    @Override
    public String getNombreServicio() {
        return "Servicio de Transferencias";
    }

    /**
     * Transfiere un monto desde una cuenta origen hacia una cuenta destino.
     *
     * @param origen  Cuenta que envía el dinero
     * @param destino Cuenta que recibe el dinero
     * @param monto   Cantidad a transferir (debe ser > 0)
     * @throws CuentaInvalidaException   si alguna cuenta es nula
     * @throws SaldoInsuficienteException si la cuenta origen no tiene saldo suficiente
     */
    public void transferir(Cuenta origen, Cuenta destino, double monto)
            throws CuentaInvalidaException, SaldoInsuficienteException {

        validarCuentas(origen, destino);
        validarMonto(monto);

        if (origen.getSaldo() < monto) {
            throw new SaldoInsuficienteException(
                String.format("Saldo insuficiente en cuenta [%s]. Disponible: $%.2f | Requerido: $%.2f",
                        origen.getNumeroCuenta(), origen.getSaldo(), monto)
            );
        }

        // Débito en cuenta origen
        origen.ajustarSaldo(
            -monto,
            TipoTransaccion.TRANSFERENCIA_ENVIADA,
            String.format("Transferencia enviada a %s [%s]", destino.getTitular(), destino.getNumeroCuenta())
        );

        // Crédito en cuenta destino
        destino.ajustarSaldo(
            monto,
            TipoTransaccion.TRANSFERENCIA_RECIBIDA,
            String.format("Transferencia recibida de %s [%s]", origen.getTitular(), origen.getNumeroCuenta())
        );

        System.out.printf("Transferencia exitosa: $%.2f de [%s] → [%s]%n",
                monto, origen.getNumeroCuenta(), destino.getNumeroCuenta());
        System.out.printf("   Saldo %s: $%.2f | Saldo %s: $%.2f%n",
                origen.getTitular(), origen.getSaldo(),
                destino.getTitular(), destino.getSaldo());
    }

    // ── Validaciones privadas ────────────────────────────────────────────────

    private void validarCuentas(Cuenta origen, Cuenta destino) throws CuentaInvalidaException {
        if (origen == null) {
            throw new CuentaInvalidaException("La cuenta de origen no puede ser nula.");
        }
        if (destino == null) {
            throw new CuentaInvalidaException("La cuenta de destino no puede ser nula.");
        }
        if (origen.getNumeroCuenta().equals(destino.getNumeroCuenta())) {
            throw new CuentaInvalidaException("No se puede transferir a la misma cuenta.");
        }
    }

    private void validarMonto(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de la transferencia debe ser mayor a cero.");
        }
    }
}
