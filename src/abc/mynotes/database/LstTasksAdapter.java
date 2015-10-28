package abc.mynotes.database;

import java.util.List;

import abc.mynotes.activity.R;
import abc.mynotes.activity.listAct;
import abc.mynotes.models.Note;
import abc.mynotes.models.Task;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LstTasksAdapter extends BaseAdapter {

	private List<Task> mlstTasks;
	private Context mContext;
	private static LayoutInflater inflater=null;
	
	public LstTasksAdapter(Context context, List<Task> lstTasks ) {
		// TODO Auto-generated constructor stub
		this.mlstTasks = lstTasks;
		this.mContext = context;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mlstTasks.size();
	}

	public Task getItem(int position) {
		// TODO Auto-generated method stub
		return mlstTasks.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mlstTasks.get(position).getId();
	}
	
//	@Override
//	public void notifyDataSetChanged() {
//		// TODO Auto-generated method stub
//		super.notifyDataSetChanged();
//	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.task_rowlist, parent, false);
		    TextView name = (TextView) rowView.findViewById(R.id.txvName_listTask);
		    final CheckBox chkDone = (CheckBox) rowView.findViewById(R.id.checkDone_listTask);		    
		    
		    final int location = position;		    
		    
		    Task task = mlstTasks.get(position);
		    name.setText(task.getName());
		    name.setTag(task);
		    chkDone.setChecked(task.isDone());
		    
		    chkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					//Toast.makeText(mContext, "Check", Toast.LENGTH_SHORT).show();
					 mlstTasks.get(location).setDone(chkDone.isChecked());
				}
			});
			    
		    if(task.getId() == -2){ //task do ct tao : new task
		    	name.setTextColor(Color.LTGRAY);
		    	chkDone.setVisibility(View.GONE);
		    }

		    return rowView;
	}

}
