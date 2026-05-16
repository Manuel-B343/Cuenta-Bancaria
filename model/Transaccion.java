package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una transacción registrada en el historial de una cuenta.
 * Responsabilidad única: almacenar datos de una operación bancaria.
 */
public class Transaccion {

    private final TipoTransaccion tipo;
    private final double monto;
    private final double saldoResultante;
    private final String descripcion;
    private final LocalDateTime fecha;

    public Transaccion(TipoTransaccion tipo, double monto, double saldoResultante, String descripcion) {
        this.tipo = tipo;
        this.monto = monto;
        this.saldoResultante = saldoResultante;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    // Getters
    public TipoTransaccion getTipo()       { return tipo; }
    public double getMonto()               { return monto; }
    public double getSaldoResultante()     { return saldoResultante; }
    public String getDescripcion()         { return descripcion; }
    public LocalDateTime getFecha()        { return fecha; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("[%s] %-28s | Monto: %+10.2f | Saldo: %10.2f | %s",
                fecha.format(fmt), tipo, monto, saldoResultante, descripcion);
    }
}
