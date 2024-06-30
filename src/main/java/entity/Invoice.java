package entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {
    Integer invoiceId;
    Integer studentId;
    String title;
    BigDecimal amount;
    Date dueDate;
    String paymentStatus;
    Student student;

    List<InvoiceDetail> invoiceDetailList;

    public Invoice(ResultSet rs) throws SQLException {
        this.invoiceId = rs.getInt("invoice_id");
        this.studentId = rs.getInt("student_id");
        String studentName = rs.getString("full_name");
        Integer roomId = rs.getInt("room_id");
        this.student = Student.builder()
                              .studentId(this.studentId)
                              .fullName(studentName)
                              .roomId(roomId)
                              .build();
        this.title = rs.getString("title");
        this.amount = rs.getBigDecimal("amount");
        this.dueDate = rs.getDate("due_date");
        this.paymentStatus = rs.getString("payment_status");
    }

    public void buildInvoiceDetail(List<Utility> utilities) {
        this.invoiceDetailList = utilities.stream()
                                          .map(InvoiceDetail::fromUtility)
                                          .collect(Collectors.toList());
    }
}
