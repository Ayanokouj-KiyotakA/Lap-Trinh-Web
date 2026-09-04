package h1.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "app_user")
@NamedQuery(name = "AppUser.findAll", query = "SELECT u FROM AppUser u")
public class AppUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "email", columnDefinition = "NVARCHAR(255) NULL")
	private String email;

	@Column(name = "username", columnDefinition = "NVARCHAR(50) NULL")
	private String userName;

	@Column(name = "fullname", columnDefinition = "NVARCHAR(255) NULL")
	private String fullName;

	@Column(name = "password", columnDefinition = "NVARCHAR(255) NULL")
	private String passWord;

	@Column(name = "avatar", columnDefinition = "NVARCHAR(255) NULL")
	private String avatar;

	@Column(name = "roleid")
	private int roleid;

	@Column(name = "phone", columnDefinition = "NVARCHAR(20) NULL")
	private String phone;

	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate")
	private Date createdDate;

	// Trang thai kich hoat tai khoan: 0 = chua kich hoat (moi dang ky, cho OTP),
	// 1 = da kich hoat (duoc phep dang nhap)
	@Column(name = "active")
	private int active;

	// Ma OTP dang cho xac nhan, dung chung cho ca kich hoat dang ky lan quen mat
	// khau (tai 1 thoi diem user chi chay 1 luong nen khong can tach rieng)
	@Column(name = "otpCode", columnDefinition = "NVARCHAR(10) NULL")
	private String otpCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "otpExpiredAt")
	private Date otpExpiredAt;

	public AppUser() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	public Date getOtpExpiredAt() {
		return otpExpiredAt;
	}

	public void setOtpExpiredAt(Date otpExpiredAt) {
		this.otpExpiredAt = otpExpiredAt;
	}
}
