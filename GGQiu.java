import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Random;



public class GGQiu extends JFrame{
	public static Points p = new Points();
	public static Edges e = new Edges();
	public static GraphGui g;
	public static int testnum = 0;
	public static int pointCounter = 0;
	public static int edgeCounter = 0;
	public static int firstPointIndex = -1;
	public static int lastPointIndex = -1;
	public static int movePointIndex = -1;
	public static int changeWP1 = -1;	// Point index 1 for changing weight
	public static int changeWP2 = -1;	// Point index 2 for changing weight
	public static int shortestPahtP1 = -1;	// Point index 1 for the starting point in finding shortest path
	public static int shortestPathP2 = -1;	// Point index 2 for the ending point in finding shortest path
	public static ArrayList<Integer> shortestPath = new ArrayList<Integer>();
	
	public static void main(String args[]){
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			
		}
		g = new GraphGui();
	}
	
	
}


class GraphGui extends JFrame{
	public static JRadioButton aV;
	public static JRadioButton aE;
	public static JRadioButton mV;
	public static JRadioButton sP;
	public static JRadioButton cW;
	public static JButton b1;
	public static JButton b2;
	public static JButton b3;
	public static JButton b4;
	public static JTextField inputText;
	
	
	public GraphGui() {
		this.setTitle("Graph GUI");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(2000, 2000);
		this.setLocation(50, 50);
		this.setLayout(new BorderLayout());
		
		// Set up the left panel
		// Radio buttons
		String addVertex	= "Add Vertex";
		String addEdge		= "Add Edge";
		String moveVertex	= "Move Vertex";
		String shortestPath	= "Shortest Path";
		String changeWeight	= "Change a weight to:";
		
		aV = new JRadioButton(addVertex);
		aV.setFont(aV.getFont().deriveFont(24.0f));
		aV.setSelected(true);
		
		aE = new JRadioButton(addEdge);
		aE.setFont(aE.getFont().deriveFont(24.0f));
		aE.setSelected(true);
		
		mV = new JRadioButton(moveVertex);
		mV.setFont(mV.getFont().deriveFont(24.0f));
		mV.setSelected(true);
		
		sP = new JRadioButton(shortestPath);
		sP.setFont(sP.getFont().deriveFont(24.0f));
		sP.setSelected(true);
		
		cW = new JRadioButton(changeWeight);
		cW.setFont(cW.getFont().deriveFont(24.0f));
		cW.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(aV);
		group.add(aE);
		group.add(mV);
		group.add(sP);
		group.add(cW);
		
		// Textfield
		inputText = new JTextField(10);
		inputText.setFont(inputText.getFont().deriveFont(30.0f));
		inputText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = inputText.getText();
				int temp = Integer.parseInt(input);
				
				// Changing weight for an edge
				if(GGQiu.changeWP1 != -1 && GGQiu.changeWP2 != -1) {
					GGQiu.e.getEdge(GGQiu.e.edgeSelector(GGQiu.changeWP1, GGQiu.changeWP2)).setWeight(temp);
					
					// Re-initializing
					GGQiu.changeWP1 = -1;
					GGQiu.changeWP2 = -1;
				}
			}
		});
		
		// Normal buttons
		b1 = new JButton("Add All Edges");
		b1.setFont(b1.getFont().deriveFont(24.0f));
		b1.setVerticalTextPosition(AbstractButton.CENTER);
		b1.setHorizontalTextPosition(AbstractButton.CENTER);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// No edge needed
				if(GGQiu.p.sizeOfPointList() <= 1)
					return;
				else {
					int newAdded = 0;
					for(int i = 1; i < GGQiu.p.sizeOfPointList(); i++)
						for(int j = 0; j < i; j++) {
							// Check if the edge existed already, if so skip this edge
							if(GGQiu.e.edgeSelector(i, j) != -1)
								continue;
							// No such edge, add it to the list
							else {
								newAdded++;
								GGQiu.edgeCounter++;
								Edge tempE = new Edge(GGQiu.edgeCounter, i, j);
								GGQiu.e.addEdge(tempE);
							}
						}
					System.out.println(newAdded + " new edges added");
				}
			}
		});
		
		b2 = new JButton("Random Weights");
		b2.setFont(b2.getFont().deriveFont(24.0f));
		b2.setVerticalTextPosition(AbstractButton.CENTER);
		b2.setHorizontalTextPosition(AbstractButton.CENTER);
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int max = 50;
				int min = 1;
				Random rand = new Random();
				for(int i = 0; i < GGQiu.e.sizeOfEdgeList(); i++) {
					int randomWeight = rand.nextInt(max) + min;
					GGQiu.e.getEdge(i).setWeight(randomWeight);
				}
				if(GraphGui.sP.isSelected()) {
					Paths tempPaths2 = new Paths(GGQiu.pointCounter);
					GGQiu.shortestPath = tempPaths2.shortestPath(GGQiu.shortestPahtP1, GGQiu.shortestPathP2);
				}
			}
		});
		
		b3 = new JButton("Minimal Spanning Tree");
		b3.setFont(b3.getFont().deriveFont(24.0f));
		b3.setVerticalTextPosition(AbstractButton.CENTER);
		b3.setHorizontalTextPosition(AbstractButton.CENTER);
		
		b4 = new JButton("Help");
		b4.setFont(b4.getFont().deriveFont(24.0f));
		b4.setVerticalTextPosition(AbstractButton.CENTER);
		b4.setHorizontalTextPosition(AbstractButton.CENTER);
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame help = new JFrame();
				help.setTitle("Help");
				help.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				help.setSize(100, 100);
				help.setLayout(new GridLayout(1, 1));
				JTextArea text = new JTextArea(20, 40);
				text.setEditable(false);
				text.setText("Adding vertex: click on the white panel; \n"
						   + "Adding edge: click on one vertex first, and then click on another; \n"
						   + "Change Weight: click on two vertex to select edge, then enter the weight; \n");
				Font helpFont = new Font("Serif", 0, 40);
				text.setFont(helpFont);
				
				help.add(text);
				
				help.pack();
				help.setVisible(true);
			}
		});
		
		
		JPanel leftPanel = new JPanel(new GridLayout(9, 1));
		leftPanel.setPreferredSize(new Dimension(550, 1000));
		
		leftPanel.add(aV);
		leftPanel.add(aE);
		leftPanel.add(mV);
		leftPanel.add(sP);
		
		// Add the radio button with textfield
		JPanel wPanel = new JPanel(new GridLayout(1, 2));
		wPanel.add(cW);
		wPanel.add(inputText);
		leftPanel.add(wPanel);
		
		leftPanel.add(b1);
		leftPanel.add(b2);
		leftPanel.add(b3);
		leftPanel.add(b4);		
		
		this.add(leftPanel, BorderLayout.WEST);
		
		// Add drawing panel
		GraphPicturePanel gp = new GraphPicturePanel();
		gp.setPreferredSize(new Dimension(1500, 1500));
		gp.setBackground(Color.WHITE);
		this.add(gp, BorderLayout.CENTER);
		
		
		if(aV.isSelected())
			gp.setMode(1);
		if(aE.isSelected())
			gp.setMode(2);
		
		
		this.pack();
		this.setVisible(true);
	}
}


