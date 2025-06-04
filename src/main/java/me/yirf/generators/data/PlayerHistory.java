package me.yirf.generators.data;

public class PlayerHistory {

    private Integer DROPS_SOLD;
    private Double DROPS_EARNINGS;
    private Integer GENS_PLACED;
    private Integer GENS_BROKEN;

    public PlayerHistory() {
        this.DROPS_SOLD = 0;
        this.DROPS_EARNINGS = 0D;
        this.GENS_PLACED = 0;
        this.GENS_BROKEN = 0;
    }

    public int getTotalSold() {
        return DROPS_SOLD;
    }

    public double getDropsEarnings() {
        return DROPS_EARNINGS;
    }

    public int getGensPlaced() {
        return GENS_PLACED;
    }

    public int getGensBroken() {
        return GENS_BROKEN;
    }

    public void increaseTotal(int amount) {
        this.DROPS_SOLD += amount;
    }

    public void increaseDropEarnings(double amount) {
        this.DROPS_EARNINGS += amount;
    }

    public void increaseGensPlaced(int amount) {
        this.GENS_PLACED += amount;
    }

    public void increaseGensBroken(int amount) {
        this.GENS_BROKEN += amount;
    }
}
