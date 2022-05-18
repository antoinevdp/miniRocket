package game.example.testminirocket.GameObjects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.minirocket.game.R;

import java.util.ArrayList;
import java.util.Random;

import game.example.testminirocket.BFS;
import game.example.testminirocket.GamePanels.GameOver;
import game.example.testminirocket.GamePanels.InfosDisplay;
import game.example.testminirocket.graphics.Animator;
import game.example.testminirocket.graphics.SpriteSheet;


/*
This class manages everything that deals with the game
Responsible of rendering, updating, etc...

 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private static final int SCORE_PER_ARRIVED_TRAVELLER = 50;
    private GameLoop gameLoop;

    private boolean canDragLine = false; // peut on tracer la ligne en maintenant le doigt
    private boolean canLeaveLine = false; // peut on relâcher la ligne pour a créée
    private boolean overlapping = false; // Boolean permettant de savoir si la planète que l'on veut faire spawn touche une autre
    private boolean isGameOver;

    private int score;
    private static int SCORE_TO_UNLOCK = 1000;
    public boolean NEXT_LEVEL_IS_UNLOCKABLE = false;
    private int current_familly_color = 1;

    private Planet currentStartPlanet; // Planète où commence la ligne
    private Planet currentStopPlanet; // Planète où finie la ligne

    public ArrayList<Planet> list_planets = new ArrayList<Planet>(); // liste des planète du niveau
    public ArrayList<Trajectory> list_trajectories = new ArrayList<Trajectory>(); // liste des Trajets entre planètes
    public ArrayList<Traveller> list_travellers = new ArrayList<Traveller>(); // liste des Trajets entre planètes
    public ArrayList<Asteroid> list_asteroids = new ArrayList<Asteroid>(); // liste des Trajets entre planètes
    public ArrayList<SpaceShip> list_spaceShips = new ArrayList<>(); // liste des Trajets entre planètes


    public ArrayList<ArrayList<Integer>> list_connections = new ArrayList<>();
    public ArrayList<Integer> list_traj_drawn = new ArrayList<>();

    private InfosDisplay infosDisplay;
    private GameOver gameOver;

    int remainingTrajCounter;

    private boolean spaceshipHasSpawn;

    private int[] androidColors = getResources().getIntArray(R.array.planet_colors);
    private int[] spriteList = new int[]{
            R.drawable.ea98b1_planet,
            R.drawable.fffdf1_planet,
            R.drawable.n813923_planet,
            R.drawable.test_planet,
            R.drawable.n936292805_planet,
            R.drawable.n936292805,
            R.drawable.n2098505015
    };

    private int total_number_planets = 0;


    private int trajectoryIndex; // index dans la liste des trajectoires de la trajectoire actuelle

    @SuppressLint("ClickableViewAccessibility")
    public Game(Context context, int numberOfPlanets, double facteurDeDistance) { // Constructor
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        this.total_number_planets = numberOfPlanets;

        gameLoop = new GameLoop(this, surfaceHolder); // loop du jeu
        infosDisplay = new InfosDisplay(context, 2000, 50);

        //Init GameOver
        gameOver = new GameOver(context);

        //initialisationd es planètes et ajout dans la liste des planètes
        generatePlanets(numberOfPlanets, facteurDeDistance);

        // initialisation des trajectoires et ajout dans la liste des trajectoires
        this.remainingTrajCounter = list_trajectories.size();
        this.score = 0;

        setFocusable(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override // Touch Event Handler
    public boolean onTouchEvent(MotionEvent event) {
        //Touch events
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: // Dès que l'utilisateur appui
                startLine(event.getX(), event.getY()); // On crée ou non la ligne en récupérant les coord de l'endroit cliqué
                return true; // Action terminée

            case MotionEvent.ACTION_MOVE: // Tant que l'utilisateur reste appuyé
                if(canDragLine) draggingLine(event.getX(), event.getY()); // on trace la ligne en fonction de l'endroit où l'ulisateur se déplace
                return true; // Action terminée

            case MotionEvent.ACTION_UP: // Dès que l'utilisateur relâche
                stopLine(); // on arrête la ligne sur une planète ou alors on l'efface

                return true; // Action terminée

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.d("Game.java", "surfaceDestroyed()");
    }

    @Override // Affichage des elements à l'écran
    public void draw(Canvas canvas){
        super.draw(canvas);
        drawFPS(canvas);

        // on affiche toutes les planètes et trajectoires
        if (list_planets.size()>0){
            for (int i = 0; i < list_planets.size(); i++) {
                list_planets.get(i).draw(canvas);
            }
        }
        if (list_trajectories.size()>0){
            for (int i = 0; i < list_trajectories.size(); i++) {
                list_trajectories.get(i).draw(canvas);
            }
        }
        if (list_travellers.size()>0){
            for (int i = 0; i < list_travellers.size(); i++) {
                list_travellers.get(i).draw(canvas);
            }
        }
        if (list_asteroids.size()>0){
            for (int i = 0; i < list_asteroids.size(); i++) {
                list_asteroids.get(i).draw(canvas);
            }
        }

        infosDisplay.draw(canvas);

        //Draw Game Over
        if (isGameOver){
            gameOver.draw(canvas);
        }
        for (int i = 0; i < list_spaceShips.size(); i++) {
            list_spaceShips.get(i).draw(canvas);
        }

    }
    public void update() {
        // Update
        // Stop updating if gameover
        /*if (isGameOver){
            return;
        }*/

        //Spawn Traveller if its time to Spawn
        if (Traveller.readyToSpawn()){
            Random rand = new Random(); //instance of random class
            int spawn_planet_random = rand.nextInt(list_planets.size());
            int target_planet_random = rand.nextInt(list_planets.size());
            while (spawn_planet_random == target_planet_random){
                target_planet_random = rand.nextInt(list_planets.size());
            }

            Traveller traveller_test = new Traveller(getContext(), 0,0, 30, list_planets.get(spawn_planet_random), list_planets.get(target_planet_random), list_planets);
            list_travellers.add(traveller_test);

            list_planets.get(target_planet_random).addTraveller(traveller_test);
        }
        if (Asteroid.readyToSpawn() && remainingTrajCounter <= 3){
            Random generator = new Random();
            int randomIndex = generator.nextInt(list_traj_drawn.size());
            int rand_trajectory = list_traj_drawn.get(randomIndex);
            Log.d("rand_traj", String.valueOf(rand_trajectory));
            SpriteSheet spriteSheet = new SpriteSheet(getContext());
            Animator animator = new Animator(spriteSheet.getAsteroidSpriteArray());
            Asteroid asteroid = new Asteroid(getContext(), 150, 150, list_trajectories.get(rand_trajectory), 1, animator);
            list_asteroids.add(asteroid);
        }

        //update
        int t_remaining = list_trajectories.size();
        for (int i = 0; i < list_planets.size(); i++) {
            list_planets.get(i).update();
            if (list_planets.get(i).isLinkedWithPlanet()) t_remaining--;
        }
        this.remainingTrajCounter = t_remaining;
        for (int i = 0; i < list_trajectories.size(); i++) {
            list_trajectories.get(i).update();
        }
        for (int i = 0; i < list_travellers.size(); i++) {
            if(list_travellers.get(i).hasArrived){
                list_travellers.remove(list_travellers.get(i));
                this.score += SCORE_PER_ARRIVED_TRAVELLER;
                if (this.score >= SCORE_TO_UNLOCK) NEXT_LEVEL_IS_UNLOCKABLE=true;
                infosDisplay.setScore(this.score);
            }
            else if(list_travellers.get(i).canBeDestroyed){
                list_travellers.remove(list_travellers.get(i));
                isGameOver = true;
            }
            else{
                list_travellers.get(i).update();
            }
        }
        for (int i = 0; i < list_asteroids.size(); i++) {
            list_asteroids.get(i).update();
            if (list_asteroids.get(i).hasArrived){
                trajectoryIndex = list_trajectories.indexOf(list_asteroids.get(i).getTargetTrajectory());
                currentStartPlanet = list_trajectories.get(trajectoryIndex).getStartPlanet();
                removeTrajectory();
                list_asteroids.remove(list_asteroids.get(i));
            }
        }
        for (int i = 0; i < list_spaceShips.size(); i++) {
            list_spaceShips.get(i).update();
        }

        infosDisplay.setNb_trajectories(remainingTrajCounter);
    }

    // Si l'utilisateur touche une planète
    public boolean hasTouchedPlanet(float cursorX, float cursorY, Planet t_planet){
        return ((t_planet != currentStartPlanet) &&
                (cursorX >= t_planet.getPositionX() - t_planet.radius) &&
                (cursorX <= (float) t_planet.getPositionX() + t_planet.radius)) &&
                ((cursorY >= t_planet.getPositionY() - t_planet.radius) &&
                        (cursorY <= t_planet.getPositionY() + t_planet.radius));
    }
    // Trace la ligne
    public void startLine(float cursorX, float cursorY){
        for (int i = 0; i < list_planets.size(); i++) { // on parcourt la liste des planètes
            Planet t_planet = list_planets.get(i); // La planète actuelle
            if(hasTouchedPlanet(cursorX, cursorY, t_planet)){ // Si la planète actuelle est touché
                currentStartPlanet = t_planet; // on assigne la planète touchée à la variable qui stocke la planète de départ
                trajectoryIndex = list_planets.indexOf(currentStartPlanet); // on assigne son index à celui de la trajectoire actuelle
                Trajectory current_trajectory = list_trajectories.get(i);
                current_trajectory.setFamilly(current_familly_color);
                current_trajectory.resetToCursor(cursorX,cursorY); // On place le départ de la ligne sur la position de la planète
                current_trajectory.setStartPosition(t_planet.getPositionX(), t_planet.getPositionY()); // On positionne le début de la trajectoire à la planète de départ
                if (currentStartPlanet.linkedPlanet != null){ // Si la planète associée à la planète de départ existe
                    currentStartPlanet.linkedPlanet.getListOfArrPlanets().remove(currentStartPlanet); // On retire la planète associée
                }


                canDragLine = true; // On peut créer une ligne depuis la planète de départ
                break; // Comme nous avons trouve la planète touchée, on arrête la boucle for
            }
            else{ // Si l'utilisateur n'appui pas sur une planète, on reset toutes les variables
                resetVariables();
            }

        }
    }
    public void draggingLine(float cursorX, float cursorY){ // Quand le joueur maintient
        if (trajectoryIndex != -1){
            list_trajectories.get(trajectoryIndex).setEndPosition(cursorX, cursorY); // On récupère la trajectoire actuelle
            for (int i = 0; i < list_planets.size(); i++) { // on parcourt la liste des planètes
                Planet t_planet = list_planets.get(i); // on recupere la planète actuelle
                if(hasTouchedPlanet(cursorX, cursorY, t_planet)){ // Si l'utilisateur passe sur une planète
                    canLeaveLine = true; // On peut relacher la ligne
                    currentStopPlanet = t_planet; // la planète d'arrivée est la planète actuelle
                    // On set la fin du segment à l'emplacement du doigt
                    if (currentStopPlanet.isLinkedWithPlanet() && currentStopPlanet.getListOfArrPlanets().size() > 0 && currentStopPlanet.my_trajectory.getFamilly() == current_familly_color){
                        canLeaveLine = false;
                        return;
                    }else if (currentStopPlanet.getListOfArrPlanets().size() > 0 && !currentStopPlanet.isLinkedWithPlanet() && currentStopPlanet.my_trajectory.getFamilly() == current_familly_color){
                        canLeaveLine = false;
                        return;
                    }
                    list_trajectories.get(trajectoryIndex).setEndPosition(currentStopPlanet.getPositionX(), currentStopPlanet.getPositionY());

                    break; // On a trouve la planète donc on quitte la boucle
                }
                else{ // Si l'utilisateur ne passe pas au dessus d'une planète
                    canLeaveLine = false; // On ne peut pas relâcher (en créant une ligne)
                }
            }
            invalidate(); // on call draw pour actualiser
        }

    }
    public void stopLine(){ // Quand on relâche le doigt
        if(trajectoryIndex != -1){
            // Si on peut relâcher sur une planète
            if(canLeaveLine){

                if (currentStopPlanet.isLinkedWithPlanet() && currentStopPlanet.getListOfArrPlanets().size() != 0 && (currentStopPlanet.my_trajectory.getFamilly() == current_familly_color)){
                    currentStartPlanet.my_trajectory.setFamilly(2);
                    spaceshipHasSpawn = false;
                }
                // On set la fin du segment à l'emplacement de la deuxieme planète
                list_trajectories.get(trajectoryIndex).setEndPosition(currentStopPlanet.getPositionX(), currentStopPlanet.getPositionY());
                currentStartPlanet.setLinkedPlanet(currentStopPlanet); // On link la planète d'arr à la planète de départ
                list_trajectories.get(trajectoryIndex).setStartPlanet(currentStartPlanet);
                list_trajectories.get(trajectoryIndex).setEndPlanet(currentStopPlanet);
                currentStartPlanet.setMyTrajectory(list_trajectories.get(trajectoryIndex)); // on link la trajectoire à la planète de départ
                currentStopPlanet.getListOfArrPlanets().add(currentStartPlanet);
                Log.d("nombre de planètes rattachées ", String.valueOf(currentStopPlanet.getListOfArrPlanets().size()));
                Log.d("id de la trajectoire : ", String.valueOf(currentStartPlanet.my_trajectory.getId()));
                Log.d("planete start : ", String.valueOf(list_trajectories.get(trajectoryIndex).getStartPlanet().id));
                Log.d("planete stop : ", String.valueOf(list_trajectories.get(trajectoryIndex).getEndPlanet().id));
                ArrayList<Integer> list_to_push = new ArrayList<>();
                list_to_push.add(currentStartPlanet.id);
                list_to_push.add(currentStopPlanet.id);
                list_connections.set(trajectoryIndex, list_to_push);
                list_traj_drawn.add(trajectoryIndex);
                System.out.println(list_connections);
                if (!spaceshipHasSpawn){
                    SpriteSheet spriteSheet = new SpriteSheet(getContext());
                    Animator animator = new Animator(spriteSheet.getRocketSpriteArray());
                    SpaceShip spaceShip = new SpaceShip(currentStartPlanet.getPositionX()+50, currentStartPlanet.getPositionY()+50, currentStartPlanet, currentStartPlanet.my_trajectory.getFamilly(), animator);
                    list_spaceShips.add(spaceShip);
                    spaceshipHasSpawn = true;
                }
                for (int i = 0; i < list_spaceShips.size(); i++) {
                    SpaceShip current_spaceShip = list_spaceShips.get(i);
                    if (!current_spaceShip.getList_planet_stations().contains(list_trajectories.get(trajectoryIndex))){
                        current_spaceShip.addPlanet_station(list_trajectories.get(trajectoryIndex));
                    }
                }

                // As soon as path added, calculate traveller's path from this planet

            }
            else { // Si on relâche dans la vide
                removeTrajectory();
            }
            //on reset toutes les variables
            resetVariables();
        }

    }

    public void removeTrajectory(){
        for (int i = 0; i < list_spaceShips.size(); i++) {
            SpaceShip current_spaceShip = list_spaceShips.get(i);
            if (current_spaceShip.current_trajectory == list_trajectories.get(trajectoryIndex)){
                list_spaceShips.remove(current_spaceShip);
                isGameOver = true;
                break;
            }
        }
        list_trajectories.get(trajectoryIndex).reset();//on efface la ligne
        currentStartPlanet.unsetLinkedPlanet();// On retire la planète linked
        currentStartPlanet.unsetMyTrajectory();// on retire la trajectoire linked
        ArrayList<Integer> list_to_push = new ArrayList<>();
        list_to_push.add(0);
        list_to_push.add(0);
        list_connections.set(trajectoryIndex, list_to_push);
        list_traj_drawn.remove((Integer) trajectoryIndex);
        System.out.println(list_connections);
    }

    public void generatePlanets(int numberOfPlanets, double facteurDeDistance){
        // Coordonnées et rayon min et max
        int coordMinX = 100;
        int coordMinY = 100;
        int coordMaxX = 1800;
        int coordMaxY = 1000;
        int minRadius = 50;
        int maxRadius = 100;
        // Protection si des planètes n'arrive pas à spawn
        int protection = 0;

        // Tant que toute les planètes n'ont pas spawn
        while (list_planets.size() < numberOfPlanets) {
            protection++; // incrementation de la protection

            // Coord et rayon aleatoires de spawn
            int coordX_rand = (int)Math.floor(Math.random()*(coordMaxX-coordMinX+1)+coordMinX);
            int coordY_rand = (int)Math.floor(Math.random()*(coordMaxY-coordMinY+1)+coordMinY);
            int radius_rand = (int)Math.floor(Math.random()*(maxRadius-minRadius+1)+minRadius);

            int id = 0;
            if(list_planets.size()!=0){ // Si la liste contient deja des planètes
                for (int j = 0; j < list_planets.size(); j++) { // on parcourt l'ensemble des planètes déjà présente
                    Planet previousPlanet = list_planets.get(j); // planète actuelle
                    // Calcul de la distance entre les planètes:
                    // on recupere le max et min de chaque coord
                    int maxBtwCoordX = (int)Math.max(coordX_rand, previousPlanet.getPositionX());
                    int maxBtwCoordY = (int)Math.max(coordY_rand, previousPlanet.getPositionY());
                    int minBtwCoordX = (int)Math.min(coordX_rand, previousPlanet.getPositionX());
                    int minBtwCoordY = (int)Math.min(coordY_rand, previousPlanet.getPositionY());
                    // Pyhtagore pour déterminer la distance entre les 2 planètes
                    int distance = (int)Math.sqrt(Math.pow(maxBtwCoordX - minBtwCoordX, 2) + Math.pow(maxBtwCoordY - minBtwCoordY, 2));
                    // Si la distance est inferieur à la somme des rayons, les planètes se touchent
                    if (distance < radius_rand*facteurDeDistance + previousPlanet.radius*facteurDeDistance){
                        overlapping = true;
                        break; // on s'arrête car cette planète ne peut pas spawn
                    }
                    id++;
                }
            }

            if (!overlapping){ // Si la planète est spawnable
                // Instanciation de la planète
                int index = new Random().nextInt(androidColors.length);
                int randomAndroidColor = androidColors[index];
                Trajectory t_trajectory = new Trajectory(list_planets.size(), 0,0,0,0, null, null, 1);
                list_trajectories.add(t_trajectory);
                ArrayList<Integer> t_list = new ArrayList<>();
                t_list.add(0);
                t_list.add(0);
                list_connections.add(t_list);

                SpriteSheet spriteSheet_planet = new SpriteSheet(getContext());
                Animator animator = new Animator(spriteSheet_planet.getPlanetSpriteArray(index, spriteList));
                Planet planet = new Planet(getContext(), id, coordX_rand, coordY_rand, radius_rand, randomAndroidColor, "", t_trajectory, list_travellers, animator);
                list_planets.add(planet);
                androidColors = removeTheElement(androidColors, index);
                spriteList = removeTheElement(spriteList, index);


            }
            overlapping = false; // on reset
            if (protection > 2000)break;// Si il y a eu plus de 2000 itérations, on arrête
        }
    }

    public void resetVariables(){
        // Reset toutes les variables de mouvements ligne et planète
        canDragLine = false;
        canLeaveLine = false;
        currentStartPlanet = null;
        currentStopPlanet = null;
        trajectoryIndex = -1;
    }

    public static int[] removeTheElement(int[] arr, int index)
    {

        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null || index < 0
                || index >= arr.length) {

            return arr;
        }

        // Create another array of size one less
        int[] anotherArray = new int[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {

            // if the index is
            // the removal element index
            if (i == index) {
                continue;
            }

            // if the index is not
            // the removal element index
            anotherArray[k++] = arr[i];
        }

        // return the resultant array
        return anotherArray;
    }

    public void drawFPS(Canvas canvas){ // FPS
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.purple_200);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS : " + averageFPS, 100, 100, paint);
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
