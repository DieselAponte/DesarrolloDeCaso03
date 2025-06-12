package Service;

import strategy.*;
import model.Transferencia;
import repository.Repository;

import java.util.Date;
import java.util.List;

public class ServicioBancario {
    private final Repository transferenciaRepo;

    public ServicioBancario(Repository transferenciaRepo) {
        this.transferenciaRepo = transferenciaRepo;
    }

    public void realizarTransferencia(Transferencia transferencia, Strategy strategy) {
        strategy.ejecutar(transferencia);
        transferencia.setFecha(new Date());

        transferenciaRepo.guardarTransferencia(transferencia);
        System.out.println("Transferencia procesada con estrategia: " + strategy.getClass().getSimpleName());
    }

    public List<Transferencia> obtenerTransferenciasDeCuenta(int cuentaId) {
        return transferenciaRepo.mostrarTransaccionesDeCuenta(String.valueOf(cuentaId));
    }

    public void eliminarTransferencia(int transferenciaId) {
        transferenciaRepo.eliminarTransferencia(transferenciaId);
    }
}

