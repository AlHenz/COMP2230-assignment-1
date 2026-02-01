/** problem generator for people attending a social event.
 * 	@author M A Hakim Newton
 */

/* use tabsize 2 to see proper indentation */

// some useful stuffs
import java.lang.*;
import java.util.*;

// to display graphically
import java.awt.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.*;

/** generates x and y locations for a number of persons
 *  x and y values are in [0,length) and [0,width)
 * 	stores the locations of all persons in an array
 *  persons within a proximity param are in contacts
 *  visual display is possible for persons and contacts
 */
class problemGenerator
{
	// to throw an error message
	public class error extends Exception 
	{
		/** constructor
		 */
  	public error(String message)
		{
     	super(message);
  	}
	}

	// location in terms of x and y
	public class loc
	{
		public double x;		// x coordinate
		public double y;		// y coordinate

		/** constructor
		 *	@param x the x coordinate
		 *	@param y the y coordinate
		*/
		public loc(double x, double y) throws problemGenerator.error
		{
			if (x >= length) 
				throw new error("x " + x + " is beyond length " + length); 
			if (y >= width) 
				throw new error("y " + y + " is beyond width " + width); 
			this.x = x;
			this.y = y;
		}
	}

	// contact between two persons i and j
	// internally stored such that i < j 
	// if i > j is given, they are swapped
	public class con
	{
		public int i;			// index of the first person
		public int j;			// index of the second person

		/** constructor
		 * 	@param i index of the first person
		 *	@param j index of the second person
		 */
		public con(int i, int j) throws problemGenerator.error
		{
			if (i >= numPerson) 
				throw new error("unknown person " + i);
			if (j >= numPerson) 
				throw new error("unknown person " + j);

			if (i == j)
				throw new error(i + " and " + j + " cannot be the same");

			// determine the order (i, j) or (j, i)
			if (i < j)
			{
				this.i = i;
				this.j = j;
			}
			else
			{
				this.i = j;
				this.j = i;
			}
		}
	
		/** Whether two contacts are the same regardless of ordering i,j or j,i
		 * 	@param c contact object comprising two persons
		 *	@return whether two contact objects are essential the same 
		 */
		public boolean isSame(con c)
		{
			return ((this.i == c.i && this.j == c.j) || (this.i == c.j && this.j == c.i)); 
		}
	}

	// essential settings 
	public int numPerson;				// number of person attending the event
	public double length;				// the length of the event venue
	public double width;				// the width of the event venue
	public double proximity;		// the proximity between two persons
	public loc[] location;			// location for each person

	public int numContact;			// number of contacts found in the event
	public con[] contact;				// whether two persons are in contact

	public int numInfected;			// number of persons infected
	public int[] infected;			// list of persons infected	 

	public long randomSeed;			// seed of the random number generator

	// print settings
	public boolean printLocations;	// print the locations 
	public boolean printContacts;		// print the contacts
	public boolean printInfected;		// print the infected persons

	// display settings
	public double scaleFactor;	// scale factor used with both dimensions
	public double nodeRadius;		// radius of a node to represent a person
	public double lineWidth;		// width of a line in drawing the edges						
	public boolean showNodes; 	// show the indexes of the persones with the nodes
	public boolean showCoords;	// show coordinates of the nodes for the persons
	public boolean showEdges;		// show the indexes of the edges for the contacts
	public boolean showLines;		// show the lines to denote the contacts
	public Color[] nodeColor;		// color for the nodes representing persons
	public Color[] edgeColor;		// color for the edges representing contacts 

	// color constants to be used for nodes	
	public Color normalColor = Color.GRAY;						// normal person
	public Color infectedColor = Color.RED;						// infected person
	public Color orangeColor = new Color(255,165,0);		// orange zone
	public Color yellowColor = new Color(255,255,0);	// yellow zone
	public Color grayColor = Color.BLACK;						// gray zone
	public Color chainColor = Color.BLUE;							// purple
	// also look at groupColor(), which uses the following color values

