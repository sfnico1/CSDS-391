

import java.util.HashMap;


public class BallSortNode implements Comparable<BallSortNode>{
  public static HashMap<Integer, String> allStates = new HashMap<Integer, String>(2000000);
  public String state;
  public int depth;
  public String direction;
  public BallSortNode parent;
  public int value;
  public int tubes;
  public BallSortNode[] children; 
  
  
  public BallSortNode(String state, int depth, String direction, BallSortNode parent, int tubes){
    this.state = state;
    this.depth = depth;
    this.direction = direction;
    this.parent = parent;
    this.tubes = tubes;
    this.children = new BallSortNode[tubes*tubes];
    this.value = findH() + depth;
  }
  
  
  public void createChildren(){
    String newState;
    int bf = 0;
    for (int i = 0; i < tubes; i++){
      for (int j = 0; j < tubes; j++){
        if (i != j){
          StringBuilder direction = new StringBuilder(i + " " + j);
          newState = move(direction.toString());
          if (!newState.equals(state)){ 
            if (!checkVisit(newState)) children[i*4 + j] = new BallSortNode
                (newState, depth + 1, direction.toString(), this, tubes);
          }
        }
      }
    }
  }
  
  
  public Boolean checkVisit(String newState){
    Boolean visited = true;
    int hash = newState.hashCode();
    if (!allStates.containsKey(hash)){
      visited = false;
      allStates.put(hash,newState);
    }
    return visited;
  }
  
  
  public int findH(){
    int h = 0;
    for (int i = 0; i < tubes; i++){
      if (state.charAt(i*4) != state.charAt(i*4 + 1) && state.charAt(i*4) != ' ' && state.charAt(i*4 + 1) != ' ') h++;
      if (state.charAt(i*4) != state.charAt(i*4 + 2) && state.charAt(i*4) != ' ' && state.charAt(i*4 + 2) != ' ') h++;
      if (state.charAt(i*4) != state.charAt(i*4 + 3) && state.charAt(i*4) != ' ' && state.charAt(i*4 + 3) != ' ') h++;
      if (state.charAt(i*4 + 1) != state.charAt(i*4 + 2) && state.charAt(i*4 + 1) != ' ' && state.charAt(i*4 + 2) != ' ') h++;
      if (state.charAt(i*4 + 1) != state.charAt(i*4 + 3) && state.charAt(i*4 + 1) != ' ' && state.charAt(i*4 + 3) != ' ') h++;
      if (state.charAt(i*4 + 2) != state.charAt(i*4 + 3) && state.charAt(i*4 + 2) != ' ' && state.charAt(i*4  +3) != ' ') h++;
    }
    return h;
  }
  
  
  
  
  public String move(String direction){
    int tube1 = Integer.valueOf(direction.split(" ")[0]);
    int tube2 = Integer.valueOf(direction.split(" ")[1]);
    StringBuilder newState = new StringBuilder(state);
    int index1 = getTopBall(tube1);
    int index2 = getTopSpace(tube2);
    if (index1 != -1 && index2 != -1){
      if (index2 != 0){
        if (state.charAt(tube2*4 + index2 - 1) == state.charAt(tube1*4 + index1)){
          newState.setCharAt(tube2*4 + index2, state.charAt(tube1*4 + index1));
          newState.setCharAt(tube1*4 + index1, ' ');
        }
      }
      else {
        newState.setCharAt(tube2*4 + index2, state.charAt(tube1*4 + index1));
        newState.setCharAt(tube1*4 + index1, ' ');
      }
    }
    return newState.toString();
  }
  
  
  public int getTopBall(int tube){
    int index = 3;
    if (state.charAt(tube*4 + 3) == ' ') index = 2;
    if (state.charAt(tube*4 + 2) == ' ') index = 1;
    if (state.charAt(tube*4 + 1) == ' ') index = 0;
    if (state.charAt(tube*4 + 0) == ' ') index = -1;
    return index;
  }
  
  
  public int getTopSpace(int tube){
    int index = 0;
    if (state.charAt(tube*4 + 0) != ' ') index = 1;
    if (state.charAt(tube*4 + 1) != ' ') index = 2;
    if (state.charAt(tube*4 + 2) != ' ') index = 3;
    if (state.charAt(tube*4 + 3) != ' ') index = -1;
    return index;
  }
  
  
  public int compareTo(BallSortNode node){
    int compare = ((BallSortNode) node).value;
    return value - compare;
  }
}
