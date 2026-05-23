package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PoligonoIrregular implements Herramienta {

    private ArrayList<DatosPoligono> listaPoligonos;
    private ArrayList<int[]> listaPuntosActuales;

    private int numeroLados;

    private Color colorActualBorde;
    private Color colorActualRelleno;

    private boolean tieneRelleno;

    public PoligonoIrregular() {

        listaPoligonos = new ArrayList<>();
        listaPuntosActuales = new ArrayList<>();

        numeroLados = 5;

        colorActualBorde = Color.BLACK;
        colorActualRelleno = Color.WHITE;

        tieneRelleno = false;
    }

    @Override
    public void mousePressed(MouseEvent eventoMouse) {

        int nuevaCoordenadaX = eventoMouse.getX();
        int nuevaCoordenadaY = eventoMouse.getY();

        if (listaPuntosActuales.size() >= 2) {

            int[] ultimoPunto =
                    listaPuntosActuales.get(
                            listaPuntosActuales.size() - 1
                    );

            if (existeCruceLados(
                    ultimoPunto[0],
                    ultimoPunto[1],

                    nuevaCoordenadaX,
                    nuevaCoordenadaY
            )) {

                System.out.println(
                        "Ese lado cruza otro"
                );

                return;
            }
        }

        listaPuntosActuales.add(
                new int[]{
                        nuevaCoordenadaX,
                        nuevaCoordenadaY
                }
        );

        if (listaPuntosActuales.size()
                == numeroLados) {

            int[] ultimoPunto =
                    listaPuntosActuales.get(
                            listaPuntosActuales.size()-1
                    );

            int[] primerPunto =
                    listaPuntosActuales.get(0);

            if (existeCruceCierre(

                    ultimoPunto[0],
                    ultimoPunto[1],

                    primerPunto[0],
                    primerPunto[1]

            )) {

                listaPuntosActuales.remove(
                        listaPuntosActuales.size()-1
                );

                return;
            }

            Polygon poligono =
                    new Polygon();

            for (int[] punto :
                    listaPuntosActuales) {

                poligono.addPoint(
                        punto[0],
                        punto[1]
                );
            }

            listaPoligonos.add(

                    new DatosPoligono(

                            poligono,

                            colorActualBorde,
                            colorActualRelleno,

                            tieneRelleno
                    )
            );

            listaPuntosActuales.clear();
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

        for (DatosPoligono datos :
                listaPoligonos) {

            if (datos.estaRelleno) {

                grafico.setColor(
                        datos.colorRelleno
                );

                grafico.fillPolygon(
                        datos.poligono
                );
            }

            grafico.setColor(
                    datos.colorBorde
            );

            grafico.drawPolygon(
                    datos.poligono
            );
        }

        grafico.setColor(
                colorActualBorde
        );

        for (int[] punto :
                listaPuntosActuales) {

            grafico.fillOval(
                    punto[0]-3,
                    punto[1]-3,

                    6,
                    6
            );
        }

        for (int i = 0;
             i < listaPuntosActuales.size()-1;
             i++) {

            int[] punto1 =
                    listaPuntosActuales.get(i);

            int[] punto2 =
                    listaPuntosActuales.get(i+1);

            grafico.drawLine(

                    punto1[0],
                    punto1[1],

                    punto2[0],
                    punto2[1]
            );
        }
    }

    @Override
    public void setColor(
            Color nuevoColor
    ) {

        this.colorActualBorde =
                nuevoColor;
    }

    public void setColorRelleno(
            Color nuevoColor
    ) {

        this.colorActualRelleno =
                nuevoColor;
    }

    public void setRelleno(
            boolean activarRelleno
    ) {

        this.tieneRelleno =
                activarRelleno;
    }

    public void setLados(
            int numeroLados
    ) {

        this.numeroLados =
                numeroLados;

        listaPuntosActuales.clear();
    }

    private boolean existeCruceLados(

            int xInicio,
            int yInicio,

            int xFin,
            int yFin

    ) {

        for (int i = 0;
             i < listaPuntosActuales.size()-2;
             i++) {

            int[] punto1 =
                    listaPuntosActuales.get(i);

            int[] punto2 =
                    listaPuntosActuales.get(i+1);

            if (lineasSeCruzan(

                    xInicio,
                    yInicio,

                    xFin,
                    yFin,

                    punto1[0],
                    punto1[1],

                    punto2[0],
                    punto2[1]

            )) {

                return true;
            }
        }

        return false;
    }

    private boolean existeCruceCierre(

            int xInicio,
            int yInicio,

            int xFin,
            int yFin

    ) {

        for (int i = 1;
             i < listaPuntosActuales.size()-2;
             i++) {

            int[] punto1 =
                    listaPuntosActuales.get(i);

            int[] punto2 =
                    listaPuntosActuales.get(i+1);

            if (lineasSeCruzan(

                    xInicio,
                    yInicio,

                    xFin,
                    yFin,

                    punto1[0],
                    punto1[1],

                    punto2[0],
                    punto2[1]

            )) {

                return true;
            }
        }

        return false;
    }

    private boolean lineasSeCruzan(

            int x1, int y1,
            int x2, int y2,

            int x3, int y3,
            int x4, int y4

    ) {

        int direccion1 =
                calcularDireccion(
                        x3,y3,
                        x4,y4,

                        x1,y1
                );

        int direccion2 =
                calcularDireccion(
                        x3,y3,
                        x4,y4,

                        x2,y2
                );

        int direccion3 =
                calcularDireccion(
                        x1,y1,
                        x2,y2,

                        x3,y3
                );

        int direccion4 =
                calcularDireccion(
                        x1,y1,
                        x2,y2,

                        x4,y4
                );

        return direccion1
                * direccion2 < 0

                &&

                direccion3
                * direccion4 < 0;
    }

    private int calcularDireccion(

            int ax,
            int ay,

            int bx,
            int by,

            int cx,
            int cy

    ) {

        return (bx-ax)
                * (cy-ay)

                -

                (by-ay)
                * (cx-ax);
    }

    private class DatosPoligono {

        Polygon poligono;

        Color colorBorde;
        Color colorRelleno;

        boolean estaRelleno;

        public DatosPoligono(

                Polygon poligono,

                Color colorBorde,
                Color colorRelleno,

                boolean estaRelleno

        ) {

            this.poligono =
                    poligono;

            this.colorBorde =
                    colorBorde;

            this.colorRelleno =
                    colorRelleno;

            this.estaRelleno =
                    estaRelleno;
        }
    }
}