package game.example.testminirocket.GameObjects;

public class PlanetState {

    public enum State{
        NOT_MOVING,
        IS_MOVING
    }

    private Planet planet;
    private State state;

    public void update() {
        switch (state){
            case NOT_MOVING:
                if (planet.canAnimate) state = State.IS_MOVING;
                break;
            case IS_MOVING:
                if (!planet.canAnimate) state = State.NOT_MOVING;
                break;
            default:
                break;
        }
    }

    public PlanetState(Planet planet){
        this.planet = planet;
        this.state = State.NOT_MOVING;
    }



    public State getState() {
        return state;
    }
}
