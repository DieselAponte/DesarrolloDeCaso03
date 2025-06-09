package strategy;

import model.Transferencia;

public class DepositoStrategy implements Strategy{

    @Override
    public void ejecutar(Transferencia transferencia){
        if (transferencia.getMonto() <= 0){
            System.out.println("Monto debe ser positivo");
        }
        transferencia.getCuentaDestino().acreditar(transferencia.getMonto());
    }

}