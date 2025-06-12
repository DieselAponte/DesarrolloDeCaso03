package repository;

import model.Transferencia;
import java.util.List;

//Implementacion de los metodos para el manejo de la base de datos (CRUD Operations)
public interface Repository {
    void guardarTransferencia(Transferencia transferencia);
    void eliminarTransferencia(int transferenciaID);
    List mostrarTransaccionesDeCuenta(String cuentaID);


}
