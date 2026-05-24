package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class Circulo implements Herramienta {

    private Lienzo lienzo;
    private boolean esperandoPrimerClick = true;
    private int coordenadaXCentro, coordenadaYCentro;

    private Color colorActualBorde = Color.BLACK;
    private Color colorActualRelleno = Color.WHITE;
    private boolean tieneRelleno = false;

    public Circulo(Lienzo lienzo) {
        this.lienzo = lienzo;
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

            Figura circulo = new Figura("CIRCULO");

            circulo.xCentro = coordenadaXCentro;
            circulo.yCentro = coordenadaYCentro;
            circulo.radio = radioCalculado;
            circulo.colorBorde = colorActualBorde;
            circulo.colorRelleno = colorActualRelleno;
            circulo.relleno = tieneRelleno;

            lienzo.agregarFigura(circulo);
            esperandoPrimerClick = true;
        }
    }

    @Override
    public void mouseDragged(MouseEvent eventoMouse) {
    }

    @Override
    public void dibujar(Graphics grafico) {
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
}
