package com.baro.noi_that_api.common.otp;

import com.baro.noithat_api.exception.AppException;
import com.baro.noithat_api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    // Lưu OTP trong memory: key = email, value = OtpData
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    /**
     * Tạo và lưu OTP mới (thay thế OTP cũ nếu có)
     */
    @Transactional
    public void saveOtp(String email, String otpCode, int minutesExpire) {
        String normalizedEmail = email.trim().toLowerCase();

        // Xóa OTP cũ nếu tồn tại
        otpStorage.remove(normalizedEmail);

        OtpData otpData = new OtpData(
                otpCode,
                LocalDateTime.now().plusMinutes(minutesExpire)
        );

        otpStorage.put(normalizedEmail, otpData);
    }

    /**
     * Kiểm tra OTP có đúng và còn hạn không
     */
    @Transactional(readOnly = true)
    public boolean verifyOtp(String email, String otpCode) {
        String normalizedEmail = email.trim().toLowerCase();

        OtpData otpData = otpStorage.get(normalizedEmail);

        if (otpData == null) {
            return false;
        }

        // Kiểm tra hết hạn
        if (otpData.isExpired()) {
            otpStorage.remove(normalizedEmail); // dọn dẹp
            return false;
        }

        // Kiểm tra mã OTP có khớp không
        boolean isValid = otpData.getOtp().equals(otpCode);

        if (!isValid) {
            // Có thể tăng counter attempt nếu muốn giới hạn số lần nhập sai
        }

        return isValid;
    }

    /**
     * Xóa OTP sau khi dùng thành công (hoặc reset thất bại)
     */
    @Transactional
    public void deleteOtp(String email) {
        String normalizedEmail = email.trim().toLowerCase();
        otpStorage.remove(normalizedEmail);
    }

    /**
     * Kiểm tra email có OTP đang chờ không (dùng để tránh spam)
     */
    public boolean hasActiveOtp(String email) {
        String normalizedEmail = email.trim().toLowerCase();
        OtpData data = otpStorage.get(normalizedEmail);
        return data != null && !data.isExpired();
    }

    // ===================== Inner Class =====================
    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiryTime);
        }
    }
}