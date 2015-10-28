package abc.mynotes.activity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import abc.mynotes.database.DbAdapter;
import abc.mynotes.database.LstTasksAdapter;
import abc.mynotes.models.Note;
import abc.mynotes.models.Task;
import abc.mynotes.services.ReceiverTest;
import abc.mynotes.utils.StringUtil;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class noteAct extends Activity {

	ListView lvTask;
	List<Task> lstTasks;
	List<Task> lstProcessTasks;
	final Context context = this;
	Note mNote;
	ImageView btnSave, btnRemind;
	EditText txtTitle;
	EditText txtContent, txtRemind;
	String dayOfRemind, time;
	CheckBox check;
	private LstTasksAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_note);
		mNote = getIntent().getParcelableExtra("CUR_NOTE");
		txtTitle = (EditText) findViewById(R.id.txtTitle_addNote);
		txtContent = (EditText) findViewById(R.id.txtContent_addNote);
		txtRemind = (EditText) findViewById(R.id.txtRemind_addNote);
		check = (CheckBox) findViewById(R.id.checkBox1);
		check.setChecked(false);
		btnSave = (ImageView) findViewById(R.id.btnSave_addNote);
		lvTask = (ListView) findViewById(R.id.listViewTask);
		btnRemind = (ImageView) findViewById(R.id.addAlarm);
		lstProcessTasks = new ArrayList<Task>();
		lstTasks = new ArrayList<Task>();
		Task task1 = new Task(-2, "Add new task", 1, null, null, false, true);// -2
																				// task
																				// do
																				// ct
																				// tao
		lstTasks.add(0, task1);
		if (mNote != null) {
			if (mNote.getDateOfRemind() == null) {
				check.setVisibility(View.GONE);
			}

			txtTitle.setText(mNote.getTitle());
			txtContent.setText(mNote.getContent());
			txtRemind.setText(mNote.getDateOfRemind() == null ? "" : mNote
					.getDateOfRemind());
			if (mNote.getListTask() != null) {
				lstTasks.addAll(mNote.getListTask());
				lstProcessTasks.addAll(lstTasks);
				mNote.setListTask(lstProcessTasks);
			}

		} else {
			mNote = new Note(-1, "", "", lstTasks, null, null, null, null, true);
		}

		listAdapter = new LstTasksAdapter(this, lstTasks);
		lvTask.setAdapter(listAdapter);

		btnRemind.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(noteAct.this, RemindAct.class);
				if (txtTitle.getText().toString() != null) {
					intent.putExtra("title", txtTitle.getText().toString());
				} else {
					intent.putExtra("title", "null");
				}
				if (mNote.getTime() != null) {
					intent.putExtra("time", mNote.getTime());
				} else {
					intent.putExtra("time", "z");
				}
				startActivityForResult(intent, 999);
			}
		});

		lvTask.setOnItemClickListener(onItemClick);
		lvTask.setOnItemLongClickListener(onItemLongClick);

		btnSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (txtTitle.getText().toString().trim().length() == 0) {
					txtTitle.setError("Subject is required!");
				} else
					onSaveNote();
			}
		});

		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (mNote.getDateOfRemind() != null) {
					if (check.isChecked() == false) {
						txtRemind.setPaintFlags(txtRemind.getPaintFlags()
								& ~Paint.STRIKE_THRU_TEXT_FLAG);
					} else {
						txtRemind.setPaintFlags(txtRemind.getPaintFlags()
								| Paint.STRIKE_THRU_TEXT_FLAG);
					}
				} else {
					Toast.makeText(context, "You have no alarm", 2000).show();
				}
			}
		});
	}

	private OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(android.widget.AdapterView<?> parent,
				View view, int position, long id) {

			final Dialog dialogTask = new Dialog(context);
			dialogTask.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			dialogTask.setContentView(R.layout.add_task);
			dialogTask.setTitle("Task");

			final TextView txtName = (TextView) dialogTask
					.findViewById(R.id.textName_addTask);

			final CheckBox chkDone = (CheckBox) dialogTask
					.findViewById(R.id.checkDone_addTask);

			TextView taskName = (TextView) view
					.findViewById(R.id.txvName_listTask);

			final Task curTask = (Task) taskName.getTag();
			final int location = position;
			if (curTask.getId() >= -1) {
				txtName.setText(curTask.getName());
				chkDone.setChecked(curTask.isDone());
			}
			Button ok_addTask = (Button) dialogTask
					.findViewById(R.id.btnOk_addTask);
			ok_addTask.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					final String nameTask = txtName.getText().toString();

					if (curTask.getId() >= -1) {
						curTask.setName(nameTask);
						curTask.setDone(chkDone.isChecked());
						curTask.setDeleted(false);
						lstTasks.set(location, curTask);

					} else {
						Task task = new Task(-1, nameTask, -1, null, null,
								false, chkDone.isChecked());// -1 note tao moi
						lstTasks.add(1, task);
					}
					lstProcessTasks.clear();
					lstProcessTasks.addAll(lstTasks);
					mNote.setListTask(lstProcessTasks);
					listAdapter.notifyDataSetChanged();
					dialogTask.dismiss();
				}
			});
			Button btnCancel = (Button) dialogTask
					.findViewById(R.id.btnCancel_addTask);
			btnCancel.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialogTask.dismiss();
				}
			});
			dialogTask.show();
			dialogTask.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
					R.drawable.ic_task);
		};

	};

	private OnItemLongClickListener onItemLongClick = new AdapterView.OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {

			String name = lstTasks.get(position).getName();
			lstTasks.get(position).setDeleted(true);
			syncListTasks(position);
			listAdapter.notifyDataSetChanged();
			Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT)
					.show();

			return true;
		};
	};

	public void syncListTasks(int pos) {
		Task curTask = lstTasks.get(pos);
		lstTasks.remove(pos);
		mNote.getListTask().remove(pos);
		mNote.getListTask().add(curTask);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.setMessage("Would you like to save?");
		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (txtTitle.getText().toString().trim().length() == 0) {
					alertDialog.dismiss();
					txtTitle.setError("Title is required!");
				} else
					onSaveNote();
			}
		});
		alertDialog.setButton2("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		alertDialog.show();
	}

	private void onSaveNote() {

		mNote.setTitle(txtTitle.getText().toString());
		mNote.setContent(txtContent.getText().toString());
		mNote.setDeleted(false);
		int idNote = mNote.getId();

		DbAdapter dbAdapter = new DbAdapter(context);
		try {
			dbAdapter.open();
			if (mNote.getId() < 0)
				idNote = (int) dbAdapter.insertNote(mNote);
			else
				dbAdapter.updatetNote(mNote);
			if (txtRemind.length() > 0 && check.isChecked() == true) {
				mNote.setDateOfRemind(null);
				mNote.setTime(null);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbAdapter.close();
		ReceiverTest.onNewRemind(context);

		Intent returnIntent = new Intent();
		returnIntent.putExtra("NOTE_ID", idNote);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	/************************************************************************************************/
	// meo pro
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 999 && resultCode == RESULT_OK) {
			// dateOfRemind = data.getStringExtra("dateOfRemind");
			dayOfRemind = data.getStringExtra("dayOfRemind");
			time = data.getStringExtra("time");
			txtRemind.setText(StringUtil.getFormatDate(dayOfRemind,
					"dd/MM/yyyy HH:mm:ss"));
			mNote.setDateOfRemind(dayOfRemind);
			mNote.setTime(time);
			check.setVisibility(View.VISIBLE);
			check.setChecked(false);
		}
	}

	/************************************************************************************************/
}
