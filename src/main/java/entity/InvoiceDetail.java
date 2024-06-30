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
public class InvoiceDetail {
    Integer invoiceDetailId;
    Integer invoiceId;
    Integer utilityId;
    Utility utility;

    public InvoiceDetail(ResultSet rs) throws SQLException {
        this.invoiceId = rs.getInt("invoice_id");
        this.utilityId = rs.getInt("utility_id");
        String utilityName = rs.getString("utility_name");
        BigDecimal pricing = rs.getBigDecimal("pricing");
        this.utility = Utility.builder()
                              .utilityId(this.utilityId)
                              .utilityName(utilityName)
                              .pricing(pricing)
                              .build();
    }

    public static InvoiceDetail fromUtility(Utility u) {
        return InvoiceDetail.builder()
                            .utilityId(u.getUtilityId())
                            .utility(u)
                            .build();
    }
}
