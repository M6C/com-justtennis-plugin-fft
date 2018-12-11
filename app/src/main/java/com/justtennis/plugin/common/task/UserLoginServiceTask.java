package com.justtennis.plugin.common.task;

import android.content.Context;

import com.justtennis.plugin.common.manager.ServiceManager;
import com.justtennis.plugin.shared.service.IServiceLogin;

public class UserLoginServiceTask extends UserLoginTask {

    protected UserLoginServiceTask(Context context, String email, String password, String label) {
        super(context, email, password);
        ServiceManager.getInstance().setService(label);
    }

    @Override
    protected IServiceLogin newLoginService(Context context) {
        return ServiceManager.getInstance().getServiceLogin(context);
    }
}
