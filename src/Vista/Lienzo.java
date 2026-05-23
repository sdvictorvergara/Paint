package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class Lienzo extends JPanel {

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
        herramientaLapiz = new Lapiz();
        herramientaPunto = new Punto();
        herramientaRecta = new Recta();
        herramientaCirculo = new Circulo();
        herramientaPoligonoRegular = new PoligonoRegular();
        herramientaPoligonoIrregular = new PoligonoIrregular();
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);

        herramientaLapiz.dibujar(grafico);
        herramientaPunto.dibujar(grafico);
        herramientaRecta.dibujar(grafico);
        herramientaCirculo.dibujar(grafico);
        herramientaPoligonoRegular.dibujar(grafico);
        herramientaPoligonoIrregular.dibujar(grafico);
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