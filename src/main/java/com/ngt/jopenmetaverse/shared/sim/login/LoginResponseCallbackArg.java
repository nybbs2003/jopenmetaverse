package com.ngt.jopenmetaverse.shared.sim.login;

public class LoginResponseCallbackArg {

	boolean loginSuccess;
	boolean redirect;
	String message;
	String reason;
	LoginResponseData replyData;
	
	public LoginResponseCallbackArg(boolean loginSuccess, boolean redirect,
			String message, String reason, LoginResponseData replyData) {
		super();
		this.loginSuccess = loginSuccess;
		this.redirect = redirect;
		this.message = message;
		this.reason = reason;
		this.replyData = replyData;
	}
	public boolean isLoginSuccess() {
		return loginSuccess;
	}
	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public LoginResponseData getReplyData() {
		return replyData;
	}
	public void setReplyData(LoginResponseData replyData) {
		this.replyData = replyData;
	}
}
