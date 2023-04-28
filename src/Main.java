import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;
    public static final  char SHIP = '#';
    public static final char FREE_SPACE = '-';
    public static final int HORIZON = 0;
    public static final int VERTICAL = 1;

    public static void battleshipGame() {
        // TODO: Add your code here (and add more methods).
    }

    public static void printBoard(char[][] board, int n, int m){
        for(int i = 0; i <= m; i++){
            if(i == 0)
                System.out.print("  ");
            else
                System.out.print((i - 1) + " ");
        }
        System.out.println();
        for(int i = 0; i < n; i++){
            for(int j = 0; j <= m; j++){
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

    public static boolean boarderCheck(int n, int m, int x, int y, int orientation, int size){
       if(orientation == HORIZON && (y + size - 1) >= m)
           return false;
       else if(x + size - 1 >= n)
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

    public static boolean adjacent(char[][] board, int x, int y, int orientation, int size){
        if(orientation == HORIZON){
            for(int i = x - 1; i <= x + 1; i++)
                for(int j = y - 1; j <= y + size; j++)
                    if(board[i][j] == SHIP)
                        return false;
        }
        else{
            for(int i = x - 1; i <= x + size; i++)
                for(int j = y - 1; j <= y + 1; j++)
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
    public static void userPlacement(char[][] board, int n, int m, int[] ships){
        int[] shipsClone = cloneArray(ships);
        boolean flag = true;
        for(int i = 0; i < shipsClone.length; i++) {
            while(shipsClone[i] > 0){
                if(flag)
                    System.out.println("Enter location and orientation for battleship of size " + i);
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int orientation = scanner.nextInt();
                if(orientation != 0 && orientation != 1) {
                    System.out.println("Illegal orientation, try again!");
                    flag = false;
                    continue;
                }
                if((x < 0 || x >= n || y < 0 || y >= n)) {
                    System.out.println("Illegal tile, try again!");
                    flag = false;
                    continue;
                }
                if(!(boarderCheck(n, m, x, y, orientation, i))) {
                    System.out.println("Battleship exceeds the boundaries of the board, try again!");
                    flag = false;
                    continue;
                }
                if(!(overLapping(board, x, y, orientation, i))) {
                    System.out.println("Battleship overlaps another battleship, try again!");
                    flag = false;
                    continue;
                }
                if(!(adjacent(board, x, y, orientation, i))) {
                    System.out.println("Adjacent battleship detected, try again!");
                    flag = false;
                    continue;
                }
                flag = true;
                placementAux(board, x, y, orientation, i);
                ships[i]--;
            }
        }
    }

    public static void computerPlacement(char[][] board, int n, int m, int[] ships){
        int[] shipsClone = cloneArray(ships);
        for(int i = 0; i < shipsClone.length; i++) {
            while(shipsClone[i] > 0){
                int x = rnd.nextInt(n);
                int y = rnd.nextInt(m);
                int orientation = rnd.nextInt(2);
                if(orientation != 0 && orientation != 1)
                    continue;
                if((x < 0 || x >= n || y < 0 || y >= n))
                    continue;
                if(!(boarderCheck(n, m, x, y, orientation, i)))
                    continue;
                if(!(overLapping(board, x, y, orientation, i)))
                    continue;
                if(!(adjacent(board, x, y, orientation, i)))
                    continue;
                placementAux(board, x, y, orientation, i);
                ships[i]--;
            }
        }
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
