package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BillStatusUtils {
    public String convertBillStatus(String status) {
        if ("PAID".equalsIgnoreCase(status)) {
            return "Đã thanh toán";
        } else if ("PENDING".equalsIgnoreCase(status)) {
            return "Chưa thanh toán";
        }
        return "Miễn phí";
    }

    public String getBillStatus(String status) {
        if ("Đã thanh toán".equalsIgnoreCase(status)) {
            return "PAID";
        } else if ("Chưa thanh toán".equalsIgnoreCase(status)) {
            return "PENDING";
        }
        return "FREE";
    }
}
