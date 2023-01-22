

import java.util.HashMap;
import java.util.PriorityQueue;


public class AstarBall{
  public BallSortNode start;
  public int tubes;
  
  
  public AstarBall(String state, int tubes){
    this.start = new BallSortNode(state, 0, null, null, tubes);
    this.tubes = tubes;
    start.checkVisit(state);
  }
  
  
  public BallSortNode findShortPath(){
    start.createChildren();
    PriorityQueue<BallSortNode> visitList = new PriorityQueue<BallSortNode>();
    visitList.add(start);
    BallSortNode lowestN = start;
    Boolean found = false;
    while (found == false){
      for (int i = 0; i < tubes*tubes; i++){
        if (lowestN.children[i] != null) visitList.add(lowestN.children[i]);
      }
      if (visitList.peek().value == visitList.peek().depth) found = true;
      else {
        lowestN = visitList.poll();
        lowestN.createChildren();
      }
    }
    System.out.println("Visited " + start.allStates.size() + " nodes.");
    return visitList.peek();
  }
}




