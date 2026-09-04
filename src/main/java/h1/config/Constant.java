package h1.config;

public class Constant {
	public static final String DIR = "C:\\upload_jpa";

	// Cau hinh SMTP Gmail de gui mail OTP (kich hoat tai khoan / quen mat khau).
	// App Password tao tai https://myaccount.google.com/apppasswords (khong phai
	// mat khau dang nhap Gmail thuong). LUU Y: day la thong tin nhay cam, neu repo
	// nay la public tren GitHub thi nen thu hoi (revoke) App Password nay sau khi
	// nop bai, hoac doi sang repo private.
	public static final String MAIL_HOST = "smtp.gmail.com";
	public static final int MAIL_PORT = 587;
	public static final String MAIL_USERNAME = "trunggthu9092kp@gmail.com";
	public static final String MAIL_PASSWORD = "zaoezixoylcgcfpc";
	public static final String MAIL_FROM_NAME = "Bai tap 03 - He thong OTP";

	// Thoi gian OTP con hieu luc (phut)
	public static final int OTP_EXPIRE_MINUTES = 5;
}
