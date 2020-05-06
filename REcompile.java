import java.util.*;
import java.io.*;

class REcompile {

	static private final String BR = "BR";
	static private final String DUMMY = "DM";
	static private final String WC  = "WC";
	static private final String START = "START";
	static private final String END = "END";
	static private final char[] SPECS = { '.', '*', '?', '|', '(', ')', '\\' };
	
	static private ArrayList<Integer> n1 = new ArrayList<Integer>();
	static private ArrayList<Integer> n2 = new ArrayList<Integer>();
	static private ArrayList<String> ch = new ArrayList<String>();
	
	static private String regex;
	static private int index;
	static private int gDummy;
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

		    //PARSE THEN BUILD FSM
		    parser();

		    //SETTING THE LAST STATE
		    set_state(state, END, -1, -1);

		    for (int i = 0; i < ch.size() ; i++) {
		    	
		    	System.out.println(i + ", " + ch.get(i) + ", " + n1.get(i) + ", " + n2.get(i));
		    }

    	}
    	catch (Exception eReCompile) {

    		System.err.println("Error :" + eReCompile);
			return;
    	}
    }

    static public void parser() {

    	//INITIALISING
    	set_state(0, START, 0, 0);
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

	    int start = term();
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

	    int start = factor();
	    int branch1, branch2, dummy, bState = -2;

	    //IF IT FOUND END OF THE LINE
	    if (start != -2) {
	    	
	    	//CHECK IF ITS THE END OF REGEX
		    if (index < regex.length()) {

		    	if (regex.charAt(index) == '*') {

		    		bState = state;
		    		branch1 = start;

		    		//CREATING A TEMP BRANCH STATE FOR *
		    		set_state(bState, BR, branch1, state+1);

		    		start = state;
		    		index++;
		    		state++;

		    		branch2 = expression();

		    		//IF IT FOUND END OF THE LINE
		    		if (branch2 != -2) {
		    			
		    			//FINILISING BRANCH STATE FOR *
		    			set_state(bState, BR, branch1, branch2);
		    		}
		    	}
		    	else if (regex.charAt(index) == '?') {

		    		dummy = state; //f1
		    		branch1 = start; //r
		    		state++;
		    		start = state; //e
		    		index++;
		    		state++;

		    		//CREATING TEMP BRANCH STATE FOR DUMMY AND ?
		    		set_state(dummy, DUMMY, state, state);
		    		set_state(start, BR, branch1, state);

		    		branch2 = expression(); //f2

		    		//IF IT FOUND END OF THE LINE
		    		if (branch2 != -2) {

		    			//FINILISING BRANCH STATE FOR ?
			    		set_state(start, BR, branch1, branch2);

			    		//FINILISING BRANCH STATE FOR DUMMY (MERGING NEXT SAME TO START STATE OF EXPRESSION)
			    		set_state(dummy, DUMMY, branch2, branch2);
		    		}
		    	}
		    	else if (regex.charAt(index) == '|') {

		    		dummy = state; //f1
		    		branch1 = start; //r
		    		state++;
		    		start = state; //e
		    		index++;
		    		state++;

		    		//CREATING TEMP BRANCH STATE FOR DUMMY AND |
		    		set_state(dummy, DUMMY, state, state);
		    		set_state(start, BR, branch1, state);

		    		branch2 = term(); //f2

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

    	int start = -2;

    	//CHECK IF ITS THE END OF REGEX
    	if (index < regex.length()) {

	    	if (isVocab(regex.charAt(index))) {

	    		set_state(state, String.valueOf(regex.charAt(index)), state+1, state+1);
	    		start = state;
	    		index++;
	    		state++;
	    	}
	    	else if (regex.charAt(index) == '.') {

	    		set_state(state, WC, state+1, state+1);
	    		start = state;
	    		index++;
	    		state++;
	    	}
	    	else if (regex.charAt(index) == '\\') {

	    		index++;

	    		//SEE NEXT CHAR AS IT IS
	    		if (isVocab(regex.charAt(index)) || !(isVocab(regex.charAt(index)))) {

	    			set_state(state, String.valueOf(regex.charAt(index)), state+1, state+1);
	    			start = state;
	    			index++;
	    			state++;
	    		}
	    	}
	    	else if (regex.charAt(index) == '(') {

	    		index++;
	    		start = expression();

	    		if (index < regex.length()) {

		    		if (regex.charAt(index) == ')') {

		    			index++;
		    		}
		    		else {
		    			
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

	// Setting State for the FSM
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

	static public boolean isVocab(char _char) {

		for(char ch: SPECS) {

			if (ch == _char) {

				return false;
			}
		}

		return true;
	}

	static public void error() {

		System.out.println("INVALID EXPRESSION! Enter a valid regex expression");
		System.exit(0);
	}
}