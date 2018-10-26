package com.justtennis.plugin.shared.service;

import com.justtennis.plugin.shared.network.model.ResponseHttp;
import com.justtennis.plugin.shared.query.response.LoginFormResponse;

public interface IServiceLogin {

    LoginFormResponse getLoginForm(String login, String password);

    ResponseHttp submitFormLogin(LoginFormResponse form);
}