class GraphPicturePanel extends JPanel{
	
	private int mouseX = 0, mouseY = 0;
	private boolean drawOn = false;
	
	/*
	 *  drawingMode is a drawing function selector
	 *  DrawingModeNumber:
	 *  	0. Closing Drawing Function
	 *  	1. Drawing Point
	 *  	2. Drawing Edge
	 */	
	private int drawingMode = 0;
	
	public void setMode(int m) {
		drawingMode = m;
	}
	
	public GraphPicturePanel() {
		MouseHandler mh = new MouseHandler();
		this.addMouseListener(mh);
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(drawOn == true) {
			// Drawing a point
			if(GraphGui.aV.isSelected()) {
				g2d.setColor(Color.RED);
				g2d.fillOval(mouseX, mouseY, 20, 20);
			}
			
			// Adding selected feature
			if(GraphGui.aE.isSelected() || GraphGui.cW.isSelected() || GraphGui.mV.isSelected() || GraphGui.sP.isSelected()) {
				g2d.setColor(Color.GREEN);
				g2d.fillOval(mouseX, mouseY, 20, 20);
			}
		}
		
		// Refreshing all points
		g2d.setColor(Color.RED);
		for(int i = 0; i < GGQiu.p.sizeOfPointList(); i++) {
			g2d.fillOval(GGQiu.p.getPoint(i).getX(), GGQiu.p.getPoint(i).getY(), 20, 20);
		}
		
		// Refreshing all edges
		g2d.setColor(Color.BLUE);
		g2d.setStroke(new BasicStroke(5));
		for(int i = 0; i < GGQiu.e.sizeOfEdgeList(); i++) {
			g2d.drawLine(GGQiu.p.getPoint(GGQiu.e.getEdge(i).getP1Index()).getX(),
						 GGQiu.p.getPoint(GGQiu.e.getEdge(i).getP1Index()).getY(),
						 GGQiu.p.getPoint(GGQiu.e.getEdge(i).getP2Index()).getX(),
						 GGQiu.p.getPoint(GGQiu.e.getEdge(i).getP2Index()).getY());
		}
		
		// Refreshing all weight
		g2d.setColor(Color.BLUE);
		Font f = new Font("Serif", Font.BOLD, 40);
		g2d.setFont(f);
		for(int i = 0; i < GGQiu.e.sizeOfEdgeList(); i++) {
			int tempW = GGQiu.e.getEdge(i).getWeight();
			if(tempW == 99999)
				continue;
			
			// Finding out two points of an edge and calculating their middle location
			int tempP1Num = GGQiu.e.getEdge(i).getP1Index();
			int tempP2Num = GGQiu.e.getEdge(i).getP2Index();
			int midX = (GGQiu.p.getPoint(tempP1Num).getX() + GGQiu.p.getPoint(tempP2Num).getX()) / 2;
			int midY = (GGQiu.p.getPoint(tempP1Num).getY() + GGQiu.p.getPoint(tempP2Num).getY()) / 2;
			
			// Displaying the weight at the middle position of an edge
			g2d.drawString(Integer.toString(tempW), midX, midY);
		}
		
		// Refreshing the shortest path
		if(GraphGui.sP.isSelected()) {	
			// Signing the shortest path state
			g2d.setColor(Color.ORANGE);
			Font f2 = new Font("Serif", Font.BOLD, 40);
			g2d.setFont(f2);
			g2d.drawString("Shortest Path (In testing)", 50, 50);
			
			// Drawing the (shortest) path
			g2d.setStroke(new BasicStroke(5));
			int numOfPointInPath = GGQiu.shortestPath.size();
			for(int i = 0; i < numOfPointInPath - 1; i++) {
				int st_X = GGQiu.p.getPoint(GGQiu.shortestPath.get(i)).getX();
				int st_Y = GGQiu.p.getPoint(GGQiu.shortestPath.get(i)).getY();
				int ed_X = GGQiu.p.getPoint(GGQiu.shortestPath.get(i+1)).getX();
				int ed_Y = GGQiu.p.getPoint(GGQiu.shortestPath.get(i+1)).getY();
				g2d.drawLine(st_X, st_Y, ed_X, ed_Y);
			}
		}
			
		repaint();
	}
	
	
	
