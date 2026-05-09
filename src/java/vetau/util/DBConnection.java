package vetau.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    /*
     * Theo ảnh SQL Server của bạn, bảng đang nằm trong database: VeTauDB.
     * Nếu bạn muốn dùng database khác, chỉ cần đổi DB_NAME.
     */
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String SERVER = "localhost";
    private static final int PORT = 1433;
    private static final String DB_NAME = "VeTauDB";

    private static final String URL = "jdbc:sqlserver://" + SERVER + ":" + PORT + ";"
            + "databaseName=" + DB_NAME + ";"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "sendStringParametersAsUnicode=true;";

    // Sửa lại đúng tài khoản SQL Server trên máy bạn.
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "12345";

    private DBConnection() {
    }

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError(
                    "Không tìm thấy SQL Server JDBC Driver: " + DRIVER
                    + ". Hãy kiểm tra file mssql-jdbc-13.2.1.jre11.jar trong Libraries."
            );
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static String getUrlForDebug() {
        return URL;
    }

    public static String getDatabaseName() {
        return DB_NAME;
    }

    public static String getServerInfo() {
        return SERVER + ":" + PORT;
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException ex) {
            System.err.println("Lỗi kết nối database: " + ex.getMessage());
            return false;
        }
    }
}