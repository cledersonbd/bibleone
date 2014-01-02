package com.cdotti.bibleone;

public class BibleBook {
	private int id;
	private String name;
	private int id_testament;

	public BibleBook (int _id, String _name) {
		setId(_id);
		setName(_name);
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

	public int getId_testament() {
		return id_testament;
	}

	public void setId_testament(int id_testament) {
		this.id_testament = id_testament;
	}
}
