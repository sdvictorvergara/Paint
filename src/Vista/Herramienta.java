/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;



public interface Herramienta {
    public void mousePressed(MouseEvent e);
    public void mouseDragged(MouseEvent e);
    public void dibujar(Graphics g);
    public void setColor(Color color);
    
}