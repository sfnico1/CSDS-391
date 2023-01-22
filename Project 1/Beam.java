

import java.util.Arrays;


public class Beam{
  public ANode start;
  public int k;
  public int max;
  
  
  // the constructor just sets the variables, makes the starting node, creates the hashtable
  public Beam(String state, int k, int max, int puzzleType){
    this.start = new ANode(state, 0, null, null, puzzleType);
    this.max = max;
    if (puzzleType >= 1) start.createHash(10000000);
    else start.createHash(10000);
    start.checkVisit(state);
    this.k = k;
  }
  
  
  public ANode findShortPath(){
    // make the children
    start.createChildren();
    // make the empty array
    ANode[] visitList = new ANode[k];
    visitList[0] = start;
    // make a temp array
    ANode[] holder = {};
    int index = 0;
    ANode lowestN = start;
    Boolean found = false;
    // while not found
    while (found == false){
      for (int i = 0; i < k; i++){
        if (visitList[i] != null){
          lowestN = visitList[i];
          lowestN.createChildren();
          // create children from the array and add them to a temp arrar
          for (int j = 0; j < start.children.length; j++){
            if (lowestN.children[j] != null) {
              // this isn't the most efficient, should have used a priority queue but its the same idea
              holder = Arrays.copyOf(holder,index+1);
              holder[index] = lowestN.children[j];
              index++;
              // make sure we don't exceed the max nodes
              if (start.allStates.size() > max) return null;
            }
          }
        }
      }
      // sort the temp array
      Arrays.sort(holder);
      // keep the first k (or index) amount of nodes
      if (index > k) visitList = Arrays.copyOfRange(holder,0,k);
      else for (int i = 0; i < index; i++) visitList[i] = holder[i]; 
      // reset the temp array
      holder = new ANode[1];
      index = 0;
      // check if we found the solution
      if (visitList[0].value == visitList[0].depth) found = true;
    }
    return visitList[0];
  }
}



