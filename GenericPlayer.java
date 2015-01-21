package AndroidGobbler;

public class GenericPlayer {
	public Table t;
	public Team team;
	public StackWrapper[] stacks;
	public int[] stackSize;
	public Piece[] pieces;
	
	public boolean AI;
	
	public GenericPlayer(){}
	
	public GenericPlayer(Team team, Table table) {
		stacks = new StackWrapper[3];
		t = table;
		this.team = team;
		stackSize = new int[]{4, 4, 4};
		
		//Assign the correct stacks
		if (team == Team.BLACK) {
			stacks[0] = t.board.board[16];
			stacks[1] = t.board.board[17];
			stacks[2] = t.board.board[18];
		} else {
			stacks[0] = t.board.board[19];
			stacks[1] = t.board.board[20];
			stacks[2] = t.board.board[21];
		}
		
		pieces = new Piece[12];
		for (int i = 0; i < 12; ++i) {
			pieces[i] = stacks[i / 4].peek(i % 4);
		}
	}
	
	public void updateStackSizes() {
		stackSize[0] = stacks[0].getStackSize();
		stackSize[1] = stacks[1].getStackSize();
		stackSize[2] = stacks[2].getStackSize();
	}
}
