# Cuenta Bancaria — Nuevas Funcionalidades (POO)

Proyecto Java que amplía una cuenta bancaria aplicando principios de **Programación Orientada a Objetos**.

---

## Estructura del proyecto

```
src/
├── model/
│   ├── Cuenta.java            → Entidad principal (encapsulación)
│   ├── Transaccion.java       → Registro de operaciones
│   └── TipoTransaccion.java   → Enum de tipos de movimiento
├── service/
│   ├── IServicioBancario.java     → Interfaz base (inversión de dependencias)
│   ├── ServicioTransferencia.java → Funcionalidad 1: Transferencias
│   ├── ServicioHistorial.java     → Funcionalidad 2: Historial
│   └── ServicioIntereses.java     → Funcionalidad 3: Intereses y Cargos
├── exception/
│   ├── SaldoInsuficienteException.java
│   └── CuentaInvalidaException.java
└── Main.java                  → Punto de entrada y demo
```

---

## Nuevas funcionalidades

### 1. Transferencias entre cuentas (`ServicioTransferencia`)
- Mueve dinero de una cuenta origen a una cuenta destino.
- Valida que las cuentas existan, sean distintas y haya saldo suficiente.
- Registra la operación en el historial de **ambas** cuentas.

### 2. Historial de transacciones (`ServicioHistorial`)
- Muestra todas las operaciones de una cuenta con fecha, tipo, monto y saldo resultante.
- Filtra el historial por tipo de transacción.
- Genera un resumen con totales de ingresos y egresos.

### 3. Intereses y Cargos (`ServicioIntereses`)
- Aplica un interés mensual configurable sobre el saldo actual.
- Aplica un cargo fijo mensual con validación de saldo.
- Permite ejecutar el cierre mensual completo (interés + cargo).

---

## Principios POO aplicados

| Principio              | Aplicación en el proyecto |
|------------------------|---------------------------|
| **Encapsulación**      | Todos los campos de `Cuenta` son `private`. El saldo solo se modifica mediante métodos controlados. |
| **Responsabilidad Única** | Cada servicio realiza exactamente una función. `Cuenta` no contiene lógica de negocio compleja. |
| **Abierto/Cerrado**    | Se agregan nuevas funcionalidades creando nuevos servicios, sin modificar `Cuenta`. |
| **Inversión de Dependencias** | Los servicios implementan `IServicioBancario`, dependiendo de una abstracción. |

---

## Cómo compilar y ejecutar

```bash
# Desde la carpeta raíz del proyecto
javac -d out src/exception/*.java src/model/*.java src/service/*.java src/Main.java

# Ejecutar
java -cp out Main
```

---

## Requisitos
- Java 11 o superior
