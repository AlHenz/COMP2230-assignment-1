/** main program
 * @author M A Hakim Newton
 * 
 * edits for Tasks 1-4 (after the first p.print() statement)
 * made by Alex Henley, 3435482
 */

/* use tabsize 2 to see proper indentation */

import java.awt.Color;	// to get color names

//import java.io.IOException;
import java.util.Scanner;

//imports for the tasks of the assignment
//(imported by me to allow the program to function as appropriate)
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.ArrayDeque;

public class mainProgram
{
  /** main function
   * @param args array of string arguments
   */
	public static void main(String[] args) 
	{
		try
		{
			// 300 persons, venue length 150, venue width 150, proximity measure 10, 
			// infected person 50, random seed is taken from clock with the 
			// System.nanoTime() passed as the random seed, the locations generated 
			// will be different every time the program is run. Instead of 
			// System.nanoTime(), pass a fixed number so that the same locations 
			// are generated every time the program is run.
	//		problemGenerator p = new problemGenerator(300, 150, 150, 10, 50, System.nanoTime());
			problemGenerator p = new problemGenerator(30, 15, 10, 1.5, 5, System.nanoTime());

			// YOU CAN PROVIDE SEVERAL PROBLEM GENERATOR ABOVE.
			// EACH ONE CAN HAVE A DIFFERENT PARAMETER SETTING.
			// SOME FIXED SEEDS MAY BE LISTED FOR NICE SCENARIOS. 
			// ALL THOSE SHOULD BE COMMENTED OUT, EXCEPT ONE.
			// WE WILL COMMENT OR UNCOMMENT TO RUN ONLY ONE. 

			System.out.println("seed: " + p.randomSeed);	// do not change this line

			// scale factor 5, node radius 5, line width 2, show node indexes true, 
			// show node coords false, show edge indexes false, show edge lines true 
			//p.setDisplay(5, 7, 2,  true, false, false, true);
			p.setDisplay(30, 10, 2,  true, false, false, true);

			// print locations true, print contacts true, print infected true
			p.setPrint(true, true, true);

			p.display();		// graphically display 
			Thread.sleep(1000);	// pause needed after display

			p.print();			// print on screen

			// IF YOU WANT YOU CAN PROVIDE MULTIPLE DISPLAY SETTINGS ABOVE. 
			// EITHER ONE FOR EACH PROBLEM GENERATOR OR JUST ONE FOR ALL.
			// KEEP THE PRINT SETTING AS IT IS, WE MAY TRY OPTIONS THOUGH.

			// YOU ARE FREE TO WRITE YOUR CODE FROM HERE FOR THE TASKS
			// YOU CAN REMOVE ALL THE CODE FROM BELOW AND WRITE YOUR OWN
			
			
			//Task 1: Graph Construction
		  /*Represented as adjacency lists (ArrayList<Integer>[])
			Why?
				> It fits the problem as its contacts form a sparse graph, taking up O(N + M) space.
					- O(N) to allocate
					- O(M) to add edges
				> It is a better fit for later traversal tasks as BFS and DFS run in O(N + M), where:
					- N represents the number of nodes in the graph
					- M represents the number of edges in the graph
				> The input is a list of edges (p.contact), so building lists takes O(M).
			Undirected edges by adding both directions, which will be crucial for BFS and DFS to traverse over either way along edges
			Didn't use an adjacency matrix as it's O(N x N) - worse than adjacency list.
		  */
			System.out.println(" ");
			System.out.println("==== Task 1: Constructing The Graph ====");
			
			/* Adapted from the graph constructor from "prog03GraphSearch.java"
				I used the "adjacency list" idea, but:
					> I built the lists from p.contact[] (in pairs i, j) instead of an adjacency matrix
					> Stored raw int IDs  without graphNode or GraphEdge classes
					> Undirected so both directions
			*/
		  //==================================================
			@SuppressWarnings("unchecked") //was utilized in another assignment so I'm using it here
			List<Integer>[] adj = new ArrayList[p.numPerson]; //An array with "p.numPerson" slots; each slot holds a List<Integer> of each person's neighbours
			for (int i = 0; i < p.numPerson; i++) { //numPerson being a variable from problemGenerator.java entailing the number of people attending the event
				adj[i] = new java.util.ArrayList<>(); //Each slot is now an ArrayList<Integer>
			}//                                         adj[i] ready to add() neighbours of the relevant person (being i)
			
			for (int k = 0; k < p.numContact; k++) { 
				int u = p.contact[k].i; //Two endpoints of i and j under p.contact[k] (the two people in contact)
				int v = p.contact[k].j; //u and v are Person A and B respectively in this contact.
				if (u == v) {
					continue; // this will help skip any self-loops in the graph
					//			 (A person can't be in close contact with themselves)
				}
				adj[u].add(v); // Adds v to u's neighbout list
				adj[v].add(u); // And vice versa as the contact is two-way
			}// (u can reach v, v can reach u)
		  //==================================================
		  
			//Error check for the graph (to ensure the adjacency matrix has been built):
			int degSum = 0;
			for (var nbrs : adj) degSum += nbrs.size();
			System.out.println("Graph built: N=" + p.numPerson + ", M=" + (degSum/2));
			
			
			//Task 2: Contact Groups
		  /*Finds each connected component in the graph and prints out: "root <start> size <count>"
			Represented in BFS
			Why?
				> It is linear: O(N + M), where each node is enqueued once; each edge checked at most twice.
				> It starts at an unvisited node and reaches its whole group
			- visited[] marks people already assigned to a group (which is set as true when enqueuing).
			- Queue holds the frontier; members collects this group to count/colour.
			- For per (int meaning "person") = 0..n-1:
				if visited[per] skip;
				else run BFS from per, then print "root (per) size (members.size())"
			
		  */
			System.out.println(" ");
			System.out.println("==== Task 2: Contact Groups and BFS Components ====");
			int n = p.numPerson; //Number of people (from 0..n-1 (indexes))
			boolean[] visited = new boolean[n]; //Keeps tabs on what has been visited - initializes all slots to false initially
			
			for (int per = 0; per < n; per++) { //Scan every person left-to-right: start new group if not assigned
				if (visited[per]) { //if they've already been visited, skip 'em
					continue;
				} 
				
				/*Adapted from prog03GraphSearch.java (BFS Pattern in NodeExplorer)
				  I used the "adjacency list" idea, but:
					> I built the lists from p.contact[] (in pairs i, j) instead of an adjacency matrix
					> I collect a component into 'members', print "root __ size __"
					> No TreeNode, uses int IDs
				*/
			//	==============================================
				java.util.ArrayDeque<Integer> q = new java.util.ArrayDeque<>(); //To-Process line for this group (FIFO)
				java.util.ArrayList<Integer> members = new java.util.ArrayList<>(); //Who ends up in the relevant group
				visited[per] = true; //start person
				q.add(per); //put them in line so we can check the neighbors
				
				while (!q.isEmpty()) {
					int u = q.poll();
					members.add(u);
					
					for (int v : adj[u]) {
						if (!visited[v]) {
							visited[v] = true;
							q.add(v);
						}
					}
				}
				if (members.size() > 1) {
				System.out.println("root " + per + " size " + members.size());
				Color c = p.groupColor(per);
				for (int node : members) p.setNodeColor(node, c);
				}
			//  ==============================================
			}
				
			// you can selectively change the color of some nodes
			// for example infected persons are shown with in a color
			for(int k = 0; k < p.numInfected; k++)
				p.setNodeColor(p.infected[k], p.infectedColor);
			
			
			
			//Task 3: Contact Chains
		  /*For three provided people out of the group of people, list their contact chains that end with infected individuals
		    Why DFS?:
				> naturally enumerates all simple paths, whereas BFS would give only one shortest path
			How it's done:
				> infected[]: O(1) test to see if a node is infected
				> onPath[]: marks node in the current chain (prevents loops during this branch)
				> path: the live check of nodes from s to current node
				> DFS Rule:
					- if it's infected, then print path and stop that branch
					- else we recurse to each neighbour !onPath
					- then pop (or backtrack) 
					- edges along those printed paths are coloured
					- Then a new onPath, path, and counter for each 3 inputs.
		  */
			System.out.println(" ");
			System.out.println("==== Task 3: Contact Chains via DFS ====");
			
			/*Inspired by prog03GraphSearch.java (the recursive DFS and path printing in PathExplorer)
			  Changes I made:
				> Stop a branch when the current node is infected and print the path
				> Track current path with the ArrayList<Integer> and prevent cycles with onPath[]
				> No TreeNode/Graph classes
			*/
		  //==================================================
			// Fast lookup: infected[x] == true if the person is infected.
			boolean[] infected = new boolean[p.numPerson];
			for (int k = 0; k < p.numInfected; k++) {
				infected[p.infected[k]] = true;
			} // Then obtain the input of the 3 values
			
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter 3 Person IDs from 0.." + (p.numPerson - 1) + "):");
			
			int[] chainStarts = new int[] {scanner.nextInt(), scanner.nextInt(), scanner.nextInt()}; //creates an array from the chosen ids
			
			//For each start ID, run DFS that enumerates ALL simple chains to infected nodes
			for (int s : chainStarts) { //iterates the chainStarts array for each int value (labelled as 's' each time)
				if (s < 0 || s >= p.numPerson) {
					System.out.println("chain: null"); //for anything out of bounds, there is no chain
				}
				if (infected[s]) { //If the chosen entity (from the persons) is infected, then it is its own chain
					System.out.println("chain: " + s);
				}
				
				boolean[] onPath = new boolean[p.numPerson]; //used to stop cycles on the relevant path
				ArrayList<Integer> path = new ArrayList<>(); //holds current chain
				int[] chainsFound = new int[1];
				
				dfsForChains(s, infected, adj, new boolean[p.numPerson], new ArrayList<>(), chainsFound, p);
				//this helper method above will be called for each inputted value
				
				if (chainsFound[0] == 0) {
					System.out.println("chain: null");
		  //==================================================
				}
			}
			
			//Task 4: Zone Classification
		  /*Classifies non-infected people by hop distance to the nearest infected person:
				> dist = 1 --> orange
				> dist = 2 --> yellow
				> dist = 3 --> gray
			Why BFS?:
				The multi-source BFS from all infected gives minimum hops in O(N+M) time
				All infested are dist = 0; when a node is first reached, the dist is set as "parentDist + 1"
				One pass gives everyone's nearest distance.
		  */
			System.out.println(" ");
			System.out.println("==== Task 4: Zones (Orange / Yellow / Gray) ====");
			
			//Multi-source BFS distances to nearest infected
			int[] dist = new int[n];
			Arrays.fill(dist, -1);
			ArrayDeque<Integer> q2 = new ArrayDeque<>();
			
			//This sets all infected nodes to be distance 0
			for (int i = 0; i < n; i++) {
				if (infected[i]) {
					dist[i] = 0;
					q2.add(i);
				}
			}
			//This covers the first time you reach a node, which is the shortest hops to any infected node
			//(Loop copied from Task 2: Contact Groups)
			while (!q2.isEmpty()) {
				int u = q2.poll();
				for (int v : adj[u]) {
					if (dist[v] == -1) {
						dist[v] = dist[u] + 1;
						q2.add(v);
					}
				}
			}
			
			//Each zone is organized into three different lists
			ArrayList<Integer> orange = new ArrayList<>();
			ArrayList<Integer> yellow = new ArrayList<>();
			ArrayList<Integer> gray   = new ArrayList<>();
			
			//Add the nodes to their correct lists 
			for (int i = 0; i < n; i++) {
				if (infected[i]) continue;   //In case of bumping into infected (dist==0) node
				if (dist[i] == 1) orange.add(i); //If 1 hop away from infected, go in orange
				if (dist[i] == 2) yellow.add(i); //If 2 hops, go in yellow
				if (dist[i] == 3) gray.add(i);   //If 3 hops, go to gray
			}// 							   If none of those, don't care
			
			//Now print out all the nodes/numbers of the different zones
			System.out.print("orange: ");
			for (int x : orange) System.out.print(x + " ");
			System.out.print("\nyellow: ");
			for (int y : yellow) System.out.print(y + " ");
			System.out.print("\ngray: ");
			for (int z : gray) System.out.print(z + " ");
			
			//Now the colors for the graph visual
			for (int id : orange) p.setNodeColor(id, p.orangeColor);
			for (int id : yellow) p.setNodeColor(id, p.yellowColor);
			for (int id : gray)   p.setNodeColor(id, p.grayColor);
			for (int x=0; x < p.numPerson; x++) {
				if (infected[x]) {
					p.setNodeColor(x, p.infectedColor);
				}
			}
		//	Needed to update the display otherwise the color changes and zone groups don't show
			p.display();		
			Thread.sleep(1000);	
			
		}
		catch(problemGenerator.error e)
		{
			System.out.println(e.getMessage());
		}
		catch (InterruptedException e) 
		{
			System.err.println("Thread was interrupted while sleeping!");
		}
	}

	//Task 3 Helper Function:
	/* 
	  DFS skeleton adapted from PathExplorer in prog03GraphSearch.java, but altered to:
		> print every chain from start (the 3 inputted nodes) to any infected nodes
		> color the chain edges
		> backtrack by popping from 'path' and clearing 'onPath'
	*/
  //==========================================================
	static void dfsForChains(int u, boolean[] infected, List<Integer>[] adj, boolean[] onPath, ArrayList<Integer> path, int[] chainsFound, problemGenerator p) throws problemGenerator.error{
		//onPath marks nodes that are currently on the path                                                                                                    ^ Added from the following error: unreported exception problemGenerator.error; 
		//path represents what the current path from start to u is																						          must be caught or declared to be thrown
		//chainsFound[0] increments whenever we print a chain
		
		//Node u is entered first (which is int 's' as stated in the previous for loop)
		
		//Some Notes To Remember:
		// > for every person u, adj[u] is the list of people directly connected to u (u's neighbours)

		path.add(u); 
		onPath[u] = true;
	
		//stops the branch at the first infected node hit, prints the path there and colors the chain path/edges
		if (infected[u]) {
			//The chain is printed out in order
			System.out.print("chain:");
			for (int id : path) {
				System.out.print(" " + id); //prints out the full path from the inputted node u to the infected node
			}
			System.out.println();
			chainsFound[0]++;
			
			//colour the edges
			for (int i = 0; i + 1 < path.size(); i++) {
				int a = path.get(i), b = path.get(i + 1);
				p.setEdgeColor(a, b, p.chainColor); //grabbed from problemGenerator
			}
			
			//for backtracking
			onPath[u] = false;
			path.remove(path.size() - 1);
			return;
		}
		
		//otherwise it'll explore neighbours that aren't already on the current path
		for (int v : adj[u]) { //int v is a neighbour of u, so for each neighbour of u (every person in direct contact with u)
			if(!onPath[v]) { //if v is not on the path that u is (so not already on the current chain being explored) then we can check it
				dfsForChains(v, infected, adj, onPath, path, chainsFound, p); //check v to see if it's infected or connects to infected nodes
				//When exloring from v (using dfsForChains), either:
				// > An infected person is hit and the branch is stopped
				// > Or it hits a dead end and backtracks to u and tries the next neighbour (v)
			}
		}
		onPath[u] = false; //Makes it so the spot isn't explored anymore
		path.remove(path.size() - 1);//This will return the pointer back to each pervious node of the graph after the depth has 
		//                             been searched.
	}
}
  //==========================================================