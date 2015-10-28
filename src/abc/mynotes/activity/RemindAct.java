package abc.mynotes.activity;

import java.util.Calendar;
import java.util.Date;

import abc.mynotes.database.DbAdapter;
import abc.mynotes.models.Note;
import abc.mynotes.utils.StringUtil;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class RemindAct extends Activity {
	DatePicker dpDay;
	TextView txtTitle;
	Spinner chooseTime;
	TimePicker tpTime;
	ImageView btnRemind;
	String str = "";
	char msp = 'a';
	int day, month, year, hour, minute, index = 0;
	public static final String PREFS_NAME = "UserSelectionPref";
	public static final String SELECTION_KEY = "SelectionKey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind);
		/*------------------------------------------Init controls----------------------------------*/
		txtTitle = (TextView) findViewById(R.id.remind_txtTitle);
		dpDay = (DatePicker) findViewById(R.id.remind_dpDay);
		tpTime = (TimePicker) findViewById(R.id.remind_tpTime);
		btnRemind = (ImageView) findViewById(R.id.remind_btnRemind);
		chooseTime = (Spinner) findViewById(R.id.remind_spinner);
		/*******************************************************************************************/
		Bundle b = getIntent().getExtras();
		String title = b.getString("title");
		txtTitle.setText(title);
		String strDate = b.getString("time");
		Date dTime = StringUtil.getDate(strDate);
		int d1 = dTime.getYear() + 1900;
		dpDay.init(d1, dTime.getMonth(), dTime.getDate(), null);
		tpTime.setCurrentHour(dTime.getHours());
		tpTime.setCurrentMinute(dTime.getMinutes());

		char time = strDate.charAt(strDate.length() - 1);
		switch (time) {
		case 'a':
			index = 0;
			break;
		case 'b':
			index = 1;
			break;
		case 'c':
			index = 2;
			break;
		case 'd':
			index = 3;
			break;
		case 4:
			index = 4;
			break;
		case 'f':
			index = 5;
			break;
		case 'g':
			index = 6;
			break;
		case 'h':
			index = 7;
			break;
		}

		/*---------------------------------set adapter for spinner----------------------------------------------*/
		String[] items = { "On time", "15 mins before", "30 mins before",
				"45 mins before", "1 hour before", "1 day before",
				"3 days before", "1 week before" };
		ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				items);
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chooseTime.setAdapter(arrayAdapter);
		chooseTime.setSelection(index);
		/*---------------------------------set selected on spinner----------------------------------------------*/
		chooseTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					msp = 'a';
					break;
				case 1:
					msp = 'b';
					break;
				case 2:
					msp = 'c';
					break;
				case 3:
					msp = 'd';
					break;
				case 4:
					msp = 'e';
					break;
				case 5:
					msp = 'f';
					break;
				case 6:
					msp = 'g';
					break;
				case 7:
					msp = 'h';
					break;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		/*--------------------------------set OnCLickListener for button set Alarm------------------------------*/
		btnRemind.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				index = 0;
				day = dpDay.getDayOfMonth();
				month = dpDay.getMonth();
				year = dpDay.getYear();
				hour = tpTime.getCurrentHour();
				minute = tpTime.getCurrentMinute();

				Calendar mCal = Calendar.getInstance();
				mCal.set(year, month, day, hour, minute, 00);

				// switch values of spinner
				switch (msp) {

				case 'b':
					calculateMinute(15);
					break;
				case 'c':
					calculateMinute(30);
					break;
				case 'd':
					calculateMinute(45);
					break;
				case 'e':
					if (hour == 1) {
						hour = 12;
					} else {
						hour -= 1;
					}
					break;
				case 'f':
					if (day == 1) {
						calculateTimeBefore();
					} else
						day -= 1;
					break;
				case 'g':
					if (day < 3) {
						switch (day) {
						case 2:
							index = 1;
							break;
						case 1:
							index = 2;
							break;
						case 0:
							index = 3;
							break;
						}//
						calculateTimeBefore();
						break;
					}
					if (day == 3) {
						calculateTimeBefore();
						break;
					} else if (day > 3) {
						day -= 3;
					}
					break;
				case 'h':
					if (day < 7) {
						switch (day) {
						case 6:
							index = 1;
							break;
						case 5:
							index = 2;
							break;
						case 4:
							index = 3;
							break;
						case 3:
							index = 4;
							break;
						case 2:
							index = 5;
							break;
						case 1:
							index = 6;
							break;
						}//
						calculateTimeBefore();
						break;
					}
					if (day == 7) {
						calculateTimeBefore();
						break;
					} else if (day > 7) {
						day -= 7;
					}
					break;
				}
				// add values to Calendar
				Calendar cal = Calendar.getInstance();
				cal.set(year, month, day, hour, minute, 00);
				DbAdapter dbAdapter;
				try {
					dbAdapter = new DbAdapter(getApplicationContext());
					dbAdapter.open();
					Note mNote = dbAdapter.getNoteRemind(StringUtil
							.getCurrentDate());
					if (mNote != null) {
						String strRemind = mNote.getDateOfRemind();
						Date dateRemind = StringUtil.getDate(strRemind);
						if (!cal.after(dateRemind) || !cal.before(dateRemind)) {
							int second = dateRemind.getSeconds() + 1;
							cal.set(Calendar.SECOND, second);
						}
					}
					dbAdapter.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (cal.getTimeInMillis() < new Date().getTime()) {
					Toast.makeText(getApplicationContext(),
							"You cannot set alarm time less than current time",
							2000).show();
				} else {
					Intent intent = new Intent();
					intent.putExtra("dayOfRemind",
							StringUtil.getDatebyDate(cal.getTime()));
					intent.putExtra(
							"time",
							StringUtil.getDatebyDate(mCal.getTime())
									+ String.valueOf(msp));
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}
	/******************************** calculate *************************************************************************/
	public void calculateMinute(int mMinute) {
		if (minute >= mMinute) {
			minute -= mMinute;
		} else {
			hour -= 1;
			int t1 = mMinute - minute;
			minute = 60 - t1;
		}
	}

	public void calculateTimeBefore() {
		if (month == 0) {
			System.out.println("0");
			year -= 1;
			month = 11;
			day = 31 - index;
		} else if (month == 1) {
			System.out.println("1");
			day = 31 - index;
			month = 0;
		} else if (month == 2) {
			if (year % 4 == 0) {
				System.out.println("2");
				day = 29 - index;
				month = 1;
			} else {
				System.out.println("3");
				day = 28 - index;
				month = 1;
			}
		} else if (month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10) {
			System.out.println("4");
			day = 31 - index;
			System.out.println("day = " + day);
			month -= 1;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			System.out.println("5");
			day = 30 - index;
			month -= 1;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
