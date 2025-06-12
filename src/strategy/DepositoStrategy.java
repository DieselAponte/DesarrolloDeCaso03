package strategy;

import model.Transferencia;

public class DepositoStrategy implements Strategy{

    @Override
    public void ejecutar(Transferencia transferencia) {
        if (transferencia.getMonto() <= 0) {
            System.out.println("Monto debe ser positivo");
            return;
        }

        if (transferencia.getCuentaDestino() == null) {
            System.out.println("Cuenta destino no puede ser nula");
            return;
        }

        transferencia.getCuentaDestino().acreditar(transferencia.getMonto());

        if (transferencia.getFecha() == null) {
            transferencia.setFecha(new java.util.Date());
        }

        System.out.println("Depósito exitoso");
    }
}
