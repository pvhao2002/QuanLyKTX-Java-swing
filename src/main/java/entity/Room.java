package entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {
    Integer roomId;
    String roomName;
    Integer capacity;
    String block;
    String status;

    public Room(ResultSet rs) throws SQLException {
        this.roomId = rs.getInt("room_id");
        this.roomName = rs.getString("room_name");
        this.capacity = rs.getInt("capacity");
        this.block = rs.getString("block");
        this.status = rs.getString("status");
    }
}
