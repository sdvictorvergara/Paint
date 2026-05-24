package Controlador;

import Modelo.Modelo;
import Vista.VentanaPrincipal;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Controlador {

    private Modelo modelo;
    private VentanaPrincipal vista;
    private int idDibujoActual = -1;

    public Controlador(Modelo modelo, VentanaPrincipal vista) {
        this.modelo = modelo;
        this.vista = vista;
        iniciarEventos();
    }

    private void iniciarEventos() {
        vista.getNuevo().addActionListener(e -> nuevoDibujo());
        vista.getGuardar().addActionListener(e -> guardarDibujo());
        vista.getCargar().addActionListener(e -> cargarDibujo());
        vista.getModificar().addActionListener(e -> modificarNombre());
        vista.getEliminar().addActionListener(e -> borrarDibujo());
        vista.getExportar().addActionListener(e -> exportarSVG());
    }

    private void nuevoDibujo() {
        vista.getLienzo().nuevoDibujo();
        idDibujoActual = -1;
    }

    private void guardarDibujo() {
        if (vista.getLienzo().getListaFiguras().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay figuras para guardar");
            return;
        }

        String nombreDibujo = JOptionPane.showInputDialog(vista, "Nombre del dibujo:");

        if (nombreDibujo == null || nombreDibujo.trim().isEmpty()) {
            return;
        }

        idDibujoActual = modelo.guardarDibujoCompleto(
                nombreDibujo.trim(),
                vista.getLienzo().getListaFiguras()
        );

        if (idDibujoActual != -1) {
            JOptionPane.showMessageDialog(vista, "Dibujo guardado correctamente con ID: " + idDibujoActual);
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar el dibujo");
        }
    }

    private void cargarDibujo() {
        ArrayList<String> dibujosGuardados = modelo.obtenerDibujosGuardados();

        if (dibujosGuardados.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay dibujos guardados");
            return;
        }

        String dibujoSeleccionado = (String) JOptionPane.showInputDialog(
                vista,
                "Selecciona un dibujo:",
                "Cargar dibujo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                dibujosGuardados.toArray(),
                dibujosGuardados.get(0)
        );

        if (dibujoSeleccionado == null) {
            return;
        }

        idDibujoActual = Integer.parseInt(dibujoSeleccionado.split(" - ")[0]);
        vista.getLienzo().cargarFiguras(modelo.cargarDibujoCompleto(idDibujoActual));

        JOptionPane.showMessageDialog(vista, "Dibujo cargado correctamente");
    }

    private void modificarNombre() {
        if (idDibujoActual == -1) {
            JOptionPane.showMessageDialog(vista, "Primero carga o guarda un dibujo");
            return;
        }

        String nuevoNombre = JOptionPane.showInputDialog(vista, "Nuevo nombre del dibujo:");

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return;
        }

        if (modelo.modificarNombreDibujo(idDibujoActual, nuevoNombre.trim())) {
            JOptionPane.showMessageDialog(vista, "Nombre modificado correctamente");
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo modificar el nombre");
        }
    }

    private void borrarDibujo() {
        if (idDibujoActual == -1) {
            cargarDibujoParaBorrar();
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                vista,
                "¿Seguro que quieres borrar el dibujo actual?",
                "Confirmar borrado",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (modelo.borrarDibujo(idDibujoActual)) {
            vista.getLienzo().nuevoDibujo();
            idDibujoActual = -1;
            JOptionPane.showMessageDialog(vista, "Dibujo borrado correctamente");
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo borrar el dibujo");
        }
    }

    private void cargarDibujoParaBorrar() {
        ArrayList<String> dibujosGuardados = modelo.obtenerDibujosGuardados();

        if (dibujosGuardados.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay dibujos guardados");
            return;
        }

        String dibujoSeleccionado = (String) JOptionPane.showInputDialog(
                vista,
                "Selecciona el dibujo que quieres borrar:",
                "Borrar dibujo",
                JOptionPane.WARNING_MESSAGE,
                null,
                dibujosGuardados.toArray(),
                dibujosGuardados.get(0)
        );

        if (dibujoSeleccionado == null) {
            return;
        }

        idDibujoActual = Integer.parseInt(dibujoSeleccionado.split(" - ")[0]);
        borrarDibujo();
    }

    private void exportarSVG() {
        if (vista.getLienzo().getListaFiguras().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay figuras para exportar");
            return;
        }

        JFileChooser selectorArchivo = new JFileChooser();
        selectorArchivo.setSelectedFile(new File("dibujo.svg"));

        int resultado = selectorArchivo.showSaveDialog(vista);

        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivoSVG = selectorArchivo.getSelectedFile();

        if (!archivoSVG.getName().toLowerCase().endsWith(".svg")) {
            archivoSVG = new File(archivoSVG.getAbsolutePath() + ".svg");
        }

        try (FileWriter escritor = new FileWriter(archivoSVG)) {
            escritor.write(vista.getLienzo().generarSVG());
            JOptionPane.showMessageDialog(vista, "SVG exportado correctamente");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(vista, "Error al exportar SVG: " + e.getMessage());
        }
    }
}
