import java.util.*;
import java.io.*;

class REsearch {

	static private final String[] SPLSTATE = { "START","END", "BR", "DM", "WC"};

	public static void main(String[] args) {

		try {

			if (args.length != 1) {

				//print error
				System.err.println("Enter valid argument: java REsearch <textfile> < FSMtable");
				return;
			}

			//READ AS STREAM OF BYTES AND GET ARG TO VAR
			FileReader file = new FileReader(args[0]);
			InputStreamReader regex = new InputStreamReader(System.in);
			BufferedReader reader = new BufferedReader(regex);

			//DECLARE VARIABLE
			String[] parts;
			String[] pParts;
			String line;
			String pLine;

			Node stateNode;
			String stateNum;
			String stateChar;
			String branch1;
			String branch2;

			boolean success = false;
			boolean isEmpty = false;
			String sTracker = "0";
			int p;
			String marker;
			String pointer;
			String dqHead;

			//CREATING FSM
			FSMtable fsm = new FSMtable();
			Node states;

			//READ LINE IN SYSTEM IN
			line = reader.readLine();

			//WHILE NOT END OF THE FILE
			while (line != null) {

				parts = line.split(", ");

				//ADD ALL THE STATES TO THE FSM TABLE
				states = new Node(parts[0], parts[1], parts[2], parts[3]);

				fsm.add(states);

				//NEXT LINE
				line = reader.readLine();
			}

			System.out.println("");
			fsm.displayFSM();
			System.out.println("");

			//CREATING DEQUE
			Deque deque = new Deque();

			//READ LINE IN ARG
			reader = new BufferedReader(file);
			line = reader.readLine();

			//WHILE NOT END OF THE FILE
			while (line != null) {

				//GET EACH CHAR OF THE LINE
				parts = line.split("");

				System.out.println("Line: " + line);
				System.out.println("");

				//FOR EVERY CHAR IN THE LINE FOR START OF THE PATTERN
				for (int m = 0; m < parts.length ; m++) {

					if (success) { break; }

					//MAKING SURE THAT PATTERN IS SEARCH FROM START
					deque.clear();
					isEmpty = false;
					p = 0;
					
					marker = parts[m];
					pLine = line.substring(m);
					pParts = pLine.split("");

					/* DEBUG CODE */
					System.out.println("MARKER");
					System.out.println("marker: " + marker);
					System.out.println("pLine: " + pLine);
					System.out.println("");
					/* END DEBUG CODE */

					System.out.println("sTracker: " + sTracker);
					System.out.println("sTracker.boolean: " + (isSpecial(sTracker)));
					System.out.println("p: " + p);
					System.out.println("pParts.length: " + pParts.length);
					System.out.println("");

					//FOR EVERY CHAR IN THE LINE FOR SEARCHING PATTER
					// for (int p = 0; (p < pParts.length) || (isSpecial(sTracker)); p++) {
					while ((p < pParts.length) || (isSpecial(sTracker))) {

						System.out.println("p: " + p);
						System.out.println("pParts.length: " + pParts.length);
						
						if (success) { break; }
						if (isEmpty) { break; }
						
						if (p < pParts.length) {

							pointer = pParts[p];
						}
						else {

							pointer = pParts[pParts.length-1];
						}

						//GET THE DEQUE HEAD LOL :)
						dqHead = deque.getHead().getState();

						/* DEBUG CODE */
						System.out.println("POINTER");
						System.out.println("dqHead: " + dqHead);
						System.out.println("pointer: " + pointer);
						System.out.println("pLine: " + pLine);
						System.out.println("");
						/* END DEBUG CODE */

						//WHILE THERE ARE CURRENT STATE
						while (dqHead.compareTo("SCAN") != 0) {

							if (success) { break; }

							//GET THE STATE NODE AND ITS INFO
							stateNode = fsm.findState(dqHead);
							stateChar = stateNode.getChar();
							branch1 = stateNode.getBranch1();
							branch2 = stateNode.getBranch2();

							System.out.println("After while !SCAN");
							System.out.println("dqHead: " + dqHead);
							System.out.println("stateNode.state: " + stateNode.getState());
							System.out.println("stateChar: " + stateChar);
							System.out.println("branch1: " + branch1);
							System.out.println("branch2: " + branch2);
							System.out.println("");

							if (stateChar.compareTo("END") != 0) {

								System.out.println("After if !END");
								System.out.println("");

								//IF STATE IS NOT A BRANCH, DUMMY, START STATE
								if (!(isSpecial(stateChar))) {
									
									System.out.println("Not special char");
									System.out.println("");

									//IF THE CHAR MATCHES
									// if (pointer.compareTo(stateChar) == 0) {
									if ((pointer.compareTo(stateChar) == 0) || 
										(stateChar.compareTo("WC") == 0)) {

										System.out.println("The same char");
										System.out.println("");

										//PUSH BRANCH (ADD TO THE END OF THE DEQUE)
										deque.push(branch1);
									}

									//POP THE STATE
									deque.pop();
								}
								else {

									System.out.println("Is special char");
									System.out.println("");
									deque.put(branch1, branch2);
								}
							}
							else {
								
								//WE FOUND A MATCH PRINT OUTPUT
								System.out.println("OUTPUT SUCCESSFUL");
								System.out.println(line);
								success = true;
								p = -1;
							}

							dqHead = deque.getHead().getState();

							System.out.println("End of !SCAN");
							System.out.println("dqHead: " + dqHead);
							System.out.println("");
						}

						deque.pop();
						deque.push("SCAN");

						//CHECK IF DEQUE IS EMPTY
						dqHead = deque.getHead().getState();

						System.out.println("HERE PO");
						System.out.println("dqHead: " + dqHead);

						if (dqHead.compareTo("SCAN") != 0) {

							sTracker = fsm.findState(dqHead).getChar();
						}

						System.out.println("Testing if deque is empty");
						System.out.println("dqHead: " + dqHead);
						System.out.println("sTracker: " + sTracker);
						System.out.println("");

						if (dqHead.compareTo("SCAN") == 0) {

							System.out.println("Deque is empty");
							System.out.println("");
							isEmpty = true;
							p = 0;
						}

						p++;
					}
				}

				System.out.println("Success and isEmpty is false. clearing deque");

				line = reader.readLine();
				success = false;
				deque.clear();
				isEmpty = false;
			}

			System.out.flush();
			reader.close();
		}
		catch (Exception eREsearch) {

			System.err.println("Error: " + eREsearch);
			return;
		}
	}

	static boolean isSpecial(String _state) {

		for (String state : SPLSTATE) {

			if (state.compareTo(_state) == 0) {

				return true;
			}
		}

		return false;
	}
}