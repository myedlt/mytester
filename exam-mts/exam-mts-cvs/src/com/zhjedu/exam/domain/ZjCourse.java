package com.zhjedu.exam.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * ZjCourse generated by MyEclipse - Hibernate Tools
 */

public class ZjCourse implements java.io.Serializable {

	// Fields

	private String id;

	private String originId;

	private String name;

	private String itemType;

	private String delflag;

	private String parent;

	private Set zjQuizs = new HashSet(0);

	// Constructors

	/** default constructor */
	public ZjCourse() {
	}

	/** full constructor */
	public ZjCourse(String originId, String name, String itemType,
			String delflag, String parent, Set zjQuizs) {
		this.originId = originId;
		this.name = name;
		this.itemType = itemType;
		this.delflag = delflag;
		this.parent = parent;
		this.zjQuizs = zjQuizs;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOriginId() {
		return this.originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getDelflag() {
		return this.delflag;
	}

	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}

	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Set getZjQuizs() {
		return zjQuizs;
	}

	public void setZjQuizs(Set zjQuizs) {
		this.zjQuizs = zjQuizs;
	}

}