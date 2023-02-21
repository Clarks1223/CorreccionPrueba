import java.sql.*;
public class ConxBD {
    public Connection estbConexion() {
        Connection con=null;
        /*Datos para la coneccion*/
        String base="farmacia";
        String url="jdbc:mysql://localhost:3306/"+base;
        String user="root";
        String password="3264";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,user,password);
        }catch(ClassNotFoundException | SQLException e){
            System.err.println(e);
        }
        return con;
    }
}