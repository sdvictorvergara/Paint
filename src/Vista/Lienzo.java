package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Lienzo extends JPanel {

    private ArrayList<Figura> listaFiguras = new ArrayList<>();

    private Lapiz herramientaLapiz;
    private Punto herramientaPunto;
    private Recta herramientaRecta;
    private Circulo herramientaCirculo;
    private PoligonoRegular herramientaPoligonoRegular;
    private PoligonoIrregular herramientaPoligonoIrregular;

    private Herramienta herramientaActual;

    public Lienzo() {
        setBackground(Color.WHITE);
        crearHerramientas();
        herramientaActual = herramientaLapiz;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent eventoMouse) {
                herramientaActual.mousePressed(eventoMouse);
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent eventoMouse) {
                herramientaActual.mouseDragged(eventoMouse);
                repaint();
            }
        });
    }

    private void crearHerramientas() {
        herramientaLapiz = new Lapiz(this);
        herramientaPunto = new Punto(this);
        herramientaRecta = new Recta(this);
        herramientaCirculo = new Circulo(this);
        herramientaPoligonoRegular = new PoligonoRegular(this);
        herramientaPoligonoIrregular = new PoligonoIrregular(this);
    }

    public void agregarFigura(Figura nuevaFigura) {
        nuevaFigura.orden = listaFiguras.size();
        listaFiguras.add(nuevaFigura);
    }

    public ArrayList<Figura> getListaFiguras() {
        return listaFiguras;
    }

    public void cargarFiguras(ArrayList<Figura> figurasCargadas) {
        listaFiguras.clear();
        listaFiguras.addAll(figurasCargadas);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);
        for (Figura figura : listaFiguras) {
            dibujarFigura(grafico, figura);
        }

        herramientaActual.dibujar(grafico);
    }

    private void dibujarFigura(Graphics grafico, Figura figura) {
        switch (figura.tipo) {
            case "PUNTO", "LAPIZ" -> {
                grafico.setColor(figura.colorBorde);
                grafico.fillOval(figura.x, figura.y, 5, 5);
            }
            case "RECTA" -> {
                grafico.setColor(figura.colorBorde);
                grafico.drawLine(figura.x1, figura.y1, figura.x2, figura.y2);
            }
            case "CIRCULO" -> {
                int posicionX = figura.xCentro - figura.radio;
                int posicionY = figura.yCentro - figura.radio;
                int diametro = figura.radio * 2;

                if (figura.relleno) {
                    grafico.setColor(figura.colorRelleno);
                    grafico.fillOval(posicionX, posicionY, diametro, diametro);
                }

                grafico.setColor(figura.colorBorde);
                grafico.drawOval(posicionX, posicionY, diametro, diametro);
            }
            case "POLIGONO_REGULAR" -> {
                Polygon poligono = crearPoligonoRegular(figura);

                if (figura.relleno) {
                    grafico.setColor(figura.colorRelleno);
                    grafico.fillPolygon(poligono);
                }

                grafico.setColor(figura.colorBorde);
                grafico.drawPolygon(poligono);
            }
            case "POLIGONO_IRREGULAR" -> {
                Polygon poligono = new Polygon();

                for (int[] vertice : figura.vertices) {
                    poligono.addPoint(vertice[0], vertice[1]);
                }

                if (figura.relleno) {
                    grafico.setColor(figura.colorRelleno);
                    grafico.fillPolygon(poligono);
                }

                grafico.setColor(figura.colorBorde);
                grafico.drawPolygon(poligono);
            }
        }
    }

    private Polygon crearPoligonoRegular(Figura figura) {
        Polygon poligono = new Polygon();

        for (int indice = 0; indice < figura.lados; indice++) {
            double angulo = figura.anguloInicial + indice * 2 * Math.PI / figura.lados;
            int xVertice = (int) Math.round(figura.xCentro + figura.radio * Math.cos(angulo));
            int yVertice = (int) Math.round(figura.yCentro + figura.radio * Math.sin(angulo));
            poligono.addPoint(xVertice, yVertice);
        }

        return poligono;
    }

    public String generarSVG() {
        StringBuilder svg = new StringBuilder();

        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"")
                .append(getWidth()).append("\" height=\"").append(getHeight()).append("\">\n");
        svg.append("<rect width=\"100%\" height=\"100%\" fill=\"white\"/>\n");

        for (Figura figura : listaFiguras) {
            svg.append(figuraComoSVG(figura));
        }

        svg.append("</svg>");
        return svg.toString();
    }

    private String figuraComoSVG(Figura figura) {
        String borde = colorHex(figura.colorBorde);
        String rellenoSVG = figura.relleno ? colorHex(figura.colorRelleno) : "none";

        return switch (figura.tipo) {
            case "PUNTO", "LAPIZ" -> "<circle cx=\"" + (figura.x + 2) + "\" cy=\"" + (figura.y + 2)
                    + "\" r=\"2.5\" fill=\"" + borde + "\" />\n";

            case "RECTA" -> "<line x1=\"" + figura.x1 + "\" y1=\"" + figura.y1 + "\" x2=\"" + figura.x2
                    + "\" y2=\"" + figura.y2 + "\" stroke=\"" + borde + "\" stroke-width=\"2\" />\n";

            case "CIRCULO" -> "<circle cx=\"" + figura.xCentro + "\" cy=\"" + figura.yCentro + "\" r=\""
                    + figura.radio + "\" stroke=\"" + borde + "\" fill=\"" + rellenoSVG + "\" stroke-width=\"2\" />\n";

            case "POLIGONO_REGULAR" -> "<polygon points=\"" + puntosPoligonoRegular(figura)
                    + "\" stroke=\"" + borde + "\" fill=\"" + rellenoSVG + "\" stroke-width=\"2\" />\n";

            case "POLIGONO_IRREGULAR" -> "<polygon points=\"" + puntosPoligonoIrregular(figura)
                    + "\" stroke=\"" + borde + "\" fill=\"" + rellenoSVG + "\" stroke-width=\"2\" />\n";

            default -> "";
        };
    }

    private String puntosPoligonoRegular(Figura figura) {
        StringBuilder puntos = new StringBuilder();

        for (int indice = 0; indice < figura.lados; indice++) {
            double angulo = figura.anguloInicial + indice * 2 * Math.PI / figura.lados;
            int xVertice = (int) Math.round(figura.xCentro + figura.radio * Math.cos(angulo));
            int yVertice = (int) Math.round(figura.yCentro + figura.radio * Math.sin(angulo));
            puntos.append(xVertice).append(",").append(yVertice).append(" ");
        }

        return puntos.toString().trim();
    }

    private String puntosPoligonoIrregular(Figura figura) {
        StringBuilder puntos = new StringBuilder();

        for (int[] vertice : figura.vertices) {
            puntos.append(vertice[0]).append(",").append(vertice[1]).append(" ");
        }

        return puntos.toString().trim();
    }

    private String colorHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public void usarLapiz() {
        herramientaActual = herramientaLapiz;
    }

    public void usarPunto() {
        herramientaActual = herramientaPunto;
    }

    public void usarRecta() {
        herramientaActual = herramientaRecta;
    }

    public void usarCirculo() {
        herramientaActual = herramientaCirculo;
    }

    public void usarPoligonoRegular() {
        herramientaActual = herramientaPoligonoRegular;
    }

    public void usarPoligonoIrregular() {
        herramientaActual = herramientaPoligonoIrregular;
    }

    public void setColorActual(Color nuevoColor) {
        herramientaLapiz.setColor(nuevoColor);
        herramientaPunto.setColor(nuevoColor);
        herramientaRecta.setColor(nuevoColor);

        herramientaCirculo.setColor(nuevoColor);
        herramientaCirculo.setColorRelleno(nuevoColor);

        herramientaPoligonoRegular.setColor(nuevoColor);
        herramientaPoligonoRegular.setColorRelleno(nuevoColor);

        herramientaPoligonoIrregular.setColor(nuevoColor);
        herramientaPoligonoIrregular.setColorRelleno(nuevoColor);
    }

    public void setRellenoCirculo(boolean activarRelleno) {
        herramientaCirculo.setRelleno(activarRelleno);
    }

    public void setRellenoPoligono(boolean activarRelleno) {
        herramientaPoligonoRegular.setRelleno(activarRelleno);
        herramientaPoligonoIrregular.setRelleno(activarRelleno);
    }

    public void setLadosPoligono(int numeroLados) {
        herramientaPoligonoRegular.setLados(numeroLados);
        herramientaPoligonoIrregular.setLados(numeroLados);
    }

    public void nuevoDibujo() {
        listaFiguras.clear();
        crearHerramientas();
        herramientaActual = herramientaLapiz;
        repaint();
    }

    public Lapiz getLapiz() {
        return herramientaLapiz;
    }

    public Punto getPunto() {
        return herramientaPunto;
    }

    public Recta getRecta() {
        return herramientaRecta;
    }

    public Circulo getCirculo() {
        return herramientaCirculo;
    }

    public PoligonoRegular getPoligonoRegular() {
        return herramientaPoligonoRegular;
    }

    public PoligonoIrregular getPoligonoIrregular() {
        return herramientaPoligonoIrregular;
    }
}
