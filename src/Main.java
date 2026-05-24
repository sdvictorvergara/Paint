import Controlador.Controlador;
import Modelo.Modelo;
import Vista.VentanaPrincipal;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author sdvictorvergara
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VentanaPrincipal vista = new VentanaPrincipal();
        Modelo modelo = new Modelo();
        Controlador controlador = new Controlador(modelo, vista);
        
        vista.setVisible(true);
        
    }
    
}