	/** constructor
	 *	@param numPerson the number of persons attending the event
	 *	@param length the length of the event venue
	 *	@param width the width of the event venue
	 *	@param proximity the distance between two person to be in contact
	 *	@param numInfected the number of infected persons
	 *	@param randomSeed the seed to be used for random number generation
	 */
	problemGenerator(int numPerson, double length, double width, 
			double proximity, int numInfected, long randomSeed) 
				throws problemGenerator.error
	{
		// given essential settings
		this.numPerson = numPerson;
		this.length = length;
		this.width = width;
		this.proximity = proximity;

		// default display settings
		this.scaleFactor = 20.0;
		this.nodeRadius = 5.0;
		this.lineWidth = 2.0;
		this.showNodes = true;
		this.showCoords = true;
		this.showEdges = true;
		this.showLines = true;

		this.printLocations = true;
		this.printContacts = true;
		this.printInfected = true;
		
		this.location = new loc[numPerson]; 

		this.randomSeed = randomSeed;
		Random rng = new Random(randomSeed);

		// randomly generate locations 
		for (int i = 0; i < numPerson; i++)
		{
			double x = length * rng.nextDouble();
			double y = width * rng.nextDouble();
			this.location[i] = new loc(x, y);	
		}
	
		// determine contacts between persons
		this.numContact = 0;
		for(int i = 0; i < numPerson - 1; i++)
			for(int j = i + 1; j < numPerson; j++)
				if (distance(i,j) <= proximity)
					this.numContact++;
		this.contact = new con[numContact];
		this.numContact = 0;
		for(int i = 0; i < numPerson - 1; i++)
			for(int j = i + 1; j < numPerson; j++)
				if (inContact(i,j))
				{
					contact[numContact] = new con(i, j);
					this.numContact++;
				}
		
		// generate the infected persons
		if (numInfected > numPerson)
			throw new error("infected persons are more than total persons");

		this.infected = new int[numInfected];
		int [] tempInfected = new int[numPerson];
		for(int i = 0; i < numPerson; i++)
			tempInfected[i] = i;
		for(int k = 0; k < numInfected; k++)
		{
			int i = rng.nextInt(numPerson);
			int attempt = 0;
			while(tempInfected[i] == numPerson && attempt < numPerson * numInfected)
				i = rng.nextInt(numPerson);
			if (attempt == numPerson * numInfected)
				throw new error("attempts exhausted in generating infected persons");
			tempInfected[i] = numPerson;	// mark as infected
		}
		this.numInfected = 0;
		for(int i = 0; i < numPerson; i++)
			if (tempInfected[i] == numPerson)
			{
				this.infected[this.numInfected] = i;
				this.numInfected++;
			}

		// color settings for the nodes
		this.nodeColor = new Color[numPerson];
		for(int i = 0; i < numPerson; i++)
			this.nodeColor[i] = normalColor;
	
		// color settings for the edges
		this.edgeColor = new Color[numContact];
		for(int k = 0; k < numContact; k++)
			this.edgeColor[k] = normalColor;
	}

	/** calculate the distance between two persons
   *	@param i index of the first person
   *  @param j index of the second person
	 *  @return the distance between the two persons
   */
	public double distance(int i, int j) throws problemGenerator.error
	{
		if (i >= numPerson) 
			throw new error("unknown person " + i);

		if (j >= numPerson) 
			throw new error("unknown person " + j);
		if (i == j) 
			throw new error("the same person + i " + " " + j);

		// simple Euclidean distance formula
		return Math.sqrt((location[i].x - location[j].x)*(location[i].x - location[j].x) +
						(location[i].y - location[j].y)*(location[i].y - location[j].y));
	}

	/** are two persons in contact?
   * 	@param i index of the first person
   * 	@param j index of the second person
	 *  @return whether two persons are in contact based on proximity
   */
	public boolean inContact(int i, int j) throws problemGenerator.error
	{
		return distance(i, j) <= proximity;
	}

	/** set the print settings
	 *	@param printLocations print the locations
	 *  @param printContacts print the contacts
   */
	public void setPrint(boolean printLocations, 
												boolean printContacts, boolean printInfected)
	{
		this.printLocations = printLocations;
		this.printContacts = printContacts;
		this.printInfected = printInfected;
	}

	/** print the locations of the persons and the contacts
	 */
	public void print()
	{
		// print the locations of the persons
		if (printLocations)
		{
			System.out.println(numPerson + " locations");
			for(int i = 0; i < numPerson; i++)
				System.out.println(i + "(" + Math.round(location[i].x) + "," 
																		+ Math.round(location[i].y) + ")");
			System.out.println(numPerson + " locations");
		}

		// print the contacts between persons
		if (printContacts)
		{
			System.out.println(numContact + " contacts"); 
			for(int k = 0; k < numContact; k++)
				System.out.println(k +"(" + contact[k].i + "," + contact[k].j + ")");
			System.out.println(numContact + " contacts"); 
		}

		// print the indexes of the infected persons
		if (printInfected)
		{
			System.out.println(numInfected + " infected");
			for(int k = 0; k < numInfected; k++)
				System.out.println(infected[k]);
			System.out.println(numInfected + " infected");
		}
	}

