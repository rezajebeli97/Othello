package players;

import game.*;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliiz on 16/07/18.
 */
public class greedy extends AbstractPlayer{

    private static final int[][] VALUE_TABLE = {{99, -8, 8, 6, 6, 6, 8, -8, 99},
            {-8, -24, -4, -3, -3, -5, -24, -8},
            {8, -4, 7, 4, 4, 7, -4, 8},
            {6, -3, 4, 0, 0, 4, -3, 6},
            {99, -8, 8, 6, 6, 6, 8, -8, 99},
            {-8, -24, -4, -3, -3, -5, -24, -8},
            {8, -4, 7, 4, 4, 7, -4, 8},
            {6, -3, 4, 0, 0, 4, -3, 6}};
    public greedy(int Depth){
        super(Depth);
    }

    public void print_board(int [][] board){
        for(int i =0 ; i< 8; i++){
            for(int j =0 ; j< 8; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public BoardSquare play(int[][] board) {

        List<Move> moves = getGame().getValidMoves(board, getMyBoardMark());


        State best_state = minimax(new State(board, 0), 4, false);

        for(int i =0 ; i< 8; i++){
            for(int j =0 ; j< 8; j++){
                if(best_state.board[i][j] - board[i][j] != 0 ) {
                    System.out.println(i+1 + " " + (j+1) );
                    return new BoardSquare(i+1, j+1);

                }
            }
        }

//        BoardSquare best = new BoardSquare(-1,-1);
//        int best_value = Integer.MIN_VALUE;
//
//        for(Move move: moves){
//            int row = move.getBardPlace().getRow();
//            int column = move.getBardPlace().getCol();
//            if (VALUE_TABLE[row][column] > best_value){
//                best_value = VALUE_TABLE[row][column];
//                best = move.getBardPlace();
//            }
//        }
//        System.out.println("greedy" + best.getRow() + " " + best.getCol());
        return new BoardSquare(-1, -1);
    }

    private int[][] copy(int[][] board) {
        board = board.clone();
        for (int i = 0; i < board.length; i++) {
            board[i] = board[i].clone();
        }
        return board;
    }

    private int best_moves_value(State state){

        List<Move> moves = getGame().getValidMoves(state.board, getMyBoardMark());

        int best_value = Integer.MIN_VALUE;

        for(Move move: moves){
            int row = move.getBardPlace().getRow();
            int column = move.getBardPlace().getCol();
            if (VALUE_TABLE[row][column] > best_value){
                best_value = VALUE_TABLE[row][column];
            }
        }
        return best_value;
    }

    public class State{
        public int [][] board;
        public int value;
        public State(int [][] board, int value){
            this.board = board.clone();
            this.value = value;
        }

        public boolean has_no_moves(){
            return getGame().getValidMoves(this.board, getMyBoardMark()).size() == 0;
        }
    }


    public State minimax(State state, int depth, boolean maxPlayer){
        System.out.println(depth);
        print_board(state.board);
        System.out.println(state.value);
        if(depth == 0 | state.has_no_moves()) {
            return state;
        }
        maxPlayer = !maxPlayer;
        if(maxPlayer) {
            List<Move> moves = getGame().getValidMoves(state.board, getMyBoardMark());

            ArrayList<State> states = new ArrayList<>();
            for (Move move: moves){
                System.out.println(move.getBardPlace().getRow() + " " + move.getBardPlace().getCol());
                int value = VALUE_TABLE[move.getBardPlace().getRow()-1][move.getBardPlace().getCol()-1];
                int[][] newstate = copy(state.board);
                State s = new State(getGame().do_move(newstate, move.getBardPlace() ,this ), value+state.value);
                states.add(minimax(s, depth - 1, maxPlayer ));
            }

            int max = Integer.MIN_VALUE;
            State best_state = new State(states.get(0).board, states.get(0).value);
            int best = states.get(0).value;
            for(State s: states){
                if(best > max) {
                    max = best;
                    best_state = s;
                }
            }
            return best_state;
        }
        else {
            List<Move> moves = getGame().getValidMoves(state.board, -getMyBoardMark());

            ArrayList<State> states = new ArrayList<>();
            OthelloPlayer player = new OthelloPlayer(-1);
            player.setBoardMark(-getMyBoardMark());
            for (Move move: moves){
                int value = VALUE_TABLE[move.getBardPlace().getRow()-1][move.getBardPlace().getCol()-1];
                int[][] newstate = copy(state.board);
                State s = new State(getGame().do_move(newstate, move.getBardPlace() ,this ), state.value - value);
                states.add(minimax(s, depth - 1, maxPlayer ));
            }

            int max = Integer.MAX_VALUE;
            State best_state = new State(states.get(0).board, states.get(0).value);
            int best = states.get(0).value;
            for(State s: states){
                if(best < max) {
                    max = best;
                    best_state = s;
                }
            }

            return best_state;
        }
    }
}
