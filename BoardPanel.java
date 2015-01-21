package AndroidGobbler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6486121233879849248L;
	int width;
	int height;
	Board board;
	GobblerGUI gui;
	AI ai;
	
	int dragStart;
	
	public BoardPanel(GobblerGUI gui, Board board, int width, int height) {
		this.gui = gui;
		this.board = board;
		this.width = width;
		this.height = height;
		addMouseListener(this);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Piece p;
		int renX = 0;
		int renY = 0;
		for (int i = 0; i < board.board.length; ++i) {
			p = board.board[i].peek();
			if (p != null) {
				switch (p.team) {
				case BLACK:
					g.setColor(gui.BLACK);
					break;
				case RED:
					g.setColor(gui.RED);
					break;
				default:
					g.setColor(Color.WHITE);
					break;
				}
			} else {
				g.setColor(Color.WHITE);
			}
			
			
			if (i < 16) {
				renX = (i % 4) * gui.SQUARE_SIZE;
				renY = (i / 4) * gui.SQUARE_SIZE;
			}
			else if (i < 19) {
				renX = (i - 16) * gui.SQUARE_SIZE;
				renY = 5 * gui.SQUARE_SIZE;
			}
			else if (i < 22) {
				renX = 5 * gui.SQUARE_SIZE;
				renY = (i - 19) * gui.SQUARE_SIZE;
			}
			g.fillRect(renX, renY, gui.SQUARE_SIZE, gui.SQUARE_SIZE);
			if (p != null) {
				g.setColor(Color.GREEN);
				g.drawString("" + p.power, renX + (gui.SQUARE_SIZE / 3), (int) (renY + (gui.SQUARE_SIZE / 1.8)));
			}
		}
		g.drawString("Turn: " + board.t.whosTurn.toString(), 400, 25);
		Team winner = board.getWinner2();
		if (winner == null) {
			g.drawString("Winner: None", 400, 55);
		} else {
			g.drawString("Winner: " + winner.toString(), 400, 55);
		}
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {/*TODO*/}


	@Override
	public void mouseEntered(MouseEvent arg0) {/*TODO*/}


	@Override
	public void mouseExited(MouseEvent arg0) {/*TODO*/}


	@Override
	public void mousePressed(MouseEvent arg0) {
		int normX = arg0.getX() / gui.SQUARE_SIZE;
		int normY = arg0.getY() / gui.SQUARE_SIZE;
		
		//TODO Use these in a meaningful way when there is a more sophisticated gui
//		int panelWidth = getWidth();
//		int panelHeight = getHeight();
		
		int position = -1;
		if (normX > 3 || normY > 3) {
			if (normX == 5) {
				if (normY < 3) {
					position = 19 + normY;
				}
			} else if (normY == 5) {
				if (normX < 3) {
					position = 16 + normX;
				}
			}
		} else {
			position = normX + (4 * normY);
		}
		dragStart = position;
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		int normX = arg0.getX() / gui.SQUARE_SIZE;
		int normY = arg0.getY() / gui.SQUARE_SIZE;
		
		//TODO Use these in a meaningful way when there is a more sophisticated gui
//		int panelWidth = getWidth();
//		int panelHeight = getHeight();
		
		int position = -1;
		if (normX > 3 || normY > 3) {
			if (normX == 5) {
				if (normY < 3) {
					position = 19 + normY;
				}
			} else if (normY == 5) {
				if (normX < 3) {
					position = 16 + normX;
				}
			}
		} else {
			position = normX + (4 * normY);
		}
		Move m;
		m = board.t.generateMove(dragStart, position);
		if (m.type != Move.MoveType.INVALID) {
			board.t.performMove(m);
		} else {
			System.out.println("Generated move was invalid.");
			repaint();
		}
		
	}


	

}
