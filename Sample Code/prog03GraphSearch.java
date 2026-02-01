import java.util.*;

/** graph search algorithms
 * @author M A Hakim Newton
 */

/** graph node
 */
class GraphNode
{
	// the node id
	public int Id;

	/** constructor
	 * @param nodeId the node id
	 */
	GraphNode(int nodeId)
	{
		Id = nodeId;
	}
}

/** graph edge
 */
class GraphEdge
{
	public int source;	// source node id
	public int target;	// target node id
	public int weight;	// edge weight

	/** constructor
	 */
	GraphEdge(int sourceId, int targetId, int edgeWeight)
	{
		source = sourceId;
		target = targetId;
		weight = edgeWeight;
	}
}

/** graph: a list of nodes with adjacency lists
 */
class Graph
{
	// list of nodes
	public List<GraphNode> nodeList;

	// list of adjacency lists
	public List<List<GraphEdge>> adjList;

	/** constructor for unweighted graph
	 * @param nodeCount the number of nodes, indexes 0 to nodeCount - 1
	 * @param adjMatrix the adjacency matrix for the graph  
	 */ 
	public Graph(int nodeCount, int [][] adjMatrix)
	{
		nodeList = new ArrayList<GraphNode>();
    adjList = new ArrayList<List<GraphEdge>>();

		for (int id = 0; id < nodeCount; id++)
		{
			nodeList.add(new GraphNode(id));	
    	adjList.add(new ArrayList<GraphEdge>());
		}

		for (int i = 0; i < nodeCount; i++)
			for (int j = 0; j < nodeCount; j++)
				if (adjMatrix[i][j] > 0)
					adjList.get(i).add(new GraphEdge(i, j, adjMatrix[i][j]));	
	}
}

/** tree node
 */
class TreeNode
{
	// corresponding graph node 
	public GraphNode graphNode;

	// the parent tree node in the search tree
	public TreeNode parentTreeNode;

	/** constructor
	 * @param node the graph node
	 * @param parent the parent tree node, could be null
	 */
	TreeNode(GraphNode node, TreeNode parent)
	{
		graphNode = node;
		parentTreeNode = parent; 
	}
}

/** an interface to generalise stack and queues
 * stacks and queues will be specific implementations
 */
interface TreeNodeList 
{ 
	public boolean insert(TreeNode node);
	public TreeNode remove();
	public boolean contains(TreeNode node);
	public boolean contains(GraphNode node);
	public String toString();
	public boolean empty();
	public int size();
}

class StackTreeNodeList implements TreeNodeList 
{ 
	private Stack<TreeNode> nodes = new Stack<TreeNode>();

	@Override
	public boolean insert(TreeNode node) 
	{
		nodes.push(node);
		return true;
	}

	@Override
	public TreeNode remove() 
	{
		return nodes.pop();
	}

	@Override
	public boolean contains(TreeNode node) 
	{
		return nodes.contains(node);
	}

	@Override 
	public boolean contains(GraphNode gnode)
	{
		for(TreeNode tnode : nodes)
			if (tnode.graphNode.Id == gnode.Id)
				return true;
		return false;
	}

	@Override
	public String toString()
	{
		String output = "";
		for(int i = 0; i < nodes.size(); i++)
			output += " " + nodes.get(i).graphNode.Id;
		return output;
	}

	@Override
	public boolean empty() 
	{
		return nodes.empty();
	}

	@Override
	public int size(){
		return nodes.size();
	}
}

class QueueTreeNodeList implements TreeNodeList 
{ 
  private Queue<TreeNode> nodes = new LinkedList<TreeNode>();

	@Override
	public boolean insert(TreeNode node) 
	{
		return nodes.offer(node);
	}

	@Override
	public TreeNode remove() 
	{
		return nodes.poll();
	}

	@Override
	public boolean contains(TreeNode node) 
	{
		return nodes.contains(node);
	}

	@Override 
	public boolean contains(GraphNode gnode)
	{
		for(TreeNode tnode : nodes)
			if (tnode.graphNode.Id == gnode.Id)
				return true;
		return false;
	}

 	@Override
	public String toString(){
			String output = "";
			TreeNode tnode;
			for (int i = 0; i < nodes.size(); i++){
					tnode = remove();
					output += " " + tnode.graphNode.Id;
					insert(tnode);
			}
			return output;
	}

	@Override
	public boolean empty() 
	{
		return nodes.isEmpty();
	}

	@Override
	public int size()
	{
		return nodes.size();
	}
}

/** Node Explorer
 */
class NodeExplorer
{
 /**
	* generic iterative graph search algorithm
	* @param G the given graph
	* @param O the open list
	* @param C the closed list
	* @param tnode the tree node
	*/
	public static void searchIterSub(Graph G, TreeNodeList O, TreeNodeList C, TreeNode tnode) 
	{
		O.insert(tnode);
//		System.out.println("O inserted: " + tnode.graphNode.Id);
		while (!O.empty())
		{
			TreeNode u = O.remove();
//			System.out.println("O removed: " + u.graphNode.Id);
			C.insert(u);
//			System.out.println("C inserted: " + u.graphNode.Id);
			if (C.size() == G.nodeList.size()) // goal check
			{
					System.out.println("solution:" + C);
					return;  
			}

			List<GraphEdge> neigh = G.adjList.get(u.graphNode.Id);
			for (int i = 0; i < neigh.size(); i++) 
			{
				GraphNode w = G.nodeList.get(neigh.get(i).target);
				if (!O.contains(w) && !C.contains(w))
				{
					O.insert(new TreeNode(w, u));
//					System.out.println("O inserted: " + w.Id);
				}
			}
		}
		System.out.println("search failed");
	}

