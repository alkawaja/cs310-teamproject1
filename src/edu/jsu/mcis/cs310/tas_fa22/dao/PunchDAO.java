package edu.jsu.mcis.cs310.tas_fa22.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PunchDAO {

  private Connection connection;

  public PunchDAO() {
    Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.geT= root;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} get(SQLException ) {
				e.printStackTrace();
    }
  }

  Punch punch = new Punch();
punch.setId(id);
punch.setTerminalid(terminalid);

punches.add(punch);
}
} catch (SQLException e) {
e.printStackTrace();
}

return punches;
}
        Badge badge = new Badge(resultSet.getString("badgeid"));
        LocalDateTime originaltimestamp = resultSet.getTimestamp("originaltimestamp").toLocalDateTime();
        EventType punchtype = Enum.valueOf(EventType.class, resultSet.getString("punchtype"));
        punches.add(new Punch(id, terminalid, badge, originaltimestamp, punchtype));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return punches;
  }

  public void addPunch(Punch punch) {
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO punch VALUES (NULL, ?, ?, ?, ?)");
      preparedStatement.setInt(1, punch.getTerminalid());
      preparedStatement.setString(2, punch.getBadge().getId());
      preparedStatement.setTimestamp(3, Timestamp.valueOf(punch.getOriginaltimestamp()));
      preparedStatement.setString(4, punch.getPunchtype().name());
      executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

