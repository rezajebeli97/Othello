package players;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

import java.util.List;
import java.util.Random;

/**
 * Created by invisible on 7/16/18.
 */
public class Bear extends AbstractPlayer {

    int t = 64;

    public Bear(int depth) {
        super(depth);
    }

    @Override
    public BoardSquare play(int[][] tab) {
        int index = 0;
        OthelloGame game = new OthelloGame();
        value(tab);
        List<Move> moves = game.getValidMoves(tab, getMyBoardMark());
        Random r = new Random();

        if (moves.size() > 0) {
            if (moves.size() > 1) {
                if (r.nextBoolean()) {
                    index = 1;
                }
            }
            Move bestMove = moves.get(index);
            return bestMove.getBardPlace();
        }

        return new BoardSquare(-1, -1);
    }

    private int[][] copy(int[][] board) {
        board = board.clone();
        for (int i = 0; i < board.length; i++) {
            board[i] = board[i].clone();
        }
        return board;
    }

    public int value(int [][]tab) {
        int fit = 0;
        for (int r = 0; r < tab.length; r++) {
            for (int c = 0; c < tab[0].length; c++) {
                if (tab[r][c] == getMyBoardMark())
                    fit++;
            }
        }

        return fit;
    }

    public boolean checkCorner(Move move) {
        int val = 20;
        int column = move.getBardPlace().getCol();
        int row = move.getBardPlace().getRow();

        if ((column == 0 && row == 0) || (column == 0 && row == 7) || (column == 7 && row == 0) || (column == 7 && row == 7)) {
            return true;
        }

        return false;
    }
}