	private class MouseHandler implements MouseListener{
		
		public void mouseClicked(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			drawOn = true;
			
			System.out.println("Point(" + mouseX + ", " + mouseY + ")");
			
			// Adding vertex
			if(GraphGui.aV.isSelected()) {
				Point tempPoint = new Point(GGQiu.pointCounter, mouseX, mouseY);
				GGQiu.pointCounter++;
				GGQiu.p.addPoint(tempPoint);
			}
			
			// Adding edge
			if(GraphGui.aE.isSelected()) {
				int temp = -1;
				// Selecting the first point
				if(GGQiu.firstPointIndex == -1) {
					temp = GGQiu.p.pointSelector(mouseX, mouseY);
					if(temp != -1)
						GGQiu.firstPointIndex = temp;
					return;
				}
				else{
					temp = GGQiu.p.pointSelector(mouseX, mouseY);
					// Error avoiding
					if(temp == -1)
						return;
					if(temp != -1 && temp != GGQiu.firstPointIndex)
						GGQiu.lastPointIndex = temp;
					
					System.out.println("Point " + GGQiu.firstPointIndex + "---->" + "Point " + GGQiu.lastPointIndex);
					
					// Got two different points, add edge
					// And make sure not to duplicate the edge
					if(GGQiu.e.edgeSelector(GGQiu.firstPointIndex, GGQiu.lastPointIndex) == -1) {
						Edge tempEdge = new Edge(GGQiu.edgeCounter, GGQiu.firstPointIndex, GGQiu.lastPointIndex);
						GGQiu.e.addEdge(tempEdge);
						GGQiu.edgeCounter++;
					}
					
					// re-initialize
					GGQiu.firstPointIndex = -1;
					GGQiu.lastPointIndex = -1;
				}
			}
			
			// Moving vertex
			if(GraphGui.mV.isSelected()) {
				if(GGQiu.movePointIndex == -1) {
					int tempMovePointIndex = GGQiu.p.pointSelector(mouseX, mouseY);
					if(tempMovePointIndex != -1)
						GGQiu.movePointIndex = tempMovePointIndex;
					return;
				}
				else if(GGQiu.movePointIndex != -1) {
					GGQiu.p.getPoint(GGQiu.movePointIndex).setX(mouseX);
					GGQiu.p.getPoint(GGQiu.movePointIndex).setY(mouseY);
					
					// Re-initializing
					GGQiu.movePointIndex = -1;
					return;
				}
			}
			
			// Adding weight to an edge
			if(GraphGui.cW.isSelected()) {
				int temp;
				// No point is selected
				if(GGQiu.changeWP1 == -1 && GGQiu.changeWP2 == -1) {
					temp = GGQiu.p.pointSelector(mouseX, mouseY);
					if(temp != -1)
						GGQiu.changeWP1 = temp;
					return;
				}
				// The first point is already selected
				else if(GGQiu.changeWP1 != -1) {
					temp = GGQiu.p.pointSelector(mouseX, mouseY);
					if(temp == -1)
						return;
					if(temp != -1 && temp != GGQiu.changeWP1 &&
					   // The edge does exist
					   GGQiu.e.edgeSelector(GGQiu.changeWP1, temp) != -1)
						GGQiu.changeWP2 = temp;
					return;
				}
				// The first point is selected, but the second point isn't selected successfully
				else {
					// Safety re-initializing
					GGQiu.changeWP1 = -1;
					GGQiu.changeWP2 = -1;
				}
			}
			
			// Selecting the path (for shortest path)
			if(GraphGui.sP.isSelected()) {
				int temp;
				// Not even starting point is selected
				if(GGQiu.shortestPahtP1 == -1 && GGQiu.shortestPathP2 == -1) {
					temp = GGQiu.p.pointSelector(mouseX, mouseY);
					if(temp != -1)
						GGQiu.shortestPahtP1 = temp;
					System.out.println("Starting point of path selected: Point# " + temp);
					return;
				}
				// The starting point is selected but not the ending point
				else if(GGQiu.shortestPahtP1 != -1 && GGQiu.shortestPathP2 == -1) {
					temp = GGQiu.p.pointSelector(mouseX, mouseY);
					if(temp == -1)
						return;
					if(temp != -1 && temp != GGQiu.shortestPahtP1)	// It's possible that there's no edges between two points selected
						GGQiu.shortestPathP2 = temp;
					System.out.println("Path selected: Point# " + GGQiu.shortestPahtP1 + " -----> Point# " + temp);
					
					/***********************************************************/
					/************		Shortest Path Function      ************/
					/***********************************************************/
					// Shortest path's code goes here
					Paths tempPaths = new Paths(GGQiu.pointCounter);
					GGQiu.shortestPath = tempPaths.shortestPath(GGQiu.shortestPahtP1, GGQiu.shortestPathP2);
					
					return;
				}
				// Both the starting and ending point are selected
				// In this case, erasing the path and selecting the starting point again!
				else if(GGQiu.shortestPahtP1 != -1 && GGQiu.shortestPathP2 != -1) {
					// Re-initializing first
					GGQiu.shortestPahtP1 = GGQiu.shortestPathP2 = -1;
					temp = GGQiu.p.pointSelector(mouseX, mouseY);
					if(temp != -1) {
						GGQiu.shortestPahtP1 = temp;
						System.out.println("Re-select the path from starting: Point# " + temp);
					}
					GGQiu.shortestPath.clear();
					return;
				}
				// Unexpected situation: the starting point is not selected but the ending point is selected
				else{
					// Safety re-initializing
					GGQiu.shortestPahtP1 = GGQiu.shortestPathP2 = -1;
					GGQiu.shortestPath.clear();
					System.out.println("Unexpected re-initialization");
				}
			}
		}
		
		

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}

	
}


