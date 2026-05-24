package Vista;

import java.awt.Color;
import java.util.ArrayList;

public class Figura {

    public String tipo;
    public int orden;

    public int x, y;
    public int x1, y1, x2, y2;
    public int xCentro, yCentro, radio;
    public int lados;
    public double anguloInicial;

    public Color colorBorde;
    public Color colorRelleno;
    public boolean relleno;

    public ArrayList<int[]> vertices = new ArrayList<>();

    public Figura(String tipo) {
        this.tipo = tipo;
        this.colorBorde = Color.BLACK;
        this.colorRelleno = Color.WHITE;
        this.relleno = false;
    }
}
