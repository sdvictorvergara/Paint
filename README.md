# 🎨 Paint — MVC · CRUD · GUI Swing · SVG

Aplicación de dibujo vectorial desarrollada en **Java** como proyecto universitario. Permite dibujar figuras planas sobre un lienzo, guardarlas en una base de datos local y exportarlas como archivos SVG.

---

## ✨ Funcionalidades

- Dibujar **puntos, rectas, circunferencias, polígonos regulares y polígonos irregulares**
- Selección de **color de trazo y color de relleno** mediante selector de color
- Polígonos regulares con **slider** para elegir el número de lados (sin teclado)
- Circunferencias definidas con **dos clics** (centro y radio)
- Polígonos irregulares con **validación** de lados que no se crucen
- **Guardar, cargar, modificar y borrar** dibujos (CRUD completo)
- **Exportar** cualquier dibujo guardado a un archivo **.svg**

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| **Java** | Lenguaje principal de la aplicación |
| **Java Swing** | Interfaz gráfica (GUI) |
| **SQLite** | Base de datos local para persistir los dibujos |
| **sqlite-jdbc 3.53.1.0** | Driver JDBC para conectar Java con SQLite |
| **SVG** | Formato de exportación de los dibujos |
| **Apache NetBeans** | IDE utilizado para el desarrollo |

---

## 🏗️ Arquitectura — Patrón MVC

El proyecto sigue el patrón **Modelo-Vista-Controlador**:

```
src/
├── Main.java                     # Punto de entrada
├── Controlador/
│   └── Controlador.java          # Gestiona eventos y coordina Modelo y Vista
├── Modelo/
│   └── Modelo.java               # Lógica de negocio y acceso a la BD (SQLite)
└── Vista/
    ├── VentanaPrincipal.java     # Ventana principal (JFrame)
    ├── Lienzo.java               # Lienzo de dibujo (JPanel)
    ├── Figura.java               # Clase base de figura
    ├── Punto.java
    ├── Recta.java
    ├── Circulo.java
    ├── PoligonoRegular.java
    ├── PoligonoIrregular.java
    ├── Lapiz.java
    └── Herramienta.java
```

---

## 🗄️ Base de datos

La base de datos **paint.db** (SQLite) se crea automáticamente al iniciar la aplicación. Contiene las siguientes tablas:

- **dibujos** — almacena cada dibujo con su nombre
- **figuras** — almacena cada figura con su tipo, orden de pintado y colores
- **puntos** — coordenadas de puntos y trazos de lápiz
- **rectas** — coordenadas de inicio y fin de cada recta
- **circulos** — centro y radio de cada circunferencia
- **poligonos_regulares** — centro, radio, número de lados y ángulo inicial
- **vertices_poligonos** — lista de vértices de polígonos irregulares

Las claves foráneas en cascada garantizan que al borrar un dibujo se eliminan todas sus figuras y datos asociados automáticamente.

---

## ▶️ Cómo ejecutar

### Opción 1 — JAR precompilado

```bash
java -jar dist/paint.jar
```

### Opción 2 — Desde NetBeans

1. Abre NetBeans y selecciona **File → Open Project**
2. Navega a la carpeta del proyecto y ábrelo
3. Pulsa **Run** (F6)

> El archivo `paint.db` se generará automáticamente en el directorio raíz del proyecto la primera vez que se ejecute.

---

## 📦 Dependencias

- [sqlite-jdbc 3.53.1.0](https://github.com/xerial/sqlite-jdbc) — incluida en `lib/`

No se necesita ninguna instalación adicional de base de datos.