class Point {
	private int point_Index;
	private int x;
	private int y;
	
	public Point(int n, int a, int b) {
		point_Index = n;
		x = a;
		y = b;
	}
	
	public void setNum(int n) {
		point_Index = n;
	}
	
	public void setX(int a) {
		x = a;
	}
	
	public void setY(int a) {
		y = a;
	}
	
	public int getPointIndex() {
		return point_Index;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}

class Points{
	private int pListSize;
	private ArrayList<Point> list;
	
	public Points() {
		list = new ArrayList<Point>();
		pListSize = 0;
	}
	
	public void addPoint(Point p) {
		list.add(p);
		pListSize++;
	}
	
	public int sizeOfPointList() {
		return pListSize;
	}
	
	public Point getPoint(int i) {
		return list.get(i);
	}
	
	// If the clicking point is inside the square with the central point which is in the point list,
	// return its point number.
	public int pointSelector(int inputX, int inputY) {
		int areaRange = 60;
		
		for(int i = 0; i < pListSize; i++) {
			if((inputX >= list.get(i).getX() - areaRange && inputX <= list.get(i).getX() + areaRange) &&
			   (inputY >= list.get(i).getY() - areaRange && inputY <= list.get(i).getY() + areaRange)) {
				return list.get(i).getPointIndex();
			}
		}
		// No point is selected
		return -1;
	}
	
