CREATE TABLE DBVERSION (
	VALUE INTEGER NOT NULL,
	UPDATED TIMESTAMP NOT NULL
);

CREATE TABLE APPLICATION (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	CONSTRAINT PK_APPLICATION PRIMARY KEY (ID),
	CONSTRAINT UQ_APPLICATION UNIQUE (NAME)
);

CREATE TABLE VERSION (
	APPLICATION INTEGER NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	CONSTRAINT PK_VERSION PRIMARY KEY (APPLICATION, NAME),
	CONSTRAINT FK_VERSION_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE
);

CREATE TABLE ENVIRONMENT (
	APPLICATION INTEGER NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	CONSTRAINT PK_ENVIRONMENT PRIMARY KEY (APPLICATION, NAME),
	CONSTRAINT FK_ENVIRONMENT_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE
);


CREATE TABLE KEY (
	APPLICATION INTEGER NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	DESCRIPTION VARCHAR(500) NULL,
	CONSTRAINT PK_KEY PRIMARY KEY (APPLICATION, NAME),
	CONSTRAINT FK_KEY_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE
);

CREATE TABLE VERSION_KEY (
	APPLICATION INTEGER NOT NULL,
	VERSION VARCHAR(80) NOT NULL,
	KEY VARCHAR(80) NOT NULL,
	CONSTRAINT PK_VERSION_KEY PRIMARY KEY (APPLICATION, VERSION, KEY),
	CONSTRAINT FK_VERSION_KEY_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE,
	CONSTRAINT FK_VERSION_KEY_VERSION FOREIGN KEY (APPLICATION, VERSION) REFERENCES VERSION (APPLICATION, NAME) ON DELETE CASCADE,
	CONSTRAINT FK_VERSION_KEY_KEY FOREIGN KEY (APPLICATION, KEY) REFERENCES KEY (APPLICATION, NAME) ON DELETE CASCADE
);

CREATE TABLE CONFIG (
	APPLICATION INTEGER NOT NULL,
	VERSION VARCHAR(80) NOT NULL,
	KEY VARCHAR(80) NOT NULL,
	ENVIRONMENT VARCHAR(80) NOT NULL,
	VALUE VARCHAR(500) NOT NULL,
	CONSTRAINT PK_CONFIG PRIMARY KEY (APPLICATION, VERSION, ENVIRONMENT, KEY),
	CONSTRAINT FK_CONFIG_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE,
	CONSTRAINT FK_CONFIG_VERSION FOREIGN KEY (APPLICATION, VERSION) REFERENCES VERSION (APPLICATION, NAME) ON DELETE CASCADE,
	CONSTRAINT FK_CONFIG_KEY FOREIGN KEY (APPLICATION, KEY) REFERENCES KEY (APPLICATION, NAME) ON DELETE CASCADE,
	CONSTRAINT FK_CONFIG_VERSION_KEY FOREIGN KEY (APPLICATION, VERSION, KEY) REFERENCES VERSION_KEY (APPLICATION, VERSION, KEY) ON DELETE CASCADE,
	CONSTRAINT FK_CONFIG_ENVIRONMENT FOREIGN KEY (APPLICATION, ENVIRONMENT) REFERENCES ENVIRONMENT (APPLICATION, NAME) ON DELETE CASCADE
);

