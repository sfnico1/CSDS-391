

import java.util.HashMap;
import java.util.PriorityQueue;


public class Astar{
  public ANode start;
  public int max;
  
  // the constructor just sets the variables, makes the starting node, creates the hashtable
  public Astar(String state, int max, int puzzleType){
    this.start = new ANode(state, 0, null, null, puzzleType);
    this.max = max;
    if (puzzleType >= 1) start.createHash(1000000);
    else start.createHash(10000);
    start.checkVisit(state);
  }
  
  
  public ANode findShortPath(){
    // make the children
    start.createChildren();
    // make  the priority queue
    PriorityQueue<ANode> visitList = new PriorityQueue<ANode>();
    ANode lowestN = start;
    Boolean found = false;
    // while not found
    while (found == false){
      for (int i = 0; i < start.children.length; i++){
        // add children to the queue
        if (lowestN.children[i] != null) visitList.add(lowestN.children[i]);
      }
      // make sure we don't exceed max nodes
      if (start.allStates.size() > max) return null;
      // if we found it, exit the while loop
      if (visitList.peek().value == visitList.peek().depth) found = true;
      else {
        // otherwise take it out of the queue and make more children
        lowestN = visitList.poll();
        lowestN.createChildren();
      }
    }
    return visitList.peek();
  }
}




