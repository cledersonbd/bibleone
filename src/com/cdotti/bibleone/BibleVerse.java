package com.cdotti.bibleone;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class BibleVerse implements Parcelable{
	private Integer numChapter;
	private Integer id;
	private String text;
	
	public BibleVerse (int _numChapter, int _id, String _text) {
		setNumChapter(_numChapter);
		setId(_id);
		setText(_text);
	}
	
	public BibleVerse (Parcel in) {
		readFromParcel(in);
	}
	
	public Integer getNumChapter() {
		return numChapter;
	}

	public void setNumChapter(Integer numChapter) {
		this.numChapter = numChapter;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	/** * * Called from the constructor to create this 
	 * * object from a parcel. 
	 * * 
	 * * @param in parcel from which to re-create object 
	 * */ 
	private void readFromParcel(Parcel in) {   
		// We just need to read back each 
		// field in the order that it was 
		// written to the parcel 
		id = in.readInt();
		text = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// We just need to write each field into the
		// parcel. When we read from parcel, they
		// will come back in the same order
		dest.writeInt(id);
		dest.writeString(text);		
	}

	/** *
	 *  * This field is needed for Android to be able to 
	 *  * create new objects, individually or as arrays. 
	 *  * 
	 *  * This also means that you can use use the default 
	 *  * constructor to create the object and use another 
	 *  * method to hyrdate it as necessary. 
	 *  * 
	 *  * I just find it easier to use the constructor. 
	 *  * It makes sense for the way my brain thinks ;-) 
	 *  * 
	 *  */ 
	
	@SuppressWarnings({ "rawtypes" })
	public static final Parcelable.Creator CREATOR = 
			new Parcelable.Creator() {
				public BibleVerse createFromParcel(Parcel in) {
					return new BibleVerse(in);
				}
				
				public BibleVerse[] newArray(int size) {
					return new BibleVerse[size];
				}
				
				@SuppressWarnings("unused")
				public ArrayList<BibleVerse> newArrayList() {
					return new ArrayList<BibleVerse>();
				}
			};
}
