package com.hades.example.android.app_component._fragment.back;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.hades.example.android.R;
import com.hades.example.android.tools.FragmentUtils;

public class TestBackActivity extends AppCompatActivity {

    OnBackPressedCallback mOnBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_test);

        findViewById(R.id.add_1).setOnClickListener(v -> FragmentUtils.replaceFragment(TestBackActivity.this, R.id.fragmentContainer, HandleBackFragment.newInstance(), HandleBackFragment.TAG));
        findViewById(R.id.add_2).setOnClickListener(v -> FragmentUtils.replaceFragment(TestBackActivity.this, "TestBack", R.id.fragmentContainer, NotHandleBackFragment.newInstance(),
                NotHandleBackFragment.TAG));

        // Way 2 :
        mOnBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackPressed2();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, mOnBackPressedCallback);
    }

//    @Override
//    public void onBackPressed() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
//        if (fragment instanceof IBack) {
//            boolean flag = ((IBack) fragment).handleBack();
//            if (flag) {
//                return;
//            }
//        }
//        super.onBackPressed();
//    }


    public void onBackPressed2() {
//        FragmentUtils.popBackStack(TestBackActivity.this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment instanceof IBack) {
            boolean flag = ((IBack) fragment).handleBack();
            if (flag) {
                // we have consumed the test
                mOnBackPressedCallback.setEnabled(true);
                return;
            }
        }
        mOnBackPressedCallback.setEnabled(false);
        getOnBackPressedDispatcher().onBackPressed();
    }
}