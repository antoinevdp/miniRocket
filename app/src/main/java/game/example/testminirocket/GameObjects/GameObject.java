package game.example.testminirocket.GameObjects;
// Cette classe est utile pour tout les objets du jeu (Planètes, trajectoires, etc...)
public class GameObject {
    // Coordonées X et Y sur le canvas
    protected double coordX;
    protected double coordY;

    public GameObject(double coordX, double coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }
    // Retourne la distance entre deux objets
    protected static double getDistanceBetweenObjects(GameObject gameObject1, GameObject gameObject2){
        double maxBtwCoordX = Math.max(gameObject1.getPositionX(), gameObject2.getPositionX());
        double maxBtwCoordY = Math.max(gameObject1.getPositionY(), gameObject2.getPositionY());
        double minBtwCoordX = Math.min(gameObject1.getPositionX(), gameObject2.getPositionX());
        double minBtwCoordY = Math.min(gameObject1.getPositionY(), gameObject2.getPositionY());
        // Pyhtagore pour déterminer la distance entre les 2 planètes
        return Math.sqrt(Math.pow(maxBtwCoordX - minBtwCoordX, 2) + Math.pow(maxBtwCoordY - minBtwCoordY, 2));
    }
    // Retourne la position de l'objet
    public double getPositionX(){ return this.coordX; }
    public double getPositionY(){ return this.coordY; }
}