	public void printPoints() {
		if(pListSize == 0) {
			System.out.println("Point List Empty!");
			return;
		}			
		System.out.println("Here is the point list:");
		for(int i = 0; i < pListSize; i++) {
			System.out.println("Point #" + list.get(i).getPointIndex() + ": " + "(" + list.get(i).getX() + ", " + list.get(i).getY() + ")");
		}
	}
}



class Edge {
	private int edge_Index;
	private int p1_Num;
	private int p2_Num;
	private int weight;
	
	public Edge(int i, int p1, int p2, int w) {
		edge_Index = i;
		weight = w;
		
		// Making sure that p1_Num is smaller than p2_Num
		if(p1 <= p2) {
			p1_Num = p1;
			p2_Num = p2;
		}
		else {
			p1_Num = p2;
			p2_Num = p1;
		}
	}
	
	public Edge(int i, int p1, int p2) {
		edge_Index = i;
		weight = 99999;
		
		// Making sure that p1_Num is smaller than p2_Num
		if(p1 <= p2) {
			p1_Num = p1;
			p2_Num = p2;
		}
		else {
			p1_Num = p2;
			p2_Num = p1;
		}
	}

	public int getEdgeIndex() {
		return edge_Index;
	}
	
	public String getEdgePoints() {
		String s1 = Integer.toString(p1_Num);
		String s2 = Integer.toString(p2_Num);
		String result = s1 + "_" + s2;
		return result;	// The keyword of an edge could be the combination of two indexes of the points
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int w) {
		weight = w;
	}
	
	public int getP1Index() {
		return p1_Num;
	}
	
	public int getP2Index() {
		return p2_Num;
	}
}


class Edges {
	private int eListSize;
	private ArrayList<Edge> list;
	
	public Edges() {
		list = new ArrayList<Edge>();
		eListSize = 0;
	}
	
	public void addEdge(Edge e) {
		list.add(e);
		eListSize++;
	}
	
	public int sizeOfEdgeList() {
		return eListSize;
	}
	
	public Edge getEdge(int i) {
		return list.get(i);
	}
	
	public int edgeSelector(int p1, int p2){
		// Making sure p1_Num is smaller than p2_Num
		if(p1 > p2) {
			int temp = p1;
			p1 = p2;
			p2 = temp;
		}
		for(int i = 0; i < eListSize; i++) {
			if(list.get(i).getP1Index() == p1 && list.get(i).getP2Index() == p2)
				return list.get(i).getEdgeIndex();
		}
		return -1;
	}
	
