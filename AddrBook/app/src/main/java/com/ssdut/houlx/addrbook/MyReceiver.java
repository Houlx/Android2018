package com.ssdut.houlx.addrbook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

import cn.bmob.push.PushConstants;

/**
 * @author houlx
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (Objects.requireNonNull(intent.getAction()).equals(PushConstants.ACTION_MESSAGE)) {
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            Toast.makeText(context, intent.getStringExtra("msg"), Toast.LENGTH_LONG).show();
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
