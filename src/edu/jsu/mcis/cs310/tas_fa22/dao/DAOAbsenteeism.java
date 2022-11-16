package edu.jsu.mcis.cs310.tas_fa22.dao;
import edu.jsu.mcis.cs310.tas_fa22.Department;
import java.sql.*;

public class DAOAbsenteeism {
    
     private static final String QUERY = "SELECT * FROM Absenteeism WHERE id = ?";
     
     private final DAOFactory daofactory;

    public DAOAbsenteeism(DAOFactory daofactory) {
        this.daofactory = daofactory;
    }
     
    public Absenteeism find(int id){
         Absenteeism absenteeism = null;
    }
     
}
