package AndroidGobbler;

public class GameStack <T>{
	private int size;
	private StackNode head;
	
	public GameStack() {
		head = null;
		size = 0;
	}

	/**
	 * 
	 * @return True if the stack has 0 elements in it, false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns the number of items on the stack.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Peeks at a depth of n on the stack.
	 * @param n - The depth at which to peek.
	 * @return The value at depth n of the stack. Null if empty or the index is too large. The top of the stack if index is non-positive
	 */
	public T peek(int n) {
		StackNode cur = head; //if  0 we return the head
		
		while (n > 0 && cur != null) {
			n--;
			cur = cur.next;
		}
		if (cur == null) {
			return null;
		} else {
			return cur.data;
		}
	}
	
	/**
	 * Returns the top value on the stack. Null if it is empty.
	 */
	public T peek() {
		if (head == null) {
			return null;
		}
		return head.data;
	}
	
	/**
	 * Takes the top value off the stack and returns it.
	 * @return The top value on the stack. Null if the stack is empty.
	 */
	public T pop() {
		if (head == null) {
			return null;
		}
		T d = head.data;
		head = head.next;
		size--;
		return d;
	}
	
	/**
	 * Pushes the item on to the top of the stack.
	 * @param data - The item to push.
	 */
	public void push(T data) {
		StackNode n = new StackNode(data);
		n.next = head;
		head = n;
		size++;
	}
	
	
	
	
	private class StackNode {
		StackNode next;
		T data;
		
		StackNode(T data) {
			next = null;
			this.data = data;
		}
	}
}
