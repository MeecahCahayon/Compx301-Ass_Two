import java.util.*;
import java.io.*;

class REcompile {

	//CONSTANTS FOR REGEX SPECS
	static private final char[] SPECS = { '.', '*', '?', '|', '(', ')', '\\' };
	static private final String BR = "BR";
	static private final String DUMMY = "DM";
	static private final String WC  = "WC";
	static private final String START = "START";
	static private final String END = "END";
	
	//ARRAY LIST FOR EACH LINE OF OUTPUT
	static private ArrayList<Integer> n1 = new ArrayList<Integer>();
	static private ArrayList<Integer> n2 = new ArrayList<Integer>();
	static private ArrayList<String> ch = new ArrayList<String>();
	
	//DECLARE GLOBAL VARIABLES
	static private String regex;
	static private int index;
	static private int state = 1;

    public static void main(String[] args) {

    	try {

	    	//IF THERE'S 0 || > 1 ARGUMENT
			if (args.length != 1) {

				//PRINT ERROR
				error();
			}

			//GET ARGUMENT INTO VARIABLE
			regex = args[0];

		    //PARSE EXPRESSION, THEN BUILD FSM
		    parser();

		    //SETTING THE END STATE
		    set_state(state, END, -1, -1);

		    //OUTPUT
		    for (int i = 0; i < ch.size() ; i++) {
		    	
		    	System.out.println(i + ", " + ch.get(i) + ", " + n1.get(i) + ", " + n2.get(i));
		    }
    	}
    	catch (Exception eREcompile) {

    		System.err.println("Error: " + eREcompile);
			return;
    	}
    }

    static public void parser() {

    	//INITIALISING
    	set_state(0, START, 0, 0);
    	//INDEX OF EXPRESSION
    	index = 0;

    	int initial = expression();

    	//IF DID NOT CHECK ALL REGEX
		if (index != regex.length()) {

			error();
		}

		//IF IT FOUND END OF THE LINE
		if (initial != -2) {

			//SET START OF THE FSM
	    	set_state(0, START, initial, initial);
		}

		return;
    }

    static public int expression() {

    	//FROM EXPRESSION, GO TO TERM
	    int start = term();
	    //DECLARE VARIABLES
	    int cDummy, branch = -2;

	    //CHECK IF ITS THE END OF REGEX
	    if (index < regex.length()) {

	    	//CHECK IF NEXT CHAR IS A FACTOR
	    	if (isVocab(regex.charAt(index)) || regex.charAt(index) == '(' || 
	    		regex.charAt(index) == '\\' || regex.charAt(index) == '.') {

	    		//GO EXPRESSION
	    		cDummy = state - 1;
	    		branch = expression();
	    		set_state(cDummy, null, branch, branch);
	    	}	
    	}

    	return start;
    }

