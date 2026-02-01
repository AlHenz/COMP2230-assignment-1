/** disjoint sets
 * @author M A Hakim Newton
 */

/* use tabsize 2 to see proper indentation of the code */

public class prog02DisjointSets
{
	private static final int size = 7;
	
	/** disjoint sets simple
	 */
	static class disjointSetsSimple
	{
		// to store the parent of each node
		private int [] parent;				
		
		/** constructor
		 */
		public disjointSetsSimple()
		{
			parent = new int[size];
		}

		/** make a singleton for a node
		 * @param k the node
		 */
		public void make(int k) 
		{
			parent[k] = k;
		}

		/** find the parent of a node
		 * @param k the node
		 */
		public int find(int k) 
		{
			while (k != parent[k]) 
				k = parent[k];
			return k;
		}

		/** find the union of two nodes
		 * @param i one node
		 * @param j another node
		 */
		public void union(int i, int j) 
		{
			i = find(i);	// find the root of the set 
			j = find(j);	// find the root of the set
			parent[i] = j; // make one root child of another
		}

		public void print()
		{
			System.out.print("  nodes:");
			for(int k = 0; k < size; k++)
				System.out.print(" " + k);
			System.out.print("\nparents:");
			for(int k = 0; k < size; k++)
				System.out.print(" " + parent[k]);
			System.out.println();
		}
  }

	/** disjoint sets height
	 */
	static class disjointSetsHeight
	{
		// to store the parent of each node
		private int [] parent;			
		private int [] height;	
		
		/** constructor
		 */
		public disjointSetsHeight()
		{
			parent = new int[size];
			height = new int[size];
		}

		/** make a singleton for a node
		 * @param k the node
		 */
		public void make(int k) 
		{
			parent[k] = k;
			height[k] = 0;
		}

		/** find the parent of a node
		 * @param k the node
		 */
		public int find(int k) 
		{
			while (k != parent[k]) 
				k = parent[k];
			return k;
		}

		/** find the union of two nodes
		 * @param i one node
		 * @param j another node
		 */
		public void union(int i, int j) 
		{
			i = find(i);	// find the root of the set 
			j = find(j);	// find the root of the set
			// make one root child of another
			if (height[i] < height[j])
				parent[i] = j; 
			else if (height[i] > height[j])
				parent[j] = i;
			else
			{
				parent[i] = j;
				height[j] = height[j] + 1;
			}
		}

		public void print()
		{
			System.out.print("  nodes:");
			for(int k = 0; k < size; k++)
				System.out.print(" " + k);
			System.out.print("\nparents:");
			for(int k = 0; k < size; k++)
				System.out.print(" " + parent[k]);
			System.out.print("\nheights:");
			for(int k = 0; k < size; k++)
				System.out.print(" " + height[k]);
			System.out.println();
		}
  }

	/** disjoint sets Rank
	 */
	static class disjointSetsRank
	{
		// to store the parent of each node
		private int [] parent;			
		private int [] rank;	
		
		/** constructor
		 */
		public disjointSetsRank()
		{
			parent = new int[size];
			rank = new int[size];
		}

		/** make a singleton for a node
		 * @param k the node
		 */
		public void make(int k) 
		{
			parent[k] = k;
			rank[k] = 0;
		}

		/** find the parent of a node
		 * @param k the node
		 */
		public int find(int k) 
		{
			int r = k;
			while (r != parent[r]) 
				r = parent[r];
			while(parent[k] != r)
			{
				int kk = parent[k];
				parent[k] = r;
				k = kk;
			}
			return r;
		}

		/** find the union of two nodes
		 * @param i one node
		 * @param j another node
		 */
		public void union(int i, int j) 
		{
			i = find(i);	// find the root of the set 
			j = find(j);	// find the root of the set
			// make one root child of another
			if (rank[i] < rank[j])
				parent[i] = j; 
			else if (rank[i] > rank[j])
				parent[j] = i;
			else
			{
				parent[i] = j;
				rank[j] = rank[j] + 1;
			}
		}

		public void print()
		{
			System.out.print("  nodes:");
			for(int k = 0; k < size; k++)
				System.out.print(" " + k);
			System.out.print("\nparents:");
			for(int k = 0; k < size; k++)
				System.out.print(" " + parent[k]);
			System.out.print("\n  ranks:");
			for(int k = 0; k < size; k++)
				System.out.print(" " + rank[k]);
			System.out.println();
		}
  }

  /** main function
   * @param args array of string arguments
   */
  public static void main(String[] args) 
	{
		System.out.println("Disjoint Sets Simple");
		disjointSetsSimple s1 = new disjointSetsSimple();
		for(int k = 0; k < size; k++)
			s1.make(k);
		System.out.println("singletons created 0-6");
		s1.print();
		System.out.println("union(1,2), union(3,2), union(4,5), union(5,6)");
		s1.union(1,2);
		s1.union(3,2);
		s1.union(4,5);
		s1.union(5,6);
		s1.print();
		System.out.println("find(4): " + s1.find(4));

		System.out.println();
		System.out.println("Disjoint Sets Height");
		disjointSetsHeight s2 = new disjointSetsHeight();
		for(int k = 0; k < size; k++)
			s2.make(k);
		System.out.println("singletons created 0-6");
		s2.print();
		System.out.println("union(1,2), union(3,2), union(4,5), union(5,6)");
		s2.union(1,2);
		s2.union(3,2);
		s2.union(4,5);
		s2.union(5,6);
		s2.print();
		System.out.println("find(4): " + s2.find(4));
		
		System.out.println();
		System.out.println("Disjoint Sets Simple");
		disjointSetsRank s3 = new disjointSetsRank();
		for(int k = 0; k < size; k++)
			s3.make(k);
		System.out.println("singletons created 0-6");
		s3.print();
		System.out.println("union(1,2), union(3,2), union(4,5), union(5,6)");
		s3.union(1,2);
		s3.union(3,2);
		s3.union(4,5);
		s3.union(5,6);
		s3.print();
		System.out.println("find(4): " + s3.find(4));
		s3.print();
	}	
}
