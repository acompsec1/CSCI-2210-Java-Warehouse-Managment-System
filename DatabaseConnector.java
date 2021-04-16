import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseConnector {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/wmdb";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "ki8&fRda";
    private static final String SELECT_QUERY = "SELECT * FROM users WHERE username = ? and password_hash = ?";
    private static final String ROLE_QUERY = "SELECT role_id FROM users WHERE username = ? and password_hash = ?";
    private static final String USER_QUERY = "SELECT username FROM users WHERE username = ?";
    private static final String CREATEUSER_QUERY = "Insert into " ;

    public static Connection getConnection() throws SQLException{
        try{

            Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
//            System.out.println("Connected to database");
            return conn;
        } catch(SQLException e){
            System.out.println(e);
        }

        return null;
    }

    public boolean validate(String usernameId, String password) throws SQLException{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = con.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, usernameId);
            preparedStatement.setString(2, password);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public boolean getRole(String usernameId, String password) throws SQLException{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(ROLE_QUERY)) {
            preparedStatement.setString(1, usernameId);
            preparedStatement.setString(2, password);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);
//                System.out.print(value);
                if (value.equals("1")){
                    return true;
                }
                else{
                    return false;
                }
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public boolean userExists(String usernameId) throws Exception{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(USER_QUERY)) {
            preparedStatement.setString(1, usernameId);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);
//                System.out.print(value);
                if (value.equals(usernameId)){
                    return true;
                }
                else{
                    return false;
                }
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public boolean create(String usernameId, String password) throws Exception{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(CREATEUSER_QUERY)) {
            preparedStatement.setString(1, usernameId);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);
//                System.out.print(value);
                if (value.equals(usernameId)){
                    return true;
                }
                else{
                    return false;
                }
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }


    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }



}