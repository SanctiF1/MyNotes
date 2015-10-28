package abc.mynotes.database;

import java.util.List;

import abc.mynotes.activity.R;
import abc.mynotes.activity.listAct;
import abc.mynotes.models.Note;
import abc.mynotes.models.Task;
import abc.mynotes.utils.StringUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class grdNotesAdapter extends BaseAdapter {

	private List<Note> mlstNotes;
	private Context mContext;

	public grdNotesAdapter(Context context, List<Note> lstNotes) {
		// TODO Auto-generated constructor stub
		this.mlstNotes = lstNotes;
		this.mContext = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mlstNotes.size();
	}

	public Note getItem(int position) {
		// TODO Auto-generated method stub
		return mlstNotes.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mlstNotes.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.grid_item, parent, false);
		TextView title = (TextView) rowView.findViewById(R.id.grid_txtTitle);
		TextView day = (TextView) rowView.findViewById(R.id.grid_txtDay);
		ImageView check = (ImageView) rowView.findViewById(R.id.grid_check);
		Note note = mlstNotes.get(position);
		if (note.getTitle().trim().length() >= 5) {
			title.setText(note.getTitle().substring(0, 4) + "..");
		} else
			title.setText(note.getTitle());
		title.setTag(note);
		String strRemindDate = "";
		if (note.getDateOfRemind() != null
				&& note.getDateOfRemind().trim().length() > 0)
			strRemindDate = StringUtil.getFormatDate(note.getDateOfRemind(),
					StringUtil.VAL_DATE_FORMAT);
		day.setText(strRemindDate);

		List<Task> listTask = note.getListTask();
		int flag = 1;
		if (listTask != null) {
			for (int i = 0; i < listTask.size(); i++) {
				if (!listTask.get(i).isDone()) {
					flag = 0;
					break;
				}
			}
			if (listTask.size() == 0) {
				flag = 0;
			}
		} else
			flag = 0;
		if (flag == 1) {
			check.setImageResource(R.drawable.checkalready);
		}
		return rowView;
	}

}
