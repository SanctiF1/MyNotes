package abc.mynotes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
	private int id;
	private String name;
	private int idNote;
	private String dateOfCreate;
	private String dateOfUpdate;
	private boolean deleted;
	private boolean done;

	public Task() {
		super();
	}
	
	public Task( Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.idNote =  in.readInt();
		this.dateOfCreate = in.readString();
		this.dateOfUpdate = in.readString();
		this.deleted = in.readByte()== 1? true: false ;
		this.done = in.readByte()== 1? true: false ;
	}

	public Task(int id, String name, int idNote, String dateOfCreate,
			String dateOfUpdate, boolean deleted, boolean done) {
		super();
		this.id = id;
		this.name = name;
		this.idNote = idNote;
		this.dateOfCreate = dateOfCreate;
		this.dateOfUpdate = dateOfUpdate;
		this.deleted = deleted;
		this.done = done;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIdNote() {
		return idNote;
	}

	public void setIdNote(int idNote) {
		this.idNote = idNote;
	}

	public String getDateOfCreate() {
		return dateOfCreate;
	}

	public void setDateOfCreate(String dateOfCreate) {
		this.dateOfCreate = dateOfCreate;
	}

	public String getDateOfUpdate() {
		return dateOfUpdate;
	}

	public void setDateOfUpdate(String dateOfUpdate) {
		this.dateOfUpdate = dateOfUpdate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(name); 
		dest.writeInt(idNote);
		dest.writeString( dateOfCreate);
		dest.writeString( dateOfUpdate);
		dest.writeByte((byte) (deleted ? 1: 0));
		dest.writeByte((byte) (done ? 1: 0));
				
	}
	
	 @SuppressWarnings("rawtypes")
		public static final Parcelable.Creator CREATOR =
			    	new Parcelable.Creator() {
			            public Task createFromParcel(Parcel in) {
			                return new Task(in);
			            }
			 
			            public Task[] newArray(int size) {
			                return new Task[size];
			            }
		};

}
