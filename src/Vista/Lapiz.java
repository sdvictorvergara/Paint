package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Lapiz implements Herramienta {

    private ArrayList<DatosTrazo> listaPuntos = new ArrayList<>();

    private Color colorActual = Color.BLACK;

    @Override
    public void mousePressed(MouseEvent eventoMouse) {
    }

    @Override
    public void mouseDragged(MouseEvent eventoMouse) {

        listaPuntos.add(
                new DatosTrazo(
                        eventoMouse.getX(),
                        eventoMouse.getY(),
                        colorActual
                )
        );
    }

    @Override
    public void dibujar(Graphics grafico) {

        for (DatosTrazo datosPunto : listaPuntos) {

            grafico.setColor(
                    datosPunto.colorTrazo
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
    public void setColor(Color nuevoColor) {

        this.colorActual =
                nuevoColor;
    }

    private class DatosTrazo {

        int coordenadaX;
        int coordenadaY;

        Color colorTrazo;

        public DatosTrazo(

                int coordenadaX,
                int coordenadaY,

                Color colorTrazo
        ) {

            this.coordenadaX =
                    coordenadaX;

            this.coordenadaY =
                    coordenadaY;

            this.colorTrazo =
                    colorTrazo;
        }
    }
}