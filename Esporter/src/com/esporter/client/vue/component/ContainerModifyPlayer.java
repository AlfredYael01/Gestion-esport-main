package com.esporter.client.vue.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.esporter.both.types.TypesPlayer;
import com.esporter.both.types.TypesRegisterPlayer;
import com.esporter.client.controleur.Controler;
import com.esporter.client.vue.MasterFrame;
import com.esporter.client.vue.stable.AddPlayer;
import com.esporter.client.vue.stable.ModifyPlayer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.SystemColor;
import java.awt.Dimension;
import java.awt.Font;
public class ContainerModifyPlayer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6951669578094176958L;
	private TypesPlayer player = null;
	private JLabel image;
	private JLabel playerName;
	private static final int WIDTH = 100;
	private static final int HEIGHT = 150;
	/**
	 * Create the panel.
	 */
	public ContainerModifyPlayer() {
		setBorder(new LineBorder(SystemColor.textHighlight, 2, true));
		setSize(WIDTH, HEIGHT);
		setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {WIDTH};
		gridBagLayout.rowHeights = new int[] {HEIGHT-HEIGHT/4, HEIGHT/4};
		gridBagLayout.columnWeights = new double[]{0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		setLayout(gridBagLayout);
		
		image = new JLabel("");
		GridBagConstraints gbc_gridBagConstraint = new GridBagConstraints();
		gbc_gridBagConstraint.gridx = 0;
		gbc_gridBagConstraint.gridy = 0;
		add(image, gbc_gridBagConstraint);
		
		ContainerModifyPlayer self = this;
		
		playerName = new JLabel("");
		playerName.setFont(new Font("Cambria", Font.PLAIN, 11));
		GridBagConstraints gbc_playerName = new GridBagConstraints();
		gbc_playerName.gridx = 0;
		gbc_playerName.gridy = 1;
		add(playerName, gbc_playerName);
		addMouseListener(Controler.getInstance());
		
	}
	
	public void setPlayer(TypesPlayer player) {
		this.player = player;
		playerName.setText(player.getName()+" "+player.getFirstName());
		playerName.setPreferredSize(new Dimension(100,30));
		playerName.setHorizontalAlignment(SwingConstants.CENTER);
		playerName.setToolTipText(player.getFirstName() + " " + player.getName());
		BufferedImage bf = player.getImage().getImage();
		bf = resize(bf, WIDTH-1, HEIGHT-HEIGHT/4-1);
		image.setIcon(new ImageIcon(bf));
		revalidate();
		repaint();
		repaint();
	}
	
	
	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
		BufferedImage bf = player.getImage().getImage();
		g.drawImage(bf, 0, 0, null);
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
	public TypesPlayer getPlayer() {
		return player;
	}
	
	

}
