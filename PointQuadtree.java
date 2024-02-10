package ps2;
import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 * 
 */
public class PointQuadtree<E extends Point2D>  {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4; // children
	private List<E> allPointsList;
	private List<E> circleList;
	private PointQuadtree<E> nTree;
	
	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE
	
		if (p2.getX() >= (int) point.getX() && p2.getX() < x2 && p2.getY() > y1 && p2.getY() < (int) point.getY()){
			if (c1!=null){
				c1.insert(p2);
				
			}
			else{
				c1 = new PointQuadtree<E>(p2, (int) point.getX(), y1, x2, (int)point.getY());
			
			}
		}
		if (p2.getX() < (int) point.getX() && p2.getX() > x1 && p2.getY() > y1 && p2.getY() <(int) point.getY()){
			if (c2!=null){
				c2.insert(p2);
				
			}
			else{
				c2 = new PointQuadtree<E>(p2, x1, y1, (int)point.getX(), (int)point.getY());
				
			}
		}
		if (p2.getX() > x1 && p2.getX() < point.getX() && p2.getY() < y2  && p2.getY() > (int) point.getY()){
			if (c3!=null){
				c3.insert(p2);
				
			}
			else{
				c3 = new PointQuadtree<E>(p2, x1, (int)point.getY(), (int)point.getX(), y2 );
		}
		}
		if (p2.getX() > (int) point.getX() && p2.getX() < x2 && p2.getY() < y2 && p2.getY()> (int) point.getY()){
			if (c4!=null){
				c4.insert(p2);
				
			}
			else{
				c4 = new PointQuadtree<E>(p2,(int)point.getX(), (int)point.getY(), x2, y2);
				
			}
		}
	}
	
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// TODO: YOUR CODE HERE
		//get point
		if (c1!=null){
			c1.size();
			
		}
		if (c2!=null){
			c2.size();
			
		}
		if (c3 !=null){
			c3.size();
		}
		if (c4 !=null){
			c4.size();
		}
		return 1 + c1.size() + c2.size() + c3.size() +c4.size();
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE
		allPointsList= new ArrayList<E>();
		allPointsh(allPointsList);
		return allPoints();

	}	

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE
		//a= new ist
		//helper (cx,cy,cr)
		//return a
		//helper is called find parts
		/*in find parts(cx,cy,cr, lista)
		a.add(curr)
		if cl is there
		find parts call on c1
		etc */
		circleList= new ArrayList<E>();
		//findPointsh(cx,cy,cr);
		findPointsh(cx,cy,cr,circleList);
		return circleList;
		
	
	
		}
	
	public void allPointsh(List<E> allPointsList){
			allPointsList.add(point);
		if (c1!=null){
			c1.allPointsh(allPointsList);
			}
		if (c1!=null){
			c2.allPointsh(allPointsList);
			}
		if (c1!=null){
			c3.allPointsh(allPointsList);
			}
		if (c1!=null){
			c4.allPointsh(allPointsList);
			
	}
}
	public void findPointsh(double cx, double cy, double cr, List<E> circleList){
	if (Geometry.circleIntersectsRectangle(cx , cy, cr, x1, y1, x2, y2)){
		if (Geometry.pointInCircle(point.getX(), point.getY(), cx, cy, cr)){
			circleList.add(point);
		}
		
		if (c1!= null){
			c1.findPointsh(cx, cy,  cr, circleList);
		}
		if (c2!= null){
			c2.findPointsh(cx, cy,  cr, circleList);
		}
		if (c3!= null){
			c3.findPointsh(cx, cy,  cr, circleList);
		}
		if (c4!= null){
			c4.findPointsh(cx, cy,  cr, circleList);
			
		}
		
		
	}
}

	// TODO: YOUR CODE HERE for any helper methods

}