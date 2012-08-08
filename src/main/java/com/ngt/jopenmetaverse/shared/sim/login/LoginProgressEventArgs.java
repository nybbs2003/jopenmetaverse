package com.ngt.jopenmetaverse.shared.sim.login;

public class LoginProgressEventArgs {
	private LoginStatus m_Status;
    private String m_Message;
    private String m_FailReason;

    public LoginStatus getStatus() { return m_Status; }
    public String getMessage() { return m_Message; } 
    public String getFailReason() { return m_FailReason; } 

    public LoginProgressEventArgs(LoginStatus login, String message, String failReason)
    {
        this.m_Status = login;
        this.m_Message = message;
        this.m_FailReason = failReason;
    }
}
