package dao;

import entity.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class RoomDAO {
    private static final String INSERT = "insert into room(room_name, capacity, block, status) VALUES (?, ?, ?, 'active');";
    private static final String UPDATE = "update room set room_name = ?, capacity = ?, block = ?  where room_id = ?;";
    private static final String DELETE = "update room set status = 'inactive' where room_id = ?;";

    private static final String FIND_ALL = "select * from room where status = 'active';";

    private static final String FIND_BY_ID = "select * from room where room_id = ? and status = 'active';";

    private static final String COUNT = "select count(*) from room where status = 'active';";

    private static RoomDAO instance;

    private RoomDAO() {
    }

    public static RoomDAO getInstance() {
        if (instance == null) {
            instance = new RoomDAO();
        }
        return instance;
    }


    public int count() {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(COUNT); ResultSet rs = psmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Room findById(int id) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(FIND_BY_ID)) {
            psmt.setInt(1, id);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return new Room(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Room> findAll() {
        List<Room> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(FIND_ALL); ResultSet rs = psmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Room(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean save(Room room) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(room.getRoomId() == null ? INSERT : UPDATE)) {
            psmt.setObject(1, room.getRoomName());
            psmt.setObject(2, room.getCapacity());
            psmt.setObject(3, room.getBlock());
            if (room.getRoomId() != null) {
                psmt.setObject(4, room.getRoomId());
            }
            return psmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(DELETE)) {
            psmt.setInt(1, id);
            return psmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Room room) {
        return delete(room.getRoomId());
    }
}
