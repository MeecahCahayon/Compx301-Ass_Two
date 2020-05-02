import java.util.*;
import java.io.*;

class REcompile {

	static private char[] SPECS = { '.', '*', '?', '|', '(', ')', '\\' };
	static private boolean accepted = false;
	static private String regex;
	static private int index;

    public static void main(String[] args) {
    	
    	try {

	    	//if there's 0 || >1 argument
			if (args.length != 1) {

				//print error
				invalidRegex();
			}

			//check if expression is a valid regex expression
			regex = args[0];

			System.out.println("regex: " + regex);
			System.out.println("length: " + regex.length());
			for (int i = 0; i < regex.length() ; i++ ) {
		    		
		    	System.out.println(i + ": " + regex.charAt(i));
		    }
		    System.out.println("");


	    	accepted = parser();

	    	if (accepted) {
	    		
	    		//build FSM then output
	    		//compile(args[0]);

	    		System.out.println("TAMA NA SYA EUNICE !!!");
	    	}

	    	/*

	    	else {

	    		invalidRegex();

	    	}*/

    	}
    	catch (Exception eReCompile) {
    		
    		System.err.println("Error :" + eReCompile);
			return;
    	}
    }

    static public boolean parser() {

    	//INITIALIZE INDEX
    	index = 0;

    	expression();

		if (index != (regex.length() - 1)) {

			invalidRegex();
		}
    
    	return true;
    }

    static public void expression() {

    	if (index < regex.length() - 1) {

	    	term();

	    	if (isVocab(regex.charAt(index)) || regex.charAt(index) == '(' || 
	    		regex.charAt(index) == '\\'|| regex.charAt(index) == '.') {
	    			
	    		expression();
	    	}	
    	}

    	return;
    }

    static public void term() {

    	if (index < regex.length() - 1) {

	    	factor();

	    	if (regex.charAt(index) == '*') {

	    		index++;
	    	}
	    	else if (regex.charAt(index) == '?') {
	    		
	    		index++;
	    	}
	    	else if (regex.charAt(index) == '|') {

	    		index++;
	    	}
    	}

    	return;
    }

    static public void factor() {

    	if (index < regex.length() - 1) {

	    	if (isVocab(regex.charAt(index))) {
	    		
	    		index++;
	    	}
	    	else if (regex.charAt(index) == '.') {
	    		
	    		index++;
	    	}
	    	else if (regex.charAt(index) == '\\') {

	    		index++;
	    	}
	    	else if (regex.charAt(index) == '(') {
	    		
	    		index++;

	    		expression();

	    		if (regex.charAt(index) == ')') {
	    			
	    			index++;
	    		}
	    		else {
	    			
	    			invalidRegex();
	    		}
	    	}
    	}

    	return;
    }

    static public void compile(String expression) {

	}

	static public boolean isVocab(char c) {

		for(char ch: SPECS) {

			if (ch == c) {

				return false;
			}
		}

		return true;
	}

	static public void invalidRegex() {

		System.out.println("INVALID EXPRESSION! Enter a valid regex expression");
		System.exit(0);
	}
}