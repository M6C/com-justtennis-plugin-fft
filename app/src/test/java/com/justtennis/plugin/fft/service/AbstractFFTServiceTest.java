package com.justtennis.plugin.fft.service;

import com.justtennis.plugin.fft.network.model.ResponseHttp;
import com.justtennis.plugin.fft.query.response.LoginFormResponse;
import com.justtennis.plugin.fft.query.response.PalmaresMillesimeResponse;
import com.justtennis.plugin.fft.skeleton.IProxy;

abstract class AbstractFFTServiceTest extends AbstractServiceTest {

    // ROCAdavid75 / JEkonCAEN26 - leonie.roca / ymNgfBeJ36 - delphin.roca / 123456789
    private static final String LOGIN = "leandre.roca2006";
    private static final String PASWD = "lR123456789";

    abstract IProxy initializeService();

    static FFTServiceLogin fftServiceLogin;

    @Override
    protected LoginFormResponse getLoginForm() {
        initializeFFTService();
        return fftServiceLogin.getLoginForm(LOGIN, PASWD);
    }

    @Override
    protected ResponseHttp doLogin() {
        LoginFormResponse response = getLoginForm();

        return fftServiceLogin.submitFormLogin(response);
    }

    @Override
    protected String getPaswd() {
        return PASWD;
    }

    @Override
    protected String getLogin() {
        return LOGIN;
    }

    void changeMillesimeSelected(PalmaresMillesimeResponse palmaresMillesimeResponse) {
        for(PalmaresMillesimeResponse.Millesime millesime : palmaresMillesimeResponse.listMillesime) {
            if (!millesime.equals(palmaresMillesimeResponse.millesimeSelected)) {
                palmaresMillesimeResponse.millesimeSelected = millesime;
                break;
            }
        }
    }

    @Override
    protected void initializeFFTService() {
        fftServiceLogin = FFTServiceLogin.newInstance(null);
        initializeProxy(fftServiceLogin);
        initializeProxy(initializeService());
    }
}