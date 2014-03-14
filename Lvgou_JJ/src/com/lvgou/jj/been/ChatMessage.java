package com.lvgou.jj.been;

import java.util.Date;

public class ChatMessage {
	private String from;
	private String to;
	private String type;
	private String body;
	private String date;
	private boolean isnew;
	private boolean issend;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isIsnew() {
		return isnew;
	}

	public void setIsnew(boolean isnew) {
		this.isnew = isnew;
	}

	public boolean isIssend() {
		return issend;
	}

	public void setIssend(boolean issend) {
		this.issend = issend;
	}
}
