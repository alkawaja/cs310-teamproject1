package edu.jsu.mcis.cs310.tas_fa22;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Punch {

    private final Integer id, terminalid;
    private final Badge badge;
    private final EventType punchtype;
    private final LocalDateTime originaltimestamp;
    private LocalDateTime adjustedtimestamp;
    private PunchAdjustmentType adjustmentType;

    
    public Punch(int terminalid, Badge badge, EventType punchtype) {
        this.id = null;
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = punchtype;
        this.originaltimestamp = LocalDateTime.now();
    }

    public Punch(int id, int terminalid, Badge badge, LocalDateTime originaltimestamp, EventType punchtype) {
        this.id = id;
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = punchtype;
        this.originaltimestamp = originaltimestamp;
    }

    public void adjust(Shift s) {
        //initialize variables in scope;
        LocalTime originalLocalTime;
        LocalTime Grace;
        LocalTime IntervalBeforeShift;
        LocalTime shiftStart;
        LocalTime DockAfter;
        LocalTime lunchStart;
        LocalTime lunchEnd;
        LocalTime DockNearEnd;
        LocalTime GraceNearEnd;
        LocalTime ShiftEnd;
        LocalTime IntervalAfterShift;
        
        this.adjustedtimestamp = this.getOriginaltimestamp();
        
        int WeekDay;
        boolean Weekend;
        
        WeekDay = this.getOriginaltimestamp().getDayOfWeek().getValue();
        originalLocalTime = this.getOriginaltimestamp().toLocalTime();
        Weekend = (WeekDay == 6 || WeekDay == 7);
        
        
        IntervalBeforeShift = s.getShiftStart().minus(s.getRoundInterval(), ChronoUnit.MINUTES);
        shiftStart = s.getShiftStart();
        Grace = s.getShiftStart().plus(s.getGracePeriod(), ChronoUnit.MINUTES);
        DockAfter = s.getShiftStart().plus(s.getDockPenalty(), ChronoUnit.MINUTES);

        lunchStart = s.getLunchStart();
        lunchEnd = s.getLunchStop();

        DockNearEnd = s.getShiftStop().minus(s.getDockPenalty(), ChronoUnit.MINUTES);
        GraceNearEnd = s.getShiftStop().minus(s.getGracePeriod(), ChronoUnit.MINUTES);
        ShiftEnd = s.getShiftStop();
        IntervalAfterShift = s.getShiftStop().plus(s.getRoundInterval(), ChronoUnit.MINUTES);

        boolean InInterval = false;
        boolean InGrace = false;
        boolean InDock = false;
        boolean InLunch = false; 
        
        boolean OutInterval = false;
        boolean OutGrace = false;
        boolean OutDock = false;
        boolean OutLunch = false; 
        
        if (EventType.CLOCK_IN == this.getPunchtype()) {
            if (originalLocalTime.isAfter(IntervalBeforeShift.minusSeconds(1))) {
                if (originalLocalTime.isBefore(shiftStart)) {
                    InInterval = true;
                }
            }
            if (originalLocalTime.isAfter(shiftStart)) {
                if (originalLocalTime.isBefore(Grace)) {
                    InGrace = true;
                }
            }
            if (originalLocalTime.isAfter(Grace)) {
                if (originalLocalTime.isBefore(DockAfter.plusSeconds(1))) {
                    InDock = true;
                }
            }
            if (originalLocalTime.isAfter(lunchStart)) {
                if (originalLocalTime.isBefore(lunchEnd)) {
                    InLunch = true;
                }
            }
        }

        if (EventType.CLOCK_OUT == this.getPunchtype()) {
            if (originalLocalTime.isAfter(lunchStart)) {
                if (originalLocalTime.isBefore(lunchEnd)) {
                    OutLunch = true;
                }
            }
            if (originalLocalTime.isAfter(DockNearEnd.minusSeconds(1))) {
                if (originalLocalTime.isBefore(GraceNearEnd)) {
                    OutDock = true;
                }
            }
            if (originalLocalTime.isAfter(GraceNearEnd)) {
                if (originalLocalTime.isBefore(ShiftEnd)) {
                    OutGrace = true;
                }
            }
            if (originalLocalTime.isAfter(ShiftEnd)) {
                if (originalLocalTime.isBefore(IntervalAfterShift.plusSeconds(1))) {
                    OutInterval = true;
                }
            }

        }


        if (!Weekend) {
            if (InInterval || InGrace) {
                this.adjustedtimestamp = this.getOriginaltimestamp().with(shiftStart);
                this.adjustmentType = PunchAdjustmentType.SHIFT_START;
                return;
            } else if (InDock) {
                this.adjustedtimestamp = this.originaltimestamp.with(DockAfter);
                this.adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                return;
            } else if (OutLunch) {
                this.adjustedtimestamp = this.getOriginaltimestamp().with(lunchStart);
                this.adjustmentType = PunchAdjustmentType.LUNCH_START;
                return;
            } else if (InLunch) {
                this.adjustedtimestamp = this.getOriginaltimestamp().with(lunchEnd);
                this.adjustmentType = PunchAdjustmentType.LUNCH_STOP;
                return;
            } else if (OutDock) {
                this.adjustedtimestamp = this.originaltimestamp.with(DockNearEnd);
                this.adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                return;
            } else if (OutGrace || OutInterval) {
                this.adjustedtimestamp = this.getOriginaltimestamp().with(ShiftEnd);
                this.adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                return;
            }
        } 
        
        int Seconds;
        int Minutes;
        
        Seconds = this.getOriginaltimestamp().getSecond();
        Minutes = (Seconds >= 30) ? this.getOriginaltimestamp().plusMinutes(1).getMinute() : this.getOriginaltimestamp().getMinute();

        if (Minutes % 15 == 0) {
            this.adjustedtimestamp = this.getOriginaltimestamp().truncatedTo(ChronoUnit.MINUTES);
            this.adjustmentType = PunchAdjustmentType.NONE;
            return;
        } else if (Minutes % 15 < 8) {
            this.adjustedtimestamp = this.getOriginaltimestamp().truncatedTo(ChronoUnit.MINUTES).minusMinutes(Minutes % 15);
        } else if (Minutes % 15 >= 8) {
            this.adjustedtimestamp = this.getOriginaltimestamp().truncatedTo(ChronoUnit.MINUTES).plusMinutes(16 - (Minutes % 15));
        }
        this.adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
    }

    public String printOriginal() {
        String original;
        
        original = String.format("#%s %s: %s", getBadge().getId(), getPunchtype().toString(),
                getOriginaltimestamp().format(DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss")).toUpperCase());
        return original;
    }

    public String printAdjusted() {
        String adjusted;
        
        adjusted = String.format("#%s %s: %s (%s)", getBadge().getId(), getPunchtype().toString(),
                getAdjustedtimestamp().format(DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss")).toUpperCase(), getAdjustmentType());
        return adjusted;
    }

    public Integer getId() {
        return id;
    }

    public Badge getBadge() {
        return badge;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }

    public LocalDateTime getAdjustedtimestamp() {
        return adjustedtimestamp;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public EventType getPunchtype() {
        return punchtype;
    }

    public PunchAdjustmentType getAdjustmentType() {
        return adjustmentType;
    }
}