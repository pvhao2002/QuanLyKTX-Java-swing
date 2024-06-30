package entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Utility {
    Integer utilityId;
    Integer roomId;
    String utilityName;
    String type;
    String description;
    BigDecimal pricing;

    Room room;

    public Utility(ResultSet rs) throws SQLException {
        this.utilityId = rs.getInt("utility_id");
        this.utilityName = rs.getString("utility_name");
        this.type = rs.getString("type");
        this.description = rs.getString("description");
        this.pricing = rs.getBigDecimal("pricing");
        this.roomId = rs.getInt("room_id");

        Room.RoomBuilder roomBuilder = Room.builder().roomId(rs.getInt("room_id"));
        if (rs.findColumn("room_name") > 0) {
            roomBuilder.roomName(rs.getString("room_name"));
        }
        if (rs.findColumn("block") > 0) {
            roomBuilder.block(rs.getString("block"));
        }
        this.room = roomBuilder.build();
    }
}
