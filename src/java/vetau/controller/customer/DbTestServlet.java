package vetau.controller.customer;

import vetau.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

@WebServlet(name = "DbTestServlet", urlPatterns = {"/db-test"})
public class DbTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean dbOk = false;
        String dbMessage;
        String serverInfo = "";
        int gaCount = -1;

        try (Connection connection = DBConnection.getConnection()) {
            dbOk = true;

            DatabaseMetaData metaData = connection.getMetaData();
            serverInfo = metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion();

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM dbo.GA")) {
                if (resultSet.next()) {
                    gaCount = resultSet.getInt("total");
                }
            }

            dbMessage = "Kết nối database thành công.";
        } catch (SQLException ex) {
            dbMessage = ex.getClass().getName() + ": " + ex.getMessage();
            ex.printStackTrace();
        }

        request.setAttribute("dbOk", dbOk);
        request.setAttribute("dbMessage", dbMessage);
        request.setAttribute("dbName", DBConnection.getDatabaseName());
        request.setAttribute("dbUrl", DBConnection.getUrlForDebug());
        request.setAttribute("serverInfo", serverInfo);
        request.setAttribute("gaCount", gaCount);

        request.getRequestDispatcher("/views/customer/db-test.jsp").forward(request, response);
    }
}
