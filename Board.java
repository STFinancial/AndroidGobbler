package AndroidGobbler;

import java.util.ArrayList;
import java.util.Comparator;


public class Board implements Comparator<Board> {
	StackWrapper[] board;
	Table t;
	int[] scoreBoardRed = {0,0,0,0,0,0,0,0,0,0};
	int[] scoreBoardBlack = {0,0,0,0,0,0,0,0,0,0};
	
	public Board(Table t) {
		this.t = t;
		
		board = new StackWrapper[22];
		for (int i = 0; i < 22; ++i) {
			board[i] = new StackWrapper(this, i);
			if (i > 18) {
				for (int j = 0; j < 4; ++j) {
					board[i].push(new Piece(i, j + 1, Team.RED));
				}
			} else if (i > 15) {
				for (int j = 0; j < 4; ++j) {
					board[i].push(new Piece(i, j + 1, Team.BLACK));
				}
			}
		}
	}
	
	
	/**
	 * Checks to see if the specified team is allowed to cover the piece at the given index.
	 */
	public boolean check3Exception(int index, Team team){
		int total, i, j;
		int cur;
		int[] lines = Constants.lines[index];
		Piece topPiece;
		for (i = 0; i < 3; ++i) {
			total = 0;
			for (j = 0; j < 4; ++j) {
				cur = lines[i * 4 + j];
				if (cur > 0) {
					topPiece = board[cur].peek();
					if (topPiece != null && topPiece.team == team) {
						total++;
					}
				}
				if (total > 2) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	/**
	 * Returns a list of stacks for each line of the board intersecting the index.
	 * @param index - The index at which we want to find all lines that intersect it
	 * @return A list of stacks
	 */
	public ArrayList<StackWrapper> getLines(int index){
		ArrayList<StackWrapper> lines = new ArrayList<StackWrapper>();
		boolean notFoundIndex = true;
		int[] curLine;
		int curIndex;
		curLine = Constants.lines[index];
		for (int i = 0; i < 12; ++i) {
			curIndex = curLine[i];
			if (i < 0) {
				//negatives are substituted for no diagonal
			} else {
				if (curIndex == index) {
					if (notFoundIndex) {
						notFoundIndex = false;
						lines.add(board[curIndex]);
					}
				} else {
					lines.add(board[curIndex]);
				}
			}
		}
		return lines;
	}
	
	
	/**
	 * Performs the specified move, assuming it is valid.
	 * @param m - The move to perform.
	 */
	public void performMove(Move m) {
		Piece covered = board[m.end].peek(); //need to check if we're covering a piece so we can update score properly
		board[m.end].push(board[m.start].pop());
		Piece uncovered = board[m.start].peek();
		m.piece.stackIndex = m.end;
		setScore(m, uncovered, covered);
	}
	
	private void setScore(Move m, Piece uncovered, Piece covered) {
		//given that we have the move, we need to only to update the spots that were affected
		int[] scoreBoard;
		int[] start;
		int[] end;
		if (m.player.team == Team.RED) {
			scoreBoard = scoreBoardRed;
		} else {
			scoreBoard = scoreBoardBlack;
		}
		
		if (m.start < 16) {
			start = Constants.linesCorrespondance[m.start];
			for (int i = 0; i < start.length; ++i) {
				--scoreBoard[start[i]];
			}
			
			if (uncovered != null) {
				if (uncovered.team == Team.RED) {
					for (int i = 0; i < start.length; ++i) {
						++scoreBoardRed[start[i]];
					}
				} else {
					for (int i = 0; i < start.length; ++i) {
						++scoreBoardBlack[start[i]];
					}
				}
			}
		}
		end = Constants.linesCorrespondance[m.end];
		for (int i = 0; i < end.length; ++i) {
			++scoreBoard[end[i]];
		}
		if (covered != null) {
			if (covered.team == Team.RED) {
				for (int i = 0; i < end.length; ++i) {
					--scoreBoardRed[end[i]];
				}
			} else {
				for (int i = 0; i < end.length; ++i) {
					--scoreBoardBlack[end[i]];
				}
			}
		}
	}
	
	
	/**
	 * Undoes the specified move, assuming it is valid to undo.
	 * @param m - The move to undo.
	 */
	public void undoMove(Move m) {
		Piece covered = board[m.start].peek(); //need to check if we're covering a piece so we can update score properly
		board[m.start].push(board[m.end].pop());
		Piece uncovered = board[m.end].peek();
		m.piece.stackIndex = m.start;
		setScoreUndo(m, uncovered, covered);
	}
	
	private void setScoreUndo(Move m, Piece uncovered, Piece covered) {
		//given that we have the move, we need to only to update the spots that were affected
		int[] scoreBoard;
		int[] start;
		int[] end;
		if (m.player.team == Team.RED) {
			scoreBoard = scoreBoardRed;
		} else {
			scoreBoard = scoreBoardBlack;
		}	
		
		if (m.end < 16) {
			end = Constants.linesCorrespondance[m.end];
			for (int i = 0; i < end.length; ++i) {
				--scoreBoard[end[i]];
			}
			if (uncovered != null) {
				if (uncovered.team == Team.RED) {
					for (int i = 0; i < end.length; ++i) {
						++scoreBoardRed[end[i]];
					}
				} else {
					for (int i = 0; i < end.length; ++i) {
						++scoreBoardBlack[end[i]];
					}
				}
			}
		}
		if (m.start < 16) {
			start = Constants.linesCorrespondance[m.start];
			for (int i = 0; i < start.length; ++i) {
				++scoreBoard[start[i]];
			}
			if (covered != null) {
				if (covered.team == Team.RED) {
					for (int i = 0; i < start.length; ++i) {
						--scoreBoardRed[start[i]];
					}
				} else {
					for (int i = 0; i < start.length; ++i) {
						--scoreBoardBlack[start[i]];
					}
				}
			}
		}
		
	}
	
	
	
	public void testMove(Move m) {
		board[m.end].push(m.piece);
	}
	
	
	
	/**
	 * Returns the team of the winner. Null if there is no winner.
	 * The function assumes that there can be only one winner at the time of checking.
	 */
	public Team getWinner2() {
		for (int i = 0; i < 10; ++i) {
			if (scoreBoardRed[i] > 3) {
				return Team.RED;
			}
		}
		for (int j = 0; j < 10; ++j) {
			if (scoreBoardBlack[j] > 3) {
				return Team.BLACK;
			}
		}
		return null;
	}
	
//	/**
//	 * Returns the team of the winner. Null if there is no winner.
//	 * The function assumes that there can be only one winner at the time of checking.
//	 */
//	public Team getWinner() {
//		boolean foundWinner = false;
//		int[][] lines = Constants.lines;
//		int[][] winningLines = {lines[0], lines[6], lines[11], lines[13]};
//		
//		int[] line;
//		Team curColor = null;
//		Piece p;
//		for (int i = 0; i < 4; ++i) {
//			line = winningLines[i];
//			for (int j = 0; j < 3; ++j) {
//				if (line[j * 4] >= 0) {
//					p = board[line[j * 4]].peek();
//				} else {
//					continue;
//				}
//				if (p == null) { 
//					continue; 
//				} else {
//					curColor = p.team;
//				}
//				
//				for (int k = 1; k < 4; ++k) {
//					p = board[line[(j * 4) + k]].peek();
//					if (p == null) {
//						break;
//					} else if (p.team != curColor) {
//						break;
//					}
//					if (k == 3) { 
//						foundWinner = true;
//					}
//				}
//				if (foundWinner) { break; }
//			}
//			if (foundWinner) { break; }
//		}
//		if (foundWinner) {
//			return curColor;
//		} else {
//			return null;
//		}
//	}



	@Override
	public int compare(Board b1, Board b2) {
//		if (b1.score < b2.score) {
//			return 1;
//		} else if (b1.score > b2.score) {
//			return -1;
//		} else {
//			return -1;
//		}
		return 1;
	}
}
