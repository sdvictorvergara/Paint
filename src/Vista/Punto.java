package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Punto implements Herramienta {

    private ArrayList<DatosPunto> listaPuntos;
    private Color colorActual = Color.BLACK;

    public Punto() {
        listaPuntos = new ArrayList<>();
    }

    @Override
    public void mousePressed(MouseEvent eventoMouse) {

        listaPuntos.add(

                new DatosPunto(

                        eventoMouse.getX(),
                        eventoMouse.getY(),

                        colorActual
                )
        );
    }

    @Override
    public void dibujar(Graphics grafico) {

        for (DatosPunto datosPunto : listaPuntos) {

            grafico.setColor(
                    datosPunto.colorPunto
            );

            grafico.fillOval(

                    datosPunto.coordenadaX,
                    datosPunto.coordenadaY,

                    5,
                    5
            );
        }
    }

    @Override
    public void mouseDragged(
            MouseEvent eventoMouse
    ) {
    }

    @Override
    public void setColor(
            Color nuevoColor
    ) {

        this.colorActual =
                nuevoColor;
    }

    private class DatosPunto {

        int coordenadaX;
        int coordenadaY;

        Color colorPunto;

        public DatosPunto(

                int coordenadaX,
                int coordenadaY,

                Color colorPunto

        ) {

            this.coordenadaX =
                    coordenadaX;

            this.coordenadaY =
                    coordenadaY;

            this.colorPunto =
                    colorPunto;
        }
    }
}