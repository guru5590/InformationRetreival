/**
 * 
 */
package com.informationretreival.assignments;

/**
 * @author guru
 *
 */
public class DocumentStructure {

	private int 	lineId;
	private String 	playName;
	private String 	speechNumber;
	private String 	lineNumber;
	private String 	speaker;
	private String 	textEntry;

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public String getPlayName() {
		return playName;
	}

	public void setPlayName(String playName) {
		this.playName = playName;
	}

	public String getSpeechNumber() {
		return speechNumber;
	}

	public void setSpeechNumber(String speechNumber) {
		this.speechNumber = speechNumber;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getTextEntry() {
		return textEntry;
	}

	public void setTextEntry(String textEntry) {
		this.textEntry = textEntry;
	}

	@Override
	public String toString() {
		return "Document Structure:: Line ID=" + this.lineId + " Play Name=" + this.playName + " Speech Number="
				+ this.speechNumber + " Line Number=" + this.lineNumber + " Speaker=" + this.speaker + " Text Entry="
				+ this.textEntry;
	}

}
