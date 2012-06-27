package net.myconfig.service.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class MatrixConfiguration {

	private final int id;
	private final String name;
	private final List<MatrixVersionConfiguration> versionConfigurationList;
	private final List<Key> keyList;

	public MatrixConfiguration(int id, String name, List<MatrixVersionConfiguration> versionConfigurationList, List<Key> keyList) {
		this.id = id;
		this.name = name;
		this.versionConfigurationList = ImmutableList.copyOf(versionConfigurationList);
		this.keyList = ImmutableList.copyOf(keyList);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<MatrixVersionConfiguration> getVersionConfigurationList() {
		return versionConfigurationList;
	}

	public List<Key> getKeyList() {
		return keyList;
	}

}