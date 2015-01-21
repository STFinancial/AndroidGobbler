package AndroidGobbler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class AI extends GenericPlayer {

	public AI(Team team, Table t) {
		super(team, t);
		AI = true;
	}
	
	public AI(){}
	
	
	public Move getNextMove(Table t) {
		ArrayList<Move> moves = generateMoves(team, t);
		ArrayList<MoveScorePair> msp = new ArrayList<MoveScorePair>(moves.size());
		
		MoveScorePair cur;
		for (int i = 0; i < moves.size(); ++i) {
			cur = new MoveScorePair(moves.get(i));
			t.minimaxPerformMove(cur.move);
			cur.score = minimax(t, 3, false);
			t.minimaxUndoMove(cur.move);
			msp.add(cur);
		}
		
		Collections.sort(msp, new MoveScorePair());
		
		return msp.get(0).move;
	}
	
	private class MoveScorePair implements Comparator<MoveScorePair> {
		public Move move;
		public int score;
		
		public MoveScorePair() {}
		
		public MoveScorePair(Move m) {
			move = m;
		}
		
		@Override
		public int compare(MoveScorePair arg0, MoveScorePair arg1) {
			if (arg0.score < arg1.score) {
				return 1;
			} else if (arg0.score > arg1.score) {
				return -1;
			} else {
				return 0;
			}
		}
		
	}
	
	public static ArrayList<Move> generateMoves(Team team, Table t) {
		ArrayList<Move> moves = new ArrayList<Move>();
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		ArrayList<Integer> pieceIndex = new ArrayList<Integer>();
		
		StackWrapper[] board = t.board.board;
		//generate all of the moves
		
		//check for stack symmetry
		int SI, SI2, SI3;
		if (team == Team.RED) {
			SI = 19;
		} else {
			SI = 16;
		}
		
		SI2 = SI + 1;
		SI3 = SI + 2;
		int SS1 = board[SI].getStackSize();
		int SS2 = board[SI2].getStackSize();
		int SS3 = board[SI3].getStackSize();
		
		//if the stack sizes are the same, we don't need to generate moves for those
		boolean stack2 = SS1 != SS2;
		boolean stack3 = SS1 != SS3 && SS2 != SS3;
		
		//generate stack moves
		Move newMove;
		Piece stackPiece1 = board[SI].peek();
		boolean sp1 = stackPiece1 != null;
		Piece p;
		
		//Always generate for stack 1
		for (int i = 0; i < 16; i++) {
			p = board[i].peek(); //get all the pieces
			if (p != null && p.team == team) {
				pieces.add(p); //This list keeps track of all of the player's piece on the board
				pieceIndex.add(i); //This probably won't be needed anymore
			} else if (sp1) {
				newMove = new Move(Move.MoveType.STACK, t.getPlayer(team), board[SI].peek(), SI, i);
				if (t.validateMove(newMove)) {
					moves.add(newMove);
				}
			}
		}
		
		if (stack2) {
			Piece stackPiece2 = board[SI2].peek();
			if (stackPiece2 != null) {
				for (int i = 0; i < 16; i++) {
					newMove = new Move(Move.MoveType.STACK, t.getPlayer(team), stackPiece2, SI2, i);
					if (t.validateMove(newMove)) {
						moves.add(newMove);
					}
				}
			}
		}
		if (stack3) {
			Piece stackPiece3 = board[SI3].peek();
			if (stackPiece3 != null) {
				for (int i = 0; i < 16; i++) {
					newMove = new Move(Move.MoveType.STACK, t.getPlayer(team), stackPiece3, SI3, i);
					if (t.validateMove(newMove)) {
						moves.add(newMove);
					}
				}
			}
		}
		
		//check for board moves
		Piece tempPiece;
		for (int j = 0; j < pieces.size(); j++) {
			tempPiece = pieces.get(j);
			for (int i = 0; i < 16; i++) {
				newMove = new Move(Move.MoveType.BOARD, t.getPlayer(team), tempPiece, tempPiece.stackIndex, i);
				if (t.validateMove(newMove)) {
					moves.add(newMove);
				}
			}
		}
		
		return moves;
	}
	
	
	
//	public int scoreBoard(Board b, Team team) {
//		int score = 0;
//		int[][] lines = Constants.lines;
//		score += scoreLines(lines[0], b, team);
//		score += scoreLines(lines[6], b, team);
//		score += scoreLines(lines[11], b, team);
//		score += scoreLines(lines[13], b, team);
//		return score;
//	}
//	
//	private int scoreLines(int[] lines, Board b, Team team) {
//		StackWrapper sw;
//		Piece p;
//		int pTotal = 0;
//		int eTotal = 0;
//		int total = 0;
//		
//		for (int j = 0; j < 3; ++j) {
//			pTotal = 0;
//			eTotal = 0;
//			if (lines[j * 4] == -1) {
//				continue;
//			}
//			for (int i = 0; i < 4; ++i) {
//				sw = b.board[lines[(j * 4) + i]];
//				p = sw.peek();
//				if (p == null) {
//					continue;
//				} else {
//					if (p.team == team) {
//						++pTotal;
//					} else {
//						++eTotal;
//					}
//				}
//			}
//			
//			switch(pTotal) {
//			case 0:
//			case 1:
//				break;
//			case 2:
//				pTotal = 4;
//				break;
//			case 3:
//				pTotal = 8;
//				break;
//			case 4:
//				pTotal = 1000;
//				break;
//			default:
//				System.out.println("Shouldn't get here. Board score switch 1");
//				break;
//			}
//			
//			switch(eTotal) {
//			case 0:
//			break;
//			case 1: 
//				eTotal = -1;
//				break;
//			case 2:
//				eTotal = -4;
//				break;
//			case 3:
//				eTotal = -8;
//				break;
//			case 4:
//				eTotal = -1000;
//				break;
//			default:
//				System.out.println("Shouldn't get here. Board score switch 2");
//				break;
//			}
//			System.out.print(pTotal + eTotal);
//			total += pTotal + eTotal;
//			System.out.println("   " + total);
//		}
//		return total;
//	
//	}
	
	public static int minimax(Table table, int depth, boolean maximizingPlayer) {
		if (depth == 0 || isLeaf(table)) {
			return Utility.scoreBoard2(table.board, table.whosTurn);
		}
		int bestValue;
		int currentValue;
		ArrayList<Move> successors;
		if (maximizingPlayer) {
			bestValue = -10000;
			successors = generateMoves(table.whosTurn, table);
			for (Move successor: successors) {
				table.minimaxPerformMove(successor);
				currentValue = minimax(table, depth - 1, false);
				table.minimaxUndoMove(successor);
				bestValue = Math.max(currentValue, bestValue);
			}
			return bestValue;
		} else {
			bestValue = 10000;
			successors = generateMoves(table.whosTurn, table);
			for (Move successor: successors) {
				table.minimaxPerformMove(successor);
				currentValue = minimax(table, depth - 1, true);
				table.minimaxUndoMove(successor);
				bestValue = Math.min(currentValue, bestValue);
			}
			return bestValue;
		}
	}
	
	private static boolean isLeaf(Table table) {
		Team t = table.board.getWinner2();
		if (t == null) {
			return false;
		} else {
			return true;
		}
	}
	
	
	
//	private Move getOppositeMove(Move move) {
//		return new Move(move.piece, move.start, move.end);
//	}

}
