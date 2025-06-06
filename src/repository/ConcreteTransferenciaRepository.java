package repository;

import model.Transferencia;
import model.Cuenta;
import model.TipoTransferencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ConcreteTransferenciaRepository implements Repository {
    private final Connection connection;

    public ConcreteTransferenciaRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void guardarTransferencia(Transferencia transferencia) {
        if (transferencia == null) return;

        String sql = "INSERT INTO transacciones (fecha, descripcion, monto, cuentaId, referencia, comision) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(transferencia.getFecha().getTime()));
            stmt.setString(2, transferencia.getTipo().name());
            stmt.setDouble(3, transferencia.getMonto());
            stmt.setInt(4, transferencia.getCuentaID());
            stmt.setString(5, transferencia.getReferencia());
            stmt.setDouble(6, transferencia.getComision());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar transferencia: " + e.getMessage());
        }
    }

    @Override
    public List<Transferencia> mostrarTransaccionesDeCuenta(String cuentaID) {
        List<Transferencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM transacciones WHERE cuentaId = ? ORDER BY fecha DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(cuentaID));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transferencia t = new Transferencia();
                    t.setFecha(new Date(rs.getTimestamp("fecha").getTime()));
                    t.setMonto(rs.getDouble("monto"));
                    t.setReferencia(rs.getString("referencia"));
                    t.setComision(rs.getDouble("comision"));
                    t.setTipo(TipoTransferencia.valueOf(rs.getString("descripcion")));

                    Cuenta cuenta = new Cuenta(rs.getInt("cuentaId"), "", "", 0);
                    t.setCuentaOrigen(cuenta);
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
