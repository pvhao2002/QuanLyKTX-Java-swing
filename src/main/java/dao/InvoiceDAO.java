package dao;

import entity.Invoice;
import entity.InvoiceDetail;
import entity.Room;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceDAO {
    private static final String INSERT = "insert into invoice(student_id, title, amount, due_date, payment_status) VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE = "update invoice set payment_status = ? where invoice_id = ? and payment_status = 'pending';";
    private static final String INSERT_DETAIL = "insert into invoice_detail(invoice_id, utility_id) VALUES (?, ?);";
    private static final String FIND_ALL = "select i.*, s.full_name, s.room_id from invoice i inner join student s on i.student_id = s.student_id ;";
    private static final String FIND_INVOICE_DETAIL_BY_ID = "select id.*, u.utility_name, u.pricing from invoice_detail id inner join utility u on id.utility_id = u.utility_id where id.invoice_id = ?;";

    private static final String CALCULATE_REVENUE = "select sum(amount) from invoice where payment_status = 'paid';";
    private static final String CALCULATE_REVENUE_NOT_PAID = "select sum(amount) from invoice where payment_status = 'pending';";
    private static InvoiceDAO instance;

    private InvoiceDAO() {
    }

    public static InvoiceDAO getInstance() {
        if (instance == null) {
            instance = new InvoiceDAO();
        }
        return instance;
    }

    public Map<String, BigDecimal> calculateRevenue() {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(
                CALCULATE_REVENUE); ResultSet rs = psmt.executeQuery()) {
            if (rs.next()) {
                BigDecimal revenue = rs.getBigDecimal(1);
                try (PreparedStatement psmt2 = conn.prepareStatement(CALCULATE_REVENUE_NOT_PAID); ResultSet rs2 = psmt2.executeQuery()) {
                    if (rs2.next()) {
                        BigDecimal revenueNotPaid = rs2.getBigDecimal(1);
                        Map<String, BigDecimal> map = new HashMap<>();
                        map.put("revenue", revenue == null ? BigDecimal.ZERO : revenue);
                        map.put("revenueNotPaid", revenueNotPaid == null ? BigDecimal.ZERO : revenueNotPaid);
                        return map;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Invoice> findAll() {
        List<Invoice> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(FIND_ALL); ResultSet rs = psmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Invoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<InvoiceDetail> findInvoiceDetailById(Object id) {
        List<InvoiceDetail> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(
                FIND_INVOICE_DETAIL_BY_ID)) {
            psmt.setObject(1, id);
            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new InvoiceDetail(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(Integer invoiceId, String status) {
        try (Connection conn = DBContext.getConnection();
             PreparedStatement psmt = conn.prepareStatement(UPDATE)) {
            psmt.setObject(2, invoiceId);
            psmt.setObject(1, status);
            psmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean save(Invoice item) {
        try (Connection conn = DBContext.getConnection(); PreparedStatement psmt = conn.prepareStatement(
                item.getInvoiceId() == null ? INSERT : UPDATE,
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            int lastId;
            conn.setAutoCommit(false);
            psmt.setObject(1, item.getStudentId());
            psmt.setObject(2, item.getTitle());
            psmt.setObject(3, item.getAmount());
            psmt.setObject(4, item.getDueDate());
            psmt.setObject(5, item.getPaymentStatus());

            lastId = psmt.executeUpdate();
            if (lastId > 0) {
                item.getInvoiceDetailList().forEach(invoiceDetail -> {
                    try (PreparedStatement psmt2 = conn.prepareStatement(INSERT_DETAIL)) {
                        ResultSet rs = psmt.getGeneratedKeys();
                        int insertId = 0;
                        if (rs.next()) {
                            insertId = rs.getInt(1);
                            System.out.println(insertId);
                        }
                        psmt2.setObject(1, insertId);
                        psmt2.setObject(2, invoiceDetail.getUtilityId());
                        psmt2.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        try {
                            conn.rollback();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                conn.commit();
            } else {
                conn.rollback();
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
