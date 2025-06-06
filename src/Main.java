import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import model.*;
import strategy.*;
import repository.*;
import java.util.List;

public class Main {
    // Mapa para mantener el estado de las cuentas
    private static Map<String, Cuenta> cuentas = new HashMap<>();

    public static void main(String[] args) throws SQLException {
// Inicializar cuentas con saldos iniciales
        cuentas.put("CU123", new Cuenta(01,"CU123", "BANCO_ABC", 1000.0));
        cuentas.put("CU456", new Cuenta(02,"CU456", "BANCO_ABC", 500.0));
        cuentas.put("SERV001", new Cuenta(03,"SERV001", "SERVICIOS", 0.0));

// Configuración del sistema
        Repository repository = new ConcreteTransferenciaRepository(DatabaseConnection.getConnection());
        ProcesadorTransferencias procesador = new ProcesadorTransferencias(repository);

// Mostrar saldos iniciales
        imprimirSaldos("=== SALDOS INICIALES ===");

// Ejecutar transacciones de prueba
        ejecutarTransferencia(procesador, 200.0, "CU123", "CU456");
        ejecutarDeposito(procesador, 300.0, "CU456");
        ejecutarPagoServicio(procesador, 150.0, "CU123", "SERV001", "FACT-12345");
    }

    private static void ejecutarTransferencia(ProcesadorTransferencias procesador,
                                              double monto, String origen, String destino) {
        System.out.println("\n=== TRANSFERENCIA ===");

        Transferencia transaccion = new Transferencia();
        transaccion.setTipo(TipoTransferencia.TRANSFERENCIA);
        transaccion.setMonto(monto);
        transaccion.setCuentaOrigen(cuentas.get(origen));
        transaccion.setCuentaDestino(cuentas.get(destino));

        procesador.setStrategy(new TransferenciaStrategy());

        try {
            procesador.procesar(transaccion);
            System.out.println("Transferencia exitosa");
            System.out.printf("Detalles: Tipo=TRANSFERENCIA, Monto=%.1f, Origen=%s, Destino=%s\n",
                    monto, origen, destino);
            imprimirSaldos("Saldos actualizados:");
        } catch (Exception e) {
            System.out.println("Error en transferencia: " + e.getMessage());
        }
    }

    private static void ejecutarDeposito(ProcesadorTransferencias procesador,
                                         double monto, String destino) {
        System.out.println("\n=== DEPÓSITO ===");

        Transferencia transaccion = new Transferencia();
        transaccion.setTipo(TipoTransferencia.DEPOSITO);
        transaccion.setMonto(monto);
        transaccion.setCuentaDestino(cuentas.get(destino));

        procesador.setStrategy(new DepositoStrategy());

        try {
            procesador.procesar(transaccion);
            System.out.println("Depósito exitoso");
            System.out.printf("Detalles: Tipo=DEPOSITO, Monto=%.1f, Origen=N/A, Destino=%s\n",
                    monto, destino);
            imprimirSaldos("Saldos actualizados:");
        } catch (Exception e) {
            System.out.println("Error en depósito: " + e.getMessage());
        }
    }

    private static void ejecutarPagoServicio(ProcesadorTransferencias procesador,
                                             double monto, String origen, String destino,
                                             String referencia) {
        System.out.println("\n=== PAGO DE SERVICIOS ===");

        Transferencia transaccion = new Transferencia();
        transaccion.setTipo(TipoTransferencia.PAGO_SERVICIO);
        transaccion.setMonto(monto);
        transaccion.setCuentaOrigen(cuentas.get(origen));
        transaccion.setCuentaDestino(cuentas.get(destino));
        transaccion.setReferencia(referencia);

        procesador.setStrategy(new PagoServiciosStrategy());

        try {
            procesador.procesar(transaccion);
            System.out.println("Pago de servicio exitoso");
            System.out.printf("Detalles: Tipo=PAGO_SERVICIO, Monto=%.1f, Origen=%s, Destino=%s, Referencia=%s\n",
                    monto, origen, destino, referencia);
            imprimirSaldos("Saldos actualizados:");
        } catch (Exception e) {
            System.out.println("Error en pago de servicio: " + e.getMessage());
        }
    }

    private static void imprimirSaldos(String titulo) {
        System.out.println(titulo);
        System.out.printf("- Cuenta CU123: $%.1f\n", cuentas.get("CU123").getSaldo());
        System.out.printf("- Cuenta CU456: $%.1f\n", cuentas.get("CU456").getSaldo());
        System.out.printf("- Cuenta SERV001: $%.1f\n", cuentas.get("SERV001").getSaldo());
    }
}

