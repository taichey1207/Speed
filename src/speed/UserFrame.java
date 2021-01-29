package speed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author Cai
 */

public class UserFrame extends JFrame{
	private CardView[] CpuView = new CardView[4];
	private CardView[] PlayerView = new CardView[4];
	private ButtonAction[] Action = new ButtonAction[4];
	private CardView[] TableView = new CardView[2];

	public UserFrame(final String title) {
		super(title);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		BoxLayout boxlayout =
	            new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS);

		this.setLayout(boxlayout);

		//create radiobutton
		JRadioButton[] radio = new JRadioButton[2];
		radio[0] = new JRadioButton("", true);
		radio[1] = new JRadioButton();

		//create Button0-3
		JButton[] button = new JButton[4];
		for(int i=0 ; i<4 ; i++) {
			button[i] = new JButton("button"+i);
		}

		//create CPUArea
		JPanel CPUPanel = new JPanel();
		CPUPanel.setPreferredSize(new Dimension(1000,200));
		CPUPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

		HandCardPanel[] CpuHandCard = new HandCardPanel[4];

		for(int i=0 ; i<4 ; i++) {
			CpuView[i] = new CardView();
			CpuHandCard[i] = new HandCardPanel();
			CpuHandCard[i].setLayout(new BorderLayout());
			CpuHandCard[i].add(CpuView[i]);
		}

		HandCardPanel CpuTempHandCard = new HandCardPanel();
		DeckCardPanel CpuDeckCard = new DeckCardPanel();


		CPUPanel.add(CpuDeckCard);
		for(int i=0 ; i<4 ; i++) {
			CPUPanel.add(CpuHandCard[i]);
		}
		CPUPanel.add(CpuTempHandCard);

		//create ShareTable
		JPanel ShareTable = new JPanel();
		//ShareTable.setBackground(Color.DARK_GRAY);
		ShareTable.setPreferredSize(new Dimension(1000,200));
		ShareTable.setLayout(new FlowLayout(FlowLayout.CENTER, 120, 20));

		TableView[0] = new CardView();
		TableView[1] = new CardView();

		HandCardPanel ShareCard0 = new HandCardPanel();
		HandCardPanel ShareCard1 = new HandCardPanel();

		ShareCard0.setLayout(new BorderLayout());
		ShareCard1.setLayout(new BorderLayout());

		ShareCard0.add(TableView[0]);
		ShareCard1.add(TableView[1]);

		ShareTable.add(ShareCard0);
		ShareTable.add(ShareCard1);

		//create RadioButtonPanel
		JPanel RadioButtonPanel = new JPanel();
		//RadioButtonPanel.setBackground(Color.WHITE);
		RadioButtonPanel.setPreferredSize(new Dimension(1000,40));
		RadioButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 200, 10));

		ButtonGroup radiogroup = new ButtonGroup();
		radiogroup.add(radio[0]);
		radiogroup.add(radio[1]);

		RadioButtonPanel.add(radio[0]);
		RadioButtonPanel.add(radio[1]);

		//create playerArea
		JPanel PlayerPanel = new JPanel();
		PlayerPanel.setPreferredSize(new Dimension(1000,200));
		PlayerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

		HandCardPanel[] PlayerHandCard = new HandCardPanel[4];

		for(int i=0 ; i<4 ; i++) {
			PlayerView[i] = new CardView();
			PlayerHandCard[i] = new HandCardPanel();
			PlayerHandCard[i].setLayout(new BorderLayout());
			PlayerHandCard[i].add(PlayerView[i]);
		}

		HandCardPanel PlayerTempHandCard = new HandCardPanel();
		DeckCardPanel PlayerDeckCard = new DeckCardPanel();


		PlayerPanel.add(PlayerTempHandCard);
		for(int i=0 ; i<4 ; i++) {
			PlayerPanel.add(PlayerHandCard[i]);
		}
		PlayerPanel.add(PlayerDeckCard);

		//create ButtonPanel
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setPreferredSize(new Dimension(1000,40));
		ButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 10));

		for(int i=0 ; i<4 ; i++) {
			Action[i] = new ButtonAction(PlayerView[i],radio,TableView);
			button[i].addActionListener(Action[i]);
			ButtonPanel.add(button[i]);
		}

		//add Panel on UserFrame
		this.add(CPUPanel);
		this.add(ShareTable);
		this.add(RadioButtonPanel);
		this.add(PlayerPanel);
		this.add(ButtonPanel);

		//confirm placement
		this.pack();
		//display the window
		this.setVisible(true);
	}

	public CardView[] getCpuView() {
		return CpuView;
	}

	public CardView[] getPlayerView() {
		return PlayerView;
	}

	public CardView[] getTableView() {
		return TableView;
	}

	public ButtonAction[] getButtonAction() {
		return Action;
	}

	/* 現在のテーブル・手札の内容を全て反映させる */
	public void update(final Table table) {
		// テーブル
		for (int i = 0; i < 2; i++) {
			TableView[i].setNumber(table.getCentralCard(i).getNumber());
			TableView[i].updated();
		}
		
		// ManualPlayer
		for (int i = 0; i < 4; i++) {
			PlayerView[i].setNumber(table.getPlayer(0).getAtHandCard(i).getNumber());
			PlayerView[i].updated();
		}

		// AutoPlayer
		for (int i = 0; i < 4; i++) {
			CpuView[i].setNumber(table.getPlayer(1).getAtHandCard(i).getNumber());
			CpuView[i].updated();
		}
	}
}

class HandCardPanel extends JPanel{
	public HandCardPanel() {
		this.setPreferredSize(new Dimension(100,150));
		//this.setBackground(Color.WHITE);
	}
}

class DeckCardPanel extends JPanel{
	public DeckCardPanel() {
		this.setPreferredSize(new Dimension(100,150));
		this.setBackground(Color.RED);
	}
}

interface CardListener{
	public void updated();
}

class CardView extends JPanel implements CardListener{
	private int setNumber;
	private int width;
	private int height;
	private int RoundSize = 2;

	@Override
	public void updated() {
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		/* 未実装 */
		super.paint(g);

		width = this.getWidth();
		height = this.getHeight();

		g.setColor(Color.white);
		g.fillRect(0,0,width,height);

		g.setColor(Color.black);
		g.fillRect(0,0,width,RoundSize);
		g.fillRect(0,0,RoundSize,height);
		g.fillRect(width-RoundSize,0,width,height);
		g.fillRect(0,height-RoundSize,width,height);

		g.setFont(new Font("Standard",Font.PLAIN,50));
		if(1 <= setNumber && setNumber <= 9) {
			g.drawString(String.valueOf(setNumber),(width/2)-12,(height/2)+15);
		}else if(setNumber == 10){
			g.drawString(String.valueOf(setNumber),(width/2)-30,(height/2)+15);
		}else if(setNumber == 11) {
			g.drawString("J",(width/2)-12,(height/2)+15);
		}else if(setNumber == 12) {
			g.drawString("Q",(width/2)-12,(height/2)+15);
		}else if(setNumber == 13) {
			g.drawString("K",(width/2)-12,(height/2)+15);
		}
	}

	public void setNumber(int Number) {
		this.setNumber = Number;
	}

	public int getNumber() {
		return setNumber;
	}
}
