package players;
/**
 *
 * @author Marcelo Paglione
 */
import java.util.List;

import game.AbstractPlayer;
import game.BoardSquare;
import game.Move;
import game.OthelloGame;

public class MinMaxPlayer extends AbstractPlayer {

	OthelloGame jogo;
	int[][] scores;
	Move lastMove;
	int counter = 4;

	public MinMaxPlayer(int depth) {
		super(depth);

		scores = new int[][] { { 99, -8, 8, 6, 6, 8, -8, 99 }, 
								{ -8, -24, -4, -3, -3, -4, -24, -8 },
								{ 8, -4, 7, 4, 4, 7, -4, 8 },
								{ 6, -3, 4, 0, 0, 4, -3, 6 }, 
								{ 6, -3, 4, 0, 0, 4, -3, 6 },
								{ 8, -4, 7, 4, 4, 7, -4, 8 }, 
								{ -8, -24, -4, -3, -3, -4, -24, -8 }, 
								{ 99, -8, 8, 6, 6, 8, -8, 99 } };
	}

	@Override
	public BoardSquare play(int[][] tab) {
			jogo = new OthelloGame();
//		List<Move> jogadas = jogo.getValidMoves(tab, getMyBoardMark());

		boolean player;
		if (getBoardMark() == 1) {
			player = true;			//max player
		}
		else {
			player = false;			//min player
		}
		int score = minimax_alpha_beta(tab, player , 0, new State(null , null));
		counter ++;
		System.out.println("MinMax : " + lastMove.getBardPlace().toString() + " 	-> score = " + score);
		return lastMove.getBardPlace();

	}

	
	int minimax_alpha_beta(int[][] tab, boolean player , int level, State current) { // player may be "computer" or
																		// "opponent"
		Move lastMove = null;
		boolean gameOver = false;
		int bestScore;

		List<Move> jogadas = jogo.getValidMoves(tab, player ? 1 : -1 );		
		if (jogadas.size() == 0) {
			gameOver = true;
		}
		if (gameOver || level == 5)
			return score(tab , jogadas.size() , player);
		if (player) { // max player
			bestScore = -10000000;
			for (Move move : jogadas) {
				State s = new State(move, current);
				int score = minimax_alpha_beta(move.getBoard(), !player , level + 1, s);
				current.MIN = score;
				if (score > bestScore) {
					bestScore = score;
					lastMove = move;
				}
				if (current.parent != null && current.MIN > current.parent.MAX) {
					break;
				}
			}
		} else { // min player
			bestScore = +10000000;
			for (Move move : jogadas) {
				State s = new State(move, current);
				int score = minimax_alpha_beta(move.getBoard(), !player, level + 1, s);
				current.MAX = score;

				if (score < bestScore) {
					bestScore = score;
					lastMove = move;
				}
				if (current.parent != null && current.MAX < current.parent.MIN) {
					break;
				}
			}
		}
		this.lastMove = lastMove;
		return bestScore;
	}

	private int score(int[][] tab , int mobility , boolean player) {
		int maxScore = 0;
		int maxPieces = 0;
		int minScore = 0;
		int minPieces = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (tab[i][j] == 1) {
					maxScore += scores[i][j];
					maxPieces++;
				}
				if (tab[i][j] == -1) {
					minScore += scores[i][j];
					minPieces++;
				}
			}
		}
		
		int score = maxScore - minScore;
		if (counter < 20) {
			int value = (20 - counter);
			if (player) {
				score += mobility*value;
			}
			else {
				score -= mobility*value;
			}
		}
		else {
			score+= 2 * (maxPieces - minPieces);
		}

		return score;
	}
}


class State {
	protected Move move;
	protected State parent;
	protected int MAX = Integer.MAX_VALUE;
	protected int MIN = Integer.MIN_VALUE;

	public State(Move move, State parent) {
		this.move = move;
		this.parent = parent;
	}
}
