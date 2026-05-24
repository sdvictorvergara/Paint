package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class Recta implements Herramienta {

    private Lienzo lienzo;
    private boolean esperandoPrimerClick = true;
    private int coordenadaXInicio, coordenadaYInicio;
    private Color colorActual = Color.BLACK;

    public Recta(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    @Override
    public void mousePressed(MouseEvent eventoMouse) {
        if (esperandoPrimerClick) {
            coordenadaXInicio = eventoMouse.getX();
            coordenadaYInicio = eventoMouse.getY();
            esperandoPrimerClick = false;
        } else {
            Figura recta = new Figura("RECTA");

            recta.x1 = coordenadaXInicio;
            recta.y1 = coordenadaYInicio;
            recta.x2 = eventoMouse.getX();
            recta.y2 = eventoMouse.getY();
            recta.colorBorde = colorActual;

            lienzo.agregarFigura(recta);
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
    public void setColor(Color nuevoColor) {
        this.colorActual = nuevoColor;
    }
}
