package AndroidGobbler;

public class Piece {
	public int power; //power is 1 - 4
	public Team team;
	public int stackIndex;
	
	public Piece(int power, Team team) {
		this.power = power;
		this.team = team;
	}
	
	public Piece(int stack, int power, Team team) {
		this.power = power;
		this.team = team;
		stackIndex = stack;
	}
}
