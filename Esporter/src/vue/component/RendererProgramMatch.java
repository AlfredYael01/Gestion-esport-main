package vue.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import controleur.Controler;
import vue.MasterFrame;
import types.TypesImage;
import types.TypesMatch;
import types.TypesTournament;

import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;

import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RendererProgramMatch extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel matchDate = new JLabel();
	private JLabel matchTime = new JLabel();
	private TypesMatch match;

	public RendererProgramMatch(TypesMatch match) {
		this.match = match;
		setPreferredSize(new Dimension(500,250));
		setBorder(new MatteBorder(0, 0, 1, 0, MasterFrame.COLOR_TEXT));
		setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		setLayout(new BorderLayout(5,5));
		JPanel panelContainer = new JPanel();
		panelContainer.setBackground(MasterFrame.COLOR_MASTER_BACKGROUND);
		panelContainer.setPreferredSize(new Dimension(500,200));
		
		add(panelContainer);
		panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
		
		JPanel panelContainerDate = new JPanel();
		panelContainerDate.setLayout(new BorderLayout());
		matchDate.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		matchDate.setFont(new Font("Cambria", Font.BOLD, 16));
		matchDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(match.getDate()));
		panelContainerDate.add(matchDate, BorderLayout.SOUTH);
		panelContainer.add(panelContainerDate);
		
		JPanel panelContainerTeam = new JPanel();
		panelContainerTeam.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.BLACK));
		panelContainer.add(panelContainerTeam);
		panelContainerTeam.setLayout(new BoxLayout(panelContainerTeam, BoxLayout.X_AXIS));
		
		JPanel panelContainerTime = new JPanel();
		panelContainerTime.setPreferredSize(new Dimension(0, 60));
		panelContainerTeam.add(panelContainerTime);
		panelContainerTime.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		matchTime.setHorizontalTextPosition(SwingConstants.CENTER);
		matchTime.setHorizontalAlignment(SwingConstants.CENTER);
		matchTime.setFont(new Font("Cambria", Font.BOLD, 40));
		matchTime.setText(new SimpleDateFormat("HH:mm").format(match.getDate()));
		panelContainerTime.add(matchTime);
		
		JPanel panelContainerTeamsInfo = new JPanel();
		panelContainerTeamsInfo.setBorder(new EmptyBorder(0, 40, 0, 20));
		panelContainerTeam.add(panelContainerTeamsInfo);
		panelContainerTeamsInfo.setLayout(new GridLayout(0, 5, 0, 0));
		JLabel lblTeam1Name;
		if(match.getTeam1()!=0) {
			lblTeam1Name = new JLabel(Controler.getInstance().getData().getTeams().get(match.getTeam1()).getStable().getName());
		} else {
			lblTeam1Name = new JLabel("ADT");
		}
		lblTeam1Name.setFont(new Font("Cambria", Font.PLAIN, 16));
		panelContainerTeamsInfo.add(lblTeam1Name);
		
		JLabel lblTeam1Logo = new JLabel();
		if(match.getTeam1()!=0) {
			BufferedImage logoStable1 = Controler.getInstance().getData().getTeams().get(match.getTeam1()).getStable().getLogo().getImage();
			logoStable1 = TypesImage.resize(logoStable1, 200, 200);
			lblTeam1Logo.setIcon(new ImageIcon(logoStable1));
		}else {
			lblTeam1Logo.setText("??");
		}
		lblTeam1Logo.setFont(new Font("Cambria", Font.PLAIN, 16));
		panelContainerTeamsInfo.add(lblTeam1Logo);
		
		JLabel lblScoreOrVs = new JLabel("VS");
		lblScoreOrVs.setFont(new Font("Cambria", Font.PLAIN, 16));
		lblScoreOrVs.setHorizontalAlignment(SwingConstants.LEFT);
		panelContainerTeamsInfo.add(lblScoreOrVs);
		
		JLabel lblTeam2Logo = new JLabel();
		if(match.getTeam2()!=0) {
			BufferedImage logoStable2 = Controler.getInstance().getData().getTeams().get(match.getTeam2()).getStable().getLogo().getImage();
			logoStable2 = TypesImage.resize(logoStable2, 200, 200);
			lblTeam2Logo.setIcon(new ImageIcon(logoStable2));
		}else {
			lblTeam2Logo.setText("??");
		}
		lblTeam2Logo.setFont(new Font("Cambria", Font.PLAIN, 16));
		panelContainerTeamsInfo.add(lblTeam2Logo);
		
		JLabel lblTeam2Name;
		if(match.getTeam1()!=0) {
			lblTeam2Name = new JLabel(Controler.getInstance().getData().getTeams().get(match.getTeam2()).getStable().getName());
		} else {
			lblTeam2Name = new JLabel("ADT");
		}
		lblTeam2Name.setFont(new Font("Cambria", Font.PLAIN, 16));
		panelContainerTeamsInfo.add(lblTeam2Name);
		
		JPanel panelContainerButton = new JPanel();
		panelContainerTeam.add(panelContainerButton);
		panelContainerButton.setLayout(new BoxLayout(panelContainerButton, BoxLayout.X_AXIS));
		
		JButton btnSetScore = new JButton("New button");
		panelContainerButton.add(btnSetScore);
		
		
		
 }
	public TypesMatch getMatch() {
		return match;
	}
}