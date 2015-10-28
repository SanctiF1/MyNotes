package abc.mynotes.services;

import java.util.Calendar;
import java.util.List;

import abc.mynotes.database.DbAdapter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class AlarmService extends Service {
	Calendar cal = Calendar.getInstance();
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			alarm();
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		loop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	public void loop() {
		final DbAdapter DB = new DbAdapter(getApplicationContext());
		try {
			DB.open();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (DB.getAllNotes().size() != 0) {

			Thread thread = new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					try {
						Calendar currentCal = Calendar.getInstance();
						int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
						int currentMonth = currentCal.get(Calendar.MONTH);
						int currentYear = currentCal.get(Calendar.YEAR);
						int currentHour = currentCal.get(Calendar.HOUR);
						int currentMinute = currentCal.get(Calendar.MINUTE);

						List<Calendar> listCal = DB.getAlarm();
						System.out.println("list size" + listCal.size());
						for (int i = 0; i < listCal.size(); i++) {
							int day = (listCal.get(i)
									.get(Calendar.DAY_OF_MONTH)) - 1;
							int month = listCal.get(i).get(Calendar.MONTH);
							int year = listCal.get(i).get(Calendar.YEAR);
							int hour = listCal.get(i).get(Calendar.HOUR);
							int minute = listCal.get(i).get(Calendar.MINUTE);
							if (day == currentDay && month == currentMonth
									&& year == currentYear
									&& hour == currentHour
									&& minute == currentMinute) {
								cal = listCal.get(i);
								cal.set(year, month, day, hour, minute);
								handler.sendMessage(handler.obtainMessage());
								Thread.sleep(60000);
							}
						}
						Thread.sleep(5000);
						loop();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			thread.start();
		}
	}

	public void alarm() {
		System.out.println("1");
		Intent alarmintent = new Intent(getApplicationContext(),
				AlarmReceiver.class);System.out.println("2");
		alarmintent.putExtra("title", "meo pro handler");System.out.println("3");
		alarmintent.putExtra("note", "Description of our  Notification");System.out.println("4");
		PendingIntent sender = PendingIntent.getBroadcast(
				getApplicationContext(), 1, alarmintent,
				PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);System.out.println("5");
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);System.out.println("6");
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);System.out.println("7");
	}
}
