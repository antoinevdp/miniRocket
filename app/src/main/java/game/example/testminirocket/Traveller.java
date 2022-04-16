package game.example.testminirocket;

public class Traveller {

    private Planet spawning_planet;
    private Planet target_planet;
    private double max_time;
    private double time_remaining;

    public Traveller(Planet spawning_planet, Planet target_planet, double max_time){
        this.spawning_planet = spawning_planet;
        this.target_planet = target_planet;
        this.max_time = max_time;
    }
}
