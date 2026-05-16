package model;

import exception.SaldoInsuficienteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
   Representa una cuenta bancaria.
   Aplica encapsulación: todos los campos son privados y el acceso
   se controla mediante métodos públicos con validaciones.
 */
public class Cuenta {

    private final String numeroCuenta;
    private final String titular;
    private double saldo;
    private final List<Transaccion> historial;

    public Cuenta(String numeroCuenta, String titular, double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;
        this.historial = new ArrayList<>();

        registrarTransaccion(TipoTransaccion.DEPOSITO, saldoInicial, "Apertura de cuenta");
    }

    // ── Operaciones básicas ──────────────────────────────────────────────────

    public void depositar(double monto) {
        validarMontoPositivo(monto);
        saldo += monto;
        registrarTransaccion(TipoTransaccion.DEPOSITO, monto, "Deposito en cuenta");
        System.out.printf(" Deposito de $%.2f realizado. Saldo actual: $%.2f%n", monto, saldo);
    }

    public void retirar(double monto) throws SaldoInsuficienteException {
        validarMontoPositivo(monto);
        verificarSaldo(monto);
        saldo -= monto;
        registrarTransaccion(TipoTransaccion.RETIRO, -monto, "Retiro de cuenta");
        System.out.printf(" Retiro de $%.2f realizado. Saldo actual: $%.2f%n", monto, saldo);
    }

    // ── Métodos de acceso controlado (encapsulación) ─────────────────────────

    public String getNumeroCuenta()         { return numeroCuenta; }
    public String getTitular()              { return titular; }
    public double getSaldo()               { return saldo; }

    /** Solo el servicio de transacciones puede modificar el saldo internamente */
    public void ajustarSaldo(double monto, TipoTransaccion tipo, String descripcion) {
        saldo += monto;
        registrarTransaccion(tipo, monto, descripcion);
    }

    /** Devuelve una copia inmutable del historial */
    public List<Transaccion> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    // ── Métodos privados ─────────────────────────────────────────────────────

    private void registrarTransaccion(TipoTransaccion tipo, double monto, String descripcion) {
        historial.add(new Transaccion(tipo, monto, saldo, descripcion));
    }

    private void validarMontoPositivo(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
    }

    private void verificarSaldo(double monto) throws SaldoInsuficienteException {
        if (monto > saldo) {
            throw new SaldoInsuficienteException(
                String.format("Saldo insuficiente. Disponible: $%.2f | Solicitado: $%.2f", saldo, monto)
            );
        }
    }

    @Override
    public String toString() {
        return String.format("Cuenta[%s] | Titular: %-20s | Saldo: $%.2f",
                numeroCuenta, titular, saldo);
    }
}
