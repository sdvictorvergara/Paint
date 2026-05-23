package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Recta implements Herramienta {

    private ArrayList<DatosRecta> listaRectas = new ArrayList<>();

    private boolean esperandoPrimerClick = true;

    private int coordenadaXInicio;
    private int coordenadaYInicio;

    private Color colorActual = Color.BLACK;

    @Override
    public void mousePressed(MouseEvent eventoMouse) {

        if (esperandoPrimerClick) {

            coordenadaXInicio =
                    eventoMouse.getX();

            coordenadaYInicio =
                    eventoMouse.getY();

            esperandoPrimerClick =
                    false;

        } else {

            listaRectas.add(

                    new DatosRecta(

                            coordenadaXInicio,
                            coordenadaYInicio,

                            eventoMouse.getX(),
                            eventoMouse.getY(),

                            colorActual
                    )
            );

            esperandoPrimerClick =
                    true;
        }
    }

    @Override
    public void mouseDragged(
            MouseEvent eventoMouse
    ) {
    }

    @Override
    public void dibujar(
            Graphics grafico
    ) {

        for (DatosRecta datosRecta :
                listaRectas) {

            grafico.setColor(
                    datosRecta.colorLinea
            );

            grafico.drawLine(

                    datosRecta.coordenadaXInicio,
                    datosRecta.coordenadaYInicio,

                    datosRecta.coordenadaXFin,
                    datosRecta.coordenadaYFin
            );
        }
    }

    @Override
    public void setColor(
            Color nuevoColor
    ) {

        this.colorActual =
                nuevoColor;
    }

    private class DatosRecta {

        int coordenadaXInicio;
        int coordenadaYInicio;

        int coordenadaXFin;
        int coordenadaYFin;

        Color colorLinea;

        public DatosRecta(

                int coordenadaXInicio,
                int coordenadaYInicio,

                int coordenadaXFin,
                int coordenadaYFin,

                Color colorLinea

        ) {

            this.coordenadaXInicio =
                    coordenadaXInicio;

            this.coordenadaYInicio =
                    coordenadaYInicio;

            this.coordenadaXFin =
                    coordenadaXFin;

            this.coordenadaYFin =
                    coordenadaYFin;

            this.colorLinea =
                    colorLinea;
        }
    }
}