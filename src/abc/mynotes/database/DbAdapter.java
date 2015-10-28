package abc.mynotes.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import abc.mynotes.models.Note;
import abc.mynotes.models.Task;
import abc.mynotes.utils.StringUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

	// Note
	public static final String KEY_ID_NOTE = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_DAY_CREATE = "dateOfCreate";
	public static final String KEY_DAY_REMIND = "dateOfRemind";
	public static final String KEY_DAY_UPDATE = "dateOfUpdate";
	public static final String KEY_TIME = "time";
	public static final String KEY_DELETED = "deleted";
	// Task
	public static final String KEY_ID_TASK = "_idTask";
	public static final String KEY_NAME_TASK = "nameTask";
	public static final String KEY_DELETED_TASK = "deletedTask";
	public static final String KEY_DONE_TASK = "done";
	public static final String KEY_FID_NOTE = "_idNote";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "NotesDb";
	private static final String DATABASE_TABLE_NOTE = "NoteTbl";
	private static final String DATABASE_TABLE_TASK = "TaskTbl";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;
	private static final String DATABASE_CREATE_NOTE = "CREATE TABLE NoteTbl (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "title TEXT NOT NULL, content TEXT ,dateOfCreate TEXT , "
			+ "dateOfRemind TEXT , dateOfUpdate TEXT , time TEXT );";

	private static final String DATABASE_CREATE_TASK = "CREATE TABLE TaskTbl (_idTask INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "nameTask TEXT , done INTEGER DEFAULT 0, "
			+ "dateOfCreate TEXT, dateOfUpdate TEXT, "
			+ "_idNote INTEGER, FOREIGN KEY(_idNote) REFERENCES NoteTbl(_id) );";

	// 1 is done - true & deleted - true

	public static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE_NOTE);
			db.execSQL(DATABASE_CREATE_TASK);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TASK);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_NOTE);
			onCreate(db);

		}
	}

	public DbAdapter open() throws Exception {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public DbAdapter(Context context) {
		mCtx = context;
	}

	/*
	 * ******************************************************************data
	 * binding
	 */

	public List<Note> getAllNotes() {
		mDb = mDbHelper.getReadableDatabase();
		Cursor curNote = null;
		Cursor curTask = null;
		try {
			mDb.beginTransaction();
			curNote = mDb
					.rawQuery("SELECT * FROM " + DATABASE_TABLE_NOTE, null);
			curTask = mDb
					.rawQuery("SELECT * FROM " + DATABASE_TABLE_TASK, null);
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("GET_DATA_EXCEPTION", e.getMessage());
		} finally {
			mDb.endTransaction();
		}
		return tranferToNote(curNote, curTask);
	}

	public List<Note> getAllRemindNotes() {
		mDb = mDbHelper.getReadableDatabase();
		Cursor curNote = null;
		Cursor curTask = null;
		try {
			mDb.beginTransaction();
			curNote = mDb.rawQuery(" SELECT * FROM " + DATABASE_TABLE_NOTE
					+ " WHERE IFNULL(" + KEY_DAY_REMIND + ",'') != '' ", null);
			curTask = mDb.rawQuery(" SELECT * FROM " + DATABASE_TABLE_TASK
					+ " WHERE " + KEY_FID_NOTE + " IN (SELECT " + KEY_ID_NOTE
					+ " FROM " + DATABASE_TABLE_NOTE + " WHERE IFNULL("
					+ KEY_DAY_REMIND + ",'') != '') ", null);
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("GET_DATA_EXCEPTION", e.getMessage());
		} finally {
			mDb.endTransaction();
		}
		return tranferToNote(curNote, curTask);
	}

	public Note getNoteById(int noteId) {
		mDb = mDbHelper.getReadableDatabase();
		Cursor curNote = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE_NOTE
				+ " WHERE _id = ?", new String[] { String.valueOf(noteId) });
		Cursor curTask = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE_TASK
				+ " WHERE " + KEY_FID_NOTE + " = ?",
				new String[] { String.valueOf(noteId) });
		List<Note> rtnList = tranferToNote(curNote, curTask);
		if (rtnList.size() == 0)
			return null;
		return rtnList.get(0);
	}

	public Note getNoteRemind(String currentDay) {
		mDb = mDbHelper.getReadableDatabase();
		Cursor curNote = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE_NOTE
				+ " WHERE " + KEY_DAY_REMIND + " > ?" + " ORDER BY "
				+ KEY_DAY_REMIND + " ASC LIMIT 1 ",
				new String[] { String.valueOf(currentDay) });
		// Cursor curTask = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE_TASK,
		// null);
		List<Note> rtnList = tranferToNote(curNote, null);
		if (rtnList.size() == 0)
			return null;
		return rtnList.get(0);
	}

	public List<Note> tranferToNote(Cursor curNote, Cursor curTask) {
		List<Note> lstNote = new ArrayList<Note>();

		if (curNote.moveToFirst()) {
			do {
				Note note = new Note();
				note.setId((int) getNumValueCursor(curNote, KEY_ID_NOTE,
						curNote.getPosition()));
				note.setTitle(getStringValueCursor(curNote, KEY_TITLE,
						curNote.getPosition()));
				note.setContent(getStringValueCursor(curNote, KEY_CONTENT,
						curNote.getPosition()));
				note.setDateOfCreate(getStringValueCursor(curNote,
						KEY_DAY_CREATE, curNote.getPosition()));
				note.setDateOfRemind(getStringValueCursor(curNote,
						KEY_DAY_REMIND, curNote.getPosition()));
				note.setDateOfUpdate(getStringValueCursor(curNote,
						KEY_DAY_UPDATE, curNote.getPosition()));
				note.setTime(getStringValueCursor(curNote, KEY_TIME,
						curNote.getPosition()));
				note.setDeleted(false);
				if (curTask != null)
					note.setListTask(tranferToTask(curTask, note.getId()));
				lstNote.add(note);
			} while (curNote.moveToNext());
		}
		if (curTask != null)
			curTask.close();
		curNote.close();
		return lstNote;
	}

	public List<Task> tranferToTask(Cursor curTask, int noteId) {
		List<Task> lstTask = new ArrayList<Task>();
		if (curTask.moveToFirst()) {
			do {
				Task task = new Task();
				task.setIdNote((int) getNumValueCursor(curTask, KEY_FID_NOTE,
						curTask.getPosition()));
				if (task.getIdNote() == noteId) {
					task.setId((int) getNumValueCursor(curTask, KEY_ID_TASK,
							curTask.getPosition()));
					task.setName(getStringValueCursor(curTask, KEY_NAME_TASK,
							curTask.getPosition()));
					task.setDone(getBoolValueCursor(curTask, KEY_DONE_TASK,
							curTask.getPosition()));
					task.setDeleted(false);
					task.setDateOfCreate(getStringValueCursor(curTask,
							KEY_DAY_CREATE, curTask.getPosition()));
					task.setDateOfUpdate(getStringValueCursor(curTask,
							KEY_DAY_UPDATE, curTask.getPosition()));
					lstTask.add(task);
				}
			} while (curTask.moveToNext());
		}
		return lstTask;
	}

	public long insertNote(Note note) {
		long nId = -1;
		if (note.isDeleted())
			return -1;
		mDb.beginTransaction();
		try {
			ContentValues insertedValue = new ContentValues();
			insertedValue.put(KEY_TITLE, note.getTitle());
			insertedValue.put(KEY_CONTENT, note.getContent());
			insertedValue.put(KEY_DAY_CREATE, StringUtil.getCurrentDate());
			insertedValue.put(KEY_DAY_REMIND, note.getDateOfRemind());
			insertedValue.put(KEY_DAY_UPDATE, StringUtil.getCurrentDate());
			insertedValue.put(KEY_TIME, note.getTime());
			nId = mDb.insert(DATABASE_TABLE_NOTE, null, insertedValue);
			String noteId = String.valueOf(nId);
			Log.v("MAXID", noteId);
			if (note.getListTask() != null) {
				for (int i = 0; i < note.getListTask().size(); i++) {
					Task insertTask = note.getListTask().get(i);
					if (insertTask.getId() == -1 && !insertTask.isDeleted()) {
						insertTask(insertTask, noteId);
					}
				}
			}
			mDb.setTransactionSuccessful();
			Log.v("INSERT", "Success");
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("DB_NOTE_EXCEPTION", e.getMessage());

		} finally {
			mDb.endTransaction();
		}
		return nId;
	}

	public long updatetNote(Note note) {
		mDb.beginTransaction();
		try {
			ContentValues contentNote = new ContentValues();
			contentNote.put(KEY_TITLE, note.getTitle());
			contentNote.put(KEY_CONTENT, note.getContent());
			// contentNote.put(KEY_DAY_CREATE, note.getContent());
			contentNote.put(KEY_DAY_REMIND, note.getDateOfRemind());
			contentNote.put(KEY_DAY_UPDATE, StringUtil.getCurrentDate());
			contentNote.put(KEY_TIME, note.getTime());
			String noteId = String.valueOf(note.getId());
			mDb.update(DATABASE_TABLE_NOTE, contentNote, "_id = " + noteId,
					null);
			if (note.getListTask() != null) {
				for (int i = 0; i < note.getListTask().size(); i++) {
					Task task = note.getListTask().get(i);
					if (!task.isDeleted()) {
						if (task.getId() == -1)// task duoc tao moi set id = -1
							insertTask(task, noteId);
						else if (task.getId() >= 0)
							updateTask(task, String.valueOf(task.getId()));
					} else
						deleteTask(String.valueOf(task.getId()));
				}
			}
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("DB_NOTE_EXCEPTIONt", e.getMessage());
		} finally {
			mDb.endTransaction();
		}
		return (long) note.getId();
	}

	private long insertTask(Task task, String noteId) {

		if (!task.isDeleted()) {
			ContentValues contentTask = new ContentValues();
			contentTask.put(KEY_FID_NOTE, noteId);
			contentTask.put(KEY_NAME_TASK, task.getName());
			contentTask.put(KEY_DONE_TASK, task.isDone());
			contentTask.put(KEY_DAY_CREATE, StringUtil.getCurrentDate());
			contentTask.put(KEY_DAY_UPDATE, StringUtil.getCurrentDate());
			return mDb.insert(DATABASE_TABLE_TASK, null, contentTask);
		}
		return -1;
	}

	private int updateTask(Task task, String taskId) {
		if (!task.isDeleted()) {
			ContentValues contentTask = new ContentValues();
			// contentTask.put(KEY_FID_NOTE, 0);
			contentTask.put(KEY_NAME_TASK, task.getName());
			contentTask.put(KEY_DONE_TASK, task.isDone());
			// contentTask.put(KEY_DAY_CREATE, fDate);
			contentTask.put(KEY_DAY_UPDATE, StringUtil.getCurrentDate());
			return mDb.update(DATABASE_TABLE_TASK, contentTask, KEY_ID_TASK
					+ " = " + taskId, null);
		}
		return -1;
	}

	private int deleteTask(String taskId) {
		return mDb.delete(DATABASE_TABLE_TASK, KEY_ID_TASK + " = " + taskId,
				null);
	}

	public boolean deleteNote(String noteId) {
		mDb.beginTransaction();
		try {
			mDb.delete(DATABASE_TABLE_TASK, "_idNote = " + noteId, null);
			mDb.delete(DATABASE_TABLE_NOTE, "_Id = " + noteId, null);
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("DELETE_EXCEPTION", e.getMessage());
		} finally {
			mDb.endTransaction();
		}

		return true;
	}

	public void deleteAlarm(int noteId) {
		ContentValues values = new ContentValues();
		values.putNull(KEY_TIME);
		values.putNull(KEY_DAY_REMIND);
		mDb.update(DATABASE_TABLE_NOTE, values, "_id = ?",
				new String[] { String.valueOf(noteId) });
	}

	private String getStringValueCursor(Cursor cursor, String columnName,
			int pos) {
		int columnId = cursor.getColumnIndexOrThrow(columnName);
		if (cursor.moveToPosition(pos) && columnId >= 0) {
			return cursor.getString(columnId);
		}
		return "";
	}

	private double getNumValueCursor(Cursor cursor, String columnName, int pos) {
		int columnId = cursor.getColumnIndexOrThrow(columnName);
		if (cursor.moveToPosition(pos) && columnId >= 0) {
			return cursor.getDouble(columnId);
		}
		return -1;
	}

	private boolean getBoolValueCursor(Cursor cursor, String columnName, int pos) {
		int columnId = cursor.getColumnIndexOrThrow(columnName);
		int intVal = 0;
		if (cursor.moveToPosition(pos) && columnId >= 0) {
			intVal = cursor.getInt(columnId);
		}
		return intVal == 0 ? false : true;
	}

}
