package it.stasbranger.rotarylive.service;

import it.stasbranger.rotarylive.domain.User;

public interface MailService {
	public void sendForgotPassword(User user, String code);
}
