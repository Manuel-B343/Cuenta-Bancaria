package service;

import exception.CuentaInvalidaException;
import model.Cuenta;
import model.Transaccion;
import model.TipoTransaccion;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FUNCIONALIDAD 2: Historial de transacciones.
 *
 * Responsabilidad única: consultar y mostrar el historial de operaciones.
 * No modifica ninguna cuenta, solo lee información.
 */
public class ServicioHistorial implements IServicioBancario {

    @Override
    public String getNombreServicio() {
        return "Servicio de Historial";
    }

    /**
     * Muestra el historial completo de transacciones de una cuenta.
     */
    public void mostrarHistorial(Cuenta cuenta) throws CuentaInvalidaException {
        validarCuenta(cuenta);

        List<Transaccion> historial = cuenta.getHistorial();

        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════╗");
        System.out.printf( "║  HISTORIAL DE TRANSACCIONES — %s [%s]%n",
                cuenta.getTitular(), cuenta.getNumeroCuenta());
        System.out.println("╠══════════════════════════════════════════════════════════════════════════╣");

        if (historial.isEmpty()) {
            System.out.println("║  No hay transacciones registradas.                                       ║");
        } else {
            historial.forEach(t -> System.out.println("║  " + t));
        }

        System.out.println("╠══════════════════════════════════════════════════════════════════════════╣");
        System.out.printf( "║  Total de operaciones: %-5d | Saldo actual: $%-10.2f               ║%n",
                historial.size(), cuenta.getSaldo());
        System.out.println("╚══════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Filtra el historial por tipo de transacción.
     */
    public void mostrarHistorialPorTipo(Cuenta cuenta, TipoTransaccion tipo)
            throws CuentaInvalidaException {
        validarCuenta(cuenta);

        List<Transaccion> filtradas = cuenta.getHistorial().stream()
                .filter(t -> t.getTipo() == tipo)
                .collect(Collectors.toList());

        System.out.printf("%n── Historial filtrado por [%s] ── %d registro(s) ──%n", tipo, filtradas.size());

        if (filtradas.isEmpty()) {
            System.out.println("  No hay transacciones de este tipo.");
        } else {
            filtradas.forEach(t -> System.out.println("  " + t));
        }
    }

    /**
     * Calcula el total de depósitos y retiros de la cuenta.
     */
    public void mostrarResumen(Cuenta cuenta) throws CuentaInvalidaException {
        validarCuenta(cuenta);

        double totalDepositado = cuenta.getHistorial().stream()
                .filter(t -> t.getMonto() > 0)
                .mapToDouble(Transaccion::getMonto)
                .sum();

        double totalRetirado = cuenta.getHistorial().stream()
                .filter(t -> t.getMonto() < 0)
                .mapToDouble(t -> Math.abs(t.getMonto()))
                .sum();

        System.out.printf("%n── Resumen de cuenta [%s] ──%n", cuenta.getNumeroCuenta());
        System.out.printf("  Total ingresado : $%.2f%n", totalDepositado);
        System.out.printf("  Total egresado  : $%.2f%n", totalRetirado);
        System.out.printf("  Saldo actual    : $%.2f%n", cuenta.getSaldo());
    }

    // ── Validaciones ─────────────────────────────────────────────────────────

    private void validarCuenta(Cuenta cuenta) throws CuentaInvalidaException {
        if (cuenta == null) {
            throw new CuentaInvalidaException("La cuenta no puede ser nula.");
        }
    }
}
