package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class Punto implements Herramienta {

    private Lienzo lienzo;
    private Color colorActual = Color.BLACK;

    public Punto(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    @Override
    public void mousePressed(MouseEvent eventoMouse) {
        Figura punto = new Figura("PUNTO");

        punto.x = eventoMouse.getX();
        punto.y = eventoMouse.getY();
        punto.colorBorde = colorActual;

        lienzo.agregarFigura(punto);
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
