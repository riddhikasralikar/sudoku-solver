import java.io.*;   // allows us to read from a file
import java.util.*;

public class sudoku {    
    // The current contents of the cells of the puzzle. 
    private final int[][] grid;
    
    /*
     * Indicates whether the value in a given cell is fixed 
     * (i.e., part of the initial configuration).
     * valIsFixed[r][c] is true if the value in the cell 
     * at row r, column c is fixed, and false otherwise.
     */
    private final boolean[][] valIsFixed;
    
    /*
     * This 3-D array allows us to determine if a given subgrid (i.e.,
     * a given 3x3 region of the puzzle) already contains a given
     * value.
     */
    private final boolean[][][] subgridHasVal;
    
    /*** Additional Fields ***/
    private final boolean[][] rowHasVal;
    private final boolean[][] colHasVal;
    
    /* 
     * Constructs a new Sudoku object, which initially
     * has all empty cells.
     */
    public sudoku() {
        this.grid = new int[9][9];
        this.valIsFixed = new boolean[9][9];     
        this.subgridHasVal = new boolean[3][3][10];        
        this.rowHasVal = new boolean[9][10];
        this.colHasVal = new boolean[9][10];
        
        /*** Initialize Additional Fields Here ***/
    }
    
    /*
     * Place the specified value in the cell with the specified
     * coordinates, and update the state of the puzzle accordingly.
     */
    public void placeVal(int val, int row, int col) {
        this.grid[row][col] = val;
        this.subgridHasVal[row / 3][col / 3][val] = true;
        this.rowHasVal[row][val] = true;
        this.colHasVal[col][val] = true;
    }
        
    /*
     * Remove the specified value from the cell with the specified
     * coordinates, and update the state of the puzzle accordingly.
     */
    public void removeVal(int val, int row, int col) {
        this.grid[row][col] = 0;
        this.subgridHasVal[row / 3][col / 3][val] = false;
        this.rowHasVal[row][val] = false;
        this.colHasVal[col][val] = false;
    }  
        
    /*
     * Read in the initial configuration of the puzzle from the specified 
     * Scanner, and use that config to initialize the state of the puzzle.  
     * The configuration should consist of one line for each row, with the
     * values in the row specified as integers separated by spaces.
     * A value of 0 should be used to indicate an empty cell.
     */
    public void readConfig(Scanner input) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
        }
    }
                
    /*
     * Displays the current state of the puzzle.
     */        
    public void printGrid() {
        for (int r = 0; r < 9; r++) {
            printRowSeparator();
            for (int c = 0; c < 9; c++) {
                System.out.print("|");
                if (this.grid[r][c] == 0) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + this.grid[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        printRowSeparator();
    }
        
    // A private helper method used by display() to print a line separating two rows of the puzzle.
    private static void printRowSeparator() {
        System.out.print("-------------------------------------\n");
    }
    
    /*
     * This is the key recursive-backtracking method.  Returns true if
     * a solution has already been found, and false otherwise.
     */
    private boolean solveRB(int n) {
        if (n == 81) return true;

        int row = n / 9;
        int col = n % 9;

        if (this.valIsFixed[row][col]) {
            return solveRB(n + 1);
        }

        for (int val = 1; val <= 9; val++) {
            if (!rowHasVal[row][val] && !colHasVal[col][val] && !subgridHasVal[row / 3][col / 3][val]) {
                placeVal(val, row, col);
                if (solveRB(n + 1)) return true;
                removeVal(val, row, col);
            }
        }
        return false;
    } 
    
    /*
     * Public "wrapper" method for solveRB().
     * Makes the initial call to solveRB, and returns whatever it returns.
     */
    public boolean solve() { 
        return this.solveRB(0);
    }
    
    public static void main(String[] args) {
        try (Scanner scan = new Scanner(System.in)) {
            sudoku puzzle = new sudoku();
            
            System.out.print("Enter the name of the puzzle file: ");
            String filename = scan.nextLine();
            
            try {
                Scanner input = new Scanner(new File(filename));
                puzzle.readConfig(input);
            } catch (IOException e) {
                System.out.println("Error accessing file " + filename);
                System.out.println(e);
                System.exit(1);
            }
            
            System.out.println("\nHere is the initial puzzle:");
            puzzle.printGrid();
            System.out.println();
            
            if (puzzle.solve()) {
                System.out.println("Here is the solution:");
            } else {
                System.out.println("No solution could be found.");
                System.out.println("Here is the current state of the puzzle:");
            }
            puzzle.printGrid();
        }  
    }    
}