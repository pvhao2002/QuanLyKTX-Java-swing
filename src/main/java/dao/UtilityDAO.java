package dao;

import entity.Room;
import entity.Student;
import entity.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class UtilityDAO {
    private static final String INSERT = "insert into utility(utility_name, type, pricing, description, room_id) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE = "update utility set utility_name = ?, type = ?, pricing = ?, description = ?, room_id = ? where utility_id = ?;";

    private static final String DELETE = "delete from utility where utility_id = ?;";

    private static final String FIND_ALL = "select u.*, r.room_name, r.block from utility u inner join room r on u.room_id = r.room_id and r.status = 'active';";

    private static final String FIND_BY_ID = "select u.*, r.room_name, r.block from utility u inner join room r on u.room_id = r.room_id and r.status = 'active' where utility_id = ?;";

    private static UtilityDAO instance;

    private UtilityDAO() {
    }

    public static UtilityDAO getInstance() {
        if (instance == null) {
            instance = new UtilityDAO();
        }
        return instance;
    }

    public List<Utility> findAll() {
        List<Utility> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(FIND_ALL); ResultSet rs = psmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Utility(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean save(Utility item) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(item.getUtilityId() == null ? INSERT : UPDATE)) {
            psmt.setObject(1, item.getUtilityName());
            psmt.setObject(2, item.getType());
            psmt.setObject(3, item.getPricing());
            psmt.setObject(4, item.getDescription());
            psmt.setObject(5, item.getRoomId());
            if (item.getUtilityId() != null) {
                psmt.setObject(6, item.getUtilityId());
            }
            return psmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(DELETE)) {
            psmt.setInt(1, id);
            return psmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean delete(Utility item) {
        return delete(item.getUtilityId());
    }
}
