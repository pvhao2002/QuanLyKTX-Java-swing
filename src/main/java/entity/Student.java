package entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    Integer studentId;
    Integer roomId;
    String fullName;
    String gender;
    String phone;
    Integer age;
    Date birthday;
    String address;
    String status;

    Room room;

    public Student(ResultSet rs) throws SQLException {
        this.studentId = rs.getInt("student_id");
        this.fullName = rs.getString("full_name");
        this.gender = rs.getString("gender");
        this.phone = rs.getString("phone");
        this.age = rs.getInt("age");
        this.birthday = rs.getDate("birthday");
        this.address = rs.getString("address");
        this.status = rs.getString("status");
        this.roomId = rs.getInt("room_id");
        try {
            Room.RoomBuilder roomBuilder = Room.builder().roomId(rs.getInt("room_id"));
            roomBuilder.roomName(rs.getString("room_name"));
            this.room = roomBuilder.build();
        } catch (Exception ignored) {
        }
    }
}
