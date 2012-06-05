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
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	APPLICATION INTEGER NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	CONSTRAINT PK_VERSION PRIMARY KEY (ID),
	CONSTRAINT UQ_VERSION UNIQUE (APPLICATION, NAME),
	CONSTRAINT FK_VERSION_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE
);

CREATE TABLE ENVIRONMENT (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	APPLICATION INTEGER NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	CONSTRAINT PK_ENVIRONMENT PRIMARY KEY (ID),
	CONSTRAINT UQ_ENVIRONMENT UNIQUE (APPLICATION, NAME),
	CONSTRAINT FK_ENVIRONMENT_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE
);


CREATE TABLE KEY (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	APPLICATION INTEGER NOT NULL,
	NAME VARCHAR(80) NOT NULL,
	DESCRIPTION VARCHAR(500) NULL,
	CONSTRAINT PK_KEY PRIMARY KEY (ID),
	CONSTRAINT UQ_KEY UNIQUE (APPLICATION, NAME),
	CONSTRAINT FK_KEY_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE
);

CREATE TABLE VERSION_KEY (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	VERSION INTEGER NOT NULL,
	KEY INTEGER NOT NULL,
	CONSTRAINT PK_VERSION_KEY PRIMARY KEY (ID),
	CONSTRAINT UQ_VERSION_KEY UNIQUE (VERSION, KEY),
	CONSTRAINT FK_VERSION_KEY_VERSION FOREIGN KEY (VERSION) REFERENCES VERSION (ID) ON DELETE CASCADE,
	CONSTRAINT FK_VERSION_KEY_KEY FOREIGN KEY (KEY) REFERENCES KEY (ID) ON DELETE CASCADE
);

CREATE TABLE CONFIG (
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,
	VERSION_KEY INTEGER NOT NULL,
	ENVIRONMENT INTEGER NOT NULL,
	VALUE VARCHAR(500) NOT NULL,
	CONSTRAINT PK_CONFIG PRIMARY KEY (ID),
	CONSTRAINT UQ_CONFIG UNIQUE (VERSION_KEY, ENVIRONMENT),
	CONSTRAINT FK_CONFIG_VERSION_KEY FOREIGN KEY (VERSION_KEY) REFERENCES VERSION_KEY (ID) ON DELETE CASCADE,
	CONSTRAINT FK_CONFIG_ENVIRONMENT FOREIGN KEY (ENVIRONMENT) REFERENCES ENVIRONMENT (ID) ON DELETE CASCADE
);

