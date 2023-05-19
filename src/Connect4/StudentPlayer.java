package Connect4;
import java.util.ArrayList;
import static java.lang.Math.*;
public class StudentPlayer extends Player{
        int requiredDepth;
        int position;
        final int infinity=10000000;
        ArrayList<Integer> validSteps;

        public StudentPlayer(int playerIndex, int[] boardSize, int nToConnect) {
            super(playerIndex, boardSize, nToConnect);
            requiredDepth = 5;
            position = 0;
        }

        @Override
        public int step(Board board) {
           int alpha = -infinity;
           int beta =infinity;
           boolean maximizingPlayer=true;
           int column = minimax(board, alpha, beta, requiredDepth,maximizingPlayer);
           return column;

        }
    private int minimax(Board board, int alpha, int beta, int depth,boolean maximizingPlayer) {
        if (depth == 0) {
            return countWeight(board);
        }else{
           validSteps = board.getValidSteps();
            Board newBoard;
        if (maximizingPlayer) {
                int maxEval = -infinity;
              for (int validStep : validSteps) {
                    newBoard = new Board(board);
                    newBoard.step(2, validStep);
                    int eval = minimax(newBoard, alpha, beta, depth - 1, false);
                    if (eval >= maxEval) {
                        maxEval = eval;
                        if (depth == requiredDepth)
                            position = validStep;
                    }
                    if (maxEval >= beta) {
                        if (depth == requiredDepth)
                           return position;
                        return maxEval;
                    }
                    alpha = max(alpha, maxEval);
                }
                if (depth == requiredDepth) {
                    return position;
                } else
                    return maxEval;
        }else {
                int minEval = infinity;
                for (int validStep : validSteps) {
                    newBoard = new Board(board);
                    newBoard.step(1, validStep);
                    int eval = minimax(newBoard, alpha, beta, depth - 1,true);
                    minEval = min(eval,minEval);
                    if (minEval <= alpha)
                        return minEval;
                    beta = min(beta, minEval);
                }
                return minEval;
        }}
    }

    private int countWeight(Board board) {

            int value = 0;
            int[][] state = board.getState();
            for(int row = 0; row < state.length; row++){
                for (int column= 0; column < state[0].length; column++){
                    if (state[row][column] != 0 ){
                        int playerIndex = state[row][column];
                        int maxValue=valueOnRow(state, row, column, playerIndex)+valueOnColumn(state, row, column, playerIndex)+ valueOnLeftDiagonal(state, row, column, playerIndex)+valueOnRightDiagonal(state,row ,column, playerIndex);
                        if ( playerIndex == 2)
                            value+= (maxValue * 0.5);
                        else value -=maxValue;
                    }
                }
            }
            return value;
        }
        private int countAll(int getPoint,int freeCell){
            if (getPoint >= 4)
                return infinity;
            else if (getPoint != 0 && freeCell != 0)
                return (int) (pow(10, getPoint) * ((int) pow(4,freeCell)));
            return 0;
        }
        private int valueOnColumn(int[][] state, int row, int column, int playerIndex){
        int oppositeIndex = 3 - playerIndex;
        int getPoint = 1, freeCell = 0, notOpposite = 0;

        for (int i = 1; row + i < state.length && state[row + i][column] != oppositeIndex; i++)
            notOpposite++;
        for (int i = 1; row - i >= 0 && state[row - i][column] != oppositeIndex; i++)
            notOpposite++;

      if (row > 0 && state[row - 1][column] == 0)
                freeCell++;
        else if(notOpposite < 3||row > 0 && state[row - 1][column] == playerIndex)
            return 0;

            int i=1,y=1;
            while(true){
            if ((row + i) >= state.length)
                break;
            else if (state[row + i][column] == playerIndex)
                getPoint++;
            else if( state[row + i][column]== 0)
            {freeCell++;
                break;}
            else
                break;
            i++;
        }

            return countAll(getPoint,freeCell);
    }


        private int valueOnRow(int[][] state, int row, int column, int playerIndex){

            int oppositeIndex = 3 - playerIndex;
            int getPoint = 1, freeCell = 0, notOpposite = 0;

            for (int i = 1; column - i >= 0 && state[row][column - i] != oppositeIndex; i++)
                notOpposite++;
            for (int i = 1; column + i < state[0].length && state[row][column + i] != oppositeIndex; i++)
                notOpposite++;

            if (notOpposite < 3||(column > 0&&state[row][column - 1] == playerIndex))
                return 0;
            if(column > 0&&state[row][column - 1] == 0){
                    freeCell++;
          }

            int i=1;
            while(true){
                if ((column + i) >= state[0].length)
                    break;
                else if (state[row][column + i] == playerIndex)
                    getPoint++;
                else if( state[row][column + i] == 0){
                    freeCell++;
                    break;}
                 else
                    break;
                 i++;
                }
            return countAll(getPoint,freeCell);
        }
        private int valueOnLeftDiagonal(int[][] state, int row, int column, int playerIndex){

            int oppositeIndex = 3 - playerIndex;
            int getPoint = 1, freeCell = 0,notOpposite= 0;
            for (int i = 1; row + i < state.length && column + i < state[0].length && state[row + i][column + i] != oppositeIndex; i++)
                notOpposite++;
            for (int i = 1; row - i >= 0 && column - i >= 0 && state[row - i][column - i] != oppositeIndex; i++)
                notOpposite++;

            if (notOpposite < 3||(row > 0 && column > 0&&state[row - 1][column - 1] == playerIndex))
                return 0;
            if(row > 0 && column > 0&&state[row - 1][column - 1] == 0){
                    freeCell++;
            }
            int i=1;
            while(true){

                if ((row + i) >= state.length || (column + i) >= state[0].length)
                    break;
                else if (state[row + i][column + i] == playerIndex)
                    getPoint++;
                else if (state[row + i][column + i] == 0){
                        freeCell +=1;
                    break;
                }
                else
                   break;
                i++;
            }
            return countAll(getPoint,freeCell);
        }

        private int valueOnRightDiagonal(int[][] state, int row, int column, int playerIndex){

            int  oppositeIndex = 3 - playerIndex;
            int getPoint = 1, freeCell = 0, notOpposite = 0;
            for (int i = 1; row - i >= 0 && column + i < state[0].length && state[row - i][column + i] !=  oppositeIndex; i++)
                notOpposite++;
            for (int i = 1; row + i < state.length && column - i > 0 && state[row + i][column - i] !=  oppositeIndex; i++)
                notOpposite++;

            if (notOpposite < 3||(row < (state.length-1) && column > 0&&state[row + 1][column - 1] == playerIndex))
                return 0;
            if(row < (state.length-1) && column > 0&&state[row + 1][column - 1] == 0){
                    freeCell++;
            }
            int i=1;
            while(true){
                if ((row - i) < 0 || (column + i) >= state[0].length)
                    break;
                else if (state[row - i][column + i] == playerIndex)
                    getPoint++;
                else if (state[row - i][column + i] == 0){
                    freeCell +=1;
                    break;
                }
                else break;
                i++;
            }
            return countAll(getPoint,freeCell);
        }

    }

