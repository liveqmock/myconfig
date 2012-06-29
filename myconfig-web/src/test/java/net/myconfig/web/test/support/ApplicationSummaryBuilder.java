package net.myconfig.web.test.support;

import net.myconfig.service.model.ApplicationSummary;

public class ApplicationSummaryBuilder {

	public static ApplicationSummaryBuilder create() {
		return new ApplicationSummaryBuilder();
	}

	public static ApplicationSummaryBuilder create(int i, String name) {
		return create().setId(i).setName(name);
	}

	private int id;
	private String name;
	private int versionCount;
	private int keyCount;
	private int environmentCount;
	private int configCount;
	private int valueCount;

	protected ApplicationSummaryBuilder() {
	}

	public ApplicationSummaryBuilder setId(int id) {
		this.id = id;
		return this;
	}

	public ApplicationSummaryBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ApplicationSummaryBuilder setVersionCount(int versionCount) {
		this.versionCount = versionCount;
		return this;
	}

	public ApplicationSummaryBuilder setKeyCount(int keyCount) {
		this.keyCount = keyCount;
		return this;
	}

	public ApplicationSummaryBuilder setEnvironmentCount(int environmentCount) {
		this.environmentCount = environmentCount;
		return this;
	}

	public ApplicationSummaryBuilder setConfigCount(int configCount) {
		this.configCount = configCount;
		return this;
	}

	public ApplicationSummaryBuilder setValueCount(int valueCount) {
		this.valueCount = valueCount;
		return this;
	}

	public ApplicationSummary build() {
		return new ApplicationSummary(id, name, versionCount, keyCount, environmentCount, configCount, valueCount);
	}

}