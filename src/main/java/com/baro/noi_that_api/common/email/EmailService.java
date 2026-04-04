package com.baro.noi_that_api.common.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

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
                        "Mã này sẽ hết hạn sau 10 phút.\n" +
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
            // Tạm thời chỉ log lỗi, không throw để không làm gián đoạn flow
        }
    }
}