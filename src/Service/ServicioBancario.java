package Service;

import model.Cuenta;
import model.Transferencia;
import model.TipoTransferencia;
import repository.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServicioBancario {
    private final Repository transferenciaRepo;
    private final List<Cuenta> cuentasRegistradas = new ArrayList<>();

    public ServicioBancario(Repository transferenciaRepo) {
        this.transferenciaRepo = transferenciaRepo;
    }

    public void registrarCuentas(Cuenta[] cuentas) {
        for (Cuenta c : cuentas) {
            if (!cuentasRegistradas.contains(c)) {
                cuentasRegistradas.add(c);
            }
        }
    }

    public void realizarTransferencia(Cuenta origen, Cuenta destino, double monto, Transferencia transferencia) {
        System.out.println("=== TRANSFERENCIA ===");
        if (origen.getSaldo() >= monto) {
            origen.transferir(monto, destino);
            transferencia.setTipo(TipoTransferencia.TRANSFERENCIA);
            transferencia.setCuentaOrigen(origen);
            transferencia.setCuentaDestino(destino);
            transferencia.setMonto(monto);
            transferencia.setFecha(new Date());
            transferencia.setComision(0.0);

            transferenciaRepo.guardarTransferencia(transferencia);
            System.out.println("Transferencia exitosa");
            System.out.printf("Detalles: Tipo=%s, Monto=%.1f, Origen=%s, Destino=%s%n",
                    transferencia.getTipo(), transferencia.getMonto(),
                    origen.getNumero(), destino.getNumero());
        } else {
            System.out.println("Fondos insuficientes.");
        }
        imprimirSaldos();
    }

    public void realizarDeposito(Cuenta destino, double monto, Transferencia transferencia) {
        System.out.println("\n=== DEPÓSITO ===");
        destino.acreditar(monto);
        transferencia.setTipo(TipoTransferencia.DEPOSITO);
        transferencia.setCuentaOrigen(null);
        transferencia.setCuentaDestino(destino);
        transferencia.setMonto(monto);
        transferencia.setFecha(new Date());
        transferencia.setComision(0.0);

        transferenciaRepo.guardarTransferencia(transferencia);
        System.out.println("Depósito exitoso");
        System.out.printf("Detalles: Tipo=%s, Monto=%.1f, Origen=N/A, Destino=%s%n",
                transferencia.getTipo(), transferencia.getMonto(),
                destino.getNumero());
        imprimirSaldos();
    }

    public void realizarPagoServicio(Cuenta origen, Cuenta servicio, double monto,
                                     String referencia, Transferencia transferencia) {
        System.out.println("\n=== PAGO DE SERVICIOS ===");
        if (origen.getSaldo() >= monto) {
            origen.transferir(monto, servicio);
            transferencia.setTipo(TipoTransferencia.PAGO_SERVICIO);
            transferencia.setCuentaOrigen(origen);
            transferencia.setCuentaDestino(servicio);
            transferencia.setMonto(monto);
            transferencia.setReferencia(referencia);
            transferencia.setFecha(new Date());
            transferencia.setComision(0.0);

            transferenciaRepo.guardarTransferencia(transferencia);
            System.out.println("Pago de servicio exitoso");
            System.out.printf("Detalles: Tipo=%s, Monto=%.1f, Origen=%s, Destino=%s, Referencia=%s%n",
                    transferencia.getTipo(), transferencia.getMonto(),
                    origen.getNumero(), servicio.getNumero(), referencia);
        } else {
            System.out.println("Fondos insuficientes.");
        }
        imprimirSaldos();
    }


    public void imprimirSaldos() {
        System.out.println("Saldos actualizados:");
        for (Cuenta c : cuentasRegistradas) {
            System.out.printf("- Cuenta %s: $%.1f%n", c.getNumero(), c.getSaldo());
        }
    }

    public List<Transferencia> obtenerTransferenciasDeCuenta(int cuentaId) {
        return transferenciaRepo.mostrarTransaccionesDeCuenta(String.valueOf(cuentaId));
    }

    public void eliminarTransferencia(int transferenciaId) {
        transferenciaRepo.eliminarTransferencia(transferenciaId);
    }
}
