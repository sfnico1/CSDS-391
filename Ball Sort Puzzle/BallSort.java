


import java.util.HashMap;


public class BallSort{
  public String state;
  public int tubes;
  public String[] answers;
  
  
  public void setState(String start, int tubes){
    this.state = start;
    this.tubes = tubes;
  }
  
  
  public void printState(){
    for (int i = 3; i >= 0; i--){
      for (int j = 0; j < tubes; j++){
        System.out.print(state.charAt(i + 4*j) + "  ");
      }
      System.out.println("");
    }
  }
  
  
  public void move(String direction){
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
    state = newState.toString();
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
  
  
  public String[] solveAstarBall(){
    AstarBall solve = new AstarBall(state, tubes);
    BallSortNode goalNode = solve.findShortPath();
    answers = getAnswers(goalNode);
    System.out.println("The effective branching factor was (approx): " 
                         + Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth));
    return answers;
  }
  
  
  public String[] getAnswers(BallSortNode goalNode){
    String[] answers = new String[goalNode.depth];
    int index = goalNode.depth-1;
    System.out.println("It will take " + goalNode.depth + " moves to complete the puzzle.");
    while (goalNode.parent != null){
      answers[index] = goalNode.direction;
      goalNode = goalNode.parent;
      index--;
    }
    this.answers = answers;
    return answers;
  }
  
  
  public void visuallySolve(){
    printState();
    for (int i = 0; i < answers.length; i++){
      move(answers[i]);
      System.out.println("\n[ " + answers[i] + " ]");
      printState();
    }
  }
}


/* brown = 1
 * grey = 2
 * purple = P
 * pink = p
 * dark blue = B
 * light blue = b
 * dark green = G
 * light green = g
 * green = 3
 * yellow = Y
 * orange = O
 * red = R
 * 
 * given state = ("1O21Pb3pRO32p1RBGBYOBOgYbggB3GPpYGg3b12Pb2PGpRRY        ",14)
 * give colors from the bottom of the pile and go up
 */



