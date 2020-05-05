import java.util.*;
import java.io.*;

class Deque {

	Node head;
	Node tail;

	public Deque() {

		//setting SCAN to separate
		head = new Node("SCAN");
	}

	/*
	 * Push to the end of deque
	*/
	public void push(String data) {

		//If there's nothing in the deque, append data to tail
		// and set tail as head
		if(tail == null) {
			tail = new Node(data);
			head = tail;
		}
		else {

			//Add new data to the right of the tail
			tail.setRight(new Node(data));

			//Set the left of the data as the tail
			tail.getRight().setLeft(tail);

			//Set new tail
			tail = tail.getRight();
		}
	}

	//Remove from tail
	public String pop() {

		//Check if tail is empty
		if(tail == null) {
			System.out.println("Deque is empty.");
			return null;
		}
		else {

			//Get value of tail of deque
			String value = tail.getValue();
			
			//Set new tail 
			tail = tail.getLeft();
		}

		return value;
 	}


 	//Append at the head
 	public void put(String _branch1, String _branch2) {

 		Node temp = new Node(head.getRight());
 		Node branch1 = new Node(_branch1);
 		Node branch2 = new Node(_branch2);

 		branch1.setRight(branch2);
 		branch2.setRight(temp);
 		head = branch1;

 	}

 	//Remove at the head
 	public String remove() { 	 //?


 	}

}