package ps2;
import java.awt.*;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Using a quadtree for collision detection
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, updated for blobs
 * @author CBK, Fall 2016, using generic PointQuadtree
 */
public class CollisionGUI extends DrawingGUI {
	private static final int width=800, height=600;		// size of the universe

	private List<Blob> blobs;						// all the blobs
	private List<Blob> colliders;					// the blobs who collided at this step
	private char blobType = 'b';						// what type of blob to create
	private char collisionHandler = 'c';				// when there's a collision, 'c'olor them, or 'd'estroy them
	private int delay = 100;					// timer control
	private PointQuadtree<Blob> cTree;
	public CollisionGUI() {
		super("super-collider", width, height);

		blobs = new ArrayList<Blob>();
		

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * Adds an blob of the current blobType at the location
	 */
	private void add(int x, int y) {
		if (blobType=='b') {
			blobs.add(new Bouncer(x,y,width,height));
		}
		else if (blobType=='w') {
			blobs.add(new Wanderer(x,y));
		}
		else {
			System.err.println("Unknown blob type "+blobType);
		}
	}

	/**
	 * DrawingGUI method, here creating a new blob
	 */
	public void handleMousePress(int x, int y) {
		add(x,y);
		repaint();
	}

	/**
	 * DrawingGUI method
	 */
	public void handleKeyPress(char k) {
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 'r') { // add some new blobs at random positions
			for (int i=0; i<10; i++) {
				add((int)(width*Math.random()), (int)(height*Math.random()));
				repaint();
			}			
		}
		else if (k == 'c' || k == 'd') { // control how collisions are handled
			collisionHandler = k;
			System.out.println("collision:"+k);
		}
		else { // set the type for new blobs
			blobType = k;			
		}
	}

	/**
	 * DrawingGUI method, here drawing all the blobs and then re-drawing the colliders in red
	 */
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		// Ask all the blobs to draw themselves.
		// Ask the colliders to draw themselves in red.
		
		
		for (Blob blob: blobs){
			blob.draw(g);
		}
		if (colliders!=null){
		for (Blob collider: colliders){
			g.setColor(Color.RED);
			collider.draw(g);
		}
		//g.setColor(Color.BLACK);
		}
		
	}

	/**
	 * Sets colliders to include all blobs in contact with another blob
	 */
	private void findColliders() {
		colliders= new ArrayList<Blob>();
		// TODO: YOUR CODE HERE
		// Create the tree
/*		cTree= new PointQuadtree<Blob>(blobs.get(0), (int)(blobs.get(0).getX()-blobs.get(0).getR()) 
				,(int)(blobs.get(0).getY()-blobs.get(0).getR()),(int)(blobs.get(0).getX()+blobs.get(0).getR()),
				(int)(blobs.get(0).getY()-blobs.get(0).getR()));*/
		//if(blobs!=null){
		cTree= new PointQuadtree<Blob>(blobs.get(0), 0, 0, width, width);
		//}
		// For each blob, see if anybody else collided with it
		//int i=0
		
		for(int i=1;i<blobs.size(); i++){
			cTree.insert(blobs.get(i));
			
		}
		for (Blob blob: blobs){
			if(cTree.findInCircle(blob.getX(),blob.getY(), 2*(blob.getR())).size()>1){
				//System.out.println("collided");
				colliders.add(blob);
			if(!(cTree.findInCircle(blob.getX(),blob.getY(), 2*(blob.getR())).size()>1)){
				//to test if any of the blobs are being colliders when they shouldnt
				System.out.println("improper collision");
			}
			}
	
		}
	
		//collide test on paths that will and wont collide so we can predict what happes. do a random clicking test, but make a case that shows it more concretely
	}

	/**
	 * DrawingGUI method, here moving all the blobs and checking for collisions
	 */
	public void handleTimer() {
		// Ask all the blobs to move themselves.
		//System.out.println(colliders.size());
		for (Blob blob : blobs) {
			blob.step();
			
		}
		// Check for collisions
		if (blobs.size() > 0) {
			
			findColliders();
			if (collisionHandler=='d') {
				blobs.removeAll(colliders);
				colliders = null;
			}
		}
		// Now update the drawing
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CollisionGUI();
			}
		});
	}
}
