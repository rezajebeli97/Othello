package players;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

import java.util.List;
import java.util.Random;

public class SimplePlayer extends AbstractPlayer{

    public SimplePlayer(int depth) {
        super(depth);
    }
    double [][]scorecell = {
            {16,-4,1,0.5,0.5,1,-4,16},
            {-4,-7,0,0,0,0,-7,-4},
            {1,0,0.5,0,0,0.5,0,1},
            {0.5,0,0,3,3,0,0,0.5},
            {0.5,0,0,3,3,0,0,0.5},
            {1,0,0.5,0,0,0.5,0,1},
            {-4,-7,0,0,0,0,-7,-4},
            {16,-4,1,0.5,0.5,1,-4,16}
    };

public void changescore(int[][] tab){
    if(tab[0][0] == getMyBoardMark()){
        scorecell[0][1] = +7;
        scorecell[1][0] = +7;
        scorecell[1][1] = +7;
    }
    if(tab[0][7] == getMyBoardMark()){
        scorecell[0][6] = +7;
        scorecell[1][7] = +7;
        scorecell[1][6] = +7;
    }
    if(tab[7][0] == getMyBoardMark()){
        scorecell[6][1] = +7;
        scorecell[6][0] = +7;
        scorecell[7][1] = +7;
    }
    if(tab[7][7] == getMyBoardMark()){
        scorecell[6][6] = +7;
        scorecell[6][7] = +7;
        scorecell[7][6] = +7;
    }
}

    @Override
    public BoardSquare play(int[][] tab) {

        OthelloGame jogo = new OthelloGame();
        Random r = new Random();
        List<Move> jogadas = jogo.getValidMoves(tab, getMyBoardMark());
        int maxscore = -100;
        BoardSquare bestMove = new BoardSquare(-1,-1);
        for (int i = 0; i < jogadas.size(); i++) {
            Move move = jogadas.get(i);
            int [][] board = move.getBoard();
            changescore(tab);
            int score = eval(board, jogo);

            if(score > maxscore){
                maxscore = score;
                bestMove = move.getBardPlace();
            }
        }
        if (jogadas.size() > 0) {
            return bestMove;
        } else {
            return new BoardSquare(-1, -1);
        }

    }
    int eval(int board [][] ,OthelloGame jogo ){
        double score = 0.0;

        List<Move>nextAvailabeMoves = jogo.getValidMoves(board,getBoardMark());
        int availabeMoves = nextAvailabeMoves.size();
        int potentalMoves = 0;
        int potentialOponnentMoves = 0;
        //changed

        int numStable = numStableEdges(board);
        int stones = 0;
        for (int i = 0; i < 8 ; i++){
            for(int j= 0 ; j < 8 ; j ++){
                if(board[i][j] == getMyBoardMark()){
                    score+=scorecell[i][j];
                }
                else if (board[i][j] == -getMyBoardMark() ){
                    //changes
                    score-=scorecell[i][j];

                }
                else{
                    potentialOponnentMoves += potentialOpponnentMove(i,j,board);
                    potentalMoves += potentialMove(i,j,board);
                }

            }
        }

        score += availabeMoves;
        score += (potentalMoves-potentialOponnentMoves)/2;
        score += numStable * 2;
        Random rand = new Random();
        return (int)score + rand.nextInt(5);
    }
    int potentialMove(int a, int b, int board[][]){
        int potentail = 0;int ok = 0;
        int []x = {-1,0,1};
        int []y = {-1,0,1};
        for (int i = 0; i < 3; i++){
            for(int j = 0 ; j < 3; j++){
                if(a+x[i]>=0 && a+x[i]<8 && b+y[j] >= 0&&b+y[j]<8){
                    if(x[i]!=0||y[j]!=0){
                        if(board[a+x[i]][b+y[j]]==-getMyBoardMark()&&ok==0){
                            potentail++;
                            ok = 1;
                        }
                    }
                }
            }
        }
        return potentail ;
    }
    int potentialOpponnentMove(int a, int b, int board[][]){
        int potentail = 0;int ok = 0;
        int []x = {-1,0,1};
        int []y = {-1,0,1};
        for (int i = 0; i < 3; i++){
            for(int j = 0 ; j < 3; j++){
                if(a+x[i]>=0 && a+x[i]<8 && b+y[j] >= 0&&b+y[j]<8){
                    if(x[i]!=0||y[j]!=0){
                        if(board[a+x[i]][b+y[j]]==getMyBoardMark()&&board[a][b]==0&&ok==0){

                            potentail++;
                            ok = 1;
                        }
                    }
                }
            }
        }
        return potentail;
    }
    int numStableEdges(int board[][]){
        int numStable = 0;
        if(board[0][0] == getMyBoardMark()){
            for (int i = 1; i < 7; i++){
                if(board[0][i] == getMyBoardMark()  )
                    numStable++;
                else
                    break;
            }
        }
        if(board[0][0] == getMyBoardMark()){
            for (int i = 1; i < 7; i++){
                if(board[i][0] == getMyBoardMark()  )
                    numStable++;
                else
                    break;
            }
        }
        if(board[0][7] == getMyBoardMark()){
            for (int i = 1; i < 7; i++){
                if(board[i][7] == getMyBoardMark()  )
                    numStable++;
                else
                    break;
            }
        }
        if(board[0][7] == getMyBoardMark()){
            for (int i = 7; i >0; i--){
                if(board[0][i] == getMyBoardMark()  )
                    numStable++;
                else
                    break;
            }
        }
        if(board[7][0] == getMyBoardMark()){
            for (int i = 1; i < 7; i++){
                if(board[7][i] == getMyBoardMark()  )
                    numStable++;
                else
                    break;
            }
        }
        if(board[7][0] == getMyBoardMark()){
            for (int i = 7; i >0; i--){
                if(board[i][0] == getMyBoardMark()  )
                    numStable++;
                else
                    break;
            }
        }
        if(board[7][7] == getMyBoardMark()){
            for (int i = 7; i >0; i--){
                if(board[i][7] == getMyBoardMark()  )
                    numStable++;
                else
                    break;
            }
        }
        if(board[7][7] == getMyBoardMark()){
            for (int i = 7; i >0; i--){
                if(board[7][i] == getMyBoardMark()  )
                    numStable++;
                else
                    break;
            }
        }
        return numStable;
    }
}
