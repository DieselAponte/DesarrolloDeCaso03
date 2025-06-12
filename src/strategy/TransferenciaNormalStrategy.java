package strategy;

import model.*;

public class TransferenciaNormalStrategy implements Strategy {

    @Override
    public void ejecutar(Transferencia transferencia){
        if (transferencia.getCuentaOrigen().getSaldo() < transferencia.getMonto()){
            System.out.println("Saldo insuficiente");
            return;
        }

        if (esInterbancaria(transferencia)){
            double comision = transferencia.getMonto() * 0.01;
            transferencia.setComision(comision);
            transferencia.getCuentaOrigen().debitar(transferencia.getMonto() + comision);
        } else {
            transferencia.getCuentaOrigen().debitar(transferencia.getMonto());
        }

        transferencia.getCuentaDestino().acreditar(transferencia.getMonto());
    }

    private boolean esInterbancaria(Transferencia transferencia){
        return !transferencia.getCuentaOrigen().getBanco()
                .equals(transferencia.getCuentaDestino().getBanco());
    }

}