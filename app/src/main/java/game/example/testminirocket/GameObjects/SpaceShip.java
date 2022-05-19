package game.example.testminirocket.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

import game.example.testminirocket.graphics.Animator;

public class SpaceShip extends GameObject{
    private static final double SPEED_PIXELS_PER_SECOND = 100;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWN_PER_MINUTE = 20;
    private static final double SPAWN_PER_SECOND = SPAWN_PER_MINUTE / 60.0;
    private static final double UPDATE_PER_SPAWN = GameLoop.MAX_UPS / SPAWN_PER_SECOND;
    private static double updateUntilNextSpawn = UPDATE_PER_SPAWN;
    private static double WAITING_TIME = 3.0;
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

    public Trajectory current_trajectory;

    private SpaceShipState spaceShipState;
    private Animator animator;

    private int familly;
    private int counterOfArrTraj = 1;
    private int maxTravellerInSpaceShip;
    private double waiting;

    public boolean hasBoucled;
    private boolean hasReachedFirstPlanet;
    private boolean followTraj;
    private boolean followArr;

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

        this.waiting = 0;

        this.followTraj = true;
        this.followArr = false;

        this.familly = familly;
        this.canAnimate = true;
        this.maxTravellerInSpaceShip = 4;

        this.spaceShipState = new SpaceShipState(this);
        this.animator = animator;
        Log.d("spaceship familly =============================", String.valueOf(this.familly));

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

                /*for (int i = 0; i < list_travellers_in_spaceship.size(); i++) {
                    list_travellers_in_spaceship.get(i).moveToCoord(this.coordX, this.coordY);
                }*/

            }else { // Sinon, on est arrivé à la planète cible
                this.velocityX = 0;
                this.velocityY = 0;
                this.current_planet = this.next_target_planet;
                this.current_trajectory = this.current_planet.my_trajectory;

                // Deposer ceux qui sont dans le vaisseau
                for (int i = 0; i < list_travellers_in_spaceship.size(); i++) {
                    Traveller traveller = list_travellers_in_spaceship.get(i);
                    if (this.current_planet == traveller.getFinal_Target_planet()){
                        traveller.hasArrived = true;
                        this.list_travellers_in_spaceship.remove(traveller);
                        Log.d("::::::::::::::::::::::::::remove::::::::::::::::::::::::::::::::::::::", "");
                        Log.d("::::::::::        :::::::::::::   ", String.valueOf(this.list_travellers_in_spaceship));
                    }else{
                        for (int j = 0; j < this.current_planet.getListOfArrPlanets().size(); j++) {
                            if(this.current_planet.getListOfArrPlanets().get(j).my_trajectory.getFamilly() != this.familly && this.current_planet.getListOfArrPlanets().get(j).linkedPlanet == this.current_planet){
                                Log.d("::::::::::::::::::::::::::drop::::::::::::::::::::::::::::::::::::::", "");
                                Log.d("::::::::::        :::::::::::::   ", String.valueOf(this.list_travellers_in_spaceship));
                                traveller.setInitial_planet(this.current_planet);
                                this.current_planet.addTraveller(traveller);
                                this.list_travellers_in_spaceship.remove(traveller);
                            }
                        }
                    }
                }

                if (this.current_planet.getList_travellers().size() > 0){
                    if (this.list_travellers_in_spaceship.size() <= this.maxTravellerInSpaceShip){
                        for (int i = 0; i < current_planet.getList_travellers().size(); i++) {
                            Traveller traveller = current_planet.getList_travellers().get(i);
                            if (this.list_planet.contains(traveller.getFinal_Target_planet()) && this.current_planet == traveller.getInitial_planet()){
                                this.list_travellers_in_spaceship.add(traveller);
                                this.current_planet.removeTraveller(traveller);
                            }else {
                                Log.d("::::::::::::::::::::::::::cant take::::::::::::::::::::::::::::::::::::::", "");
                            }
                        }
                    }
                }




                if (this.current_planet.isEndingPlanetTraj()){
                    this.followTraj = true;
                    this.followArr = false;
                }else if (this.current_planet.isEndingPlanetArr() || this.current_planet.my_trajectory.getFamilly() != this.familly){
                    this.followTraj = false;
                    this.followArr = true;
                }

                if (followTraj){
                    this.next_target_planet = this.current_planet.linkedPlanet;

                }else if (followArr){
                    if (this.current_planet.my_trajectory.getFamilly() != this.familly){
                        this.next_target_planet = this.current_planet.getListOfArrPlanets().get(1);
                    }else {
                        this.next_target_planet = this.current_planet.getListOfArrPlanets().get(0);
                    }
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
