package com.cdotti.bibleone;

import java.sql.Date;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class VerseFav implements Parcelable {
	private Integer dbID;
	private Integer bookID;
	private String bookName;
	private Integer numChapter;
	private Integer idNum;
	private String text;
	private Date date;
	
	public VerseFav (Integer _dbID, Integer _bookID, String _bookName, int _numChapter, int _id, String _text) {
		setDbID(_dbID);
		setBookID(_bookID);
		setBookName(_bookName);
		setNumChapter(_numChapter);
		setVerseNum(_id);
		setText(_text);
	}
	
	public VerseFav (Integer _dbID, Integer _bookID, String _bookName, int _numChapter, int _id, String _text, Date dt) {
		setDbID(_dbID);
		setBookID(_bookID);
		setBookName(_bookName);
		setNumChapter(_numChapter);
		setVerseNum(_id);
		setText(_text);
		setDate(dt);
	}
	
	public VerseFav (BibleVerse bVerse) {
		setDbID(-1);	// Novo favorito (a partir de versiculo padrao)
		setBookID(bVerse.getBookID());
		setBookName("");
		setNumChapter(bVerse.getNumChapter());
		setVerseNum(bVerse.getId());
		setText(bVerse.getText());
		//setDate(dt);
	}
	
	public VerseFav (Parcel in) {
		readFromParcel(in);
	}
	
	public Integer getNumChapter() {
		return numChapter;
	}

	public void setNumChapter(Integer numChapter) {
		this.numChapter = numChapter;
	}
	
	public Integer getVerseNum() {
		return idNum;
	}
	public void setVerseNum(Integer id) {
		this.idNum = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean isHeader() {
		return getVerseNum() < 0 && getNumChapter() > 0;
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
		idNum = in.readInt();
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
		dest.writeInt(idNum);
		dest.writeString(text);		
	}

	public Integer getBookID() {
		return bookID;
	}

	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public Integer getDbID() {
		return dbID;
	}

	public void setDbID(Integer dbID) {
		this.dbID = dbID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
				public VerseFav createFromParcel(Parcel in) {
					return new VerseFav(in);
				}
				
				public VerseFav[] newArray(int size) {
					return new VerseFav[size];
				}
				
				@SuppressWarnings("unused")
				public ArrayList<VerseFav> newArrayList() {
					return new ArrayList<VerseFav>();
				}
			};
}
