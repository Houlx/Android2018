package com.ssdut.houlx.addrbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author houlx
 */
public class SplashActivity extends AppCompatActivity {

    private static final int GO_SIGNUP = 1;
    private static final int GO_HOME = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GO_SIGNUP:
                    Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case GO_HOME:
                    Intent intent1 = new Intent(SplashActivity.this, ContactsActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Bmob.initialize(this, "381f45436aabb971eadd32ec2470cf3a");

        BmobUser user = BmobUser.getCurrentUser();
        if (user != null) {
            BmobQuery<BmobUser> query = new BmobQuery<>();
            query.addWhereEqualTo("username", user.getUsername());
            query.findObjects(new FindListener<BmobUser>() {
                @Override
                public void done(List<BmobUser> list, BmobException e) {
                    if (e == null) {
                        handler.sendEmptyMessageDelayed(GO_HOME, 2000);
                    } else {
                        handler.sendEmptyMessageDelayed(GO_SIGNUP, 2000);
                    }
                }
            });
        } else {
            handler.sendEmptyMessageDelayed(GO_SIGNUP, 2000);
        }
    }
}
