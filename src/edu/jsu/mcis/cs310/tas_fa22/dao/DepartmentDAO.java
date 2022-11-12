package edu.jsu.mcis.cs310.tas_fa22.dao;
import edu.jsu.mcis.cs310.tas_fa22.Department;
import java.sql.*;
/**
 *
 * @author Adam Parton
 */
public class DepartmentDAO {
    
    private static final String QUERY = "SELECT * FROM department WHERE id = ?";
    
    private final DAOFactory daofactory;
    
    DepartmentDAO(DAOFactory daofactory ) {
        this.daofactory = daofactory;
    }
    
    public Department find(int id){
        Department department = null;
        PreparedStatement PS = null;
        ResultSet RS = null;
        
        int anId;
        String aDescription;
        int aTerminalId;
        
        try{
            Connection conn = daofactory.getConnection();
            
            if(conn.isValid(0)){
                PS = conn.prepareStatement(QUERY);
                PS.setInt(1, id);
                
                boolean hasresults = PS.execute();
                
                    if (hasresults) {
                        RS = PS.getResultSet();
                    
                        
                        while (RS.next()) {                            
                            anId =  RS.getInt("id");
                            aTerminalId = RS.getInt("terminalid");
                            aDescription = RS.getString("description");                            
                            department = new Department(anId, aTerminalId, aDescription);
                        }
                    }
                }
            }
            catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
            finally {
                        if (RS != null) {
                            try {
                                RS.close();
                            } catch (SQLException e) {
                                throw new DAOException(e.getMessage());
                            }

                        }
                        if (PS != null) {
                            try {
                                PS.close();
                            } catch (SQLException e) {
                                throw new DAOException(e.getMessage());
                            }

                        }
            }
        return department;
    }
    
    
}
