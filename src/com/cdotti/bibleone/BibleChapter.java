package com.cdotti.bibleone;

import java.util.ArrayList;

public class BibleChapter {
	private Integer id;
	private Integer book_id;
	private Integer chapter;
	private ArrayList<BibleVerse> arrListText;
	
	public BibleChapter(int _id, int _book_id, int _chapter, ArrayList<BibleVerse> _bibleverse) {
		setId(_id);
		setBook_id(_book_id);
		setChapter(_chapter);
		setArrListText(_bibleverse);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public Integer getChapter() {
		return chapter;
	}
	public void setChapter(Integer chapter) {
		this.chapter = chapter;
	}
	public ArrayList<BibleVerse> getArrListText() {
		return arrListText;
	}
	public void setArrListText(ArrayList<BibleVerse> arrListText) {
		this.arrListText = arrListText;
	}	
}
