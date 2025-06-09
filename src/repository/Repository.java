package repository;

import model.Transferencia;
import java.util.List;


public interface Repository {
    void guardarTransferencia(Transferencia transferencia);
    void eliminarTransferencia(int transferenciaID);
    List mostrarTransaccionesDeCuenta(String cuentaID);


}
