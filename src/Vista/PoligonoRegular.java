package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PoligonoRegular implements Herramienta {

    private ArrayList<DatosPoligono> listaPoligonos;
    private boolean esperandoPrimerClick;
    private int coordenadaXCentro, coordenadaYCentro;

    private int numeroLados;
    private Color colorActualBorde;
    private Color colorActualRelleno;
    private boolean tieneRelleno;

    public PoligonoRegular() {
        listaPoligonos = new ArrayList<>();
        esperandoPrimerClick = true;
        numeroLados = 5;
        colorActualBorde = Color.BLACK;
        colorActualRelleno = Color.WHITE;
        tieneRelleno = false;
    }

    @Override
    public void mousePressed(MouseEvent eventoMouse) {

        if (esperandoPrimerClick) {
            coordenadaXCentro = eventoMouse.getX();
            coordenadaYCentro = eventoMouse.getY();
            esperandoPrimerClick = false;

        } else {
            int coordenadaXVertice = eventoMouse.getX();
            int coordenadaYVertice = eventoMouse.getY();

            int radioCalculado = (int) Math.sqrt(
                    Math.pow(coordenadaXVertice - coordenadaXCentro, 2)
                    + Math.pow(coordenadaYVertice - coordenadaYCentro, 2)
            );

            double anguloInicialCalculado = Math.atan2(
                    coordenadaYVertice - coordenadaYCentro,
                    coordenadaXVertice - coordenadaXCentro
            );

            listaPoligonos.add(new DatosPoligono(
                    coordenadaXCentro,
                    coordenadaYCentro,
                    radioCalculado,
                    numeroLados,
                    anguloInicialCalculado,
                    colorActualBorde,
                    colorActualRelleno,
                    tieneRelleno
            ));

            esperandoPrimerClick = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent eventoMouse) {
    }

    @Override
    public void dibujar(Graphics grafico) {

        for (DatosPoligono datosPoligono : listaPoligonos) {

            Polygon poligonoDibujado = new Polygon();

            for (int indiceLado = 0; indiceLado < datosPoligono.numeroLados; indiceLado++) {
                double anguloVertice = datosPoligono.anguloInicial + indiceLado * 2 * Math.PI / datosPoligono.numeroLados;

                int coordenadaXVertice = (int) Math.round(
                        datosPoligono.centroX + datosPoligono.radio * Math.cos(anguloVertice)
                );

                int coordenadaYVertice = (int) Math.round(
                        datosPoligono.centroY + datosPoligono.radio * Math.sin(anguloVertice)
                );

                poligonoDibujado.addPoint(coordenadaXVertice, coordenadaYVertice);
            }

            if (datosPoligono.estaRelleno) {
                grafico.setColor(datosPoligono.colorRelleno);
                grafico.fillPolygon(poligonoDibujado);
            }

            grafico.setColor(datosPoligono.colorBorde);
            grafico.drawPolygon(poligonoDibujado);
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
    }

    private class DatosPoligono {

        int centroX, centroY;
        int radio;
        int numeroLados;
        double anguloInicial;

        Color colorBorde;
        Color colorRelleno;
        boolean estaRelleno;

        public DatosPoligono(int centroX, int centroY, int radio, int numeroLados,
                             double anguloInicial, Color colorBorde,
                             Color colorRelleno, boolean estaRelleno) {

            this.centroX = centroX;
            this.centroY = centroY;
            this.radio = radio;
            this.numeroLados = numeroLados;
            this.anguloInicial = anguloInicial;
            this.colorBorde = colorBorde;
            this.colorRelleno = colorRelleno;
            this.estaRelleno = estaRelleno;
        }
    }
}