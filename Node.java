import java.util.*;
import java.io.*;

private class Node() {

	private String _value;
	private Node _left;
	private Node _right;

	public Node(String value) {

		_value = value;
	}

	public String getValue() {
		return _value;
	}

	public Node getLeft() {
		return _left;
	}

	public Node getRight() {
		return _right;
	}

	public Node setRight(Node right) {
		_right = right;
	}

	public Node setLeft(Node left) {
		_left = left;
	}
}