	public int containPoint(int edgeIndex, int onePoint) {
		int anotherPoint = -1;
		if(list.get(edgeIndex).getP1Index() == onePoint)
			anotherPoint = list.get(edgeIndex).getP2Index();
		else if(list.get(edgeIndex).getP2Index() == onePoint)
			anotherPoint = list.get(edgeIndex).getP1Index();
		
		// If the point# is not in the edge, return -1
		// Else, return the neighbour point# of this edge
		return anotherPoint;
	}
	
	public ArrayList<Integer> findNeighbours(int sourcePoint){
		ArrayList<Integer> neighbourList = new ArrayList<Integer>();
		for(int i = 0; i < eListSize; i++) {
			int tempPointNum = this.containPoint(i, sourcePoint);
			if(tempPointNum != -1)
				neighbourList.add(tempPointNum);
		}
		return neighbourList;
	}
	
	// Finding all neighbour points of a POINT SET
	public ArrayList<Integer> findNearPoints(ArrayList<Integer> sourceSet) throws Exception{
		ArrayList<Integer> result = new ArrayList<Integer>();
		// Scanning every point in the input set
		for(int i = 0; i < sourceSet.size(); i++) {
			// Searching inside the whole list
			int sourcePoint = sourceSet.get(i);
			ArrayList<Integer> tempList = new ArrayList<Integer>();
			tempList = this.findNeighbours(sourcePoint);
			// Got the list, make sure it's not empty
			if(!tempList.isEmpty())
				// Take away those points are in the sourceSet already
				// And take away points are already in the result list
				for(int j = 0; j < tempList.size(); j++)
					if(sourceSet.contains(tempList.get(j)) || result.contains(tempList.get(j))) {
						tempList.remove(j);
						j--;
					}
			// Got the neighbour point list for a single point in the sourceSet
			// Poping the neighbour # in the result
			if(!tempList.isEmpty())
				for(int j = 0; j < tempList.size(); j++)
					result.add(tempList.get(j));
		}
		
		if(result.isEmpty())
			throw new Exception("The path is not connected");
		else
			return result;
	}
}


class Path {
	private int pointIndex;
	private int distance;
	private ArrayList<Integer> path;
	
	public Path(int index) {
		pointIndex = index;
		distance = 999;
		path = new ArrayList<Integer>();
	}
	
	public int getPointIndex() {
		return pointIndex;
	}
	
	public void setDistance(int d) {
		distance = d;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setPath(ArrayList<Integer> p) {
		path = new ArrayList<Integer>(p);
	}
	
	public ArrayList<Integer> getPath(){
		return path;
	}
}


class Paths {
	private Path[] paths;
	private int sizeOfPaths;
	private ArrayList<Integer> settledPoints;
	private ArrayList<Integer> unsettledPoints;
	
	public Paths(int numberOfPoints) {
		sizeOfPaths = numberOfPoints;
		paths = new Path[numberOfPoints];
		settledPoints = new ArrayList<Integer>();
		unsettledPoints = new ArrayList<Integer>();
		for(int i = 0; i < numberOfPoints; i++) {
			paths[i] = new Path(i);
			unsettledPoints.add(i);
		}
	}
	
	public int getSizeOfPaths() {
		return sizeOfPaths;
	}
	
	public void printSettle() {
		System.out.print("Settle List: [");
		int i = 0;
		for(; i < settledPoints.size() - 1; i++) {
			System.out.print(settledPoints.get(i) + " , ");
		}
		System.out.print(settledPoints.get(i) + "]");
		System.out.println();
	}
	
	public void printUnsettle() {
		System.out.print("Unsettle List: [");
		int i = 0;
		for(; i < unsettledPoints.size() - 1; i++) {
			System.out.print(unsettledPoints.get(i) + " , ");
		}
		System.out.print(unsettledPoints.get(i) + "]");
		System.out.println();
	}
	
