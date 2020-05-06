import java.util.*;
import java.io.*;

class Node {

	//DECLARE VARIABLES
	private String _state;
	private String _char;
	private String _branch1;
	private String _branch2;
	private Node _next;

	/* CONSTRUCTOR */
	
	//NODE FOR DEQUE
	public Node(String state) {

		_state = state;
	}

	//NODE FOR FSM
	public Node(String state, String ch, String branch1, String branch2) {

		_state = state;
		_char = ch;
		_branch1 = branch1;
		_branch2 = branch2;
	}

	/* GETTER AND SETTERS */

	public String getState() {
		return _state;
	}

	public String getChar() {
		return _char;
	}

	public void setChar(String ch) {
		_char = ch;
	}

	public String getBranch1() {
		return _branch1;
	}

	public void setBranch1(String branch1) {
		_branch1 = branch1;
	}

	public String getBranch2() {
		return _branch2;
	}

	public void setBranch2(String branch2) {
		_branch2 = branch2;
	}

	public Node getNext() {
		return _next;
	}

	public void setNext(Node next) {
		_next = next;
	}
}