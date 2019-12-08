package com.sp.bbs;

public class Board {
	private int listNum;
	private int num;
	private String subject;
	private String content;
	private String spec;
	private String name;
	private int hitCount;
	private String created;
	
	@Override
	public String toString() {
		return "Board [listNum=" + listNum + ", num=" + num + ", subject=" + subject + ", content=" + content
				+ ", spec=" + spec + ", name=" + name + ", hitCount=" + hitCount + ", created=" + created + "]";
	}
	public int getListNum() {
		return listNum;
	}
	public void setListNum(int listNum) {
		this.listNum = listNum;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHitCount() {
		return hitCount;
	}
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
}
