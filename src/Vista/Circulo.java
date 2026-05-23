package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Circulo implements Herramienta {

    private ArrayList<DatosCirculo> listaCirculos;
    private boolean esperandoPrimerClick;
    private int coordenadaXCentro, coordenadaYCentro;

    private Color colorActualBorde;
    private Color colorActualRelleno;

    private boolean tieneRelleno;

    public Circulo() {
        listaCirculos = new ArrayList<>();
        esperandoPrimerClick = true;

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

            int radioCalculado = (int) Math.sqrt(
                    Math.pow(eventoMouse.getX() - coordenadaXCentro, 2)
                    + Math.pow(eventoMouse.getY() - coordenadaYCentro, 2)
            );

            listaCirculos.add(
                    new DatosCirculo(
                            coordenadaXCentro,
                            coordenadaYCentro,
                            radioCalculado,
                            colorActualBorde,
                            colorActualRelleno,
                            tieneRelleno
                    )
            );

            esperandoPrimerClick = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent eventoMouse) {
    }

    @Override
    public void dibujar(Graphics grafico) {

        for (DatosCirculo datosCirculo : listaCirculos) {

            int posicionX = datosCirculo.centroX - datosCirculo.radio;
            int posicionY = datosCirculo.centroY - datosCirculo.radio;

            int diametro = datosCirculo.radio * 2;

            if (datosCirculo.estaRelleno) {
                grafico.setColor(datosCirculo.colorRelleno);
                grafico.fillOval(posicionX, posicionY, diametro, diametro);
            }

            grafico.setColor(datosCirculo.colorBorde);
            grafico.drawOval(posicionX, posicionY, diametro, diametro);
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

    private class DatosCirculo {

        int centroX, centroY;
        int radio;

        Color colorBorde;
        Color colorRelleno;

        boolean estaRelleno;

        public DatosCirculo(
                int centroX,
                int centroY,
                int radio,
                Color colorBorde,
                Color colorRelleno,
                boolean estaRelleno
        ) {

            this.centroX = centroX;
            this.centroY = centroY;

            this.radio = radio;

            this.colorBorde = colorBorde;
            this.colorRelleno = colorRelleno;

            this.estaRelleno = estaRelleno;
        }
    }
}