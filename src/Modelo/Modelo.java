package Modelo;

import Vista.Figura;
import java.awt.Color;
import java.sql.*;
import java.util.ArrayList;

public final class Modelo {

    private Connection conexion;
    private final String urlBaseDatos = "jdbc:sqlite:paint.db";

    public Modelo() {
        conectarBaseDatos();
        activarClavesForaneas();
        crearTablas();
    }

    private void conectarBaseDatos() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(urlBaseDatos);
            System.out.println("Conectado a SQLite: paint.db");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver SQLite: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar con SQLite: " + e.getMessage());
        }
    }

    private void activarClavesForaneas() {
        try (Statement sentencia = conexion.createStatement()) {
            sentencia.execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            System.out.println("Error activando claves foráneas: " + e.getMessage());
        }
    }

    private void crearTablas() {
        try (Statement sentencia = conexion.createStatement()) {
            sentencia.executeUpdate("CREATE TABLE IF NOT EXISTS dibujos ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL)");

            sentencia.executeUpdate("CREATE TABLE IF NOT EXISTS figuras ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "dibujo_id INTEGER NOT NULL,"
                    + "tipo TEXT NOT NULL,"
                    + "orden_figura INTEGER NOT NULL,"
                    + "color_borde TEXT,"
                    + "color_relleno TEXT,"
                    + "relleno INTEGER,"
                    + "FOREIGN KEY (dibujo_id) REFERENCES dibujos(id) ON DELETE CASCADE)");

            sentencia.executeUpdate("CREATE TABLE IF NOT EXISTS puntos ("
                    + "figura_id INTEGER PRIMARY KEY,"
                    + "x INTEGER,"
                    + "y INTEGER,"
                    + "FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE)");

            sentencia.executeUpdate("CREATE TABLE IF NOT EXISTS rectas ("
                    + "figura_id INTEGER PRIMARY KEY,"
                    + "x1 INTEGER,"
                    + "y1 INTEGER,"
                    + "x2 INTEGER,"
                    + "y2 INTEGER,"
                    + "FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE)");

            sentencia.executeUpdate("CREATE TABLE IF NOT EXISTS circulos ("
                    + "figura_id INTEGER PRIMARY KEY,"
                    + "xCentro INTEGER,"
                    + "yCentro INTEGER,"
                    + "radio INTEGER,"
                    + "FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE)");

            sentencia.executeUpdate("CREATE TABLE IF NOT EXISTS poligonos_regulares ("
                    + "figura_id INTEGER PRIMARY KEY,"
                    + "xCentro INTEGER,"
                    + "yCentro INTEGER,"
                    + "radio INTEGER,"
                    + "lados INTEGER,"
                    + "anguloInicial REAL,"
                    + "FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE)");

            sentencia.executeUpdate("CREATE TABLE IF NOT EXISTS vertices_poligonos ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "figura_id INTEGER,"
                    + "x INTEGER,"
                    + "y INTEGER,"
                    + "orden_vertice INTEGER,"
                    + "FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE)");

            System.out.println("Tablas creadas correctamente en SQLite");
        } catch (SQLException e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }

    public int guardarDibujoCompleto(String nombreDibujo, ArrayList<Figura> listaFiguras) {
        int idDibujo = guardarDibujo(nombreDibujo);

        if (idDibujo == -1) {
            return -1;
        }

        try {
            conexion.setAutoCommit(false);

            for (Figura figura : listaFiguras) {
                int idFigura = guardarFigura(idDibujo, figura);

                switch (figura.tipo) {
                    case "PUNTO", "LAPIZ" -> guardarPunto(idFigura, figura.x, figura.y);
                    case "RECTA" -> guardarRecta(idFigura, figura.x1, figura.y1, figura.x2, figura.y2);
                    case "CIRCULO" -> guardarCirculo(idFigura, figura.xCentro, figura.yCentro, figura.radio);
                    case "POLIGONO_REGULAR" -> guardarPoligonoRegular(
                            idFigura, figura.xCentro, figura.yCentro, figura.radio, figura.lados, figura.anguloInicial);
                    case "POLIGONO_IRREGULAR" -> guardarVerticesPoligono(idFigura, figura.vertices);
                }
            }

            conexion.commit();
            conexion.setAutoCommit(true);
            return idDibujo;

        } catch (SQLException e) {
            try {
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error al cancelar guardado: " + ex.getMessage());
            }

            System.out.println("Error al guardar dibujo completo: " + e.getMessage());
            return -1;
        }
    }

    private int guardarDibujo(String nombreDibujo) {
        String sql = "INSERT INTO dibujos(nombre) VALUES (?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, nombreDibujo);
            sentencia.executeUpdate();

            ResultSet resultado = sentencia.getGeneratedKeys();

            if (resultado.next()) {
                return resultado.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar dibujo: " + e.getMessage());
        }

        return -1;
    }

    private int guardarFigura(int idDibujo, Figura figura) throws SQLException {
        String sql = "INSERT INTO figuras(dibujo_id, tipo, orden_figura, color_borde, color_relleno, relleno) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, idDibujo);
            sentencia.setString(2, figura.tipo);
            sentencia.setInt(3, figura.orden);
            sentencia.setString(4, colorComoTexto(figura.colorBorde));
            sentencia.setString(5, colorComoTexto(figura.colorRelleno));
            sentencia.setInt(6, figura.relleno ? 1 : 0);
            sentencia.executeUpdate();

            ResultSet resultado = sentencia.getGeneratedKeys();

            if (resultado.next()) {
                return resultado.getInt(1);
            }
        }

        return -1;
    }

    private void guardarPunto(int idFigura, int x, int y) throws SQLException {
        String sql = "INSERT INTO puntos(figura_id, x, y) VALUES (?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            sentencia.setInt(2, x);
            sentencia.setInt(3, y);
            sentencia.executeUpdate();
        }
    }

    private void guardarRecta(int idFigura, int x1, int y1, int x2, int y2) throws SQLException {
        String sql = "INSERT INTO rectas(figura_id, x1, y1, x2, y2) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            sentencia.setInt(2, x1);
            sentencia.setInt(3, y1);
            sentencia.setInt(4, x2);
            sentencia.setInt(5, y2);
            sentencia.executeUpdate();
        }
    }

    private void guardarCirculo(int idFigura, int xCentro, int yCentro, int radio) throws SQLException {
        String sql = "INSERT INTO circulos(figura_id, xCentro, yCentro, radio) VALUES (?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            sentencia.setInt(2, xCentro);
            sentencia.setInt(3, yCentro);
            sentencia.setInt(4, radio);
            sentencia.executeUpdate();
        }
    }

    private void guardarPoligonoRegular(int idFigura, int xCentro, int yCentro, int radio,
                                        int lados, double anguloInicial) throws SQLException {
        String sql = "INSERT INTO poligonos_regulares(figura_id, xCentro, yCentro, radio, lados, anguloInicial) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            sentencia.setInt(2, xCentro);
            sentencia.setInt(3, yCentro);
            sentencia.setInt(4, radio);
            sentencia.setInt(5, lados);
            sentencia.setDouble(6, anguloInicial);
            sentencia.executeUpdate();
        }
    }

    private void guardarVerticesPoligono(int idFigura, ArrayList<int[]> vertices) throws SQLException {
        String sql = "INSERT INTO vertices_poligonos(figura_id, x, y, orden_vertice) VALUES (?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            for (int indice = 0; indice < vertices.size(); indice++) {
                int[] vertice = vertices.get(indice);

                sentencia.setInt(1, idFigura);
                sentencia.setInt(2, vertice[0]);
                sentencia.setInt(3, vertice[1]);
                sentencia.setInt(4, indice);
                sentencia.addBatch();
            }

            sentencia.executeBatch();
        }
    }

    public ArrayList<String> obtenerDibujosGuardados() {
        ArrayList<String> dibujos = new ArrayList<>();
        String sql = "SELECT id, nombre FROM dibujos ORDER BY id ASC";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                dibujos.add(resultado.getInt("id") + " - " + resultado.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar dibujos: " + e.getMessage());
        }

        return dibujos;
    }

    public ArrayList<Figura> cargarDibujoCompleto(int idDibujo) {
        ArrayList<Figura> listaFiguras = new ArrayList<>();
        String sql = "SELECT * FROM figuras WHERE dibujo_id=? ORDER BY orden_figura ASC";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idDibujo);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                int idFigura = resultado.getInt("id");
                Figura figura = new Figura(resultado.getString("tipo"));

                figura.orden = resultado.getInt("orden_figura");
                figura.colorBorde = textoComoColor(resultado.getString("color_borde"));
                figura.colorRelleno = textoComoColor(resultado.getString("color_relleno"));
                figura.relleno = resultado.getInt("relleno") == 1;

                cargarDatosFigura(idFigura, figura);
                listaFiguras.add(figura);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar dibujo completo: " + e.getMessage());
        }

        return listaFiguras;
    }

    private void cargarDatosFigura(int idFigura, Figura figura) throws SQLException {
        switch (figura.tipo) {
            case "PUNTO", "LAPIZ" -> cargarDatosPunto(idFigura, figura);
            case "RECTA" -> cargarDatosRecta(idFigura, figura);
            case "CIRCULO" -> cargarDatosCirculo(idFigura, figura);
            case "POLIGONO_REGULAR" -> cargarDatosPoligonoRegular(idFigura, figura);
            case "POLIGONO_IRREGULAR" -> cargarDatosPoligonoIrregular(idFigura, figura);
        }
    }

    private void cargarDatosPunto(int idFigura, Figura figura) throws SQLException {
        String sql = "SELECT * FROM puntos WHERE figura_id=?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                figura.x = resultado.getInt("x");
                figura.y = resultado.getInt("y");
            }
        }
    }

    private void cargarDatosRecta(int idFigura, Figura figura) throws SQLException {
        String sql = "SELECT * FROM rectas WHERE figura_id=?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                figura.x1 = resultado.getInt("x1");
                figura.y1 = resultado.getInt("y1");
                figura.x2 = resultado.getInt("x2");
                figura.y2 = resultado.getInt("y2");
            }
        }
    }

    private void cargarDatosCirculo(int idFigura, Figura figura) throws SQLException {
        String sql = "SELECT * FROM circulos WHERE figura_id=?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                figura.xCentro = resultado.getInt("xCentro");
                figura.yCentro = resultado.getInt("yCentro");
                figura.radio = resultado.getInt("radio");
            }
        }
    }

    private void cargarDatosPoligonoRegular(int idFigura, Figura figura) throws SQLException {
        String sql = "SELECT * FROM poligonos_regulares WHERE figura_id=?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                figura.xCentro = resultado.getInt("xCentro");
                figura.yCentro = resultado.getInt("yCentro");
                figura.radio = resultado.getInt("radio");
                figura.lados = resultado.getInt("lados");
                figura.anguloInicial = resultado.getDouble("anguloInicial");
            }
        }
    }

    private void cargarDatosPoligonoIrregular(int idFigura, Figura figura) throws SQLException {
        String sql = "SELECT * FROM vertices_poligonos WHERE figura_id=? ORDER BY orden_vertice ASC";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idFigura);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                figura.vertices.add(new int[]{
                    resultado.getInt("x"),
                    resultado.getInt("y")
                });
            }
        }
    }

    public boolean modificarNombreDibujo(int idDibujo, String nuevoNombre) {
        String sql = "UPDATE dibujos SET nombre=? WHERE id=?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, nuevoNombre);
            sentencia.setInt(2, idDibujo);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar dibujo: " + e.getMessage());
            return false;
        }
    }

    public boolean borrarDibujo(int idDibujo) {
        String sql = "DELETE FROM dibujos WHERE id=?";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idDibujo);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar dibujo: " + e.getMessage());
            return false;
        }
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión SQLite cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    private String colorComoTexto(Color color) {
        if (color == null) {
            color = Color.BLACK;
        }

        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private Color textoComoColor(String textoColor) {
        try {
            if (textoColor == null || textoColor.isEmpty()) {
                return Color.BLACK;
            }

            return Color.decode(textoColor);
        } catch (NumberFormatException e) {
            return Color.BLACK;
        }
    }
}
