package demomavinfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class logindao {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hr_management";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "your_password"; // <-- Change this

    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT * FROM login WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

