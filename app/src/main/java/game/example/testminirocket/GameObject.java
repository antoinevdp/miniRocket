package game.example.testminirocket;

public class GameObject {
    protected double coordX;
    protected double coordY;

    public GameObject(double coordX, double coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

    protected static double getDistanceBetweenObjects(GameObject gameObject1, GameObject gameObject2){
        double maxBtwCoordX = Math.max(gameObject1.getPositionX(), gameObject2.getPositionX());
        double maxBtwCoordY = Math.max(gameObject1.getPositionY(), gameObject2.getPositionY());
        double minBtwCoordX = Math.min(gameObject1.getPositionX(), gameObject2.getPositionX());
        double minBtwCoordY = Math.min(gameObject1.getPositionY(), gameObject2.getPositionY());
        // Pyhtagore pour déterminer la distance entre les 2 planètes
        return Math.sqrt(Math.pow(maxBtwCoordX - minBtwCoordX, 2) + Math.pow(maxBtwCoordY - minBtwCoordY, 2));
    }

    protected double getPositionX(){ return this.coordX; }
    protected double getPositionY(){ return this.coordY; }
}
