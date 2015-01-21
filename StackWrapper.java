package AndroidGobbler;


public class StackWrapper{
	public int position;
	public GameStack<Piece> stack;
	public Board board;
	
	public StackWrapper(Board board, int pos){
		position = pos;
		this.board = board;
		stack = new GameStack<Piece>();
	}
	
	public void render() {
		
	}
	
	public boolean isEmpty(){
		return stack.isEmpty();
	}
	
	public int getStackSize() {
		return stack.getSize();
	}
	
	
	public Piece peek() {
		return stack.peek();
	}
	
	public Piece peek(int n) {
		return stack.peek(n);
	}
	
	public Piece pop() {
		return stack.pop();
	}
	
	public void push(Piece p) {
		stack.push(p);
	}

	
	
}
