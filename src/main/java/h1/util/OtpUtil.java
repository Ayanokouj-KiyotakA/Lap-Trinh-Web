package h1.util;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

import h1.config.Constant;

// Sinh & kiem tra ma OTP 6 so dung chung cho ca kich hoat tai khoan va quen mat khau
public class OtpUtil {

	private static final SecureRandom RANDOM = new SecureRandom();

	private OtpUtil() {
	}

	// Sinh ma OTP gom 6 chu so, cho phep so 0 dung dau
	public static String generate() {
		int number = RANDOM.nextInt(1_000_000);
		return String.format("%06d", number);
	}

	// Thoi diem het han = hien tai + so phut cau hinh o Constant.OTP_EXPIRE_MINUTES
	public static Date expiredAtFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, Constant.OTP_EXPIRE_MINUTES);
		return calendar.getTime();
	}

	// OTP con hieu luc khi da co ma, chua het han
	public static boolean isValid(String savedOtp, Date expiredAt, String inputOtp) {
		if (savedOtp == null || expiredAt == null || inputOtp == null) {
			return false;
		}
		if (!savedOtp.equals(inputOtp.trim())) {
			return false;
		}
		return expiredAt.after(new Date());
	}
}
