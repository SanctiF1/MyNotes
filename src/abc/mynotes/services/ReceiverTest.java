package abc.mynotes.services;

import java.util.Calendar;
import java.util.Date;

import abc.mynotes.activity.R;
import abc.mynotes.database.DbAdapter;
import abc.mynotes.models.Note;
import abc.mynotes.utils.StringUtil;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ReceiverTest extends BroadcastReceiver {

	// private static DbAdapter dataAdapter;
	static AlarmManager am = null;
	private static int NOTIFICATION_ID = 1;
	static PendingIntent sender = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String value = intent.getStringExtra("SDT");
		Log.v("Broadcast", "Success");

		NotificationManager manger = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Combi Note", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				NOTIFICATION_ID, new Intent(context, ReceiverTest.class), 0);
		String title = intent.getStringExtra("title");
		String content = intent.getStringExtra("content");
		notification.setLatestEventInfo(context, title, content, contentIntent);
		notification.flags = Notification.FLAG_INSISTENT;
		notification.defaults |= Notification.DEFAULT_SOUND;

		// notification
		manger.notify(NOTIFICATION_ID++, notification);
		// Note alarmNote = null ;
		// dataAdapter = new DbAdapter(context);
		// try {
		// dataAdapter.open();
		// alarmNote = dataAdapter.getNoteRemind(value);
		// dataAdapter.close();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// if(alarmNote != null){
		onNewRemind(context);
		// }

	}

	public static void onNewRemind(Context context) {
		Note alarmNote = null;
		String currentDate = StringUtil.getCurrentDate();
		Log.v("CurrentDate", currentDate);
		DbAdapter dataAdapter = new DbAdapter(context);
		try {
			dataAdapter.open();
			alarmNote = dataAdapter.getNoteRemind(currentDate);
			dataAdapter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (alarmNote != null) {
			Log.v("NOTEALARM", alarmNote.getTitle());
			Intent intent = new Intent(context, ReceiverTest.class);
			intent.putExtra("SDT", alarmNote.getDateOfRemind());
			intent.putExtra("title", alarmNote.getTitle());
			intent.putExtra("content", alarmNote.getContent());
			sender = PendingIntent.getBroadcast(context, 192837, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Date dateRemind = StringUtil.getDate(alarmNote.getDateOfRemind());
			// Calendar cal = Calendar.getInstance();
			// cal.set(Calendar.SECOND, value +10);

			am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			// Log.v("timeCal", String.valueOf( cal.getTimeInMillis()));
			// Log.v("STRtime", String.valueOf( cal.toString()));
			Log.v("STRtime", String.valueOf(dateRemind.toString()));			
			am.set(AlarmManager.RTC_WAKEUP, dateRemind.getTime(), sender);

		} else {
			if (am != null)
				am.cancel(sender);
			if (sender != null)
				sender.cancel();
		}
	}

}
