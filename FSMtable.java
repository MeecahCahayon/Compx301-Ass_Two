import java.util.*;
import java.io.*;

class FSMtable  {

	Node head;

	/* METHODS */

	//ADD TO THE END OF THE LIST
	public void add(Node states) {

		//IF LIST IS EMPTY
		if (head == null) {
			
			//THE NEW STATE IS THE HEAD
			head = states;
			return;
		}

		Node curr = head;

		//GET TO THE END OF THE LIST
		while (curr.getNext() != null) {

			curr = curr.getNext();
		}

		//SET NEW STATE AT THE END OF THE LIST
		curr.setNext(states);
		return;
	}

	//RETURN THE NODE FOR THE PASSED STATE NUMBER
	public Node findState(String state) {

		Node curr = head;

		//IF NOT THE SAME STATE
		while (state.compareTo(curr.getState()) != 0) {
			
			//GO NEXT
			curr = curr.getNext();
		}

		return curr;
	}

	//FOR TESTING
	public void displayFSM() {

		Node curr = head;

		//WHILE NOT END OF THE LIST
		while (curr != null) {
		
			System.out.println(curr.getState() + ", " + curr.getChar() + ", " + curr.getBranch1() + ", " + curr.getBranch2());
			curr = curr.getNext();
		}

		return;
	}
}