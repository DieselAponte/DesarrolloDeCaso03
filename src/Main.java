import repository.ConcreteTransferenciaRepository;
import repository.DatabaseConnection;
import model.Cuenta;
import model.Transferencia;
import Service.ServicioBancario;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            ConcreteTransferenciaRepository repo = new ConcreteTransferenciaRepository(conn);
            ServicioBancario servicio = new ServicioBancario(repo);

            Cuenta c1 = new Cuenta(1, "CU123", "BancoA", 1000.0);
            Cuenta c2 = new Cuenta(2, "CU456", "BancoA", 500.0);
            Cuenta servicioCuenta = new Cuenta(3, "SERV001", "BancoServicios", 0.0);

            servicio.registrarCuentas(new Cuenta[] {c1, c2, servicioCuenta});

            System.out.println("=== SALDOS INICIALES ===");
            servicio.imprimirSaldos();

            servicio.realizarTransferencia(c1, c2, 200.0, new Transferencia());
            servicio.realizarDeposito(c2, 300.0, new Transferencia());
            servicio.realizarPagoServicio(c1, servicioCuenta, 150.0, "FACT-12345", new Transferencia());
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
