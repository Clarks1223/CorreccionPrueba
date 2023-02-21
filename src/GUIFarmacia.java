import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GUIFarmacia extends Ventanas{
    private JPanel JPfarmacia;
    private JButton JBagregar;
    private JButton JBbuscar;
    private JButton JBactualizar;
    private JButton JBeliminar;
    private JComboBox JCBreceta;
    private JComboBox JCBdatecaducidad;
    private JTextField JTFnomgenerico;
    private JTextField JTFnomcomercial;
    private JTextField JTFcodigo;
    private JTextField JTFstock;
    private JLabel JLtitulo;
    private JLabel JLcodproducto;
    private JLabel JLnomcomer;
    private JLabel JLdatecaducidad;
    private JLabel JLreceta;
    private JLabel JLnomgenerico;
    private JLabel JLstock;
    //variables objeto frame
    JFrame CRUD= new JFrame("Clarks Pharmacy");
    //variables y objeto para la conexion
    ConxBD conectarBD = new ConxBD();
    Connection con;
    public GUIFarmacia() {
        //Cargo los dominios a sus resepectivos combo box
        try{
            con = conectarBD.estbConexion();
            //Cargar los anios
            Statement consultaAnio = con.createStatement();
            ResultSet resultadoAnio = consultaAnio.executeQuery("Select * from ANIODOMINIO");
            while (resultadoAnio.next()){
                JCBdatecaducidad.addItem(resultadoAnio.getString(1));
            }
            //Cargar recetas
            Statement consultaReceta = con.createStatement();
            ResultSet resultadoReceta = consultaReceta.executeQuery("Select * from RECETADOMINIO");
            while (resultadoReceta.next()){
                JCBreceta.addItem(resultadoReceta.getString(1));
            }
            con.close();
        }catch (HeadlessException | SQLException f){
            System.err.println(f);
        }
        //Boton AGREGAR PRODUCTO
        JBagregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resultado=0;
                try{
                    PreparedStatement ps;
                    con=conectarBD.estbConexion();
                    ps=con.prepareStatement("Insert into PRODUCTOS VALUES (?,?,?,?,?,?)");
                    ps.setString(1, JTFcodigo.getText());
                    ps.setString(2, JTFnomcomercial.getText());
                    ps.setString(3, JTFnomgenerico.getText());
                    ps.setString(4, JTFstock.getText());
                    ps.setString(5, JCBreceta.getSelectedItem().toString());
                    ps.setString(6, JCBdatecaducidad.getSelectedItem().toString());
                    resultado=ps.executeUpdate();
                    if(resultado>0){
                        JOptionPane.showMessageDialog(null,"Producto Guardado Correctamente");
                    }else{
                        JOptionPane.showMessageDialog(null,"ERROR al Guardar Producto");
                    }
                    con.close();
                    limpiarCampos();
                }catch(HeadlessException | SQLException f){
                    System.err.println(f);
                }
            }
        });
        //Boton buscar
        JBbuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JTFcodigo.getText();
                int resultado=0;
                try{
                    con=conectarBD.estbConexion();
                    Statement consulta = con.createStatement();
                    ResultSet resultBusqueda = consulta.executeQuery("select * from PRODUCTOS where CODPROD="+id);
                    while(resultBusqueda.next()){
                        JTFnomcomercial.setText((resultBusqueda.getString(1)));
                        JTFnomgenerico.setText((resultBusqueda.getString(2)));
                        JTFnomcomercial.setText((resultBusqueda.getString(3)));
                        JTFstock.setText((resultBusqueda.getString(4)));
                        JCBreceta.setSelectedItem(resultBusqueda.getString(5));
                        JCBdatecaducidad.setSelectedItem(resultBusqueda.getString(6));
                        resultado++;
                    }
                    if (resultado>0){
                        JOptionPane.showMessageDialog(null,"Prodcuto encontrado");
                    }else{
                        JOptionPane.showMessageDialog(null,"Prodcuto NO encontrado");
                    }
                    con.close();
                }catch(HeadlessException | SQLException f){
                    System.err.println(f);
                }
            }
        });
        //Boton Actualizar
        JBactualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resultado=0;
                String id = JTFcodigo.getText();
                try{
                    //90876
                    con = conectarBD.estbConexion();
                    PreparedStatement ps = con.prepareStatement("update PRODUCTOS set NOMCOMER=?, NOMGEN=?, STOCK=?, RECETA=?, CADUC=? where CODPROD="+id);
                    ps.setString(1, JTFnomcomercial.getText());
                    ps.setString(2, JTFnomgenerico.getText());
                    ps.setString(3, JTFstock.getText());
                    ps.setString(4, JCBreceta.getSelectedItem().toString());
                    ps.setString(5, JCBdatecaducidad.getSelectedItem().toString());
                    resultado=ps.executeUpdate();
                    if(resultado>0){
                        JOptionPane.showMessageDialog(null,"Producto actualizado");
                    }else{
                        JOptionPane.showMessageDialog(null, "Producto no actualizado");
                    }
                    con.close();
                }catch (HeadlessException | SQLException f){
                    System.err.println(f);
                }
            }
        });
        JBeliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id= JTFcodigo.getText();
                int resultado=0;
                try{
                    con=conectarBD.estbConexion();
                    PreparedStatement eliminar=con.prepareStatement("Delete from PRODUCTOS where CODPROD="+id);
                    resultado=eliminar.executeUpdate();
                    if(resultado>0){
                        JOptionPane.showMessageDialog(null,"Producto eliminado");
                    }else{
                        JOptionPane.showMessageDialog(null,"Producto no eliminado");
                    }
                    con.close();
                    limpiarCampos();
                }catch(HeadlessException | SQLException f){
                    System.err.println(f);
                }
            }
        });
    }

    @Override
    public void abrirVentana(){
        CRUD.setContentPane(new GUIFarmacia().JPfarmacia);
        CRUD.setDefaultCloseOperation(CRUD.EXIT_ON_CLOSE);
        CRUD.pack();
        CRUD.setLocationRelativeTo(null);
        CRUD.setResizable(false);
        CRUD.setVisible(true);
    }
    @Override
    public void cerrarVentana(){
        CRUD.setVisible(false);
    }
    public void limpiarCampos(){
        JTFcodigo.setText("");
        JTFnomcomercial.setText("");
        JTFnomgenerico.setText("");
        JTFstock.setText("");
    }
}
