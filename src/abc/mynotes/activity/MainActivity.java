package abc.mynotes.activity;

import java.util.ArrayList;
import java.util.List;

import abc.mynotes.database.DbAdapter;
import abc.mynotes.models.Note;
import abc.mynotes.models.Task;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {
	ImageView imageView;
	DbAdapter db;
	public static final String PREFS_NAME = "UserSelectionPref";
	public static final String SELECTION_KEY = "SelectionKey";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		/*------------------------------------------------------------------------------------------------------*/
		imageView = (ImageView) findViewById(R.id.imageView1);
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.animation);
		imageView.startAnimation(animation);
		animation.setFillAfter(true);
		try {
			db = new DbAdapter(this);
			db.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Task> listTask = new ArrayList<Task>();
		new CountDownTimer(3000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				restoreSelection();
			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, null);
		return true;
	}

	public void restoreSelection() {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
		int index = sp.getInt(SELECTION_KEY, 0);
		if (index == 0) {
			Intent intent = new Intent(MainActivity.this, listAct.class);
			startActivity(intent);
		} else if (index == 1) {
			Intent intent = new Intent(MainActivity.this, gridAct.class);
			startActivity(intent);
		}
	}
	
//	MyActivity.this.finish();
}
