package com.example.goframework;

import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

import com.example.GameFramework.LocalGame;
import com.example.GameFramework.actionMessage.GameAction;
import com.example.GameFramework.infoMessage.GameInfo;
import com.example.GameFramework.infoMessage.GameState;
import com.example.GameFramework.players.GamePlayer;
import com.example.GameFramework.utilities.Logger;

public class GoLocalGame extends LocalGame{

    private int EMPTY = -1;
    private int WHITE = -2;
    private int BLACK = -3;
    private int WHITE_IN_PERIL = -4;
    private int BLACK_IN_PERIL = -5;




    public GoLocalGame(int boardSize) {
        super();
        super.state = new GoGameState(boardSize);
    }

    public GoLocalGame(GoGameState glg) {
        super();
        super.state = new GoGameState(glg);
    }

    @Override
    public void start(GamePlayer[] players) {
        super.start(players);
    }


    @Override
    protected String checkIfGameOver() {
        String win;
        GoGameState state = (GoGameState) super.state;

        Log.d("tag",state.toString());

        if(state.getGameContinueOne() == false && state.getGameContinueTwo() == false) {
            if(state.getWhiteScore() > state.getBlackScore()) {
                win = "The white piece has won the game!";
                return win;
            }
            else if (state.getWhiteScore() < state.getBlackScore()) {
                win = "The black piece has won the game!";
                return win;
            }
            else if (state.getWhiteScore() == state.getBlackScore()) {
                win = "The game is tied!";
                return win;
            }
        }
        return null;
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        //removeCapturedStones(); //no longer needed

        p.sendInfo(new GoGameState((GoGameState) state));
    }

    @Override
    protected boolean canMove(int playerIdx) {
        return playerIdx == ((GoGameState) state).getPlayerToMove();
    }

    @Override
    protected boolean makeMove(GameAction action) {

        //first checking if the action is to place a piece
        if(action instanceof GoPlacePieceAction) {

            //getting the GoGameState and action
            GoPlacePieceAction gppa = (GoPlacePieceAction) action;
            GoGameState state = (GoGameState) super.state;

            //getting the x and y coordinates
            int x = gppa.getX();
            int y = gppa.getY();

            //getting the current player's id
            int playerId = getPlayerIdx(gppa.getPlayer());

            //return false if the space is not empty
            if(state.getGameBoard(x,y) != EMPTY) {
                return false;
            }

            //else...
            else {

                //get
                int playerToMove = state.getPlayerToMove();

                if (playerId == 0) {
                    state.setGameBoard(WHITE, x, y);
                }
                else {
                    state.setGameBoard(BLACK, x, y);
                }
                removeCapturedStones(state);

                state.setPlayerToMove(1 - playerToMove);
                return true;
            }

        }
        else if(action instanceof GoSkipTurnAction) {
            GoGameState state = (GoGameState) super.state;
            Log.d("tag",state.toString());
            GoSkipTurnAction gsta = (GoSkipTurnAction)action;

            //getting the current player's id
            int playerId = getPlayerIdx(gsta.getPlayer());
            int playerToMove = state.getPlayerToMove();

            if (playerId == 0) {
                state.setGameContinueOne(false);
            }
            else {
                state.setGameContinueTwo(false);
            }

            Logger.log("GoSkipTurnAction", "gameContinueOne "
                    + state.getGameContinueOne() + " gameContinueTwo"  + state.getGameContinueTwo());
            state.setPlayerToMove(1 - playerToMove);
            return true;
        }
        else {
            return false;
        }
    }

    public void removeCapturedStones(GoGameState goGameState) {
        int[][] board = goGameState.getGameBoard();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column] == WHITE) {
                    board[row][column] = WHITE_IN_PERIL;
                }
            }
        }

        //part 3
        int loopCounter = 0;
        boolean loopIn = true;
        while (loopIn == true) {
            loopIn = false;
            for (int row = 0; row < board.length; row++) {
                for (int column = 0; column < board[row].length; column++) {
                    if (board[row][column] == WHITE_IN_PERIL) {
                        if (row > 0) {
                            if ((board[row - 1][column] == EMPTY) || (board[row - 1][column] == WHITE)) {
                                board[row][column] = WHITE;
                                loopIn = true;

                            }
                        }

                        if (row < board.length - 1) {
                            if ((board[row + 1][column] == EMPTY) || (board[row + 1][column] == WHITE)) {
                                board[row][column] = WHITE;
                                loopIn = true;
                            }
                        }

                        if (column > 0) {
                            if ((board[row][column - 1] == EMPTY) || (board[row][column - 1] == WHITE)) {
                                board[row][column] = WHITE;
                                loopIn = true;
                            }
                        }

                        if (column < board[row].length - 1) {
                            if ((board[row][column + 1] == EMPTY) || (board[row][column + 1] == WHITE)) {
                                board[row][column] = WHITE;
                                loopIn = true;
                            }
                        }
                    }
                }
            }
        }

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column] == WHITE_IN_PERIL) {
                    goGameState.incrementBlackScore();
                    board[row][column] = EMPTY;
                }
            }
        }


        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column] == BLACK) {
                    board[row][column] = BLACK_IN_PERIL;
                }
            }
        }

        int loopCounter2 = 0;
        boolean loopIn2 = true;
        while (loopIn2 == true) {
            loopIn2 = false;
            for (int row = 0; row < board.length; row++) {
                for (int column = 0; column < board[row].length; column++) {
                    if (board[row][column] == BLACK_IN_PERIL) {

                        if (row > 0) {
                            if ((board[row - 1][column] == EMPTY) || (board[row - 1][column] == BLACK)) {
                                board[row][column] = BLACK;
                                loopIn2 = true;

                            }
                        }

                        if (row < board.length - 1) {
                            if ((board[row + 1][column] == EMPTY) || (board[row + 1][column] == BLACK)) {
                                board[row][column] = BLACK;
                                loopIn2 = true;
                            }
                        }

                        if (column > 0) {
                            if ((board[row][column - 1] == EMPTY) || (board[row][column - 1] == BLACK)) {
                                board[row][column] = BLACK;
                                loopIn2 = true;
                            }
                        }

                        if (column < board[row].length - 1) {
                            if ((board[row][column + 1] == EMPTY) || (board[row][column + 1] == BLACK)) {
                                board[row][column] = BLACK;
                                loopIn2 = true;
                            }
                        }

                    }
                }
            }
        }

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                if (board[row][column] == BLACK_IN_PERIL) {
                    goGameState.incrementWhiteScore();
                    board[row][column] = EMPTY;
                }
            }
        }
    }
}
