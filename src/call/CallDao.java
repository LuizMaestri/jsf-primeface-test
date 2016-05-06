package call;

import user.domain.User;
import call.domain.Call;
import call.domain.CallPriority;
import call.domain.CallStatus;
import call.domain.CallType;
import dao.Connector;
import exception.ConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author luiz
 * @version 1
 * @since 03/05/16
 */
public class CallDao {

    public static ArrayList<Call> getCalls() throws ConnectionException {
        ArrayList<Call> calls = new ArrayList<>();
        String sql = "SELECT c.*, p.nme_usuario p_nome, a.nme_usuario a_nome FROM call c " +
                "LEFT JOIN users p on c.cod_usuario_pedido = p.cod_usuario " +
                "LEFT JOIN users a ON c.cod_usuario_atendido = a.cod_usuario;";
        try {
            Connection connection = Connector.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while (result.next()){
                Call call = new Call();
                call.setId(result.getInt("cod_chamado"));
                call.setName(result.getString("nme_chamado"));
                call.setVersion(result.getString("dsc_versao_sistema"));
                call.setDescription(result.getString("dsc_chamado"));
                call.setSolution(result.getString("dsc_solucao"));
                call.setOpen(result.getTimestamp("dta_abertura").getTime());
                Date close = result.getTimestamp("dta_fechamento");
                if (close != null) call.setClose(close.getTime());
                call.setStatus(CallStatus.getStatus(result.getInt("sta_chamado")));
                call.setPriority(CallPriority.getPriority(result.getInt("tpo_prioridade")));
                call.setType(CallType.getType(result.getInt("tpo_chamado")));
                User user = new User();
                user.setId(result.getInt("cod_usuario_pedido"));
                user.setName(result.getString("p_nome"));
                call.setRequestUser(user);
                Integer id = (Integer) result.getObject("cod_usuario_atendido");
                if (id != null){
                    user.setId(id);
                    user.setName(result.getString("a_nome"));
                    call.setUser(user);
                }
                calls.add(call);
            }
            result.close();
            stmt.close();
            connection.close();
        } catch (ClassNotFoundException e) {
                throw new ConnectionException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calls;
    }

     public static boolean insert(Call newCall) throws ConnectionException {
         String sql = "INSERT INTO call " +
                 "(cod_usuario_pedido, dsc_versao_sistema, nme_chamado, dsc_chamado, tpo_chamado, tpo_prioridade)" +
                 "VALUES (?,?,?,?,?,?) RETURNING cod_chamado;";
         Connection connection;
         Integer id = null;
         try {
             connection = Connector.getConnection();
         } catch (ClassNotFoundException | SQLException e) {
             throw new ConnectionException();
         }
         try {
             PreparedStatement stmt = connection.prepareStatement(sql);
             stmt.setInt(1, newCall.getRequestUser().getId());
             stmt.setString(2, newCall.getVersion());
             stmt.setString(3, newCall.getName());
             stmt.setString(4, newCall.getDescription());
             stmt.setInt(5, newCall.getType().ordinal());
             stmt.setInt(6, newCall.getPriority().ordinal());
             ResultSet result = stmt.executeQuery();
             if (result.next()) id = result.getInt("cod_chamado");
             result.close();
             stmt.close();
             connection.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return id != null;
     }

    public static boolean update(Call detail) throws ConnectionException {
        String sql = "UPDATE call SET " +
                "cod_usuario_pedido  = ?, cod_usuario_atendido  = ?, dsc_versao_sistema  = ?, " +
                "nme_chamado  = ?, dsc_chamado  = ?, dsc_solucao  = ?, tpo_chamado  = ?, " +
                "tpo_prioridade  = ?, sta_chamado  = ?, dta_fechamento = ? " +
                "WHERE cod_chamado  = ? RETURNING cod_chamado;";
        Connection connection;
        Integer id = null;
        try {
            connection = Connector.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionException();
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, detail.getRequestUser().getId());
            User user = detail.getUser();
            if (user != null) stmt.setInt(2, user.getId());
            else stmt.setNull(2, Types.INTEGER);
            stmt.setString(3, detail.getVersion());
            stmt.setString(4, detail.getName());
            stmt.setString(5, detail.getDescription());
            stmt.setString(6, detail.getSolution());
            stmt.setInt(7, detail.getType().ordinal());
            stmt.setInt(8, detail.getPriority().ordinal());
            stmt.setInt(9, detail.getStatus().ordinal());
            if (detail.getClose() != null) stmt.setTimestamp(10, new Timestamp(detail.getClose()));
            else stmt.setNull(10, Types.TIMESTAMP);
            stmt.setInt(11, detail.getId());
            ResultSet result = stmt.executeQuery();
            if (result.next()) id = result.getInt("cod_chamado");
            result.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id != null;
    }

    public static void delete(int id) throws ConnectionException {
        Connection connection;
        try {
            connection = Connector.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionException();
        }
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM call WHERE cod_chamado=" + id);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
