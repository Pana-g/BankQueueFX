package sample;

import java.util.ArrayList;

public class WaitingLinePositions {
    private ArrayList<Position> positions;

    public WaitingLinePositions(){
        positions = new ArrayList<>();
        for(int i=0;i<30;i++){
            positions.add(new Position(true));
        }
    }

    public int getEmptyposition(){
        int i=0;
        for(Position position : positions){
            if(position.isFree()){
                position.setFree(false);
                return i;
            }
            i++;
        }
        return -1;
    }

    public void freePosition(int pos){
        positions.get(pos).setFree(true);

    }
}
