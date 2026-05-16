package service;

import exception.CuentaInvalidaException;
import model.Cuenta;
import model.TipoTransaccion;

/**
 * FUNCIONALIDAD 3: Aplicación de intereses y cargos.
 *
 * Responsabilidad única: calcular y aplicar intereses o cargos sobre cuentas.
 * Las tasas se configuran en el constructor (principio de Inversión de Dependencias).
 */
public class ServicioIntereses implements IServicioBancario {

    private final double tasaInteres;    // ej. 0.03 = 3%
    private final double montoCargo;     // cargo fijo mensual

    /**
     * @param tasaInteres Tasa de interés mensual (ej. 0.03 para 3%)
     * @param montoCargo  Cargo fijo mensual (ej. 5000.0)
     */
    public ServicioIntereses(double tasaInteres, double montoCargo) {
        if (tasaInteres < 0) throw new IllegalArgumentException("La tasa de interes no puede ser negativa.");
        if (montoCargo < 0)  throw new IllegalArgumentException("El cargo no puede ser negativo.");
        this.tasaInteres = tasaInteres;
        this.montoCargo  = montoCargo;
    }

    @Override
    public String getNombreServicio() {
        return "Servicio de Intereses y Cargos";
    }

    /**
     * Aplica un interés positivo sobre el saldo actual de la cuenta.
     * El interés se calcula como: saldo × tasaInteres
     */
    public void aplicarInteres(Cuenta cuenta) throws CuentaInvalidaException {
        validarCuenta(cuenta);

        double interes = cuenta.getSaldo() * tasaInteres;

        cuenta.ajustarSaldo(
            interes,
            TipoTransaccion.INTERES_APLICADO,
            String.format("Interes mensual aplicado (%.1f%%)", tasaInteres * 100)
        );

        System.out.printf("Interes aplicado a [%s]: +$%.2f (%.1f%%). Nuevo saldo: $%.2f%n",
                cuenta.getNumeroCuenta(), interes, tasaInteres * 100, cuenta.getSaldo());
    }

    /**
     * Aplica un cargo fijo (comisión) a la cuenta.
     * Verifica que el saldo no quede negativo.
     */
    public void aplicarCargo(Cuenta cuenta) throws CuentaInvalidaException {
        validarCuenta(cuenta);

        if (cuenta.getSaldo() < montoCargo) {
            System.out.printf("No se pudo aplicar cargo a [%s]: saldo insuficiente ($%.2f < $%.2f)%n",
                    cuenta.getNumeroCuenta(), cuenta.getSaldo(), montoCargo);
            return;
        }

        cuenta.ajustarSaldo(
            -montoCargo,
            TipoTransaccion.CARGO_APLICADO,
            String.format("Cargo fijo mensual aplicado ($%.2f)", montoCargo)
        );

        System.out.printf("Cargo aplicado a [%s]: -$%.2f. Nuevo saldo: $%.2f%n",
                cuenta.getNumeroCuenta(), montoCargo, cuenta.getSaldo());
    }

    /**
     * Aplica interés y cargo en un solo paso (cierre de mes).
     */
    public void aplicarCierreMensual(Cuenta cuenta) throws CuentaInvalidaException {
        System.out.printf("%n── Cierre mensual para cuenta [%s] ──%n", cuenta.getNumeroCuenta());
        aplicarInteres(cuenta);
        aplicarCargo(cuenta);
    }

    // ── Getters de configuración ──────────────────────────────────────────────

    public double getTasaInteres() { return tasaInteres; }
    public double getMontoCargo()  { return montoCargo; }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private void validarCuenta(Cuenta cuenta) throws CuentaInvalidaException {
        if (cuenta == null) {
            throw new CuentaInvalidaException("La cuenta no puede ser nula.");
        }
    }
}
