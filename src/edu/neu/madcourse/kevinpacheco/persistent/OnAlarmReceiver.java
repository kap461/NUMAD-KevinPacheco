package edu.neu.madcourse.kevinpacheco.persistent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class OnAlarmReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Multiplayer_Intent.acquireStaticLock(context);
    
    Toast.makeText(context, "onAlarm", Toast.LENGTH_LONG).show();
    
    Log.d("AlarmReceiver", "AlarmReceiver");
    context.startService(new Intent(context, Multiplayer_Game_Serv.class));
  }
}