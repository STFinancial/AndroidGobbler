package stf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GobblerGUI {
	public final int FRAME_WIDTH = 700;
	public final int FRAME_HEIGHT = 500;
	
	public final int MAIN_PANEL_WIDTH = 700;
	public final int MAIN_PANEL_HEIGHT = 500;
	
	public final int BOARD_PANEL_WIDTH = 500;
	public final int BOARD_PANEL_HEIGHT = 500;
	
	public final Color BLACK = Color.BLACK;
	public final Color RED = Color.RED;
	
	public final int SQUARE_SIZE = 50;
	
	public Application app;
	public Board board;
	public JPanel mainPanel;
	public BoardPanel boardPanel;
	public JFrame frame;
	
	public GobblerGUI(Application app, Board board){
		this.board = board;
		this.app = app;
		createAndShowGUI();
	}
	
	public void createAndShowGUI() {
		frame = new JFrame("Gobblers");
		mainPanel = new JPanel();
		mainPanel.setSize(new Dimension(MAIN_PANEL_WIDTH, MAIN_PANEL_HEIGHT));
		mainPanel.setLayout(new FlowLayout());
		frame.add(mainPanel);
		
		boardPanel = new BoardPanel(this, board, BOARD_PANEL_WIDTH, BOARD_PANEL_HEIGHT);
		frame.add(boardPanel);
		
		frame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	
}
