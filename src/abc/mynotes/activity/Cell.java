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

import java.util.List;

import abc.mynotes.models.Note;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Cell {
	private static final String TAG = "Cell";
	protected Rect mBound = null;
	protected int mDayOfMonth = 1;	// from 1 to 31
	protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);
	int dx, dy;
	private  int marked = -1;
	protected List<Note> mlstNote = null;
	
	public int getMarked() {
		return marked;
	}

	public void setMarked(int marked) {
		this.marked = marked;
	}

	public Cell(int dayOfMon, Rect rect, float textSize, boolean bold) {
		mDayOfMonth = dayOfMon;
		mBound = rect;
		mPaint.setTextSize(textSize/*26f*/);
		mPaint.setColor(Color.BLACK);
		if(bold) mPaint.setFakeBoldText(true);
		
		dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
		dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
	}
	
	public Cell(int dayOfMon, Rect rect, float textSize) {
		this(dayOfMon, rect, textSize, false);
	}
	
	protected void draw(Canvas canvas) {
		Paint tPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		tPaint.setFlags(Paint.LINEAR_TEXT_FLAG);
		
		tPaint.setTextSize(10);
		tPaint.setColor(Color.BLUE);
		
//		canvas.drawText(String.valueOf("Vct"), mBound.centerX()  + 5, mBound.centerY() + dy +10, tPaint);
		canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY() + dy, mPaint);
	}
	
	public int getDayOfMonth() {
		return mDayOfMonth;
	}
	
	public boolean hitTest(int x, int y) {
		return mBound.contains(x, y); 
	}
	
	public Rect getBound() {
		return mBound;
	}
	
	public String toString() {
		return String.valueOf(mDayOfMonth)+"("+mBound.toString()+")";
	}
	
	public void setListNote(List<Note> lstNote ){
		mlstNote = lstNote;
	}
	
	public List<Note> getListNote(){
		return mlstNote;
	}
	
	
}

