package com.pcs.itext.pcsItext.bean;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormFieldResponse {
	
	@JsonProperty("#printCopyNo")
	private String printCopyNo;
	
	@JsonProperty("#printRequestDateTime")
	private String printRequestDateTime;
	
	@JsonProperty("#recipient")
	private String recipient;
	
	@JsonProperty("#userId")
	private String userId;
	
	@JsonProperty("@infocardNumber")
	private String infocardNumber;
	
	@JsonProperty("@revision")
	private String revision;
	
	@JsonProperty("^userIp1")
	private String userIp;
	
	@JsonProperty("^userIp2")
	private String userIp2;
	
	@JsonProperty("controlled_copy")
	private String controlled_copy;
	
	@JsonProperty("disclaimer")
	private String disclaimer;
	
	@JsonProperty("issued_to")
	private String issued_to;
	
	@JsonProperty("print_copy_no")
	private String Print_Copy_No;
	
	@JsonProperty("printed_by")
	private String printed_by;
	
	@JsonProperty("printed_dater")
	private String printed_date;
	
	@JsonProperty("revision_lbl")
	private String revision_lbl;
	
	public FormFieldResponse() {};

	public String getPrintCopyNo() {
		return printCopyNo;
	}

	public void setPrintCopyNo(String printCopyNo) {
		this.printCopyNo = printCopyNo;
	}

	public String getPrintRequestDateTime() {
		return printRequestDateTime;
	}

	public void setPrintRequestDateTime(String printRequestDateTime) {
		this.printRequestDateTime = printRequestDateTime;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInfocardNumber() {
		return infocardNumber;
	}

	public void setInfocardNumber(String infocardNumber) {
		this.infocardNumber = infocardNumber;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserIp2() {
		return userIp2;
	}

	public void setUserIp2(String userIp2) {
		this.userIp2 = userIp2;
	}

	public String getControlled_copy() {
		return controlled_copy;
	}

	public void setControlled_copy(String controlled_copy) {
		this.controlled_copy = controlled_copy;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getIssued_to() {
		return issued_to;
	}

	public void setIssued_to(String issued_to) {
		this.issued_to = issued_to;
	}

	public String getPrint_Copy_No() {
		return Print_Copy_No;
	}

	public void setPrint_Copy_No(String print_Copy_No) {
		Print_Copy_No = print_Copy_No;
	}

	public String getPrinted_by() {
		return printed_by;
	}

	public void setPrinted_by(String printed_by) {
		this.printed_by = printed_by;
	}

	public String getPrinted_date() {
		return printed_date;
	}

	public void setPrinted_date(String printed_date) {
		this.printed_date = printed_date;
	}

	public String getRevision_lbl() {
		return revision_lbl;
	}

	public void setRevision_lbl(String revision_lbl) {
		this.revision_lbl = revision_lbl;
	}
	
	

}
