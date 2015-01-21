package stf;

import java.util.Random;

public class Application {
	GobblerGUI gui;
	Board board;
	Table table;
	
	public static void main(String[] args) {
		Application app = new Application();
		app.initialize();
	}
	
	public void initialize() {
		table = new Table(1);
		board = table.board;
		gui = new GobblerGUI(this, board);
		table.gui = gui;
		//testRandomMoves();
		
		/**
		 * Won't render unless I wait a sec
		 */
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {e.printStackTrace();}
		stuff();
	}
	
	public void stuff() {
		gui.boardPanel.repaint();
	}
	
	public void testRandomMoves() {
		int i = 100000000;
		int action;
		Piece p;
		Move m;
		Random r = new Random();
		while ((i--) > 0) {
			action = r.nextInt(3);
			switch (action) {
			case 0:
				p = new Piece((i % 4) + 1, Constants.teams[i % 2]);
				m = new Move();
				m.piece = p;
				m.end = i % 16;
				board.testMove(m);
				break;
			case 1:
				board.board[i % 16].pop();
				break;
				
			case 2:
				m = new Move();
				
				m.start = i % (r.nextInt(16) + 1);
				if (board.board[m.start].peek() == null) {
					break;
				}
				m.end = i % (r.nextInt(16) + 1);
				if (m.end == m.start && board.board[m.end].peek() == null) {
					break;
				}
				m.piece = board.board[m.start].peek();
				board.performMove(m);
				break;
			}
			gui.boardPanel.repaint();
		}
	}
}
