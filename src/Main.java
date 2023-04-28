import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;
    public static final  char SHIP = '#';
    public static final char FREE_SPACE = '-';
    public static final char HIT = 'V';
    public static final char MISS = 'X';
    public static final int HORIZON = 0;
    public static final int VERTICAL = 1;

    public static void battleshipGame() {
        // TODO: Add your code here (and add more methods).
        int rowNum, colNum;
        System.out.println("Enter the board size");
        String boardSize = scanner.nextLine();
        rowNum = boardSize.charAt(0);
        colNum = boardSize.charAt(2);
        int maxBoardSize = findMax((int)boardSize.charAt(0),(int)boardSize.charAt(2));
        char[][] userBoard = new char[rowNum][colNum];
        char[][] compBoard = new char[rowNum][colNum];
        char[][] userGuessBoard = new char[rowNum][colNum];
        char[][] compGuessBoard = new char[rowNum][colNum];
        for(int i = 0; i < rowNum; i++)
            for(int j = 0; j < colNum; j++){
                userBoard[i][j] = FREE_SPACE;
                compBoard[i][j] = FREE_SPACE;
                userGuessBoard[i][j] = FREE_SPACE;
                compGuessBoard[i][j] = FREE_SPACE;
            }
        int [] battleShips = new int[maxBoardSize];
        //need to check this
        System.out.println("Enter the battleships sizes");
        for(int i = 1;i <= maxBoardSize ;i++){
            battleShips[i] = scanner.nextInt();
        }
        userPlacement(userBoard, rowNum, colNum, battleShips);
        computerPlacement(compBoard, rowNum, colNum, battleShips);

        //needs to be a function
        System.out.println("Your current guessing board: ");
        printBoard(userGuessBoard, rowNum, colNum);
        System.out.println("Enter a tile to attack");
        String userChoice = scanner.nextLine();

        while(!moveCheck(userBoard,rowNum,colNum,userChoice.charAt(0),userChoice.charAt(2))) {
            System.out.println("Your current guessing board: ");
            printBoard(userBoard, rowNum, colNum);
        }
    }
    public static boolean moveCheck(char [][]board, int m,int n,int x,int y) {
        if ((x > m) || (y > n) || (x < 0) || (y < 0)){
            System.out.println("Illegal tile, try again!");
            return false;
        }
        else
        if(board[x][y]==MISS){
            System.out.println("Tile already attacked, try again!");
            return false;
        }
        else return true;
    }

    public static int findMax(int num1, int num2){
        if(num1 >= num2)
            return num1;
        else return num2;
    }


    public static void printBoard(char[][] board, int rowNum, int colNum){
        for(int i = 0; i <= colNum; i++){
            if(i == 0)
                System.out.print("  ");
            else
                System.out.print((i - 1) + " ");
        }
        System.out.println();
        for(int i = 0; i < rowNum; i++){
            for(int j = 0; j <= colNum; j++){
                if(j == 0)
                    System.out.print(i + " ");
                else
                    System.out.print(board[i][j - 1] + " ");
            }
            System.out.println();
        }
    }
    public static int[] cloneArray(int[] arr){
        int[] cloneArr = new int[arr.length];
        for(int i = 0; i < arr.length; i++){
            cloneArr[i] = arr[i];
        }
        return cloneArr;
    }

    public static boolean boarderCheck(int rowNum, int colNum, int x, int y, int orientation, int size){
        if(orientation == HORIZON && (y + size - 1) >= colNum)
            return false;
        else if(orientation == VERTICAL && (x + size - 1) >= rowNum)
            return false;
        return true;
    }

    public static boolean overLapping(char[][] board, int x, int y, int orientation, int size){
        if(orientation == HORIZON){
            for(int i = y; i < y + size; i++){
                if(board[x][i] == SHIP)
                    return false;
            }
        }
        else{
            for(int i = x; i < x + size; i++){
                if(board[i][y] == SHIP)
                    return false;
            }
        }
        return true;
    }

    public static boolean adjacent(char[][] board, int rowNum, int colNum, int x, int y, int orientation, int size){
        if(orientation == HORIZON){
            for(int i = x - 1; i <= x + 1; i++)
                for(int j = y - 1; j <= y + size; j++)
                    if(i >= 0 && i < rowNum && j >= 0 && j < colNum)
                        if(board[i][j] == SHIP)
                            return false;
        }
        else{
            for(int i = x - 1; i <= x + size; i++)
                for(int j = y - 1; j <= y + 1; j++)
                    if(i >= 0 && i < rowNum && j >= 0 && j < colNum)
                        if(board[i][j] == SHIP)
                            return false;
        }
        return true;
    }
    public static void placementAux(char[][] board, int x, int y, int orientation, int size){
        if(orientation == HORIZON) {
            for(int i = y; i < y + size; i++)
                board[x][i] = SHIP;
        }
        else{
            for(int i = x; i < x + size; i++)
                board[i][y] = SHIP;
        }
    }
    public static void userPlacement(char[][] board, int rowNum, int colNum, int[] ships){
        int[] shipsClone = cloneArray(ships);
        boolean flag = true;
        for(int i = 0; i < shipsClone.length; i++) {
            while(shipsClone[i] > 0){
                if(flag) {
                    System.out.println("Your current game board: ");
                    printBoard(board, rowNum, colNum);
                    System.out.println("Enter location and orientation for battleship of size " + i);
                }
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int orientation = scanner.nextInt();
                if(orientation != 0 && orientation != 1) {
                    System.out.println("Illegal orientation, try again!");
                    flag = false;
                    continue;
                }
                if((x < 0 || x >= rowNum || y < 0 || y >= colNum)) {
                    System.out.println("Illegal tile, try again!");
                    flag = false;
                    continue;
                }
                if(!(boarderCheck(rowNum, colNum, x, y, orientation, i))) {
                    System.out.println("Battleship exceeds the boundaries of the board, try again!");
                    flag = false;
                    continue;
                }
                if(!(overLapping(board, x, y, orientation, i))) {
                    System.out.println("Battleship overlaps another battleship, try again!");
                    flag = false;
                    continue;
                }
                if(!(adjacent(board, rowNum, colNum, x, y, orientation, i))) {
                    System.out.println("Adjacent battleship detected, try again!");
                    flag = false;
                    continue;
                }
                flag = true;
                placementAux(board, x, y, orientation, i);
                shipsClone[i]--;
            }
        }
        System.out.println("Your current game board: ");
        printBoard(board, rowNum, colNum);
    }

    public static void computerPlacement(char[][] board, int rowNum, int colNum, int[] ships){
        int[] shipsClone = cloneArray(ships);
        for(int i = 0; i < shipsClone.length; i++) {
            while(shipsClone[i] > 0){
                int x = rnd.nextInt(rowNum);
                int y = rnd.nextInt(colNum);
                int orientation = rnd.nextInt(2);
                if(orientation != 0 && orientation != 1)
                    continue;
                if((x < 0 || x >= rowNum || y < 0 || y >= colNum))
                    continue;
                if(!(boarderCheck(rowNum, colNum, x, y, orientation, i)))
                    continue;
                if(!(overLapping(board, x, y, orientation, i)))
                    continue;
                if(!(adjacent(board, rowNum, colNum, x, y, orientation, i)))
                    continue;
                placementAux(board, x, y, orientation, i);
                shipsClone[i]--;
            }
        }
    }
    public  static boolean isDrowned(char[][] board, int rowNum, int colNum, int x, int y) {
        boolean drowned = false;
        int step = 1;
        if (board[x - 1][y] == FREE_SPACE && board[x + 1][y] == FREE_SPACE) {
            while (y - step >= 0 && board[x][y - step] == MISS)
                step++;
            if (y - step < 0 || board[x][y - step] == FREE_SPACE) {
                step = 1;
                while (y + step < colNum && board[x][y + step] == MISS)
                    step++;
                if (y + step >= colNum || board[x][y + step] == FREE_SPACE)
                    drowned = true;
            }
        }
        else {
            while (x - step >= 0 && board[x - step][y] == MISS)
                step++;
            if (x - step < 0 || board[x - step][y] == FREE_SPACE) {
                step = 1;
                while (x + step < rowNum && board[x + step][y] == MISS)
                    step++;
                if (x + step >= rowNum || board[x + step][y] == FREE_SPACE)
                    drowned = true;
            }
        }
        return drowned;
    }

    public static int computerAttack(char[][] guessBoard, char[][] userBoard, int rowNum, int colNum, int shipsNum){
        boolean flag = false;
        int x = 0, y = 0;
        while(!flag){
            x = rnd.nextInt(rowNum);
            y = rnd.nextInt(colNum);
            if(guessBoard[x][y] == FREE_SPACE)
                flag = true;
        }
        System.out.println("The computer attacked (" + x + ", " + y + ")");
        if(userBoard[x][y] == SHIP){
            System.out.println("That is a hit");
            guessBoard[x][y] = HIT;
            userBoard[x][y] = MISS;
            if(isDrowned(userBoard, rowNum,colNum, x, y)){
               shipsNum--;
                System.out.println("Your battleship has been drowned, you have left " + shipsNum + " more battleships!");
            }
        }
        else{
            System.out.println("That is a miss");
            guessBoard[x][y] = MISS;
        }
        return shipsNum;
    }

    public static void main(String[] args) throws IOException {
        String path = args[0];
        scanner = new Scanner(new File(path));
        int numberOfGames = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Total of " + numberOfGames + " games.");

        for (int i = 1; i <= numberOfGames; i++) {
            scanner.nextLine();
            int seed = scanner.nextInt();
            rnd = new Random(seed);
            scanner.nextLine();
            System.out.println("Game number " + i + " starts.");
            battleshipGame();
            System.out.println("Game number " + i + " is over.");
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("All games are over.");

    }
}
