package edu.jsu.mcis.cs310.tas_fa22;

 import java.time.LocalDateTime;
 import java.time.*;
 import java.sql.*;
 import java.time.LocalTime;
 import java.time.format.DateTimeFormatter;
 import java.util.HashMap;

public class Punch {
    
    private int terminalid, id, eventtypeid;
    private PunchAdjustmentType punchtypeid;
    private String badgeid, adjustmenttype;
    private LocalDateTime originaltimestamp, adjustedtimestamp;
    private Badge badge;
    
    public Punch(HashMap<String, String> params, Badge badge) {
        this.id = Integer.parseInt(params.get("id"));
        this.terminalid = Integer.parseInt(params.get("terminalid"));
        this.badgeid = params.get("badgeid");
        this.originaltimestamp = LocalDateTime.parse(params.get("timestamp"));
        this.eventtypeid = Integer.parseInt(params.get("eventtypeid"));
        this.punchtypeid = PunchAdjustmentType.values()[Integer.parseInt(params.get("eventtypeid"))];
        this.badge = badge;
        
        this.adjustedtimestamp = originaltimestamp;

    }
    
    public Punch(int terminalid, Badge badge, int eventtypeid) {
        this.terminalid = terminalid;
        this.eventtypeid = eventtypeid;
        this.badge = badge;
        java.sql.Timestamp timestamp1 = new Timestamp(new java.util.Date().getTime());
        LocalDateTime local = timestamp1.toLocalDateTime();
        local = local.withSecond(0).withNano(0);
        this.originaltimestamp = local;
        this.adjustedtimestamp = null;

    }
    

    public int getTerminalid() {
        return terminalid;
    }

    public int getId() {
        return id;
    }

    public int getEventtypeid() {
        return eventtypeid;
    }

    public PunchAdjustmentType getPunchtypeid() {
        return punchtypeid;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public String getAdjustmenttype() {
        return adjustmenttype;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }

    public LocalDateTime getAdjustedtimestamp() {
        return adjustedtimestamp;
    }

    public Badge getBadge() {
        return badge;
    }
  
    public void adjust(Shift s){
        
        
        
        
        
        
    }
    
}
