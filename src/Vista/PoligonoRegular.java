package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class PoligonoRegular implements Herramienta {

    private Lienzo lienzo;
    private boolean esperandoPrimerClick = true;
    private int coordenadaXCentro, coordenadaYCentro;

    private int numeroLados = 5;
    private Color colorActualBorde = Color.BLACK;
    private Color colorActualRelleno = Color.WHITE;
    private boolean tieneRelleno = false;

    public PoligonoRegular(Lienzo lienzo) {
        this.lienzo = lienzo;
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

            Figura poligono = new Figura("POLIGONO_REGULAR");

            poligono.xCentro = coordenadaXCentro;
            poligono.yCentro = coordenadaYCentro;
            poligono.radio = radioCalculado;
            poligono.lados = numeroLados;
            poligono.anguloInicial = anguloInicialCalculado;
            poligono.colorBorde = colorActualBorde;
            poligono.colorRelleno = colorActualRelleno;
            poligono.relleno = tieneRelleno;

            lienzo.agregarFigura(poligono);
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

    public void setLados(int nuevoNumeroLados) {
        this.numeroLados = nuevoNumeroLados;
    }
}
