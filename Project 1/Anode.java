

import java.util.HashMap;


public class ANode implements Comparable<ANode>{
  public static HashMap<Integer, String> allStates;
  public String state;
  public int depth;
  public String direction;
  public ANode parent;
  public int value;
  public ANode[] children;
  public int puzzleType;
  
  
  // the constructor sets all the variables
  public ANode(String state, int depth, String direction, ANode parent, int puzzleType){
    this.state = state;
    this.depth = depth;
    this.direction = direction;
    this.parent = parent;
    this.puzzleType = puzzleType;
    if (puzzleType > 0) {
      this.value = find2H() + depth;
      createChildArray(12);
    }
    else {
      this.value = find1H() + depth;
      createChildArray(4);
    }
  }
  
  
  // creates children based off of the puzzle type
  public void createChildren(){
    String newState;
    String[][] moves =  {{"up","down","left","right"},
      {"F","F'","B","B'","R","R'","L","L'","U","U'","D","D'"}};
    for (int i = 0; i < children.length; i++){
      if (puzzleType == 0) newState = move1(moves[puzzleType][i]);
      else newState = move2(moves[puzzleType][i]);
      // checks if its a new state and checks if it is already in the hashtable
      if (!newState.equals(state)) 
        if (!checkVisit(newState)) children[i] = new ANode(newState, depth + 1, 
                                                           moves[puzzleType][i], this, puzzleType);
    }
  }
  
  // hash a state and see if its in the hashtable
  public Boolean checkVisit(String newState){
    Boolean visited = true;
    int hash = newState.hashCode();
    if (!allStates.containsKey(hash)){
      visited = false;
      allStates.put(hash,newState);
    }
    return visited;
  }
  
  
  // I explain this in the written assigment
  public int find1H(){
    int h1 = -1;
    int h2 = 0;
    int extra = 0;
    int[][] location = {{1,2,0,1,2,0,1,2},{0,0,1,1,1,2,2,2}};
    for (int i = 0; i < 3; i++){
      if (i == 1 || i == 2) extra++;
      for (int j = 0; j < 3; j++){
        int index = 3*i + j + extra;
        if (state.charAt(index) != (char) (index - extra + '0')) h1++;
        if ((state.charAt(index) != 'b')) h2 = h2 + 
           Math.abs(location[0][Integer.valueOf(state.charAt(index) - '1')] - j) + 
           Math.abs(location[1][Integer.valueOf(state.charAt(index) - '1')] - i); 
      }
    }
    return h2;
  }
  
  
  // same move method as SlidingPuzzle but this returns the state
  public String move1(String direction){
    int index = 0;
    char holder;
    StringBuilder newState = new StringBuilder(state);
    while (state.charAt(index) != 'b'){
      index++;
    }
    if (direction.equals("up") && possibleMove(direction, index) == 1){
      holder = state.charAt(index-4);
      newState.setCharAt(index, holder);
      newState.setCharAt(index-4, 'b');
    } else if (direction.equals("down") && possibleMove(direction, index) == 1){
      holder = state.charAt(index+4);
      newState.setCharAt(index, holder);
      newState.setCharAt(index+4, 'b');
    } else if (direction.equals("left") && possibleMove(direction, index) == 1){
      holder = state.charAt(index-1);
      newState.setCharAt(index, holder);
      newState.setCharAt(index-1, 'b');
    } else if (direction.equals("right") && possibleMove(direction, index) == 1){
      holder = state.charAt(index+1);
      newState.setCharAt(index, holder);
      newState.setCharAt(index+1, 'b');
    } 
    return newState.toString();
  }
  
  
  public int possibleMove(String direction, int index){
    int possible = 0;
    if (direction.equals("up") && index > 2) possible = 1;
    else if (direction.equals("down") && index < 8) possible = 1;
    else if (direction.equals("left") && index != 0 && index != 4 && index != 8) possible = 1;
    else if (direction.equals("right") && index != 2 && index != 6 && index != 10) possible = 1;
    return possible;
  }
  
  
  // compare each node based off of the heurisitcs
  public int compareTo(ANode node){
    int compare = ((ANode) node).value;
    return value - compare;
  }
  
  
  public static void createHash(int hashSize){
    allStates = new HashMap<Integer, String>(hashSize);
  }
  
  
  public void createChildArray(int childSize){
    children = new ANode[childSize];
  }
  
  
  
  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  //                   EXTRA CREDIT 
  ////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////
  
  
  
