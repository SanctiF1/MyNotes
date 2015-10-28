package abc.mynotes.activity;

import java.util.List;

import abc.mynotes.database.DbAdapter;
import abc.mynotes.models.Note;
import abc.mynotes.models.Task;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class edit_noteAct extends Activity {
	EditText title, content;
	ImageView addTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note);
		/*---------------------------------------------Init controls--------------------------------*/
		title = (EditText) findViewById(R.id.edit_note_title);
		content = (EditText) findViewById(R.id.edit_note_content);
		addTask = (ImageView) findViewById(R.id.edit_note_btnAddTask);
		/*------------------------------------------------------------------------------------------*/
		Bundle bundle = getIntent().getExtras();
		int _id = bundle.getInt("id", 0);
		DbAdapter db = new DbAdapter(getApplicationContext());
		try {
			db.open();
		} catch (Exception e) {
			// TODO: handle exception
		}
		Note mNote = db.getNoteById(_id);
		/*--------------------------------------------bindingtext----------------------------------*/
//		title.setText(mNote.getTitle());
//		content.setText(mNote.getContent());
//		List<Task> listTask = db.getTask(_id);
//		TextView[] tv = new TextView[listTask.size()];
//		CheckBox[] ck = new CheckBox[listTask.size()];
//		for (int i = 0; i < listTask.size(); i++) {
//			tv[i] = new TextView(this);
//			tv[i].setText(listTask.get(i).getName() + "");
//			ck[i] = new CheckBox(this);
//		}

	}
}
