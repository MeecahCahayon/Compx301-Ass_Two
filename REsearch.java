import java.util.*;
import java.io.*;

class REsearch {

	static private final String[] SPLSTATE = { "START","END", "BR", "DM", "WC"};

	public static void main(String[] args) {

		try {

			if (args.length != 1) {

				//PRINT ERROR
				System.err.println("Enter valid argument: java REsearch <textfile> < FSMtable");
				return;
			}

			//READ AS STREAM OF BYTES AND GET ARG TO VAR
			FileReader file = new FileReader(args[0]);
			InputStreamReader regex = new InputStreamReader(System.in);
			BufferedReader reader = new BufferedReader(regex);

			//DECLARE VARIABLE
			ArrayList<String> outputs = new ArrayList<String>();
			String[] parts;
			String[] pParts;
			String line;
			String pLine;

			Node stateNode;
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

			//CREATING DEQUE
			Deque deque = new Deque();

			//READ LINE IN ARG
			reader = new BufferedReader(file);
			line = reader.readLine();

			//WHILE NOT END OF THE FILE
			while (line != null) {

				//GET EACH CHAR OF THE LINE
				parts = line.split("");

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

					//FOR EVERY CHAR IN THE LINE FOR SEARCHING PATTER
					while ((p < pParts.length) || (isSpecial(sTracker))) {
						
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

						//WHILE THERE ARE CURRENT STATE
						while (dqHead.compareTo("SCAN") != 0) {

							if (success) { break; }

							//GET THE STATE NODE AND ITS INFO
							stateNode = fsm.findState(dqHead);
							stateChar = stateNode.getChar();
							branch1 = stateNode.getBranch1();
							branch2 = stateNode.getBranch2();

							if (stateChar.compareTo("END") != 0) {

								//IF STATE IS NOT A BRANCH, DUMMY, START STATE
								if (!(isSpecial(stateChar))) {

									//IF THE CHAR MATCHES
									if ((pointer.compareTo(stateChar) == 0) || 
										(stateChar.compareTo("WC") == 0)) {

										//PUSH BRANCH (ADD TO THE END OF THE DEQUE)
										deque.push(branch1);
									}

									//POP THE STATE
									deque.pop();
								}
								else {

									//REPLACE DEQHEAD WITH ITS BRANCHES
									deque.put(branch1, branch2);
								}
							}
							else {
								
								//WE FOUND A MATCH - ADD TO THE ARRAYLIST

								//IF ARRAYLIST IS EMPTY - ADD
                                if(outputs.size() == 0) {

                                    outputs.add(line);
                                }
                                else {

                                	//CHECK IF WE ALREADY HAVE THAT OUTPUT
                                    for(String s: outputs) {

                                        if(s.equals(line)) {

                                            //IF STRING IS ALREADY IN THE LIST
                                            break;
                                        }
                                        else {

                                            //ELSE ADD INTO THE LIST
                                            outputs.add(line);
                                            break;
                                        }
                                    }
                                }
                                
                                success = true;
                                p = -1;
							}

							dqHead = deque.getHead().getState();
						}

						deque.pop();
						deque.push("SCAN");

						//CHECK IF DEQUE IS EMPTY
						dqHead = deque.getHead().getState();

						if (dqHead.compareTo("SCAN") == 0) {
							
							isEmpty = true;
							p = 0;
						}

						//SAVES DQHEAD'S CHAR FOR REFERENCE LATER
						if (dqHead.compareTo("SCAN") != 0) {

							sTracker = fsm.findState(dqHead).getChar();
						}

						p++;
					}
				}

				line = reader.readLine();
				deque.clear();
				success = false;
				isEmpty = false;
			}

			//PRINT OUTPUT
            for(String s: outputs) {

                System.out.println(s);
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