package Modelo;

import java.sql.*;

public final class Modelo {

    private Connection conexion;
    private final String url = "jdbc:sqlite:paint.db";

    public Modelo() {
        conectarBaseDatos();
        activarClavesForaneas();
        crearTablas();
    }

    public void conectarBaseDatos() {
        try {
            conexion = DriverManager.getConnection(url);
            System.out.println("Conectado a SQLite: paint.db");
        } catch (SQLException e) {
            System.out.println("Error al conectar con SQLite: " + e.getMessage());
        }
    }

    private void activarClavesForaneas() {
        try {
            Statement st = conexion.createStatement();
            st.execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            System.out.println("Error activando claves foráneas: " + e.getMessage());
        }
    }

    public void crearTablas() {
        try {
            Statement st = conexion.createStatement();

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS dibujos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS figuras (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    dibujo_id INTEGER,
                    tipo TEXT,
                    orden_figura INTEGER,
                    color_borde TEXT,
                    color_relleno TEXT,
                    relleno INTEGER,
                    FOREIGN KEY (dibujo_id) REFERENCES dibujos(id) ON DELETE CASCADE
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS circulos (
                    figura_id INTEGER PRIMARY KEY,
                    xCentro INTEGER,
                    yCentro INTEGER,
                    radio INTEGER,
                    FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS poligonos_regulares (
                    figura_id INTEGER PRIMARY KEY,
                    xCentro INTEGER,
                    yCentro INTEGER,
                    radio INTEGER,
                    lados INTEGER,
                    anguloInicial REAL,
                    FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS puntos (
                    figura_id INTEGER PRIMARY KEY,
                    x INTEGER,
                    y INTEGER,
                    FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS rectas (
                    figura_id INTEGER PRIMARY KEY,
                    x1 INTEGER,
                    y1 INTEGER,
                    x2 INTEGER,
                    y2 INTEGER,
                    FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE
                )
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS vertices_poligonos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    figura_id INTEGER,
                    x INTEGER,
                    y INTEGER,
                    orden_vertice INTEGER,
                    FOREIGN KEY (figura_id) REFERENCES figuras(id) ON DELETE CASCADE
                )
            """);

            System.out.println("Tablas creadas correctamente en SQLite");

        } catch (SQLException e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }

    // Creacion______________________________________________________________________________________

    public int guardarDibujo(String nombre) {
        try {
            String sql = "INSERT INTO dibujos(nombre) VALUES (?)";
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, nombre);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar dibujo: " + e.getMessage());
        }

        return -1;
    }

    public int guardarFigura(int dibujoId, String tipo, int orden, String colorBorde, String colorRelleno, boolean relleno) {
        try {
            String sql = """
                INSERT INTO figuras(dibujo_id, tipo, orden_figura, color_borde, color_relleno, relleno)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, dibujoId);
            ps.setString(2, tipo);
            ps.setInt(3, orden);
            ps.setString(4, colorBorde);
            ps.setString(5, colorRelleno);
            ps.setInt(6, relleno ? 1 : 0);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar figura: " + e.getMessage());
        }

        return -1;
    }

    public void guardarPunto(int figuraId, int x, int y) {
        try {
            String sql = "INSERT INTO puntos(figura_id, x, y) VALUES (?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, figuraId);
            ps.setInt(2, x);
            ps.setInt(3, y);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al guardar punto: " + e.getMessage());
        }
    }

    public void guardarRecta(int figuraId, int x1, int y1, int x2, int y2) {
        try {
            String sql = "INSERT INTO rectas(figura_id, x1, y1, x2, y2) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, figuraId);
            ps.setInt(2, x1);
            ps.setInt(3, y1);
            ps.setInt(4, x2);
            ps.setInt(5, y2);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al guardar recta: " + e.getMessage());
        }
    }

    public void guardarCirculo(int figuraId, int xCentro, int yCentro, int radio) {
        try {
            String sql = "INSERT INTO circulos(figura_id, xCentro, yCentro, radio) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, figuraId);
            ps.setInt(2, xCentro);
            ps.setInt(3, yCentro);
            ps.setInt(4, radio);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al guardar círculo: " + e.getMessage());
        }
    }

    public void guardarPoligonoRegular(int figuraId, int xCentro, int yCentro, int radio, int lados, double anguloInicial) {
        try {
            String sql = """
                INSERT INTO poligonos_regulares(figura_id, xCentro, yCentro, radio, lados, anguloInicial)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, figuraId);
            ps.setInt(2, xCentro);
            ps.setInt(3, yCentro);
            ps.setInt(4, radio);
            ps.setInt(5, lados);
            ps.setDouble(6, anguloInicial);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al guardar polígono regular: " + e.getMessage());
        }
    }

    public void guardarVerticePoligono(int figuraId, int x, int y, int ordenVertice) {
        try {
            String sql = "INSERT INTO vertices_poligonos(figura_id, x, y, orden_vertice) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, figuraId);
            ps.setInt(2, x);
            ps.setInt(3, y);
            ps.setInt(4, ordenVertice);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al guardar vértice: " + e.getMessage());
        }
    }

    // Lectura_________________________________________________________________________________________

    public void cargarDibujos() {
        try {
            String sql = "SELECT * FROM dibujos";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Nombre: " + rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar dibujos: " + e.getMessage());
        }
    }

    public void cargarFigurasDeDibujo(int dibujoId) {
        try {
            String sql = "SELECT * FROM figuras WHERE dibujo_id=? ORDER BY orden_figura ASC";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, dibujoId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int figuraId = rs.getInt("id");
                String tipo = rs.getString("tipo");

                System.out.println("Figura ID: " + figuraId + " | Tipo: " + tipo);

                switch (tipo) {
                    case "PUNTO" -> cargarPunto(figuraId);
                    case "RECTA" -> cargarRecta(figuraId);
                    case "CIRCULO" -> cargarCirculo(figuraId);
                    case "POLIGONO_REGULAR" -> cargarPoligonoRegular(figuraId);
                    case "POLIGONO_IRREGULAR" -> cargarVerticesPoligono(figuraId);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar figuras: " + e.getMessage());
        }
    }

    private void cargarPunto(int figuraId) {
        try {
            String sql = "SELECT * FROM puntos WHERE figura_id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, figuraId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Punto: x=" + rs.getInt("x") + " y=" + rs.getInt("y"));
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar punto: " + e.getMessage());
        }
    }

    private void cargarRecta(int figuraId) {
        try {
            String sql = "SELECT * FROM rectas WHERE figura_id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, figuraId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Recta: x1=" + rs.getInt("x1")
                        + " y1=" + rs.getInt("y1")
                        + " x2=" + rs.getInt("x2")
                        + " y2=" + rs.getInt("y2"));
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar recta: " + e.getMessage());
        }
    }

    private void cargarCirculo(int figuraId) {
        try {
            String sql = "SELECT * FROM circulos WHERE figura_id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, figuraId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Círculo: xCentro=" + rs.getInt("xCentro")
                        + " yCentro=" + rs.getInt("yCentro")
                        + " radio=" + rs.getInt("radio"));
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar círculo: " + e.getMessage());
        }
    }

    private void cargarPoligonoRegular(int figuraId) {
        try {
            String sql = "SELECT * FROM poligonos_regulares WHERE figura_id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, figuraId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Polígono regular: xCentro=" + rs.getInt("xCentro")
                        + " yCentro=" + rs.getInt("yCentro")
                        + " radio=" + rs.getInt("radio")
                        + " lados=" + rs.getInt("lados")
                        + " angulo=" + rs.getDouble("anguloInicial"));
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar polígono regular: " + e.getMessage());
        }
    }

    private void cargarVerticesPoligono(int figuraId) {
        try {
            String sql = "SELECT * FROM vertices_poligonos WHERE figura_id=? ORDER BY orden_vertice ASC";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, figuraId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("Vértice: x=" + rs.getInt("x") + " y=" + rs.getInt("y"));
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar vértices: " + e.getMessage());
        }
    }

    // Actualizacion_________________________________________________________________________________________

    public void modificarDibujo(int id, String nuevoNombre) {
        try {
            String sql = "UPDATE dibujos SET nombre=? WHERE id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, nuevoNombre);
            ps.setInt(2, id);

            ps.executeUpdate();

            System.out.println("Dibujo modificado correctamente");

        } catch (SQLException e) {
            System.out.println("Error al modificar dibujo: " + e.getMessage());
        }
    }

    public void modificarFigura(int figuraId, String colorBorde, String colorRelleno, boolean relleno) {
        try {
            String sql = "UPDATE figuras SET color_borde=?, color_relleno=?, relleno=? WHERE id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, colorBorde);
            ps.setString(2, colorRelleno);
            ps.setInt(3, relleno ? 1 : 0);
            ps.setInt(4, figuraId);

            ps.executeUpdate();

            System.out.println("Figura modificada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al modificar figura: " + e.getMessage());
        }
    }

    public void modificarPunto(int figuraId, int x, int y) {
        try {
            String sql = "UPDATE puntos SET x=?, y=? WHERE figura_id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, x);
            ps.setInt(2, y);
            ps.setInt(3, figuraId);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al modificar punto: " + e.getMessage());
        }
    }

    public void modificarRecta(int figuraId, int x1, int y1, int x2, int y2) {
        try {
            String sql = "UPDATE rectas SET x1=?, y1=?, x2=?, y2=? WHERE figura_id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, x1);
            ps.setInt(2, y1);
            ps.setInt(3, x2);
            ps.setInt(4, y2);
            ps.setInt(5, figuraId);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al modificar recta: " + e.getMessage());
        }
    }

    public void modificarCirculo(int figuraId, int xCentro, int yCentro, int radio) {
        try {
            String sql = "UPDATE circulos SET xCentro=?, yCentro=?, radio=? WHERE figura_id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, xCentro);
            ps.setInt(2, yCentro);
            ps.setInt(3, radio);
            ps.setInt(4, figuraId);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al modificar círculo: " + e.getMessage());
        }
    }

    public void modificarPoligonoRegular(int figuraId, int lados, int radio, double anguloInicial) {
        try {
            String sql = "UPDATE poligonos_regulares SET lados=?, radio=?, anguloInicial=? WHERE figura_id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, lados);
            ps.setInt(2, radio);
            ps.setDouble(3, anguloInicial);
            ps.setInt(4, figuraId);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al modificar polígono regular: " + e.getMessage());
        }
    }

    public void modificarVertice(int figuraId, int ordenVertice, int x, int y) {
        try {
            String sql = "UPDATE vertices_poligonos SET x=?, y=? WHERE figura_id=? AND orden_vertice=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, x);
            ps.setInt(2, y);
            ps.setInt(3, figuraId);
            ps.setInt(4, ordenVertice);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al modificar vértice: " + e.getMessage());
        }
    }

    // DELETE

    public void borrarDibujo(int dibujoId) {
        try {
            String sql = "DELETE FROM dibujos WHERE id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, dibujoId);
            ps.executeUpdate();

            System.out.println("Dibujo eliminado correctamente");

        } catch (SQLException e) {
            System.out.println("Error al borrar dibujo: " + e.getMessage());
        }
    }

    public void borrarFigura(int figuraId) {
        try {
            String sql = "DELETE FROM figuras WHERE id=?";
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, figuraId);
            ps.executeUpdate();

            System.out.println("Figura eliminada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al borrar figura: " + e.getMessage());
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
}