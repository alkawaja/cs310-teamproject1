package edu.jsu.mcis.cs310.tas_fa22;

/**
 *
 * @author Adam Parton
 */
public class Department {
    
    private int id;
    private int terminalid;
    private String description;

    public Department(int id, int terminalid, String description) {
        this.id = id;
        this.terminalid = terminalid;
        this.description = description;
    }
    
    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Department{" + "ID: " + id + description + ", Terminal ID: " + terminalid + '}';
    }
    
}
