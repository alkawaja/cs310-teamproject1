package edu.jsu.mcis.cs310.tas_fa22.dao;

import edu.jsu.mcis.cs310.tas_fa22.Badge;
import edu.jsu.mcis.cs310.tas_fa22.Department;
import edu.jsu.mcis.cs310.tas_fa22.Punch;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class PunchDAO {

    private static final String QUERY_ID = "SELECT * FROM event WHERE id = ?;";
    private static final String SQL_INSERT = "INSERT INTO event (terminalid, badgeid, timestamp, eventtypeid) VALUES (?, ?, ?, ?)";
    private HashMap<String, String> map = new HashMap<>();
    private DAOFactory daofactory = null;
    private final BadgeDAO badgeDAO;

    public PunchDAO(DAOFactory daoFactory) {
        this.daofactory = daoFactory;
        this.badgeDAO = new BadgeDAO(daofactory);
    }

    public Punch find(int id) {
        Punch p = null;
        PreparedStatement PS = null;
        ResultSet RS = null;

        try {
            Connection conn = daofactory.getConnection();

            if (conn.isValid(0)) {
                PS = conn.prepareStatement(QUERY_ID);
                PS.setInt(1, id);

                boolean hasresults = PS.execute();

                if (hasresults) {
                    RS = PS.getResultSet();

                    while (RS.next()) {

                        map.put("id", RS.getString("id"));
                        map.put("terminalid", RS.getString("terminalid"));
                        map.put("badgeid", RS.getString("badgeid"));
                        map.put("timestamp", RS.getString("timestamp"));
                        map.put("eventtypeid", RS.getString("eventtypeid"));
                        Badge badge = badgeDAO.find(RS.getString("badgeid"));
                        p = new Punch(map, badge); // shift class using HashMap constructor
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
                    PS.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        return p;
    }

    public ArrayList<Punch> list(Badge b, LocalDate toLocalDate) {
        return null;
    }

    public int create(Punch p1) {
        PreparedStatement pst = null;

        try {
            Connection conn = daofactory.getConnection();
            pst = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            Badge badge = p1.getBadge();
            Department d = daofactory.getDepartmentDAO().find(badge);

            if (p1.getTerminalid() != d.getTerminalid() && p1.getTerminalid() != 0) {
                return 0;
            }

            pst.setInt(1, p1.getTerminalid());
            pst.setString(2, p1.getBadgeid());
            pst.setString(3, p1.getOriginaltimestamp().toString());
            pst.setInt(4, p1.getEventtypeid());


            int rows = pst.executeUpdate();

            if (rows == 0) {
                throw new DAOException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = pst.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }

        } catch (Exception e) {
            throw new DAOException(e.getMessage());

        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        return 0;
    }
}
