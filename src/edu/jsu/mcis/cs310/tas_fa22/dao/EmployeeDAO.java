package edu.jsu.mcis.cs310.tas_fa22.dao;

import java.sql.*;
import edu.jsu.mcis.cs310.tas_fa22.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
/**
 *
 * @author Colbylee Mincey
 */
public class EmployeeDAO {
    private static final String QUERY_FIND_ID = "SELECT * FROM employee WHERE id = ?";
    private static final String QUERY_FIND_BADGE = "SELECT * FROM employee WHERE badgeid = ?";
    
    private final DAOFactory daoFactory;
    
    public EmployeeDAO (DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public Employee find(int id) {
        Employee employee = null;
        PreparedStatement PS = null;
        ResultSet RS = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                PS = conn.prepareStatement(QUERY_FIND_ID);
                PS.setInt(1, id);
                boolean results = PS.execute();
                
                if (results) {
                    RS = PS.getResultSet();
                    
                    while(RS.next()) {
                        
                        //DAOS
                        BadgeDAO badgeDAO = new BadgeDAO(daoFactory);
                        ShiftDAO shiftDAO = new ShiftDAO(daoFactory);
                        DepartmentDAO departmentDAO = new DepartmentDAO(daoFactory);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        String firstname = RS.getString("firstname");
                        String middlename = RS.getString("middlename");
                        String lastname = RS.getString("lastname");
                        LocalDateTime active = LocalDateTime.parse(RS.getString("active"), formatter);
                        Badge badge = badgeDAO.find(RS.getString("badge"));
                        Department department = departmentDAO.find(RS.getInt("department"));
                        Shift shift = shiftDAO.find(badge);
                        EmployeeType employeeType = RS.getInt('employeetype');

                        employee = new Employee (id, firstname, middlename, lastname, active, badge, department, shift, employeeType);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }finally {
            if (RS != null) {
                try {
                    RS.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        if (PS != null){
            try {
                PS.close();
            } catch (SQLException e) {
                throw new DAOException(e.getMessage());
            }
        }
        return employee;
    }
    public Employee find(Badge badge) {
        Employee employee = null;
        PreparedStatement PS = null;
        ResultSet RS = null;

        try {
            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                PS = conn.prepareStatement(QUERY_FIND_BADGE);
                PS.setString(1, badge.getId());

                boolean results = PS.execute();

                if (results) {

                    RS = PS.getResultSet();

                    while (RS.next()) {
                        ShiftDAO shiftDAO = new ShiftDAO(daoFactory);
                        DepartmentDAO departmentDAO = new DepartmentDAO(daoFactory);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        int id = RS.getInt("id");
                        String firstname = RS.getString("firstname");
                        String middlename = RS.getString("middlename");
                        String lastname = RS.getString("lastname");
                        LocalDateTime active = LocalDateTime.parse(RS.getString("active"), formatter);

                        Department department = departmentDAO.find(RS.getInt("department"));
                        Shift shift = shiftDAO.find(RS.getInt("shift"));


                        employee = new Employee(id, firstname, middlename, lastname, active, badge, department, shift, employeeType);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            if (RS != null) {
                try {
                    RS.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (PS != null) {
                try {
                    RS.close();
                } catch (SQLException e) {
                    throw  new DAOException(e.getMessage());
                }
            }
        }
        return employee;
    }   
    
}
