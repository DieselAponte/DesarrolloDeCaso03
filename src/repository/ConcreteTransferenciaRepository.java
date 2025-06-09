package repository;

import model.Transferencia;
import model.Cuenta;
import model.TipoTransferencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConcreteTransferenciaRepository implements Repository {
    private final Connection connection;

    public ConcreteTransferenciaRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void guardarTransferencia(Transferencia t) {
        if (t == null) return;

        String sql = "INSERT INTO transacciones "
                + "(tipo, monto, cuenta_origen, cuenta_destino, referencia, comision, fecha) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, t.getTipo().name());
            stmt.setDouble(2, t.getMonto());

            if (t.getCuentaOrigen() != null) {
                stmt.setString(3, t.getCuentaOrigen().getNumero());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            stmt.setString(4, t.getCuentaDestino().getNumero());
            stmt.setString(5, t.getReferencia());
            stmt.setDouble(6, t.getComision());
            stmt.setTimestamp(7, new Timestamp(t.getFecha().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar transferencia: " + e.getMessage());
        }
    }

    @Override
    public List<Transferencia> mostrarTransaccionesDeCuenta(String cuentaNumero) {
        List<Transferencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacciones "
                + "WHERE cuenta_origen = ? OR cuenta_destino = ? "
                + "ORDER BY fecha DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cuentaNumero);
            stmt.setString(2, cuentaNumero);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transferencia t = new Transferencia();
                    t.setTipo(TipoTransferencia.valueOf(rs.getString("tipo")));
                    t.setMonto(rs.getDouble("monto"));
                    t.setReferencia(rs.getString("referencia"));
                    t.setComision(rs.getDouble("comision"));
                    t.setFecha(new Date(rs.getTimestamp("fecha").getTime()));

                    String numOrigen = rs.getString("cuenta_origen");
                    if (numOrigen != null) {
                        t.setCuentaOrigen(new Cuenta(0, numOrigen, null, 0));
                    }

                    t.setCuentaDestino(new Cuenta(0, rs.getString("cuenta_destino"), null, 0));

                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al mostrar transacciones: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void eliminarTransferencia(int id) {
        String sql = "DELETE FROM transacciones WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar transferencia: " + e.getMessage());
        }
    }
}