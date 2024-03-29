package com.zhjedu.exam.domain;

/**
 * ZjQuestionOptionListOnId generated by MyEclipse Persistence Tools
 */

public class ZjQuestionOptionListOnId implements java.io.Serializable {

	// Fields

	private String question;
	
	private ZjQuestion zjQuestion;

	private Long maxTxt;

	private Long sumTxt;

	private Long listOn;

	// Constructors

	/** default constructor */
	public ZjQuestionOptionListOnId() {
	}

	/** full constructor */
	public ZjQuestionOptionListOnId(String question, Long maxTxt, Long sumTxt,
			Long listOn) {
		this.question = question;
		this.maxTxt = maxTxt;
		this.sumTxt = sumTxt;
		this.listOn = listOn;
	}

	// Property accessors

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Long getMaxTxt() {
		return this.maxTxt;
	}

	public void setMaxTxt(Long maxTxt) {
		this.maxTxt = maxTxt;
	}

	public Long getSumTxt() {
		return this.sumTxt;
	}

	public void setSumTxt(Long sumTxt) {
		this.sumTxt = sumTxt;
	}

	public Long getListOn() {
		return this.listOn;
	}

	public void setListOn(Long listOn) {
		this.listOn = listOn;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ZjQuestionOptionListOnId))
			return false;
		ZjQuestionOptionListOnId castOther = (ZjQuestionOptionListOnId) other;

		return ((this.getQuestion() == castOther.getQuestion()) || (this
				.getQuestion() != null
				&& castOther.getQuestion() != null && this.getQuestion()
				.equals(castOther.getQuestion())))
				&& ((this.getMaxTxt() == castOther.getMaxTxt()) || (this
						.getMaxTxt() != null
						&& castOther.getMaxTxt() != null && this.getMaxTxt()
						.equals(castOther.getMaxTxt())))
				&& ((this.getSumTxt() == castOther.getSumTxt()) || (this
						.getSumTxt() != null
						&& castOther.getSumTxt() != null && this.getSumTxt()
						.equals(castOther.getSumTxt())))
				&& ((this.getListOn() == castOther.getListOn()) || (this
						.getListOn() != null
						&& castOther.getListOn() != null && this.getListOn()
						.equals(castOther.getListOn())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getQuestion() == null ? 0 : this.getQuestion().hashCode());
		result = 37 * result
				+ (getMaxTxt() == null ? 0 : this.getMaxTxt().hashCode());
		result = 37 * result
				+ (getSumTxt() == null ? 0 : this.getSumTxt().hashCode());
		result = 37 * result
				+ (getListOn() == null ? 0 : this.getListOn().hashCode());
		return result;
	}

	public ZjQuestion getZjQuestion() {
		return zjQuestion;
	}

	public void setZjQuestion(ZjQuestion zjQuestion) {
		this.zjQuestion = zjQuestion;
	}

}