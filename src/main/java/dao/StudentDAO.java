package dao;

import entity.Room;
import entity.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class StudentDAO {
    private static final String INSERT = "insert into student(full_name, gender, phone, age, birthday, address, room_id) VALUES (?,?,?,?,?,?,?);";
    private static final String UPDATE = "update student set full_name = ?, gender = ?, phone = ?, age = ?, birthday = ?, address = ?, room_id = ? where student_id = ?;";

    private static final String DELETE = "update student set status = 'inactive' where student_id = ?;";

    private static final String FIND_ALL = "select s.*, r.room_name from student s inner join room r on s.room_id = r.room_id and r.status = 'active' where s.status = 'active';";

    private static final String FIND_BY_ID = "select * from student where student_id = ? and status = 'active';";

    private static final String COUNT_STUDENT = "select count(*) from student where status = 'active';";

    private static StudentDAO instance;

    private StudentDAO() {
    }

    public static StudentDAO getInstance() {
        if (instance == null) {
            instance = new StudentDAO();
        }
        return instance;
    }

    public Student findById(Object id) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(FIND_BY_ID)) {
            psmt.setObject(1, id);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countStudent() {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(COUNT_STUDENT); ResultSet rs = psmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(FIND_ALL); ResultSet rs = psmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean save(Student item) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(item.getStudentId() == null ? INSERT : UPDATE)) {
            psmt.setObject(1, item.getFullName());
            psmt.setObject(2, item.getGender());
            psmt.setObject(3, item.getPhone());
            psmt.setObject(4, item.getAge());
            psmt.setObject(5, item.getBirthday());
            psmt.setObject(6, item.getAddress());
            psmt.setObject(7, item.getRoomId());
            if (item.getStudentId() != null) {
                psmt.setObject(8, item.getStudentId());
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

    public boolean delete(Student item) {
        return delete(item.getStudentId());
    }
}
