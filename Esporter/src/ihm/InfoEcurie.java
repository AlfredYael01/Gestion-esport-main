package ihm;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;

import ihm.component.DataJPanel;
import ihm.component.InfoEcuriePalmaRenderer;
import types.EcurieInfo;
import types.Titre;

import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.imageio.ImageIO;
import javax.swing.Box;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.SystemColor;

public class InfoEcurie extends DataJPanel{

	private JTextField NomDelEcurie;
	private JPanel North;
	private JPanel BtnBack;
	private JPanel BtnEcuAndInfo;
	private JPanel Vide;
	private JPanel Vide2;
	private static final long serialVersionUID = 8722294344861036522L;
	private JPanel Center;
	private EcurieInfo ecurie;
	private JPanel containerInfo;
	private JPanel containerPalma;
	private JPanel pan;
	private JLabel lblPalmares;
	private JLabel lblLogoEcurie;
	private JPanel panel;
	private JPanel panel_1;
	
	public void createListPalma() {
		ArrayList<Titre> liste = ecurie.getPalmares();
        
        for(Titre t : liste) {
        	System.out.println("titre");
			System.out.println(t);
			pan.add(new InfoEcuriePalmaRenderer(t));
		}
	}
	
	/**
	 * Create the application.
	 */
	public InfoEcurie(EcurieInfo ecurie) {
		this.ecurie = ecurie;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel all = new JPanel();
		all.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		add(all, BorderLayout.CENTER);
		all.setLayout(new BorderLayout(0, 0));
		
		Center = new JPanel();
		Center.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		all.add(Center, BorderLayout.CENTER);
		Center.setLayout(new BoxLayout(Center, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		panel.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		Center.add(panel);
		
		containerInfo = new JPanel();
		containerInfo.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		Center.add(containerInfo);
		containerInfo.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblLogoEcurie = new JLabel();
		lblLogoEcurie.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLogoEcurie.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		lblLogoEcurie.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		containerInfo.add(lblLogoEcurie);
		
		
		try {
			//File file = new File("images/ESporterLogo.png");
			BufferedImage logoEcurie = ImageIO.read(getClass().getResource("images/karmine-corp.jpg"));
			logoEcurie = resize(logoEcurie, 400, 300);
			lblLogoEcurie.setIcon(new ImageIcon(logoEcurie));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		containerPalma = new JPanel();
		containerPalma.setBorder(null);
		containerPalma.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		containerPalma.setAlignmentX(Component.LEFT_ALIGNMENT);
		containerInfo.add(containerPalma);
		containerPalma.setLayout(new GridLayout(0, 1, 0, -30));
		
		
		lblPalmares = new JLabel("Palmarès");
		lblPalmares.setForeground(Color.WHITE);
		lblPalmares.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 30));
		lblPalmares.setHorizontalAlignment(SwingConstants.CENTER);
		lblPalmares.setAlignmentX(0.5f);
		containerPalma.add(lblPalmares);
		
		pan = new JPanel();
		pan.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		createListPalma();
		JScrollPane scrollPaneCenter = new JScrollPane(pan);
		scrollPaneCenter.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPaneCenter.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		scrollPaneCenter.setBorder(new EmptyBorder(50, 100, 50, 100));
		containerPalma.add(scrollPaneCenter);
		
		
		
		
		panel_1 = new JPanel();
		panel_1.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		Center.add(panel_1);
		
		
		
		North = new JPanel();
		all.add(North, BorderLayout.NORTH);
		North.setLayout(new BorderLayout(0, 0));
		JPanel TitleEquipe = new JPanel();
		
		TitleEquipe.setLayout(new BorderLayout(0, 0));
		North.add(TitleEquipe, BorderLayout.SOUTH);
		NomDelEcurie = new JTextField();
		NomDelEcurie.setForeground(MasterFrame.COULEUR_TEXTE);
		NomDelEcurie.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		NomDelEcurie.setEditable(false);
		NomDelEcurie.setBorder(null);
		NomDelEcurie.setHorizontalAlignment(SwingConstants.CENTER);
		NomDelEcurie.setFont(new Font("Cambria", Font.BOLD, 50));
		NomDelEcurie.setText(ecurie.getNom());
		TitleEquipe.add(NomDelEcurie, BorderLayout.CENTER);
		NomDelEcurie.setColumns(10);
		
		BtnBack = new JPanel();
		North.add(BtnBack, BorderLayout.CENTER);
		BtnBack.setLayout(new BorderLayout(0, 0));
		
		BtnEcuAndInfo = new JPanel();
		BtnEcuAndInfo.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		BtnEcuAndInfo.setBorder(new EmptyBorder(0, 20, 0, 0));
		BtnBack.add(BtnEcuAndInfo, BorderLayout.WEST);
		BtnEcuAndInfo.setLayout(new BorderLayout(0, 0));
		
		Vide = new JPanel();
		Vide.setBorder(null);
		Vide.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		BtnBack.add(Vide, BorderLayout.CENTER);
		Vide.setLayout(new BorderLayout(0, 0));
		JButton Ecuries = new JButton("Ecuries /");
		Ecuries.setForeground(MasterFrame.COULEUR_MASTER);
		Ecuries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MasterFrame.getInstance().setPanel(ihm.Ecuries.class, ecurie);
			}
		});
		Ecuries.setBackground(null);
		Ecuries.setContentAreaFilled(false);
		Ecuries.setBorder(null);
		Ecuries.setFont(new Font("Cambria", Font.PLAIN, 20));
		BtnEcuAndInfo.add(Ecuries, BorderLayout.WEST);
		
		JButton VoirInfo = new JButton(" Voir Info");
		VoirInfo.setForeground(MasterFrame.COULEUR_TEXTE);
		VoirInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		VoirInfo.setBackground(null);
		VoirInfo.setContentAreaFilled(false);
		VoirInfo.setBorder(null);
		VoirInfo.setFont(new Font("Cambria", Font.PLAIN, 20));
		BtnEcuAndInfo.add(VoirInfo, BorderLayout.EAST);
		
		Vide2 = new JPanel();
		Vide2.setBorder(null);
		Vide2.setBackground(MasterFrame.COULEUR_MASTER_FOND);
		FlowLayout fl_Vide2 = (FlowLayout) Vide2.getLayout();
		fl_Vide2.setVgap(10);
		BtnBack.add(Vide2, BorderLayout.NORTH);
				
	}

	@Override
	public void dataUpdate() {
		createListPalma();
		
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}

}