  /**
	 * Generic iterative graph search algorithm - main routine
	 * @param G the given graph
	 * @param O the open list
	 * @param C the closed list
	 */
	public static void searchIterMain(Graph G, TreeNodeList O, TreeNodeList C) 
	{
		for (GraphNode gnode: G.nodeList)
			if (!C.contains(gnode))
				searchIterSub(G, O, C, new TreeNode(gnode, null));       
	}

 /**
	* generic recursive graph search algorithm for DFS
	* @param G the given graph
	* @param C the closed list
	* @param tnode the tree node
	*/
	public static boolean searchRecurSub(Graph G, TreeNodeList C, TreeNode v) 
	{
		C.insert(v);
		if (C.size() == G.nodeList.size()) // goal check
		{
			System.out.println("solution:" + C);
			return true;  
		}

		List<GraphEdge> neigh = G.adjList.get(v.graphNode.Id);
		for (int i = 0; i < neigh.size(); i++) 
		{
			GraphNode w = G.nodeList.get(neigh.get(i).target);
			if (!C.contains(w))
				return searchRecurSub(G, C, new TreeNode(w, v));
		}
		return false;
	}

	/**
	 * Generic recursive graph search algorithm - main routine, for DFS
	 * @param G the given graph
	 */
	public static void searchRecurMain(Graph G) 
	{	
		QueueTreeNodeList C = new QueueTreeNodeList();
		boolean found = false;
		for (GraphNode gnode: G.nodeList)
			if (!C.contains(gnode) && ! found)
				found = searchRecurSub(G, C, new TreeNode(gnode, null));       
		if (!found)
			System.out.println("search failed.");
	}
}

/** Path Explorer
 */
class PathExplorer
{

 /** 
  * print path recursively
  */
	public static void printPathRootToNode(TreeNode v)
	{
		if (v == null) return;
		printPathRootToNode(v.parentTreeNode);
		System.out.print(" " + v.graphNode.Id);
	}

 /**
	* generic recursive graph search algorithm for DFS
	* @param G the given graph
	* @param C the closed list
	* @param tnode the tree node
	*/
	public static void searchRecurSub(Graph G, TreeNodeList C, TreeNode v) 
	{
		C.insert(v);
		int childCount = 0;
		List<GraphEdge> neigh = G.adjList.get(v.graphNode.Id);
		for (int i = 0; i < neigh.size(); i++) 
		{
			GraphNode w = G.nodeList.get(neigh.get(i).target);
			if (!C.contains(w))
			{
				searchRecurSub(G, C, new TreeNode(w, v));
				childCount = childCount + 1;
			}
		}
		if (childCount == 0)	// print path
		{
			System.out.print("path: ");
			printPathRootToNode(v);
			System.out.println("");
		}
		C.remove();
	}

	/*
	 * Generic recursive graph search algorithm - main routine, for DFS
	 * @param G the given graph
	 */
	public static void searchRecurMain(Graph G) 
	{	
		StackTreeNodeList C = new StackTreeNodeList();
		searchRecurSub(G, C, new TreeNode(G.nodeList.get(0), null));       
	}
}

/** set and and run
 */
class prog03GraphSearch
{
	/** main function
	 * @param args the array of string arguments
	 */
	public static void main(String [] args)
	{
		/*
		 * 0 --------- 1 
		 * | \       / |
		 * |   \   /   |
		 * |     4     |
		 * |    / \    |
		 * |   /   \   |
		 * |  6---- 5  |
		 * | /       \ |
		 * |/         \| 
		 * 3 --------- 2
		 *
		 */     
 		
		int nodeCount = 7; // number of nodes
		int[][] adjMatrix	= { // adjacency matrix
													{ 0, 1, 0, 1, 1, 0, 0 },
													{ 1, 0, 1, 0, 1, 0, 0 },
													{ 0, 1, 0, 1, 0, 1, 0 },
													{ 1, 0, 1, 0, 0, 0, 1 },
													{ 1, 1, 0, 0, 0, 1, 1 },
													{ 0, 0, 1, 0, 1, 0, 1 },
													{ 0, 0, 0, 1, 1, 1, 0 },
												};

		Graph G = new Graph(nodeCount, adjMatrix);

		System.out.print("DFS Iter: ");
		NodeExplorer.searchIterMain(G, new StackTreeNodeList(), new QueueTreeNodeList());
		System.out.print("BFS Iter: ");
		NodeExplorer.searchIterMain(G, new QueueTreeNodeList(), new QueueTreeNodeList());
		System.out.print("DFS Recur: ");
		NodeExplorer.searchRecurMain(G);
		System.out.println("DFS Paths:");
		PathExplorer.searchRecurMain(G);
	}
}
