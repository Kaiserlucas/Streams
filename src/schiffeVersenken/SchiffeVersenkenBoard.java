package schiffeVersenken;

public class SchiffeVersenkenBoard {
    public static final int DIM = 10;

    private BoardSpace[][] boardSpaces = new BoardSpace[DIM][DIM];

    public SchiffeVersenkenBoard() {
        for(int i = 0; i < DIM; i++) {
            for( int j = 0; j < DIM; j++) {
                boardSpaces[i][j] = BoardSpace.WATER;
            }
        }
    }

    public BoardSpace getSpace(int x, int y) throws SchiffeVersenkenException {
        if( x < 0 || y < 0 || x >= DIM || y >= DIM) {
            throw new SchiffeVersenkenException();
        }
        return boardSpaces[x][y];
    }

    public void setSpace(int x, int y, BoardSpace set) throws SchiffeVersenkenException {
        if( x < 0 || y < 0 || x >= DIM || y >= DIM) {
            throw new SchiffeVersenkenException();
        }
        boardSpaces[x][y] = set;
    }

    public boolean checkSunk(int x, int y) throws SchiffeVersenkenException {
        if( x < 0 || y < 0 || x >= DIM || y >= DIM) {
            throw new SchiffeVersenkenException();
        }

        if(boardSpaces[x][y] != BoardSpace.SHOT_SHIP) {
            return false;
        }

        int nowX = x;
        int nowY = y;

        while(nowX >= 0 && boardSpaces[nowX][nowY] != BoardSpace.WATER && boardSpaces[nowX][nowY] != BoardSpace.SHOT_WATER) {
            if(boardSpaces[nowX][nowY] == BoardSpace.SHIP) {
                return false;
            }
            nowX--;
        }

        nowX = x;

        while(nowX < DIM && boardSpaces[nowX][nowY] != BoardSpace.WATER && boardSpaces[nowX][nowY] != BoardSpace.SHOT_WATER) {
            if(boardSpaces[nowX][nowY] == BoardSpace.SHIP) {
                return false;
            }
            nowX++;
        }

        nowX = x;

        while(nowY >= 0 && boardSpaces[nowX][nowY] != BoardSpace.WATER && boardSpaces[nowX][nowY] != BoardSpace.SHOT_WATER) {
            if(boardSpaces[nowX][nowY] == BoardSpace.SHIP) {
                return false;
            }
            nowY--;
        }

        nowY = y;

        while(nowY < DIM && boardSpaces[nowX][nowY] != BoardSpace.WATER && boardSpaces[nowX][nowY] != BoardSpace.SHOT_WATER) {
            if(boardSpaces[nowX][nowY] == BoardSpace.SHIP) {
                return false;
            }
            nowY++;
        }

        return true;

    }

    public void printBoard() {
        System.out.println("      1   2   3   4   5   6   7   8   9   10");
        System.out.print("    _________________________________________");
        for(int x = 0; x < DIM; x++) {
            System.out.printf(" \n "+(x+1));
            if(x < DIM-1) { System.out.print(" "); }
            System.out.print(" | ");
            for( int y = 0; y < DIM; y++) {
                System.out.print(getSymbol(boardSpaces[x][y])+" | ");
            }
        }
        System.out.printf( "\n    _________________________________________");
    }

    public void setSunk(int zeile, int spalte) throws SchiffeVersenkenException {

        if(boardSpaces[zeile][spalte] != BoardSpace.SHOT_SHIP ||  zeile < 0 || spalte < 0 || zeile >= DIM || spalte >= DIM) {
            throw new SchiffeVersenkenException();
        }

        int nowX = zeile;
        int nowY = spalte;

        while(nowX >= 0 && boardSpaces[nowX][nowY] != BoardSpace.WATER && boardSpaces[nowX][nowY] != BoardSpace.SHOT_WATER) {
            if(boardSpaces[nowX][nowY] == BoardSpace.SHOT_SHIP) {
                boardSpaces[nowX][nowY] = BoardSpace.SUNK_SHIP;
            }
            nowX--;
        }
        nowX = zeile;

        while(nowX < DIM && boardSpaces[nowX][nowY] != BoardSpace.WATER && boardSpaces[nowX][nowY] != BoardSpace.SHOT_WATER) {
            if(boardSpaces[nowX][nowY] == BoardSpace.SHOT_SHIP) {
                boardSpaces[nowX][nowY] = BoardSpace.SUNK_SHIP;
            }
            nowX++;
        }

        nowX = zeile;

        while(nowY >= 0 && boardSpaces[nowX][nowY] != BoardSpace.WATER && boardSpaces[nowX][nowY] != BoardSpace.SHOT_WATER) {
            if(boardSpaces[nowX][nowY] == BoardSpace.SHOT_SHIP) {
                boardSpaces[nowX][nowY] = BoardSpace.SUNK_SHIP;
            }
            nowY--;
        }

        nowY = spalte;

        while(nowY < DIM && boardSpaces[nowX][nowY] != BoardSpace.WATER && boardSpaces[nowX][nowY] != BoardSpace.SHOT_WATER) {
            if(boardSpaces[nowX][nowY] == BoardSpace.SHOT_SHIP) {
                boardSpaces[nowX][nowY] = BoardSpace.SUNK_SHIP;
            }
            nowY++;
        }

    }

    private char getSymbol(BoardSpace space) {
        switch(space){
            case WATER:
                return ' ';
            case SHIP:
                return 'S';
            case SHOT_WATER:
                return 'X';
            case SHOT_SHIP:
                return 'O';
            case SUNK_SHIP:
                return 'Z';
        }
        return ' ';
    }





}
