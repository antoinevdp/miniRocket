package game.example.testminirocket.GameObjects;

public class AsteroidState {
    public enum State{
        NOT_MOVING,
        IS_MOVING
    }

    private Asteroid asteroid;
    private AsteroidState.State state;

    public void update() {
        switch (state){
            case NOT_MOVING:
                if (asteroid.canAnimate) state = AsteroidState.State.IS_MOVING;
                break;
            case IS_MOVING:
                if (!asteroid.canAnimate) state = AsteroidState.State.NOT_MOVING;
                break;
            default:
                break;
        }
    }

    public AsteroidState(Asteroid asteroid){
        this.asteroid = asteroid;
        this.state = AsteroidState.State.NOT_MOVING;
    }



    public AsteroidState.State getState() {
        return state;
    }
}
