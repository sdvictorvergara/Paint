package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PoligonoIrregular implements Herramienta {

    private Lienzo lienzo;
    private ArrayList<int[]> listaPuntosActuales = new ArrayList<>();

    private int numeroLados = 5;
    private Color colorActualBorde = Color.BLACK;
    private Color colorActualRelleno = Color.WHITE;
    private boolean tieneRelleno = false;

    public PoligonoIrregular(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    @Override
    public void mousePressed(MouseEvent eventoMouse) {
        int nuevaCoordenadaX = eventoMouse.getX();
        int nuevaCoordenadaY = eventoMouse.getY();

        if (listaPuntosActuales.size() >= 2) {
            int[] ultimoPunto = listaPuntosActuales.get(listaPuntosActuales.size() - 1);

            if (existeCruceLados(ultimoPunto[0], ultimoPunto[1], nuevaCoordenadaX, nuevaCoordenadaY)) {
                System.out.println("Ese lado cruza otro lado. Punto rechazado.");
                return;
            }
        }

        listaPuntosActuales.add(new int[]{nuevaCoordenadaX, nuevaCoordenadaY});

        if (listaPuntosActuales.size() == numeroLados) {
            int[] ultimoPunto = listaPuntosActuales.get(listaPuntosActuales.size() - 1);
            int[] primerPunto = listaPuntosActuales.get(0);

            if (existeCruceCierre(ultimoPunto[0], ultimoPunto[1], primerPunto[0], primerPunto[1])) {
                System.out.println("El cierre cruza otro lado. Último punto rechazado.");
                listaPuntosActuales.remove(listaPuntosActuales.size() - 1);
                return;
            }

            Figura poligono = new Figura("POLIGONO_IRREGULAR");

            poligono.colorBorde = colorActualBorde;
            poligono.colorRelleno = colorActualRelleno;
            poligono.relleno = tieneRelleno;

            for (int[] punto : listaPuntosActuales) {
                poligono.vertices.add(new int[]{punto[0], punto[1]});
            }

            lienzo.agregarFigura(poligono);
            listaPuntosActuales.clear();
        }
    }

    @Override
    public void mouseDragged(MouseEvent eventoMouse) {
    }

    @Override
    public void dibujar(Graphics grafico) {
        grafico.setColor(colorActualBorde);

        for (int[] punto : listaPuntosActuales) {
            grafico.fillOval(punto[0] - 3, punto[1] - 3, 6, 6);
        }

        for (int indice = 0; indice < listaPuntosActuales.size() - 1; indice++) {
            int[] punto1 = listaPuntosActuales.get(indice);
            int[] punto2 = listaPuntosActuales.get(indice + 1);
            grafico.drawLine(punto1[0], punto1[1], punto2[0], punto2[1]);
        }
    }

    @Override
    public void setColor(Color nuevoColorBorde) {
        this.colorActualBorde = nuevoColorBorde;
    }

    public void setColorRelleno(Color nuevoColorRelleno) {
        this.colorActualRelleno = nuevoColorRelleno;
    }

    public void setRelleno(boolean activarRelleno) {
        this.tieneRelleno = activarRelleno;
    }

    public void setLados(int nuevoNumeroLados) {
        this.numeroLados = nuevoNumeroLados;
        listaPuntosActuales.clear();
    }

    private boolean existeCruceLados(int xInicio, int yInicio, int xFin, int yFin) {
        for (int indice = 0; indice < listaPuntosActuales.size() - 2; indice++) {
            int[] punto1 = listaPuntosActuales.get(indice);
            int[] punto2 = listaPuntosActuales.get(indice + 1);

            if (lineasSeCruzan(xInicio, yInicio, xFin, yFin, punto1[0], punto1[1], punto2[0], punto2[1])) {
                return true;
            }
        }

        return false;
    }

    private boolean existeCruceCierre(int xInicio, int yInicio, int xFin, int yFin) {
        for (int indice = 1; indice < listaPuntosActuales.size() - 2; indice++) {
            int[] punto1 = listaPuntosActuales.get(indice);
            int[] punto2 = listaPuntosActuales.get(indice + 1);

            if (lineasSeCruzan(xInicio, yInicio, xFin, yFin, punto1[0], punto1[1], punto2[0], punto2[1])) {
                return true;
            }
        }

        return false;
    }

    private boolean lineasSeCruzan(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int direccion1 = calcularDireccion(x3, y3, x4, y4, x1, y1);
        int direccion2 = calcularDireccion(x3, y3, x4, y4, x2, y2);
        int direccion3 = calcularDireccion(x1, y1, x2, y2, x3, y3);
        int direccion4 = calcularDireccion(x1, y1, x2, y2, x4, y4);

        return direccion1 * direccion2 < 0 && direccion3 * direccion4 < 0;
    }

    private int calcularDireccion(int ax, int ay, int bx, int by, int cx, int cy) {
        return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
    }
}
