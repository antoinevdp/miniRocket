package game.example.testminirocket.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

import game.example.testminirocket.graphics.Animator;

public class SpaceShip extends GameObject{
    private static final double SPEED_PIXELS_PER_SECOND = 300;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWN_PER_MINUTE = 20;
    private static final double SPAWN_PER_SECOND = SPAWN_PER_MINUTE / 60.0;
    private static final double UPDATE_PER_SPAWN = GameLoop.MAX_UPS / SPAWN_PER_SECOND;
    private static double updateUntilNextSpawn = UPDATE_PER_SPAWN;
    public boolean canAnimate;

    private int id;
    private ArrayList<Traveller> list_travellers_in_spaceship = new ArrayList<>();
    private ArrayList<Trajectory> list_trajectory = new ArrayList<>();
    private ArrayList<Planet> list_planet = new ArrayList<>();



    private double velocityX; // vitesse en x
    private double velocityY; // vitesse en y

    private int counter = 0;

    public boolean canGo; // Boolean pour avancer ou non
    public boolean isTravelling;
    public boolean canBeDestroyed;
    public boolean hasArrived;

    private double directionX; // on calcule la direction à prendre en x
    private double directionY; // on calcule la direction à prendre en y

    private Planet current_planet; // Planète actuelle
    private Planet next_target_planet; // Planète où l'on veut aller
    private Planet first_planet;

    public Trajectory current_trajectory;

    private SpaceShipState spaceShipState;
    private Animator animator;

    private int familly;
    private int counterOfArrTraj = 1;
    private int maxTravellerInSpaceShip;


    private final Paint paint;
    public boolean hasBoucled;
    private boolean hasReachedFirstPlanet;
    private boolean goUp;

    public SpaceShip(double coordX, double coordY, Planet current_planet, int familly, Animator animator) {
        super(coordX, coordY);
        this.velocityX = 0;
        this.velocityY = 0;
        this.canGo = true;
        this.isTravelling = false;
        this.canBeDestroyed = false;
        this.hasArrived = false;
        this.current_planet = current_planet;
        this.counter = 1;
        this.hasBoucled = false;
        this.hasReachedFirstPlanet = false;
        this.next_target_planet = this.current_planet.my_trajectory.getEndPlanet();
        this.current_trajectory = this.current_planet.my_trajectory;
        this.goUp = true;
        this.familly = familly;
        this.canAnimate = true;
        this.maxTravellerInSpaceShip = 4;

        this.spaceShipState = new SpaceShipState(this);
        this.animator = animator;


        this.paint = new Paint();
        if (this.familly == 1) paint.setColor(Color.GREEN);
        else paint.setColor(Color.RED);
        // Planète où l'on veut aller
    }

    public void update(){

        if (this.canGo){ // Si il peut voyager
            double distanceToNextPlanetX = this.next_target_planet.getPositionX() - this.coordX; // On calcule la distance x entre sa position et la planète
            double distanceToNextPlanetY = this.next_target_planet.getPositionY() - this.coordY; // On calcule la distance y entre sa position et la planète

            double distanceToNextPlanet = GameObject.getDistanceBetweenObjects(this, this.next_target_planet); // on calcule la distance entre la planète target et le traveller

            directionX = distanceToNextPlanetX/distanceToNextPlanet; // on calcule la direction à prendre en x
            directionY = distanceToNextPlanetY/distanceToNextPlanet; // on calcule la direction à prendre en y

            if (distanceToNextPlanet > 10){ // Si la distance entre le traveller et la planète cible est > 10
                // on fait avancer dans la direction
                this.velocityX = directionX*MAX_SPEED;
                this.velocityY = directionY*MAX_SPEED;
                this.coordX += this.velocityX;
                this.coordY += this.velocityY;
                this.isTravelling = true;

                for (int i = 0; i < list_travellers_in_spaceship.size(); i++) {
                    list_travellers_in_spaceship.get(i).moveToCoord(this.coordX, this.coordY);
                }

            }else { // Sinon, on est arrivé à la planète cible
                this.velocityX = 0;
                this.velocityY = 0;
                this.current_planet = this.next_target_planet;
                this.current_trajectory = this.current_planet.my_trajectory;
                hasReachedFirstPlanet = this.current_planet.getListOfArrPlanets().size() == 0;
                for (int i = 0; i < current_planet.getList_travellers().size(); i++) {
                    Traveller traveller = current_planet.getList_travellers().get(i);

                    if (this.list_planet.contains(traveller.getFinal_Target_planet()) && traveller.getFinal_Target_planet() == this.current_planet && this.list_travellers_in_spaceship.contains(traveller)){
                        current_planet.getList_travellers().get(i).hasArrived = true;
                        this.list_travellers_in_spaceship.remove(traveller);
                    }
                    if (this.list_travellers_in_spaceship.size() <= this.maxTravellerInSpaceShip){
                        if (this.list_planet.contains(current_planet.getList_travellers().get(i).getFinal_Target_planet()) && current_planet.getList_travellers().get(i).getInitial_planet() == this.current_planet){
                            Log.d("un traveller est rentre", String.valueOf(this.list_travellers_in_spaceship.size()));
                            this.list_travellers_in_spaceship.add(current_planet.getList_travellers().get(i));
                        }
                    }
                }
                if (this.current_planet.my_trajectory.getFamilly() == this.familly){
                    if (this.current_planet.isLinkedWithPlanet() && goUp){
                        this.next_target_planet = this.current_planet.linkedPlanet;
                    }else {
                        if (hasReachedFirstPlanet){
                            this.next_target_planet = this.current_planet.linkedPlanet;
                            goUp = true;
                        }else {
                            this.next_target_planet = this.current_planet.getListOfArrPlanets().get(0);
                            goUp = false;
                        }
                    }
                }else {
                    this.next_target_planet = this.current_planet.getListOfArrPlanets().get(counterOfArrTraj);
                    goUp = false;
                    counterOfArrTraj++;
                    if (counterOfArrTraj >= this.current_planet.getListOfArrPlanets().size()) counterOfArrTraj = 1;
                }



                //this.next_target_planet = this.listAllPlanets.get(this.path_to_take.get(counter)); // Sa planète cible est la planète suivante dans la liste des planètes du chemin à prendre
            }
        }
        spaceShipState.update();

    }
    public void draw(Canvas canvas){
        animator.drawSpaceShip(canvas, this);
    }

    public ArrayList<Trajectory> getList_planet_stations(){
        return list_trajectory;
    }
    public void addPlanet_station(Trajectory trajectory){
        this.list_trajectory.add(trajectory);
        this.list_planet.add(trajectory.getStartPlanet());
        this.list_planet.add(trajectory.getEndPlanet());
    }

    public void removePlanet_station(Trajectory trajectory) {
        this.list_trajectory.remove(trajectory);
    }

    public SpaceShipState getSpaceshipState() {
        return spaceShipState;
    }
}
