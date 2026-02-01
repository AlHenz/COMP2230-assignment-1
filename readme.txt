readme

- Alex Henley
- 3435482

ASSIGNMENT 1: COMP2230 - ALGORITHMS

The following are the files submitted for this Assignment:

==============================================================================================

mainProgram.java: This file contains the complete implementation of my program code for each of the four tasks that were to be solved to successfully complete this assignment. All tasks run within the "public static void main(String[] args)" method. Helper method "static void dfsForChains(...)" is also contained within this file and after the main method. I felt is was more concise to have it all within a single file as my tasks are clearly marked in the program so error identification was simple.

problemGenerator.java: The only justifiable change that was made to this file was the editing of the color constants, such as making sure "public color orangeColor" was actually orange (255, 165, 0), and that "public color yellowColor" was actually yellow (255, 255, 0). This made it much easier to identify errors in the color generation of my graph as I could more accurately attribute the node colors to the appropriate groups they were apart of as specified in Task 4.

readme.txt: The file currently open. Explains and details the files provided for this submission.

coversheet.pdf: The file showing my signed and filled-out assessment item cover sheet for this assignment. 

---------------------------------------------------------------------------------------------

Program should run as the assignment specifies using the "javac mainProgram.java" compiling command, and shouldn't need any special execution requirements.

---------------------------------------------------------------------------------------------

Justifications:
More major justifications for choices in my program code and what segments have been copied and modified can be found in the following spots:
> Line 69: Explains my choice in the use of an adjacency list over an adjacency matrix for Task 1.
> Line 85: Shows coded segment adapted from the graph constructor in prog03GraphSearch.java and changes.
> Line 117: Explains my use of BFS in Task 2, as well as some of the variables/terms used and their importance in the program.
> Line 139: Adaption from the BFS pattern in NodeExplorer in prog03GraphSearch.java, and my modifications to the adjacency list idea.
> Line 177: Explains the process and use of DFS in the identification of contact chains, and the data types that make it possible (infected[], onPath[], path)
> Line 195: Adaption from the recursive DFS in PathExplorer in prog03GraphSearch.java
> Line 235: Explains the implementation of the zone classification
> Line 319: Explains the adaption of the DFS skeleton from prog03GraphSearch.java in PathExplorer, altered to print every chain from start to infected node, color chain edges, and and backtrack via popping from path and clearing onPath

In-line comments are also present throughout my program implementations to provide further breakdown and explanation behind every operation within the program.

- Alex Henley
- 3435482