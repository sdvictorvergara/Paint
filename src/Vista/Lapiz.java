package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class Lapiz implements Herramienta {

    private Lienzo lienzo;
    private Color colorActual = Color.BLACK;

    public Lapiz(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    @Override
    public void mousePressed(MouseEvent eventoMouse) {
    }

    @Override
    public void mouseDragged(MouseEvent eventoMouse) {
        Figura puntoLapiz = new Figura("LAPIZ");

        puntoLapiz.x = eventoMouse.getX();
        puntoLapiz.y = eventoMouse.getY();
        puntoLapiz.colorBorde = colorActual;

        lienzo.agregarFigura(puntoLapiz);
    }

    @Override
    public void dibujar(Graphics grafico) {
    }

    @Override
    public void setColor(Color nuevoColor) {
        this.colorActual = nuevoColor;
    }
}
