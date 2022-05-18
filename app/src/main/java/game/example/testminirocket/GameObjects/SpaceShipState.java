package game.example.testminirocket.GameObjects;

public class SpaceShipState {
    public enum State{
        NOT_MOVING,
        IS_WAITING,
        IS_MOVING
    }

    private SpaceShip spaceShip;
    private SpaceShipState.State state;

    public void update() {
        switch (state){
            case NOT_MOVING:
                if (spaceShip.canAnimate) state = SpaceShipState.State.IS_MOVING;
                break;
            case IS_MOVING:
                if (!spaceShip.canAnimate) state = SpaceShipState.State.NOT_MOVING;
                break;
            default:
                break;
        }
    }

    public SpaceShipState(SpaceShip spaceShip){
        this.spaceShip = spaceShip;
        this.state = SpaceShipState.State.NOT_MOVING;
    }



    public SpaceShipState.State getState() {
        return state;
    }
}
