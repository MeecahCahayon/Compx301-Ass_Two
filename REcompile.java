import java.util.*;
import java.io.*;

class REcompile {

	private boolean accepted;

	public void compile(String expression) {

	}

	public boolean isVocab(char c) {
		char[] SPECS = { '.', '*', '?', '|', '(', ')', '\\' };		//to be able to detect a backlash, we have to put another backlash so it doesn't treat is an escape character
		for(char ch: SPECS) {
			if(ch = c) {
				return false;
			}
		}
		return true;
	}


    public static void main(String[] args)
    {	
    	if(args.length == 0) 
    	{
    		System.out.println("Enter a regex expression.");
    	}
    }

}