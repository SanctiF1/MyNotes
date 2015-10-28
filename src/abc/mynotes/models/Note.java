package abc.mynotes.models;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
	private int id;
	private String title;
	private String content;
	private List<Task> listTask;
	private String dateOfCreate;
	private String dateOfRemind;
	private String dateOfUpdate;
	private String time;
	private boolean deleted;

	public Note(){
		super();
	}
	

	@SuppressWarnings("unchecked")
	public Note(Parcel in) {
		this.id = in.readInt();
		this.title = in.readString();
		this.content = in.readString();
		if(listTask == null)
			listTask = new ArrayList<Task>();
		in.readList(this.listTask, Task.class.getClassLoader());
		this.dateOfCreate = in.readString();
		this.dateOfRemind = in.readString();
		this.dateOfUpdate = in.readString();
		this.time = in.readString();
		this.deleted = in.readByte()== 1? true: false ;
	}
	
	public Note(int id, String title, String content, List<Task> listTask,
			String dateOfCreate, String dateOfRemind, String dateOfUpdate,
			String time, boolean deleted) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.listTask = listTask;
		this.dateOfCreate = dateOfCreate;
		this.dateOfRemind = dateOfRemind;
		this.dateOfUpdate = dateOfUpdate;
		this.time = time;
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Task> getListTask() {
		return listTask;
	}

	public void setListTask(List<Task> listTask) {
		this.listTask = listTask;
	}

	public String getDateOfCreate() {
		return dateOfCreate;
	}

	public void setDateOfCreate(String dateOfCreate) {
		this.dateOfCreate = dateOfCreate;
	}

	public String getDateOfRemind() {
		return dateOfRemind;
	}

	public void setDateOfRemind(String dateOfRemind) {
		this.dateOfRemind = dateOfRemind;
	}

	public String getDateOfUpdate() {
		return dateOfUpdate;
	}

	public void setDateOfUpdate(String dateOfUpdate) {
		this.dateOfUpdate = dateOfUpdate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString( content);
		dest.writeList( listTask);
		dest.writeString( dateOfCreate);
		dest.writeString( dateOfRemind);
		dest.writeString( dateOfUpdate);
		dest.writeString( time);
		dest.writeByte((byte) (deleted? 1: 0));
				
	}
	
	 @SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR =
		    	new Parcelable.Creator() {
		            public Note createFromParcel(Parcel in) {
		                return new Note(in);
		            }
		 
		            public Note[] newArray(int size) {
		                return new Note[size];
		            }
	};

}
