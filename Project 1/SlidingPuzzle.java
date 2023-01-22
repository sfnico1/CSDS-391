

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;


public class SlidingPuzzle{
  public static String state;
  public static String[] answers;
  public static int max = 5000;
  public static File reader;
  public static FileWriter writer;
  public static Random random = new Random();
  // public double nodeCount; // USED FOR TESTING
  
  /* main method, used to parse the given commands from a text file */
  public static void main(String args[]){
    random.setSeed(12);
    StringBuilder input = new StringBuilder();
    try {
      reader = new File("P1-testP.txt");
      writer = new FileWriter("P1-response.txt");
      Scanner scanner = new Scanner(reader);
      // scan to see what the first word is, no error checking since we made the test files
      while (scanner.hasNext()) {
        String data = scanner.next();
        if (data.equals("setState")) {
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
        } else if (data.equals("maxNodes")) {
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
    try { writer.write("\n[ " + state.charAt(0) + " " + state.charAt(1) + " "  + state.charAt(2) 
                         + " ]\n"  + "[ " + state.charAt(4) + " " + state.charAt(5) + " " + 
                       state.charAt(6) + " ]\n" + "[ " + state.charAt(8) + " " + state.charAt(9) 
                         + " "  + state.charAt(10) + " ]\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
  }
  
  
  public static  void move(String direction){
    int index = 0;
    char holder;
    StringBuilder newState = new StringBuilder(state);
    // find the blank tile
    while (state.charAt(index) != 'b'){
      index++;
    }
    // swap the according characters of the string to make the move
    int possible = possibleMove(direction, index);
    if (direction.equals("up") && possible == 1){
      holder = state.charAt(index-4);
      newState.setCharAt(index, holder);
      newState.setCharAt(index-4, 'b');
    } else if (direction.equals("down") && possible == 1){
      holder = state.charAt(index+4);
      newState.setCharAt(index, holder);
      newState.setCharAt(index+4, 'b');
    } else if (direction.equals("left") && possible == 1){
      holder = state.charAt(index-1);
      newState.setCharAt(index, holder);
      newState.setCharAt(index-1, 'b');
    } else if (direction.equals("right") && possible == 1){
      holder = state.charAt(index+1);
      newState.setCharAt(index, holder);
      newState.setCharAt(index+1, 'b');
    }
    state = newState.toString();
  }
  
  
  public static int possibleMove(String direction, int index){
    int possible = 0;
    if (direction.equals("up") && index > 2) possible = 1;
    else if (direction.equals("down") && index < 8) possible = 1;
    else if (direction.equals("left") && index != 0 && index != 4 && index != 8) possible = 1;
    else if (direction.equals("right") && index != 2 && index != 6 && index != 10) possible = 1;
    return possible;
  }
  
  
  public static void randomizeState(int n){
    for (int i = 0; i < n; i++){
      int move = (int) Math.floor(random.nextDouble()*4);
      if (move == 0) move("up");
      else if (move == 1) move("down");
      else if (move == 2) move("left");
      else if (move == 3) move("right");
    }
  }
  
  
  public static String[] solveAstar(){
    // make an instance of Astar
    Astar solve = new Astar(state, max, 0);
    // find the shortest path
    ANode goalNode = solve.findShortPath();
    // = null if it hit the max nodes limit
    if (goalNode == null){
      try { writer.write("\nERROR: surpassed max number of nodes searched\n");
      } catch (IOException e) { System.out.println("An error occurred.\n"); }
      return null;
    }
    // print the answers
    answers = getAnswers(goalNode);
    try { writer.write("\nThe effective branching factor was (approx): " 
                         + Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth) + "\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
    //this.nodeCount = Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth); // USED FOR TESTING
    return answers;
  }
  
  
  public static String[] solveBeam(int k){
    // make an instance of Beam
    Beam solve = new Beam(state, k, max, 0);
    // find the solution
    ANode goalNode = solve.findShortPath();
    // = null if max nodes limit was hit
    if (goalNode == null){
      try { writer.write("ERROR: surpassed max number of nodes searched\n");
      } catch (IOException e) { System.out.println("An error occurred.\n"); }
      return null;
    }
    // get the answers
    answers = getAnswers(goalNode);
    try { writer.write("\nThe effective branching factor was (approx): " 
                         + Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth) + "\n");
    } catch (IOException e) { System.out.println("An error occurred.\n"); }
    // this.nodeCount = Math.pow(solve.start.allStates.size(), 1.0/goalNode.depth); // USED FOR TESTING
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
    // find the answers
    while (goalNode.parent != null){
      answers[index] = goalNode.direction;
      goalNode = goalNode.parent;
      index--;
    }
    // print the answers
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
  // this may not work now, I made this before I wrote to a file
  public double[] test(int depth){
    double nodes = 0;
    int count = 0;
    max = 100000;
    String[] answers;
    state = "b12 345 678";
    while (count < 100){
      randomizeState(depth + 5);
      if (!state.equals("b12 345 678")){
        answers = solveBeam(100);
        if (answers.length == depth){
          nodes = nodes + nodeCount;
          count++;
        }
      }
      state = "b12 345 678";
    }
    double[] x = {nodes/count, count};
    return x;
  }
  
  
  public double test2(int max){
    maxNodes(max);
    double fraction = 0;
    state = "b12 345 678";
    for (int i = 0; i < 1000; i++){
      randomizeState(1000);
      if (solveBeam(10) != null) fraction++;
      state = "b12 345 678";
    }
    return fraction/1000*100;
  }
  */
}





