import java.util.*;
import java.io.*;

class Deque {

	private Node head;
	private Node scan;

	/* CONSTRUCTOR */

	//change
	public Deque() {

		//SETTING 'SCAN' TO SEPARATE AND INITIAL STATE
		scan = new Node("SCAN");
		Node initial = new Node("0");

		head = initial;
		head.setNext(scan);
	}

	/* METHODS */

	//ADD TO THE TOP OF THE LIST THEN POP
	public void put(String _branch1, String _branch2) {

		Node branch1 = new Node(_branch1);
		Node branch2 = new Node(_branch2);
		
		//IF BRANCHES ARE DIFFERENT
		if (_branch1.compareTo(_branch2) != 0) {

			branch1.setNext(branch2);
			branch2.setNext(head.getNext());
			head = branch1;
		}
		else {
			
			branch1.setNext(head.getNext());
			head = branch1;
		}
	}

	//ADD TO THE END OF THE LIST
	public void push(String state) {

		if (head == null) {
			
			head = new Node(state);
			return;
		}

		Node curr = head;

		//WHILE NOT END OF THE LIST
		while (curr.getNext() != null) {
			
			curr = curr.getNext();
		}

		curr.setNext(new Node(state));
		return;
	}

	//POP FROM THE TOP OF THE LIST
	public void pop() {

		head = head.getNext();
	}

	public void clear() {

		//SETTING 'SCAN' TO SEPARATE AND INITIAL STATE
		scan = new Node("SCAN");
		Node initial = new Node("0");

		head = initial;
		head.setNext(scan);
	}

	/* GETTER AND SETTERS */

	public Node getHead() {

		return head;
	}
}