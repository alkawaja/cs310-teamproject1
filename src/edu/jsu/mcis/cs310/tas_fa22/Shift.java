package edu.jsu.mcis.cs310.tas_fa22;

/**
 *
 * @author Adam Parton
 */

import java.time.*;
import java.util.HashMap;

public class Shift {
    
    private String description;
    private int id, roundInterval, gracePeriod, dockPenalty, lunchThreshold, 
                lunchDuration, shiftDuration;
    private LocalTime shiftStart, shiftStop, lunchStart, lunchStop;
    
    
    public Shift(HashMap<String, String > map ) {
        this.id = Integer.parseInt(map.get("id"));
        this.description = map.get("description");
        this.shiftStart = LocalTime.parse(map.get("shiftstart"));
        this.shiftStop = LocalTime.parse(map.get("shiftstop"));
        this.roundInterval = Integer.parseInt(map.get("roundinterval"));
        this.gracePeriod = Integer.parseInt(map.get("graceperiod"));
        this.dockPenalty = Integer.parseInt(map.get("dockpenalty"));
        this.lunchStart = LocalTime.parse(map.get("lunchstart"));
        this.lunchStop = LocalTime.parse(map.get("lunchstop"));
        this.lunchThreshold = Integer.parseInt(map.get("lunchthreshold"));    
        this.lunchDuration = (int)Duration.between(this.lunchStart, this.lunchStop).toMinutes();
        this.shiftDuration = (int)Duration.between(this.shiftStart, this.shiftStop).toMinutes();
         
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getRoundInterval() {
        return roundInterval;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public int getDockPenalty() {
        return dockPenalty;
    }

    public int getLunchThreshold() {
        return lunchThreshold;
    }

    public int getLunchDuration() {
        return lunchDuration;
    }

    public LocalTime getShiftStart() {
        return shiftStart;
    }

    public LocalTime getShiftStop() {
        return shiftStop;
    }

    public LocalTime getLunchStart() {
        return lunchStart;
    }

    public LocalTime getLunchStop() {
        return lunchStop;
    }

    public int getShiftDuration() {
        return shiftDuration;
    }

    
        @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
         
        sb.append(description).append(": ").append(shiftStart).append(" - ");
        sb.append(shiftStop).append(" (").append(shiftDuration).append(" minutes)").append("; Lunch: ");
        sb.append(lunchStart).append(" - ").append(lunchStop).append(" (").append(lunchDuration).append(" minutes)");

        return sb.toString();
    }

}