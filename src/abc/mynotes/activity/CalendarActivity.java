/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package abc.mynotes.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CalendarActivity extends Activity  implements CalendarView.OnCellTouchListener{
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.vct";

	
	CalendarView mView = null;
	TextView mHit;
	Handler mHandler = new Handler();
	
	TimePicker time ;
	ImageView preMon = null;
	ImageView nextMon = null;
	TextView txtViewMonthYear ;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        mView = (CalendarView)findViewById(R.id.calendar);
        mView.setOnCellTouchListener(this);
        
        preMon = (ImageView) findViewById(R.id.preMon);
        nextMon = (ImageView) findViewById(R.id.nextMon);
        
        preMon.setOnClickListener( preOnclick );
        nextMon.setOnClickListener( nextOnclick );
        
        txtViewMonthYear = (TextView) findViewById(R.id.txvMonthYear); 
        txtViewMonthYear.setText( String.valueOf( mView.getMonth()+1 ) + " / "+ String.valueOf( mView.getYear() ));
        
    }

	public void onTouch(Cell cell) {
		Intent intent = getIntent();
		String action = intent.getAction();
		if(action.equals(Intent.ACTION_PICK) || action.equals(Intent.ACTION_GET_CONTENT)) {
			Intent ret = new Intent();
//			ret.putExtra("year", mView.getYear());
//			ret.putExtra("month", mView.getMonth());
//			ret.putExtra("day", cell.getDayOfMonth());
//						
//			ret.putExtra("sec", 0);
			
			if(cell.getListNote() == null || cell.getListNote().size() ==0)
				this.setResult(RESULT_CANCELED, ret);
			else{
				ret.putParcelableArrayListExtra("NOTE", (ArrayList<? extends Parcelable>) cell.getListNote());			
				this.setResult(RESULT_OK, ret);
			}
			finish();
			return;
		}
		int day = cell.getDayOfMonth();
		if(mView.firstDay(day))
			mView.previousMonth();
		else if(mView.lastDay(day))
			mView.nextMonth();
		else
			return;

		mHandler.post(new Runnable() {
			public void run() {
				Toast.makeText(CalendarActivity.this, DateUtils.getMonthString(mView.getMonth()+1, DateUtils.LENGTH_LONG) + " "+mView.getYear(), Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public OnClickListener preOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mView.previousMonth();
			txtViewMonthYear.setText( String.valueOf( mView.getMonth()+1) + " / "+ String.valueOf( mView.getYear() ));
		}		
	};

	public OnClickListener nextOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mView.nextMonth();
			txtViewMonthYear.setText( String.valueOf( mView.getMonth()+1 ) + " / "+ String.valueOf( mView.getYear() ));
		}
	};

}