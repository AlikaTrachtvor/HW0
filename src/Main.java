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
        int rowNum, colNum, userShipNum = 0, comShipNum = 0;
        System.out.println("Enter the board size");
        String boardSize = scanner.nextLine();
        int[] rowCol = new int[2];
        convertToNum(boardSize, rowCol);
        rowNum = rowCol[0];
        colNum = rowCol[1];
        int maxBoardSize = findMax(rowNum,colNum);
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
        System.out.println("Enter the battleships sizes");
        String sizes = scanner.nextLine();
        int[] sizeQunt = new int[sizes.length()];
        convertToNum(sizes, sizeQunt);
        for(int i = 0; i < sizes.length() && sizeQunt[i] > 0 ; i = i + 2){
            int quantity = sizeQunt[i];
            int size = sizeQunt[i + 1];
            battleShips[size] = quantity;
            userShipNum += quantity;
        }
        comShipNum = userShipNum;
        userPlacement(userBoard, rowNum, colNum, battleShips);
        computerPlacement(compBoard, rowNum, colNum, battleShips);

        while(userShipNum > 0 && comShipNum > 0){
            comShipNum = userAttack(userGuessBoard, compBoard, rowNum, colNum, comShipNum);
            if(comShipNum == 0)
                break;
            userShipNum = computerAttack(compGuessBoard, userBoard, rowNum, colNum, userShipNum);
        }

        if(userShipNum == 0)
            System.out.println("You lost ):");
        else
            System.out.println("You won the game!");

    }
    public static int findMax(int num1, int num2){
        if(num1 >= num2)
            return num1;
        else return num2;
    }

    public static void convertToNum(String str, int[] arr){
        int start = 0, end = 0, i = 0, num = 0;
        while(end < str.length()) {
            while (end < str.length() && (Character.isDigit(str.charAt(end)) || str.charAt(end) == '-'))
                end++;
            num = Integer.parseInt(str.substring(start, end));
            arr[i++] = num;
            while (end < str.length() && !(Character.isDigit(str.charAt(end))) && !(str.charAt(end) == '-'))
                end++;
            start = end;
        }
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
        int[] position = new int[3];
        int x = 0, y = 0, orientation = 0;
        boolean flag = true;
        for(int i = 0; i < shipsClone.length; i++) {
            while(shipsClone[i] > 0){
                if(flag) {
                    System.out.println("Your current game board: ");
                    printBoard(board, rowNum, colNum);
                    System.out.println("Enter location and orientation for battleship of size " + i);
                }
                String userChoice = scanner.nextLine();
                convertToNum(userChoice, position);
                x = position[0];
                y = position[1];
                orientation = position[2];
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
        if ((y - 1 < 0 || board[x][y - 1] == FREE_SPACE) && (y + 1 >=colNum || board[x][y + 1] == FREE_SPACE)) {
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
        else {
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
        return drowned;
    }
    public static boolean moveCheck(char [][]board, int rowNum ,int colNum ,int x, int y) {
        if ((x >= rowNum) || (y >= colNum) || (x < 0) || (y < 0)){
            System.out.println("Illegal tile, try again!");
            return false;
        }
        else if (board[x][y] == MISS || board[x][y] == HIT) {
            System.out.println("Tile already attacked, try again!");
            return false;
        }
        else return true;
    }
    public static int userAttack(char guessBoard[][],char comBoard[][],int rowNum, int colNum ,int comShipNum){

        System.out.println("Your current guessing board: ");
        printBoard(guessBoard, rowNum, colNum);
        System.out.println("Enter a tile to attack");
        String userChoice = scanner.nextLine();
        int[] tile = new int[2];
        convertToNum(userChoice, tile);
        int x = tile[0];
        int y = tile[1];

        while (!moveCheck(guessBoard, rowNum, colNum, x, y)) {
            userChoice = scanner.nextLine();
            convertToNum(userChoice, tile);
            x = tile[0];
            y = tile[1];
        }
        if(comBoard[x][y] == FREE_SPACE) {
            System.out.println("That is a miss!");
            guessBoard[x][y] = MISS;
        }
        else{
            System.out.println("That is a hit!");
            guessBoard[x][y] = HIT;
            comBoard[x][y] = MISS;
            if(isDrowned(comBoard, rowNum, colNum, x, y)){
                comShipNum--;
                System.out.println("The computer's battleship has been drowned,"
                        + comShipNum + " more battleships to go!");
            }
        }
        return comShipNum;
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
                System.out.println("Your battleship has been drowned, you have left "
                        + shipsNum + " more battleships!");
            }
        }
        else{
            System.out.println("That is a miss");
            guessBoard[x][y] = MISS;
        }
        System.out.println("Your current game board: ");
        printBoard(userBoard, rowNum, colNum);
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
