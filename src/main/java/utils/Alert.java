package utils;

import lombok.experimental.UtilityClass;

import javax.swing.*;

@UtilityClass
public class Alert {

    public int showConfirm(String message) {
        return JOptionPane.showConfirmDialog(
                null,
                message,
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
    }

    public void showSuccess(String message) {
        show(message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        show(message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showWarning(String message) {
        show(message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    private void show(String message, String title, int type) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                type
        );
    }
}
