package com.hades.example.android.tools.permission;

public interface IPermissionResult {
    void granted();

    void denied();

    void onResult(Boolean granted);

}
