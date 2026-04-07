package com.baro.noi_that_api.common.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Gửi OTP qua email
     */
    public void sendOtpEmail(String toEmail, String otp, String hoTen) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("🔑 Mã OTP đổi mật khẩu - Nội Thất Baro");

        String content =
                "Xin chào " + (hoTen != null ? hoTen : "bạn") + ",\n\n" +
                        "Mã OTP của bạn là: " + otp + "\n\n" +
                        "Mã này sẽ hết hạn sau 5 phút.\n" +
                        "Vui lòng không chia sẻ mã OTP này với bất kỳ ai.\n\n" +
                        "Nếu bạn không yêu cầu đổi mật khẩu, vui lòng bỏ qua email này.\n\n" +
                        "Trân trọng,\n" +
                        "Đội ngũ Nội Thất Baro";

        message.setText(content);

        try {
            mailSender.send(message);
            System.out.println("✅ [EMAIL] Đã gửi OTP đến: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ [EMAIL] Gửi OTP thất bại cho " + toEmail + " | " + e.getMessage());
        }
    }

    /**
     * Gửi email xác nhận đơn hàng
     */
    public void sendOrderConfirmEmail(String toEmail, String hoTen, Integer orderId, String totalPrice) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("✅ Xác nhận đơn hàng #" + orderId + " - Nội Thất Baro");

        String content =
                "Xin chào " + (hoTen != null ? hoTen : "bạn") + ",\n\n" +
                        "Đơn hàng #" + orderId + " của bạn đã được xác nhận thành công.\n" +
                        "Tổng tiền: " + totalPrice + " VNĐ\n\n" +
                        "Chúng tôi sẽ liên hệ với bạn sớm nhất.\n\n" +
                        "Trân trọng,\n" +
                        "Đội ngũ Nội Thất Baro";

        message.setText(content);

        try {
            mailSender.send(message);
            System.out.println("✅ [EMAIL] Đã gửi xác nhận đơn hàng đến: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ [EMAIL] Gửi email đơn hàng thất bại cho " + toEmail + " | " + e.getMessage());
        }
    }
}