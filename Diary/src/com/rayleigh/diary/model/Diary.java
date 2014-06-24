package com.rayleigh.diary.model;

public class Diary {
	private String date;
	private String week;
	private String weather;
	private String diaryInfo;
	private String diaryTitle;
	private int id;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getDiaryInfo() {
		return diaryInfo;
	}
	public void setDiaryInfo(String diaryInfo) {
		this.diaryInfo = diaryInfo;
	}
	public String getDiaryTitle() {
		return diaryTitle;
	}
	public void setDiaryTitle(String diaryTitle) {
		this.diaryTitle = diaryTitle;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Diary [date=" + date + ", week=" + week + ", weather="
				+ weather + ", diaryInfo=" + diaryInfo + ", diaryTitle="
				+ diaryTitle + ", id=" + id + "]";
	}
}
