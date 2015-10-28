package abc.mynotes.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import abc.mynotes.database.DbAdapter;
import abc.mynotes.database.lstNotesAdapter;
import abc.mynotes.models.Note;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class listAct extends Activity implements OnClickListener {

	ListView listView;
	DbAdapter dataAdapter;
	List<Note> lstNote = new ArrayList<Note>();
	lstNotesAdapter lstAdapter;
	ImageView btnAdd, btnCalendar, btnSort, btnView;
	Context mContext = this;
	private final int REQUEST_CODE_ADD = 1;
	private final int REQUEST_CODE_EDIT = 2;
	private final int REQUEST_CODE_CAL = 3;
	int chooseOnView = 0;
	AlertDialog.Builder builder;
	AlertDialog dialog;
	public static final String PREFS_NAME = "UserSelectionPref";
	public static final String SELECTION_KEY = "SelectionKey";

	public static final String SORTMODE_VALUE = "SORTMODEVALUE";
	public static final String SORTMODE_KEY = "SORTMODEKEY";

	public static final int CREATED_DATE_KEY = 0, UPDATED_DATE_KEY = 1;
	public static final int TITLE_KEY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_list);
		listView = (ListView) findViewById(R.id.listView1);
		btnAdd = (ImageView) findViewById(R.id.list_btnAdd);
		btnCalendar = (ImageView) findViewById(R.id.list_btnCalendar);
		btnSort = (ImageView) findViewById(R.id.list_btnSort);
		btnView = (ImageView) findViewById(R.id.list_btnView);

		/*-----------------------------------------------------Set OnClick------------------------------------------*/
		btnAdd.setOnClickListener(this);
		btnCalendar.setOnClickListener(this);
		btnSort.setOnClickListener(this);
		btnView.setOnClickListener(this);
		onBindingNotes();
		btnAdd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(listAct.this, noteAct.class);
				startActivityForResult(intent, REQUEST_CODE_ADD);
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final TextView txvTitle = (TextView) view
						.findViewById(R.id.txvTitle_rowlist);

				Note note = (Note) txvTitle.getTag();
				Intent intent = new Intent(listAct.this, noteAct.class);
				intent.putExtra("CUR_NOTE", note);
				startActivityForResult(intent, REQUEST_CODE_EDIT);
			}
		});

		listView.setOnItemLongClickListener(onItemLongClick);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				onBindingNotes();
			}
		} else if (requestCode == REQUEST_CODE_CAL) {
			if (resultCode == RESULT_OK) {
				super.onActivityResult(requestCode, resultCode, data);
				lstNote = data.getParcelableArrayListExtra("NOTE");

				lstAdapter = new lstNotesAdapter(mContext, lstNote);
				lstAdapter.notifyDataSetChanged();
				listView.setAdapter(lstAdapter);
			}
		}
	}

	public void onBindingNotes() {
		dataAdapter = new DbAdapter(mContext);
		try {
			dataAdapter.open();
			lstNote = dataAdapter.getAllNotes();
			dataAdapter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		lstAdapter = new lstNotesAdapter(mContext, lstNote);
		listView.setAdapter(lstAdapter);
	}

	public OnItemLongClickListener onItemLongClick = new AdapterView.OnItemLongClickListener() {
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			final TextView txvTitle = (TextView) view
					.findViewById(R.id.txvTitle_rowlist);
			final Note note = (Note) txvTitle.getTag();

			dataAdapter = new DbAdapter(mContext);
			AlertDialog.Builder mydialog = new AlertDialog.Builder(listAct.this);
			mydialog.setTitle("Confirm !");
			mydialog.setMessage("Are you sure to delete this note?");
			AlertDialog dialog;

			mydialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							try {
								dataAdapter.open();
								dataAdapter.deleteNote(String.valueOf(note
										.getId()));
								dataAdapter.close();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							onBindingNotes();
						}
					});
			mydialog.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});//
			dialog = mydialog.create();
			dialog.show();
			return true;
		};

	};

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.list_btnAdd:
			Intent intent = new Intent(listAct.this, noteAct.class);
			startActivity(intent);
			break;

		case R.id.list_btnCalendar:
			startActivityForResult(
					new Intent(Intent.ACTION_PICK).setDataAndType(null,
							CalendarActivity.MIME_TYPE), REQUEST_CODE_CAL);
			break;
		case R.id.list_btnView:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("View on:");
			builder.setIcon(R.drawable.ic_launcher);
			final CharSequence[] muc = { "List", "Grid", "Refresh" };
			SharedPreferences spView = getSharedPreferences(PREFS_NAME, 0);
			int modeView = spView.getInt(SELECTION_KEY, 0);
			builder.setSingleChoiceItems(muc, modeView,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (which) {
							case 0:
								chooseOnView = 0;
								dialog.cancel();
								break;
							case 1:
								chooseOnView = 1;
								dialog.cancel();
								Intent intent = new Intent(listAct.this,
										gridAct.class);
								storeSelectection(chooseOnView);
								startActivity(intent);
								break;
							case 2:
								onBindingNotes();
								dialog.cancel();
								break;
							}
						}
					});
			dialog = builder.create();
			dialog.show();
			break;
		case R.id.list_btnSort:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("Sort by:");
			builder.setIcon(R.drawable.ic_launcher);
			final CharSequence[] sortFiels = { "Created Date","Updated Date", "Title" };
			SharedPreferences sp = getSharedPreferences(SORTMODE_KEY, 0);
			int modeSort = sp.getInt(SORTMODE_VALUE, 0);
			builder.setSingleChoiceItems(sortFiels, modeSort,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (which) {
							case 0:
								sortListNote(CREATED_DATE_KEY);
								dialog.cancel();
								break;
							case 1:
								sortListNote(UPDATED_DATE_KEY);
								dialog.cancel();
								break;
							case 2:
								sortListNote(TITLE_KEY);
								dialog.cancel();
								break;

							}
						}
					});
			dialog = builder.create();
			dialog.show();
			break;
		}
	}

	// shared preferences
	public void storeSelectection(int index) {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(SELECTION_KEY, index);
		editor.commit();
	}

	public void storeModeSort(int index) {
		SharedPreferences sp = getSharedPreferences(SORTMODE_KEY, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(SORTMODE_VALUE, index);
		editor.commit();
	}

	public void sortListNote(int key) {
		switch (key) {
		case CREATED_DATE_KEY:
			Collections.sort(lstNote, new Comparator<Note>() {
				public int compare(Note m1, Note m2) {
					return m1.getDateOfCreate().compareToIgnoreCase(
							m2.getDateOfCreate());
				}
			});
			Collections.reverse(lstNote);
			break;
		case UPDATED_DATE_KEY:
			Collections.sort(lstNote, new Comparator<Note>() {
				public int compare(Note m1, Note m2) {
					return m1.getDateOfUpdate().compareToIgnoreCase(
							m2.getDateOfUpdate());
				}
			});
			Collections.reverse(lstNote);
			break;
		case TITLE_KEY:
			Collections.sort(lstNote, new Comparator<Note>() {
				public int compare(Note m1, Note m2) {
					return m1.getTitle().compareToIgnoreCase(m2.getTitle());
				}
			});
			break;
		default:
			break;
		}
		storeModeSort(key);
		lstAdapter.notifyDataSetChanged();
	}
}
