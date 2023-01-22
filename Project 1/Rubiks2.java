

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;


public class Rubiks2{
  public static String state = "WWWW BBBB RRRR GGGG OOOO YYYY";
  public static String[] answers;
  public static int max = 250000;
  public static File reader;
  public static FileWriter writer;
  public static Random random = new Random();
  //public double nodeCount; // USED FOR TESTING
  
  
  /* main method find the commands from the file */
  // pretty much the same setup as SlidingPuzzle
  public static void main(String args[]){
    StringBuilder input = new StringBuilder();
    random.setSeed(123456); // set seed
    try {
      reader = new File("P1-testR.txt");
      writer = new FileWriter("P1-response.txt");
      Scanner scanner = new Scanner(reader);
      while (scanner.hasNext()) {
        String data = scanner.next();
        if (data.equals("setState")) {
          data = scanner.next();
          input.append(data + " ");
          data = scanner.next();
          input.append(data + " ");
          data = scanner.next();
          input.append(data + " ");
          data = scanner.next();
          input.append(data + " ");
          data = scanner.next();
          input.append(data + " ");
          data = scanner.next();
          input.append(data);
          setState(input.toString());
        } else if (data.equals("move")) {
          data = scanner.next();
          input.append(data);
          move(input.toString());
        } else if (data.equals("printState")) {
          printState();
        } else if (data.equals("randomizeState")) {
          data = scanner.next();
          input.append(data);
          randomizeState(Integer.valueOf(input.toString()));
        } else if (data.equals("solveAstar")) {
          solveAstar();
        } else if (data.equals("solveBeam")) {
          data = scanner.next();
          input.append(data);
          solveBeam(Integer.valueOf(input.toString()));
        } else if (data.equals("maxNodes")){
          data = scanner.next();
          input.append(data);
          maxNodes(Integer.valueOf(input.toString()));
        } else if (data.equals("visuallySolve")) {
          visuallySolve();
        } else writer.write(scanner.nextLine());
        input.delete(0, input.capacity());
      } 
      scanner.close();
      writer.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
    }
  }
  
  
  public static void setState(String start){
    state = start;
  }
  
  
  public static void printState(){
    try { writer.write("\n       " + state.charAt(25) + " " + state.charAt(26) + "\n");
      writer.write("       " + state.charAt(27) + " " + state.charAt(28) + "\n");
      writer.write(" " + state.charAt(5) + " " + state.charAt(6) + "  " + 
                       " " + state.charAt(10) + " " + state.charAt(11) + "  " + 
                       " " + state.charAt(15) + " " + state.charAt(16) + "  " + 
                       " " + state.charAt(20) + " " + state.charAt(21) + "  \n");
      writer.write(" " + state.charAt(7) + " " + state.charAt(8) + "  " + 
                       " " + state.charAt(12) + " " + state.charAt(13) + "  " + 
                       " " + state.charAt(17) + " " + state.charAt(18) + "  " + 
                       " " + state.charAt(22) + " " + state.charAt(23) + "  \n");
      writer.write("       " + state.charAt(0) + " " + state.charAt(1) + "\n");
      writer.write("       " + state.charAt(2) + " " + state.charAt(3) + "\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
  }
  
  
  public static void move(String direction){
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
    state = newState.toString();
  }
  
  
  public static void randomizeState(int n){
    for (int i = 0; i < n; i++){
      int move = (int) Math.floor(random.nextDouble()*12);
      if (move == 0) move("F");
      else if (move == 1) move("F'");
      else if (move == 2) move("B");
      else if (move == 3) move("B'");
      else if (move == 4) move("R");
      else if (move == 5) move("R'");
      else if (move == 6) move("L");
      else if (move == 7) move("L'");
      else if (move == 8) move("U");
      else if (move == 9) move("U'");
      else if (move == 10) move("D");
      else if (move == 11) move("D'");
    }
  }
  
  
  public static String[] solveAstar(){
    // same astar method as SlidingPuzzle
    Astar solve = new Astar(state, max, 1);
    ANode goalNode = solve.findShortPath();
    if (goalNode == null){
      try { writer.write("\nERROR: surpassed max number of nodes searched\n");
      } catch (IOException e) { System.out.println("An error occurred.\n"); }
      return null;
    }
    answers = getAnswers(goalNode);
    try { writer.write("\nThe effective branching factor was (approx): " 
                         + Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth) + "\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
    //this.nodeCount = Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth); // USED FOR TESTING
    return answers;
  }
  
  
  public static String[] solveBeam(int k){
    // same beam method as SlidingPuzzle
    Beam solve = new Beam(state, k, max, 1);
    ANode goalNode = solve.findShortPath();
    if (goalNode == null){
      try { writer.write("ERROR: surpassed max number of nodes searched\n");
      } catch (IOException e) { System.out.println("An error occurred.\n"); }
      return null;
    }
    answers = getAnswers(goalNode);
    try { writer.write("\nThe effective branching factor was (approx): " 
                         + Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth) + "\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
    //this.nodeCount = Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth); // USED FOR TESTING
    return answers;
  }
  
  
  public static String[] getAnswers(ANode goalNode){
    // print information
    try { writer.write("\nVisited " + goalNode.allStates.size() + " nodes.\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
    try { writer.write("It will take " + goalNode.depth + " moves to complete the puzzle.\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
    answers = new String[goalNode.depth];
    int index = goalNode.depth-1;
    // get the directions from the solution
    while (goalNode.parent != null){
      answers[index] = goalNode.direction;
      goalNode = goalNode.parent;
      index--;
    }
    // print the directions to the file
    for (int i = 0; i < answers.length; i++){
      try { writer.write(answers[i] + ", ");
      } catch (IOException e) { System.out.println("An error occurred.\n"); }
    }
    return answers;
  }
  
  
  public static void maxNodes(int newMax){
    max = newMax;
  }
  
  
  public static void visuallySolve(){
    printState();
    for (int i = 0; i < answers.length; i++){
      move(answers[i]);
      try { writer.write(answers[i] + "\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
      printState();
    }
  }
  
  
  /*
  // used for experiments, change heurisitcs in ANode
  // this can take a very long time for large depth values
  // this may not work anymore, haven't used it since the experiemnts portion of the written assigment
  public double[] test(int depth){
    double nodes = 0;
    int count = 0;
    max = 10000000;
    String[] answers;
    state = "WWWW BBBB RRRR GGGG OOOO YYYY";
    while (count < 100){
      randomizeState(depth);
      if (!state.equals("WWWW BBBB RRRR GGGG OOOO YYYY")){
        answers = solveBeam(100);
        if (answers.length == depth){
          nodes = nodes + nodeCount;
          count++;
          if (depth > 8) System.out.println(count);
        }
      }
      state = "WWWW BBBB RRRR GGGG OOOO YYYY";
    }
    double[] x = {nodes/count, count};
    return x;
  }
  */
}
  



