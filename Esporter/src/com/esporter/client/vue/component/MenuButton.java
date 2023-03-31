package com.esporter.client.vue.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import com.esporter.both.types.TypesPermission;
import com.esporter.client.vue.MasterFrame;

public class MenuButton extends JRadioButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8366959265584745626L;
	private Class<? extends JPanel> panelToChange;
	private boolean mouseIn;
	private boolean exited;
	private float alphaFadeOut;
	private Graphics2D g2;
	private Timer timer = null;
	private TypesPermission permission;
	
	public MenuButton(boolean selected,String text, Class<? extends JPanel> panelToChange, TypesPermission permission, String actionCommand) {
		super(text, new ImageIcon());
		mouseIn=false;
		exited=false;
		alphaFadeOut=1.0f;
		this.permission = permission;
		setSelected(selected);
		setFocusPainted(false);
		setText(text);
		setHorizontalAlignment(CENTER);
		setOpaque(true);
		setBackground(new Color(0,0,0,0));
		setActionCommand(actionCommand);
		
		setPreferredSize(new Dimension(150,75));
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseExited(MouseEvent e) {
				mouseIn=false;
				exited=true;
				if (timer!=null) {
					timer.stop();
				}
				timer = new Timer(10, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						repaint();
						
					}
				});
				timer.start();
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if (!isSelected()) {
					if (!mouseIn) 
						mouseIn = true;
					repaint();
				}				
			}

			@Override
			public void mouseDragged(MouseEvent e) {}
			
		});
		
	}
	
	public TypesPermission getPermission() {
		return permission;
	}
	
	public Class<? extends JPanel> getPanelToChange() {
		return panelToChange;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (!MasterFrame.getInstance().getLoginPage().isVisible() || !MasterFrame.getInstance().getError().isVisible()) {
			g2 = (Graphics2D)g.create();
			int width = getBounds().width;
			int height = getBounds().height;
			
			setForeground(MasterFrame.COLOR_TEXT_MENU);
			if (isSelected()) {	
				g2.setClip(0, 0, width, height+16);
				g2.setColor(MasterFrame.COLOR_MASTER);
				Polygon p = new Polygon();
				p.addPoint(0, 0);
				p.addPoint(0, height-1);
				p.addPoint((width/2)-20, height-1);
				p.addPoint((width/2), height+15);
				p.addPoint((width/2)+20, height-1);
				p.addPoint(width, height-1);
				p.addPoint(width, 0);
				
				g2.drawPolygon(p);
				g2.fillPolygon(p);
				
			    
			} else {
				
				g2.setClip(0, 0, width, height);
				g2.setColor(MasterFrame.COLOR_MENU_BACKGROUND);
				Polygon p = new Polygon();
				p.addPoint(0, 0);
				p.addPoint(0, height-1);
				p.addPoint(width, height-1);
				p.addPoint(width, 0);
				g2.drawPolygon(p);
				g2.fillPolygon(p);
	
			}
			g2.dispose();
			
			if (mouseIn && !isSelected()) {
				g2 = (Graphics2D)g.create();
				//int brightness = (255 - (Math.abs(width/2-mouseX)*3));
				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				GradientPaint gp = new GradientPaint(0, height, new Color(0,164,210,100), 0, ((float)height)/4, MasterFrame.COLOR_MENU_BACKGROUND);
				g2.setPaint(gp);
				g2.fillRect(0, height/4, width, height*3/4);
	
			}
			
			
			if(exited && !isSelected()) {
				if(alphaFadeOut>0 && !mouseIn) {
					g2 = (Graphics2D)g.create();
	
					g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					GradientPaint gp = new GradientPaint(0, height, new Color(0,164,210,(int)(100*alphaFadeOut)), 0, ((float)height)/4, MasterFrame.COLOR_MENU_BACKGROUND);
					g2.setPaint(gp);
					
					g2.fillRect(0, height/4, width, height*3/4);
					alphaFadeOut-=0.07f;
				} else {
					g2.dispose();
					alphaFadeOut=1.0f;
					exited=false;
					timer.stop();
				}
	
			}
			if(mouseIn && !exited && !isSelected())
				g2.dispose();
			
			super.paintComponent(g);
		}
	}
	
}