	/** set the display settings
   *	@param scaleFactor scale factor used for both dimensions
	 *  @param nodeRadius  radius of a node to represent a person
	 *	@param lineWidth	 width of a line in drawing the edges
   *	@param showNodes	 show the indexes of the persons with the nodes
	 *  @param showCoords	 show the coordinates of the nodes for the persons
	 * 	@param showEdges	 show the indexes of the edges for the contacts
	 *  @param showLines	 show the lines to denote the contacts
   */  
	public void setDisplay(double scaleFactor, double nodeRadius, double lineWidth,
													boolean showNodes, boolean showCoords, 
													boolean showEdges, boolean showLines)
	{
		this.scaleFactor = scaleFactor;
		this.nodeRadius = nodeRadius;
		this.lineWidth = lineWidth;
		this.showNodes = showNodes;
		this.showCoords = showCoords;
		this.showEdges = showEdges;
		this.showLines = showLines;
	}

	/** get a color for a member of a group
	 * 	@param j the root of the group
	 */
	public Color groupColor(int j)
	{
		int numColor = 8;		// the number of colors in the color array
		Color[] color = {	new Color(102,0,153),
											Color.CYAN,
		                  Color.GREEN,
											Color.MAGENTA,
		                  Color.PINK,
											new Color(0,102,0),		// DARK GREEN
											new Color(0,0,153),		// DARK BLUE
											new Color(102,51,0)		// BROWN
										};
		return color[j % numColor];
	}

	/** change color of the individual person
   *	@param i index of the person
	 *	@param c color to be used
	 */
	public void setNodeColor(int i, Color c) throws problemGenerator.error
	{
		if (i >= numPerson) 
			throw new error("unknown person " + i);

		nodeColor[i] = c;
	}

	/** change color for the individual edges
   *  @param i index of the first person
	 *  @param j index of the second person
	 *  @param c index of the color
   */
	public void setEdgeColor(int i, int j, Color c) throws problemGenerator.error
	{
		con cc = new con(i,j);

		// the following is not efficient, as we perform a linear search here
		for(int k = 0; k < numContact; k++)
			if (contact[k].isSame(cc))
			{
				edgeColor[k] = c; 
				return;
			}
		throw new error("contact " + i + "," + j + " not found");
	}

	/** draw the locations of the persons
	 */
	public class drawer extends JPanel
	{
		// margin to be used around
		public static final double margin = 5.0; 

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D)g;
			BasicStroke stroke = new BasicStroke((float)lineWidth);
			g2d.setStroke(stroke);
			
			// draw the persons
			for(int i = 0; i < numPerson; i++)
			{
				g.setColor(nodeColor[i]);

				int ix = (int)Math.round((location[i].x + margin) * scaleFactor);
				int iy = (int)Math.round((location[i].y + margin) * scaleFactor);
				int ir = (int)Math.round(nodeRadius);
				g.fillOval(ix, iy, ir, ir);

				String str = "";
				if (showNodes) 
					str = str + i;

				if (showCoords) 
					str = str + "(" + Math.round(location[i].x) + "," 
													+ Math.round(location[i].y) + ")";

				if (showNodes || showCoords)
					g.drawString(str, ix, iy);
			}
			
			// draw the contacts
			if (showEdges || showLines)
				for(int k = 0; k < numContact; k++)
				{
					g.setColor(edgeColor[k]);
					int i = contact[k].i;
					int j = contact[k].j;
					int ix = (int)Math.round((location[i].x + margin) * scaleFactor);
					int iy = (int)Math.round((location[i].y + margin) * scaleFactor);
					int jx = (int)Math.round((location[j].x + margin) * scaleFactor);
					int jy = (int)Math.round((location[j].y + margin) * scaleFactor);

					if (showLines)
						g.drawLine(ix, iy, jx, jy);
					if (showEdges)
						g.drawString(""+ k, (ix+jx)/2, (iy+jy)/2);
				}
			}
	}

	/** display the locations graphically
   */
	public void display()
	{
		var panel = new drawer();
		panel.setBackground(Color.WHITE);
		var frame = new JFrame("Locations of Persons");
		int l = (int)Math.round((length + 2 * drawer.margin) * scaleFactor);
		int w = (int)Math.round((width + 2 * drawer.margin) * scaleFactor);
		frame.setSize(l, w);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// EXIT_ON_CLOSE
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.setVisible(true);
	}
}
