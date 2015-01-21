package stf;

public class Move {
	Piece piece;
	int start;
	int end;
	/** Used to keep track of who is making the move to get at player variables	 */
	GenericPlayer player;
	MoveType type;
	
	public Move(){};
	
	public Move(MoveType type, GenericPlayer player, Piece p, int from, int to) {
		piece = p;
		this.start = from;
		this.end = to;
		this.player = player;
		this.type = type;
	}
	
	
	@Override
	public String toString() {
		return "Moving piece of power: " + piece.power + "  from " + start + " to " + end;
	}
	
	public enum MoveType {
		STACK, BOARD, INVALID;
	}
}