	public ArrayList<Integer> shortestPath(int source, int target){
		// Initializing
		paths[source].setDistance(0);
		ArrayList<Integer> sourcePath = new ArrayList<Integer>();
		sourcePath.add(source);
		paths[source].setPath(sourcePath);
		settledPoints.add(source);
		// Removing source from unsettledPoints list
		for(int i = 0; i < unsettledPoints.size(); i++) {
			if(unsettledPoints.get(i) == source)
				unsettledPoints.remove(i);
		}
		
		int sizeOfPathList = this.getSizeOfPaths();
		int bridge = -1;
		int bridgeWeight = -1;
		
		while(!unsettledPoints.isEmpty()) {
			this.printSettle();
			this.printUnsettle();
			
			// Finding the neighbour points of settled points, but not in settled list 
			ArrayList<Integer> tempNearestList = new ArrayList<Integer>();
			try{
				tempNearestList = GGQiu.e.findNearPoints(settledPoints);
			}catch (Exception e)
			{
				System.out.println("Illegal path!");
			}
			
			// Comparing every nearest point (with the points in the paths)
			for(int i = 0; i < tempNearestList.size(); i++)
				// Comparing with the whole path list every time
				for(int j = 0; j < sizeOfPathList; j++) {
					int nearPointIndex = tempNearestList.get(i);
					bridge = GGQiu.e.edgeSelector(tempNearestList.get(i), j);	/** j = index of point here **/
					// Skipping those path with distance 999
					if(paths[j].getDistance() == 999)
						continue;
					// Skipping those points do not connect to the current near point we scan
					else if(bridge == -1)
						continue;
					// Comparing the distance of near point with the shortest path of other points in path list
					else {
						bridgeWeight = GGQiu.e.getEdge(bridge).getWeight();
						int currentDistance = paths[nearPointIndex].getDistance();
						int compariedDistance = paths[j].getDistance() + bridgeWeight;
						// The current shortest path is not the shortest anymore
						if(currentDistance > compariedDistance) {
							// Changing the distance
							paths[nearPointIndex].setDistance(compariedDistance);
							
							// Changing the path
							ArrayList<Integer> newPath = new ArrayList<Integer>(paths[j].getPath());
							newPath.add(nearPointIndex);
							
							paths[nearPointIndex].setPath(newPath);
						}
					}
				}
			
			// Comparing among the points in the nearest point list
			// In case of any shortest paths need to be changed
			int myNearestPointIndex = -1;
			int yourNearestPointIndex = -1;
			
			for(int i = 0; i < tempNearestList.size(); i++) {
				myNearestPointIndex = tempNearestList.get(i);
				for(int j = 0; j < tempNearestList.size(); j++) {
					yourNearestPointIndex = tempNearestList.get(j);
					bridge = GGQiu.e.edgeSelector(myNearestPointIndex, yourNearestPointIndex);
					// No edge between them, not need to worry
					if(bridge == -1)
						continue;
					// There's edge, need to check
					else {
						int myDistance = paths[myNearestPointIndex].getDistance();
						int yourDistance = paths[yourNearestPointIndex].getDistance();
						bridgeWeight = GGQiu.e.getEdge(bridge).getWeight();
						
						// The shortest path is not the shortest anymore
						if(myDistance > yourDistance + bridgeWeight) {
							paths[myNearestPointIndex].setDistance(yourDistance+bridgeWeight);
							ArrayList<Integer> newPath = new ArrayList<Integer>(paths[yourNearestPointIndex].getPath());
							newPath = paths[yourNearestPointIndex].getPath();
							paths[myNearestPointIndex].setPath(newPath);
						}
					}
				}
			}
			
			/** Updating the settledList and unsettledList **/
			// Updating the settledList
			for(int i = 0; i < tempNearestList.size(); i++)
				this.settledPoints.add(tempNearestList.get(i));
			
			// Updating the unsettleList
			for(int i = 0; i < tempNearestList.size(); i++) {
				int indexRemover = tempNearestList.get(i);
				for(int j = 0; j < this.unsettledPoints.size(); j++) {
					if(unsettledPoints.get(j) == indexRemover) {
						unsettledPoints.remove(j);
						continue;
					}
				}
			}
			
		}// End of while(!UnsettledPoint.isEmpty())
		return this.paths[target].getPath();
	}
}
