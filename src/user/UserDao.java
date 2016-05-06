package user;

import dao.Connector;
import exception.ConnectionException;
import user.domain.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author luiz
 * @version 1
 * @since 03/05/16
 */
public class UserDao {

    public static User auth(String user, String pass) throws ConnectionException {
        Connection connection;
        User logged = null;
        try {
            connection = Connector.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionException();
        }
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE nme_login= ? AND nme_senha=md5(?);");
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet result = stmt.executeQuery();
            logged = result.next()? readResult(result, true): null;
            result.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logged;
    }

    public static ArrayList getUsers() throws ConnectionException {
        ArrayList<User> users = new ArrayList<>();
        try {
            Connection connection = Connector.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users;");
            ResultSet result = stmt.executeQuery();
            while (result.next()) users.add(readResult(result, false));
            result.close();
            stmt.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw  new ConnectionException();
        }
        return users;
    }

    private static User readResult(ResultSet result, boolean auth) throws SQLException {
        User user = new User();
        user.setId(result.getInt("cod_usuario"));
        user.setLogin(result.getString("nme_login"));
        user.setPassword(result.getString("nme_senha"));
        user.setName(result.getString("nme_usuario"));
        user.setAdmin(result.getBoolean("fl_admin"));
        user.setAuth(auth);
        return user;
    }

    public static boolean insert(User newUser) throws ConnectionException {
        String sql = "INSERT INTO users " +
                "(nme_usuario, nme_senha, nme_login, fl_admin)" +
                "VALUES (?, md5(?), ?, ?) RETURNING cod_usuario;";
        Connection connection;
        Integer id = null;
        try {
            connection = Connector.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw  new ConnectionException();
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, newUser.getName());
            stmt.setString(2, newUser.getPassword());
            stmt.setString(3, newUser.getLogin());
            stmt.setBoolean(4, newUser.isAdmin());
            ResultSet result = stmt.executeQuery();
            if (result.next()) id = result.getInt("cod_usuario");
            result.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id != null;
    }

    public static boolean update(User edit, boolean hasPass) throws ConnectionException {
        return hasPass ? passUpdate(edit) : update(edit);
    }

    private static boolean update(User edit) throws ConnectionException {
        String sql = "UPDATE users " +
                "SET nme_usuario = ?, nme_login = ?, fl_admin = ? " +
                "WHERE cod_usuario = ? RETURNING cod_usuario;";
        Connection connection;
        Integer id = null;
        try {
            connection = Connector.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionException();
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, edit.getName());
            stmt.setString(2, edit.getLogin());
            stmt.setBoolean(3, edit.isAdmin());
            stmt.setInt(4, edit.getId());
            ResultSet result = stmt.executeQuery();
            if (result.next()) id = result.getInt("cod_usuario");
            result.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id != null;
    }

    private static boolean passUpdate(User edit) throws ConnectionException {
        String sql = "UPDATE users " +
                "SET nme_usuario = ?, nme_senha = md5(?), nme_login = ?, fl_admin = ? " +
                "WHERE cod_usuario = ?;";
        Connection connection;
        Integer id = null;
        try {
            connection = Connector.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionException();
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, edit.getName());
            stmt.setString(2, edit.getLogin());
            stmt.setString(3, edit.getPassword());
            stmt.setBoolean(4, edit.isAdmin());
            stmt.setInt(5, edit.getId());
            ResultSet result = stmt.executeQuery();
            if (result.next()) id = result.getInt("cod_usuario");
            result.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id != null;
    }

    public static void delete(int id)throws ConnectionException {
        Connection connection;
        try {
            connection = Connector.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new ConnectionException();
        }
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM users WHERE cod_usuario=" + id);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
