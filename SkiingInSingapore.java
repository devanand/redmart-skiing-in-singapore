import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
/**
 * 
 * Skiing in Singapore Solution
 * Problem solution to RedMart's programming challenge, written with modified BFS implementation. 
 * 
 * @author Ignatius
 *
 */
public class SkiingInSingapore {

	/**
	 * This solver implements modified BFS algorithm. This algorithm works best because:
	 * 1. Sparse Map
	 * 2. Directed Acyclic Graph
	 * 
	 * Although BFS alone is notoriously known as O(V + E) slow, where E can be V^2, we know that
	 * the number of Edge in this graph is less than that due to the nature of the problem.
	 * 
	 * Thus by running the BFS for every single source, we can still guarantee the running worst
	 * case running time is less than V^3
	 * 
	 * @param map
	 * @return bestSlope
	 */
	public SlopeStats solveMap(int[][] map){

		/*
		 * First Generate the Graph that is to be processed, use adjacency
		 * matrix to solve this problem. Assumption = map is always have
		 * rectangular shape
		 */
		int height = map.length;
		if (height < 1)
			return null;
		int width = map[0].length;
		if (width < 1)
			return null;

		// Each point in the map now becomes a vertex
		int totalVertex = height * width;
		
		// Current base case, doesnt move anywhere
		SlopeStats bestSlope = new SlopeStats(0,1);
		
		// We need to check from 
		for (int sourceVertex = 0; sourceVertex< totalVertex; sourceVertex++){
			//Initialization of Breadth First Search
			SlopeStats dummyValue = new SlopeStats(0, Integer.MIN_VALUE);
			SlopeStats[] vertexSlopeStatistics = new SlopeStats[totalVertex];
			Arrays.fill(vertexSlopeStatistics, dummyValue);
			Queue<Integer> queue = new LinkedList<Integer>();
			queue.add(sourceVertex);
			
			//Initialize slope stats at the beginning, initial drop at 0, length at 1
			SlopeStats sourceVertexStats = new SlopeStats(0,1);
			vertexSlopeStatistics[sourceVertex] = sourceVertexStats; 
			
			
			
			while (!(queue.isEmpty())){
				int currentVertex = queue.poll();
				//translate to xy to check with map
				int x = currentVertex % height;
				int y = currentVertex / height;
				
				SlopeStats slopeStatsAtCurrent = vertexSlopeStatistics[currentVertex];
				
				int currentDrop = slopeStatsAtCurrent.getDrop();
				int currentLength = slopeStatsAtCurrent.getLength();

				/*
				 * Map calculation 
				 * 				N
				 *			V-width
				 *	W	V-1		V		V+1	E
				 *			V+width
				 * 				S
				 * 
				 * Special cases: - no north when vertex number is 0 to width-1 - no
				 * south when vertex number is (heigth-1)*width to (height-1)*width +
				 * width - 1 - no west when vertex number is n*width - no east when
				 * vertex number is (n*width)-1
				 */
				
				// Check and add West
				if (x > 0) {
					if (map[y][x - 1] < map[y][x]) {
						int totalDrop = currentDrop + (map[y][x] - map[y][x - 1]);
						// Set new drop and length to reach west vertex
						SlopeStats newWestStats = new SlopeStats(totalDrop, currentLength + 1);
						
						// Calculate the west vertex location
						int westVertex = currentVertex-1;
						if (newWestStats.compareTo(vertexSlopeStatistics[westVertex]) > 0){
							vertexSlopeStatistics[westVertex]= newWestStats;
							if (newWestStats.compareTo(bestSlope) > 0)
								bestSlope = newWestStats;
							queue.add(westVertex);
						}
					}
				}

				// Check and add East
				if (x < width - 1) {

					if (map[y][x + 1] < map[y][x]) {
						// Set the new drop and length to reach east vertex
						int totalDrop = currentDrop + (map[y][x] - map[y][x + 1]);
						SlopeStats newEastStats = new SlopeStats(totalDrop, currentLength + 1);
						
						// Calculate the east vertex location
						int eastVertex = currentVertex+1;
						
						// If the new one is longer, replace the old one
						if (newEastStats.compareTo(vertexSlopeStatistics[eastVertex]) > 0){
							vertexSlopeStatistics[eastVertex]= newEastStats;
							if (newEastStats.compareTo(bestSlope) > 0)
								bestSlope = newEastStats;
							queue.add(eastVertex);
						}
					}
				}

				// Check and add North
				if (y > 0) {

					if (map[y - 1][x] < map[y][x]) {
						// Set the new drop and length to reach north vertex
						int totalDrop = currentDrop + (map[y][x] - map[y-1][x]);
						SlopeStats newNorthStats = new SlopeStats(totalDrop, currentLength + 1);
						
						// Calculate the north vertex location
						int northVertex = currentVertex-width;
						
						// If the newer drop is longer or steeper, replace the old one
						if (newNorthStats.compareTo(vertexSlopeStatistics[northVertex]) > 0){
							vertexSlopeStatistics[northVertex]= newNorthStats;
							if (newNorthStats.compareTo(bestSlope) > 0)
								bestSlope = newNorthStats;
							queue.add(northVertex);
						}
					}
				}

				// Check and add South
				if (y < height - 1) {
					if (map[y + 1][x] < map[y][x]) {
						// Set the new drop and length to reach south vertex
						int totalDrop = currentDrop + (map[y][x] - map[y + 1][x]);
						SlopeStats newSouthStats = new SlopeStats(totalDrop, currentLength + 1);
						
						// Calculate the south vertex location
						int southVertex = currentVertex + width;
						
						// Replace if the new one is longer or steeper
						if (newSouthStats.compareTo(vertexSlopeStatistics[southVertex]) > 0) {
							vertexSlopeStatistics[southVertex] = newSouthStats;
							if (newSouthStats.compareTo(bestSlope) > 0)
								bestSlope = newSouthStats;
							queue.add(southVertex);
						}
					}
				}	
			}
		}
		
		
		return bestSlope;
	}

	/*
	 * Runner wrapper
	 * 
	 * @params args passed in by main method
	 */

	public void run(String args[]) {
		PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

		// Test if there is a filename input
		if (args.length < 1) {
			pr.println("Error: File Name Expected");
			return;
		}

		int map[][];
		String filePath = args[0];
		try {
			map = readMapFromFile(filePath);
		} catch (IOException e) {
			pr.println();
			return;
		}

		SlopeStats bestSlopeStats = solveMap(map);
		
		// Print the final email address
		pr.println(bestSlopeStats.getLength().toString() + bestSlopeStats.getDrop().toString() + "@redmart.com");
		pr.close();
		return;
	}

	public static void main(String args[]) {
		// Use buffered reader and writer to improve I/O Efficiency.
		SkiingInSingapore ski = new SkiingInSingapore();
		ski.run(args);

	}

	int[][] readMapFromFile(String filePath) throws IOException {
		BufferedReader bufferedReader;
		// Try to load file if, fail return error
		bufferedReader = new BufferedReader(new FileReader(filePath));

		int[][] map;
		try {
			int width, height;

			String widthHeight[] = bufferedReader.readLine().split("\\s+");
			width = Integer.parseInt(widthHeight[0]);
			height = Integer.parseInt(widthHeight[1]);

			map = new int[height][width];

			for (int i = 0; i < height; i++) {
				String[] horizontalArray = bufferedReader.readLine().split("\\s+");
				for (int j = 0; j < width; j++) {

					map[i][j] = Integer.parseInt(horizontalArray[j]);
				}
			}

		} finally {
			bufferedReader.close();

		}
		return map;
	}

}
