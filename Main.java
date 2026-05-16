import exception.CuentaInvalidaException;
import exception.SaldoInsuficienteException;
import model.Cuenta;
import model.TipoTransaccion;
import service.ServicioHistorial;
import service.ServicioIntereses;
import service.ServicioTransferencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static List<Cuenta> cuentas = new ArrayList<>();
    static ServicioTransferencia servicioTransferencia = new ServicioTransferencia();
    static ServicioHistorial servicioHistorial = new ServicioHistorial();
    static ServicioIntereses servicioIntereses = new ServicioIntereses(0.03, 15_000.0);

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   SISTEMA DE CUENTA BANCARIA - POO");
        System.out.println("==============================================");

        int opcion;
        do {
            menuPrincipal();
            opcion = leerInt("Seleccione una opcion: ");
            System.out.println();
            switch (opcion) {
                case 1 -> crearCuenta();
                case 2 -> depositar();
                case 3 -> retirar();
                case 4 -> transferir();
                case 5 -> verHistorial();
                case 6 -> verResumen();
                case 7 -> aplicarCierreMensual();
                case 8 -> listarCuentas();
                case 0 -> System.out.println("Saliendo del sistema. Hasta luego!");
                default -> System.out.println("Opcion invalida. Intente de nuevo.");
            }
            System.out.println();
        } while (opcion != 0);
    }

    // ── Menu ─────────────────────────────────────────────────────────────────

    static void menuPrincipal() {
        System.out.println("----------------------------------------------");
        System.out.println("  MENU PRINCIPAL");
        System.out.println("----------------------------------------------");
        System.out.println("  1. Crear cuenta");
        System.out.println("  2. Depositar");
        System.out.println("  3. Retirar");
        System.out.println("  4. Transferir entre cuentas");
        System.out.println("  5. Ver historial de transacciones");
        System.out.println("  6. Ver resumen de cuenta");
        System.out.println("  7. Aplicar cierre mensual (interes + cargo)");
        System.out.println("  8. Listar todas las cuentas");
        System.out.println("  0. Salir");
        System.out.println("----------------------------------------------");
    }

    // ── Opciones ─────────────────────────────────────────────────────────────

    static void crearCuenta() {
        System.out.println("-- CREAR CUENTA --");
        System.out.print("Numero de cuenta: ");
        String numero = sc.nextLine().trim();

        if (buscarCuenta(numero) != null) {
            System.out.println("ERROR: Ya existe una cuenta con ese numero.");
            return;
        }

        System.out.print("Nombre del titular: ");
        String titular = sc.nextLine().trim();

        double saldoInicial = leerDouble("Saldo inicial: $");

        try {
            Cuenta nueva = new Cuenta(numero, titular, saldoInicial);
            cuentas.add(nueva);
            System.out.println("Cuenta creada exitosamente: " + nueva);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    static void depositar() {
        System.out.println("-- DEPOSITAR --");
        Cuenta cuenta = seleccionarCuenta();
        if (cuenta == null) return;

        double monto = leerDouble("Monto a depositar: $");
        try {
            cuenta.depositar(monto);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    static void retirar() {
        System.out.println("-- RETIRAR --");
        Cuenta cuenta = seleccionarCuenta();
        if (cuenta == null) return;

        double monto = leerDouble("Monto a retirar: $");
        try {
            cuenta.retirar(monto);
        } catch (SaldoInsuficienteException | IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    static void transferir() {
        System.out.println("-- TRANSFERENCIA --");
        if (cuentas.size() < 2) {
            System.out.println("Necesita al menos 2 cuentas registradas para transferir.");
            return;
        }

        System.out.println("Cuenta ORIGEN:");
        Cuenta origen = seleccionarCuenta();
        if (origen == null) return;

        System.out.println("Cuenta DESTINO:");
        Cuenta destino = seleccionarCuenta();
        if (destino == null) return;

        double monto = leerDouble("Monto a transferir: $");

        try {
            servicioTransferencia.transferir(origen, destino, monto);
        } catch (CuentaInvalidaException | SaldoInsuficienteException | IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    static void verHistorial() {
        System.out.println("-- HISTORIAL DE TRANSACCIONES --");
        Cuenta cuenta = seleccionarCuenta();
        if (cuenta == null) return;

        System.out.println("Filtrar por tipo?");
        System.out.println("  0. Ver todo");
        System.out.println("  1. DEPOSITO");
        System.out.println("  2. RETIRO");
        System.out.println("  3. TRANSFERENCIA_ENVIADA");
        System.out.println("  4. TRANSFERENCIA_RECIBIDA");
        System.out.println("  5. INTERES_APLICADO");
        System.out.println("  6. CARGO_APLICADO");
        int filtro = leerInt("Opcion: ");

        try {
            if (filtro == 0) {
                servicioHistorial.mostrarHistorial(cuenta);
            } else {
                TipoTransaccion tipo = switch (filtro) {
                    case 1 -> TipoTransaccion.DEPOSITO;
                    case 2 -> TipoTransaccion.RETIRO;
                    case 3 -> TipoTransaccion.TRANSFERENCIA_ENVIADA;
                    case 4 -> TipoTransaccion.TRANSFERENCIA_RECIBIDA;
                    case 5 -> TipoTransaccion.INTERES_APLICADO;
                    case 6 -> TipoTransaccion.CARGO_APLICADO;
                    default -> null;
                };
                if (tipo != null) {
                    servicioHistorial.mostrarHistorialPorTipo(cuenta, tipo);
                } else {
                    System.out.println("Opcion de filtro invalida, mostrando todo.");
                    servicioHistorial.mostrarHistorial(cuenta);
                }
            }
        } catch (CuentaInvalidaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    static void verResumen() {
        System.out.println("-- RESUMEN DE CUENTA --");
        Cuenta cuenta = seleccionarCuenta();
        if (cuenta == null) return;

        try {
            servicioHistorial.mostrarResumen(cuenta);
        } catch (CuentaInvalidaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    static void aplicarCierreMensual() {
        System.out.println("-- CIERRE MENSUAL (Interes 3% + Cargo $15,000) --");
        System.out.println("  1. Aplicar a una cuenta");
        System.out.println("  2. Aplicar a todas las cuentas");
        int op = leerInt("Opcion: ");

        try {
            if (op == 1) {
                Cuenta cuenta = seleccionarCuenta();
                if (cuenta != null) servicioIntereses.aplicarCierreMensual(cuenta);
            } else if (op == 2) {
                if (cuentas.isEmpty()) {
                    System.out.println("No hay cuentas registradas.");
                    return;
                }
                for (Cuenta c : cuentas) {
                    servicioIntereses.aplicarCierreMensual(c);
                }
            } else {
                System.out.println("Opcion invalida.");
            }
        } catch (CuentaInvalidaException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    static void listarCuentas() {
        System.out.println("-- CUENTAS REGISTRADAS --");
        if (cuentas.isEmpty()) {
            System.out.println("No hay cuentas registradas.");
            return;
        }
        for (int i = 0; i < cuentas.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, cuentas.get(i));
        }
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    static Cuenta seleccionarCuenta() {
        if (cuentas.isEmpty()) {
            System.out.println("No hay cuentas registradas. Cree una primero (opcion 1).");
            return null;
        }
        listarCuentas();
        int idx = leerInt("Seleccione numero de cuenta: ") - 1;
        if (idx < 0 || idx >= cuentas.size()) {
            System.out.println("Seleccion invalida.");
            return null;
        }
        return cuentas.get(idx);
    }

    static Cuenta buscarCuenta(String numero) {
        return cuentas.stream()
                .filter(c -> c.getNumeroCuenta().equals(numero))
                .findFirst()
                .orElse(null);
    }

    static int leerInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido (ej: 50000).");
            }
        }
    }
}