  // find the heuristics of the cube
  // a better way to solve the cube would be to find better heurisitcs, I tried a lot of different 
  // things and this one seemed to be the best, i'm sure there are better though
  public int find2H(){
    int h1 = 12;
    int h2 = 12;
    String pairs = "WYBGRO";
    int[] index1 = {0,5,10};
    int[] index2 = {25,15,20};
    for (int i = 0; i < 3; i++){ 
      for (int j = 0; j < 6; j+=2){
        for (int k = 0; k < 4; k++){
            if ((state.charAt(index1[i] + k) == pairs.charAt(j)) &&
                (state.charAt(index2[i] + k) == pairs.charAt(j+1))) h1--;
            if ((state.charAt(index1[i] + k) == pairs.charAt(j+1)) &&
                (state.charAt(index2[i] + k) == pairs.charAt(j))) h2--;
        }
      }
    }
    if (h2 < h1) h1 = h2;
    return h1;
  }
  
  
  // same move method as Rubiks2 but this returns the state
  public String move2(String direction){
    int[] pos;
    StringBuilder newState = new StringBuilder(state);
    if (direction.equals("F") || direction.equals("F'")){
      pos = new int[]{17,15,0,1,8,6,27,28,12,10,11,13};
      if (direction.equals("F'")) pos = new int[]{6,8,28,27,15,17,1,0,11,13,12,10};
      newState.setCharAt(0,state.charAt(pos[0]));
      newState.setCharAt(1,state.charAt(pos[1]));
      newState.setCharAt(6,state.charAt(pos[2]));
      newState.setCharAt(8,state.charAt(pos[3]));
      newState.setCharAt(27,state.charAt(pos[4]));
      newState.setCharAt(28,state.charAt(pos[5]));
      newState.setCharAt(15,state.charAt(pos[6]));
      newState.setCharAt(17,state.charAt(pos[7]));
      newState.setCharAt(10,state.charAt(pos[8]));
      newState.setCharAt(11,state.charAt(pos[9]));
      newState.setCharAt(13,state.charAt(pos[10]));
      newState.setCharAt(12,state.charAt(pos[11]));
    } else if (direction.equals("B") || direction.equals("B'")){
      pos = new int[]{5,7,26,25,16,18,3,2,22,20,21,23};
      if (direction.equals("B'")) pos = new int[]{18,16,2,3,7,5,25,26,21,23,22,20};
      newState.setCharAt(2,state.charAt(pos[0]));
      newState.setCharAt(3,state.charAt(pos[1]));
      newState.setCharAt(5,state.charAt(pos[2]));
      newState.setCharAt(7,state.charAt(pos[3]));
      newState.setCharAt(25,state.charAt(pos[4]));
      newState.setCharAt(26,state.charAt(pos[5]));
      newState.setCharAt(16,state.charAt(pos[6]));
      newState.setCharAt(18,state.charAt(pos[7]));
      newState.setCharAt(20,state.charAt(pos[8]));
      newState.setCharAt(21,state.charAt(pos[9]));
      newState.setCharAt(23,state.charAt(pos[10]));
      newState.setCharAt(22,state.charAt(pos[11]));
    } else if (direction.equals("R") || direction.equals("R'")){
      pos = new int[]{1,3,11,13,28,26,22,20,17,15,16,18};
      if (direction.equals("R'")) pos = new int[]{26,28,22,20,3,1,11,13,16,18,17,15};
      newState.setCharAt(11,state.charAt(pos[0]));
      newState.setCharAt(13,state.charAt(pos[1]));
      newState.setCharAt(26,state.charAt(pos[2]));
      newState.setCharAt(28,state.charAt(pos[3]));
      newState.setCharAt(20,state.charAt(pos[4]));
      newState.setCharAt(22,state.charAt(pos[5]));
      newState.setCharAt(1,state.charAt(pos[6]));
      newState.setCharAt(3,state.charAt(pos[7]));
      newState.setCharAt(15,state.charAt(pos[8]));
      newState.setCharAt(16,state.charAt(pos[9]));
      newState.setCharAt(18,state.charAt(pos[10]));
      newState.setCharAt(17,state.charAt(pos[11]));
    } else if (direction.equals("L") || direction.equals("L'")){
      pos = new int[]{25,27,23,21,2,0,10,12,7,5,6,8};
      if (direction.equals("L'")) pos = new int[]{0,2,10,12,27,25,23,21,6,8,7,5};
      newState.setCharAt(10,state.charAt(pos[0]));
      newState.setCharAt(12,state.charAt(pos[1]));
      newState.setCharAt(25,state.charAt(pos[2]));
      newState.setCharAt(27,state.charAt(pos[3]));
      newState.setCharAt(21,state.charAt(pos[4]));
      newState.setCharAt(23,state.charAt(pos[5]));
      newState.setCharAt(0,state.charAt(pos[6]));
      newState.setCharAt(2,state.charAt(pos[7]));
      newState.setCharAt(5,state.charAt(pos[8]));
      newState.setCharAt(6,state.charAt(pos[9]));
      newState.setCharAt(8,state.charAt(pos[10]));
      newState.setCharAt(7,state.charAt(pos[11]));
    } else if (direction.equals("U") || direction.equals("U'")){
      pos = new int[]{15,16,10,11,5,6,20,21,27,25,26,28};
      if (direction.equals("U'")) pos = new int[]{5,6,20,21,15,16,10,11,26,28,27,25};
      newState.setCharAt(10,state.charAt(pos[0]));
      newState.setCharAt(11,state.charAt(pos[1]));
      newState.setCharAt(5,state.charAt(pos[2]));
      newState.setCharAt(6,state.charAt(pos[3]));
      newState.setCharAt(20,state.charAt(pos[4]));
      newState.setCharAt(21,state.charAt(pos[5]));
      newState.setCharAt(15,state.charAt(pos[6]));
      newState.setCharAt(16,state.charAt(pos[7]));
      newState.setCharAt(25,state.charAt(pos[8]));
      newState.setCharAt(26,state.charAt(pos[9]));
      newState.setCharAt(28,state.charAt(pos[10]));
      newState.setCharAt(27,state.charAt(pos[11]));
    }  else if (direction.equals("D") || direction.equals("D'")){
      pos = new int[]{7,8,22,23,17,18,12,13,2,0,1,3};
      if (direction.equals("D'")) pos = new int[]{17,18,12,13,7,8,22,23,1,3,2,0};
      newState.setCharAt(12,state.charAt(pos[0]));
      newState.setCharAt(13,state.charAt(pos[1]));
      newState.setCharAt(7,state.charAt(pos[2]));
      newState.setCharAt(8,state.charAt(pos[3]));
      newState.setCharAt(22,state.charAt(pos[4]));
      newState.setCharAt(23,state.charAt(pos[5]));
      newState.setCharAt(17,state.charAt(pos[6]));
      newState.setCharAt(18,state.charAt(pos[7]));
      newState.setCharAt(0,state.charAt(pos[8]));
      newState.setCharAt(1,state.charAt(pos[9]));
      newState.setCharAt(3,state.charAt(pos[10]));
      newState.setCharAt(2,state.charAt(pos[11]));
    }
    return newState.toString();
  }
}