    static public int term() {

    	//FROM TERM, GO TO FACTOR
	    int start = factor();
	    //DECLARE VARIABLES
	    int branch1, branch2, dummy, bState = -2;

	    //IF END OF LINE
	    if (start != -2) {
	    	
	    	//CHECK IF ITS THE END OF REGEX
		    if (index < regex.length()) {

		    	//APPLY SPECIFICATION FOR *
		    	if (regex.charAt(index) == '*') {

		    		bState = state;
		    		branch1 = start;

		    		//CREATING A TEMP BRANCH STATE FOR *
		    		set_state(bState, BR, branch1, state+1);

		    		start = state;
		    		index++;
		    		state++;

		    		//FIND OTHER NEXT STATE
		    		branch2 = expression();

		    		//IF IT FOUND END OF THE LINE
		    		if (branch2 != -2) {
		    			
		    			//FINILISING BRANCH STATE FOR *
		    			set_state(bState, BR, branch1, branch2);
		    		}
		    	}
		    	//APPLY SPECIFICATION FOR ?
		    	else if (regex.charAt(index) == '?') {

		    		dummy = state;
		    		branch1 = start;
		    		state++;
		    		start = state;
		    		index++;
		    		state++;

		    		//CREATING TEMP STATES FOR DUMMY AND ?
		    		set_state(dummy, DUMMY, state, state);
		    		set_state(start, BR, branch1, state);

		    		//FIND OTHER NEXT STATE
		    		branch2 = expression();

		    		//IF IT FOUND END OF THE LINE
		    		if (branch2 != -2) {

		    			//FINILISING BRANCH STATE FOR ?
			    		set_state(start, BR, branch1, branch2);

			    		//FINILISING BRANCH STATE FOR DUMMY (MERGING NEXT SAME TO START STATE OF EXPRESSION)
			    		set_state(dummy, DUMMY, branch2, branch2);
		    		}
		    	}
		    	//APPLY SPCIFICATION FOR |
		    	else if (regex.charAt(index) == '|') {

		    		dummy = state;
		    		branch1 = start;
		    		state++;
		    		start = state;
		    		index++;
		    		state++;

		    		//CREATING TEMP BRANCH STATE FOR DUMMY AND |
		    		set_state(dummy, DUMMY, state, state);
		    		set_state(start, BR, branch1, state);

		    		//FIND OTHER NEXT STATE
		    		branch2 = term();

		    		//IF IT FOUND END OF THE LINE
		    		if (branch2 != -2) {

		    			//FINILASING BRANCH STATE FOR |
			    		set_state(start, BR, branch1, branch2);

			    		//FINALISING BRANCH STATE FOR DUMMY (MERGING NEXT SAME TO START STATE OF EXPRESSION)
			    		set_state(dummy, DUMMY, state, state);
		    		}
		    	}
		    }	
	    }

    	return start;
    }

    static public int factor() {

    	//DECLARE VARIABLES
    	int start = -2;

    	//CHECK IF ITS THE END OF REGEX
    	if (index < regex.length()) {

    		//CONSUME LITERAL
	    	if (isVocab(regex.charAt(index))) {

	    		//SET STATE FOR LITERAL
	    		set_state(state, String.valueOf(regex.charAt(index)), state+1, state+1);
	    		start = state;
	    		index++;
	    		state++;
	    	}
	    	//CONSUME WILDCARD
	    	else if (regex.charAt(index) == '.') {

	    		//SET STATE FOR WILDCARD
	    		set_state(state, WC, state+1, state+1);
	    		start = state;
	    		index++;
	    		state++;
	    	}
	    	//CONSUME LITERAL IF \ IS FOUND
	    	else if (regex.charAt(index) == '\\') {

	    		index++;

	    		//SEE NEXT CHAR AS IT IS
	    		if (isVocab(regex.charAt(index)) || !(isVocab(regex.charAt(index)))) {

	    			//SET STATE FOR SUCCEEDING CHAR AFTER \
	    			set_state(state, String.valueOf(regex.charAt(index)), state+1, state+1);
	    			start = state;
	    			index++;
	    			state++;
	    		}
	    	}
	    	//APPLY PRECEDENCE
	    	else if (regex.charAt(index) == '(') {

	    		index++;
	    		//GET EXPRESSION INSIDE BRACKETS
	    		start = expression();

	    		//CHECK IF INDEX IS AT THE END OF REGEX
	    		if (index < regex.length()) {

	    			//CHECK '(' HAS MATCHING ')'
		    		if (regex.charAt(index) == ')') {

		    			index++;
		    		}
		    		else {

		    			//EXPRESSION IS INVALID IF MATCHING ')' IS NOT FOUND 
		    			error();
		    		}
		    	}
		    	else {

		    		error();
		    	}
	    	}
    	}

    	return start;
    }

	//SET STATE METHOD
    private static void set_state(int _state, String _char, int _n1, int _n2) {

        if (_state >= ch.size()) {
            ch.add(_state, _char);
            n1.add(_state, _n1);
            n2.add(_state, _n2);
        }
        else if (_state < ch.size()) {
            n1.set(_state, _n1);
            n2.set(_state, _n2);
        }
    }

    //CHECK IF CHARACTER IS A LITERAL
	static public boolean isVocab(char _char) {

		for(char ch: SPECS) {

			if (ch == _char) {

				return false;
			}
		}

		return true;
	}

	//OUTPUT ERROR METHOD
	static public void error() {

		System.out.println("INVALID EXPRESSION! Enter a valid regex expression");
		System.exit(0);
	}
}