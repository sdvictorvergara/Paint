package Controlador;

import Modelo.Modelo;
import Vista.VentanaPrincipal;

public class Controlador {
    private Modelo modelo;
    private VentanaPrincipal vista;
    
    public Controlador(Modelo modelo, VentanaPrincipal vista){
        this.modelo = modelo;
        this.vista = vista;
        iniciarEventos();
    }
    
    
    private void iniciarEventos(){
        vista.getGuardar().addActionListener(e ->{modelo.guardarDibujo("Mi dibujo");});
    
        vista.getCargar().addActionListener(e -> {modelo.cargarDibujos();});
        
        vista.getModificar().addActionListener(e -> {modelo.modificarDibujo(1, "Nuevo nombre");});

        vista.getEliminar().addActionListener(e -> {modelo.borrarDibujo(1);});

        //vista.getExportar().addActionListener(e -> {modelo.exportarBaseDatos();});
        
    }
    
    
    
    
    
}