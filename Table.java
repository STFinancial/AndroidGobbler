package AndroidGobbler;

import java.util.Random;

public class Table {
	public GobblerGUI gui;
	
	public Board board;
	public GenericPlayer red; 
	public GenericPlayer black; 
	
	public Team whosTurn; //true is RED, false is BLACK... RED always starts
	public GenericPlayer currentPlayer; //the player whose move we are waiting on
	
	public Table(int numPlayers) {
		board = new Board(this);
		
		
		//Assign the correct players
		if (numPlayers > 0) {
			Random r = new Random();
			if (r.nextBoolean()) {
				red = new Person(Team.RED, this);
				if (numPlayers == 1) {
					black = new AI(Team.BLACK, this);
				} else {
					black = new Person(Team.BLACK, this);
				}
 			} else {
				black = new Person(Team.BLACK, this);
				if (numPlayers == 1) {
					red = new AI(Team.RED, this);
				} else {
					red = new Person(Team.RED, this);
				}
 			}
		} else {
			//there are no players
			//AI duel
			red = new AI(Team.RED, this);
			black = new AI(Team.BLACK, this);
		}
		
		//RED always goes first
		currentPlayer = red;
		whosTurn = Team.RED;
		if (currentPlayer.AI) {
			System.out.println("AI is red");
		} else {
			System.out.println("AI is black");
		}
	}
	
	/**
	 * Performs the specified move and updates the turn. Assumes valid move input.
	 * @param m - The move to be performed.
	 */
	public void performMove(Move m) {
		board.performMove(m);
		currentPlayer.updateStackSizes();
		updateTurn();
		gui.boardPanel.repaint();
		if (currentPlayer.AI) {
			Move m2 = ((AI) currentPlayer).getNextMove(this);
			System.out.println(m2.toString());
		}
	}
	
	public void minimaxPerformMove(Move m) {
		board.performMove(m);
		updateTurn();
	}
	
	public void minimaxUndoMove(Move m) {
		board.undoMove(m);
		updateTurn();
	}
	
	
	/**
	 * Generates a move given the starting indices.
	 * @param start - the starting position of the move
	 * @param end - the ending position of the move
	 * @return returns a generated move that has an invalid tag if it's bad
	 */
	public Move generateMove(int start, int end) {
		Move m = new Move();
		if (start == -1 || end == -1) {
			m.type = Move.MoveType.INVALID;
			return m;
		}
		
		m.start = start;
		m.end = end;
		m.piece = board.board[start].peek();
		m.player = currentPlayer;
		if (validateMove(m)) {
			if (m.start > 15) {
				m.type = Move.MoveType.STACK;
			} else {
				m.type = Move.MoveType.BOARD;
			}
		} else {
			m.type = Move.MoveType.INVALID;
		}
		return m;
	}
	
	/**
	 * Checks to see whether the proposed move is valid.
	 * @param m - The move to be validated.
	 * @return A boolean indicating whether the move is currently allowed.
	 */
	public boolean validateMove(Move m) {
		//TODO: Validate starting indices. (Seems fine now, ensure one more time later)
		if (m.piece == null) {
			//System.out.println("Move invalid, piece gone");
			return false;
		}
		Team moverTeam = m.piece.team;
		
		if (m.type == Move.MoveType.INVALID) {
			//System.out.println("Move type set to invalid");
			return false;
		}
		
		if (whosTurn != moverTeam) {
			//System.out.println("Move invalid because not player's turn");
			return false; //invalid if its not our turn
		}
		
		if (m.end > 15) {
			//System.out.println("Move invalid: end index too large");
			return false; //cannot move a piece from the board to one of our stacks
		}
		
		Piece coveredPiece = board.board[m.end].peek();
		
		if (coveredPiece != null) {
			//then we are trying to cover a piece
			if (coveredPiece.power >= m.piece.power) {
				//System.out.println("Move invalid: Cannot cover stronger piece");
				return false; //can't cover a piece of equal or greater power
			}
			
			if (m.start > 15) { //we're trying to cover from the stack
				if (moverTeam == coveredPiece.team) {
					//System.out.println("Move invalid: Cannot cover piece from own stack");
					return false; //can't cover your own piece from the stack
				}
				
				if (!board.check3Exception(m.end, moverTeam)) {
					//System.out.println("Move invalid: Check 3 Exception");
					return false; //can't cover from the stack for the win
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the player corresponding to the passed in team color. Assumes no null value.
	 * @param team
	 * @return
	 */
	public GenericPlayer getPlayer(Team team) {
		 if (team == Team.BLACK) {
			 return black;
		 } else {
			 return red;
		 }
	}
	
	
	/**
	 * Called at the end of the turn to update the current player pointer
	 */
	private void updateTurn() {
		if (whosTurn == Team.RED) {
			currentPlayer = black;
			whosTurn = Team.BLACK;
		} else {
			currentPlayer = red;
			whosTurn = Team.RED;
		}
	}
}
