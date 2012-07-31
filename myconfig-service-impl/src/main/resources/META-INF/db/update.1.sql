CREATE TABLE USERGRANTS (
	USER VARCHAR(80) NOT NULL,
	GRANTEDFUNCTION VARCHAR(80) NOT NULL,
	CONSTRAINT PK_USERGRANTS PRIMARY KEY (USER, GRANTEDFUNCTION)
);

CREATE TABLE APPGRANTS (
	USER VARCHAR(80) NOT NULL,
	APPLICATION INTEGER NOT NULL,
	GRANTEDFUNCTION VARCHAR(80) NOT NULL,
	CONSTRAINT PK_APPGRANTS PRIMARY KEY (USER, APPLICATION, GRANTEDFUNCTION),
	CONSTRAINT FK_APPGRANTS_APPLICATION FOREIGN KEY (APPLICATION) REFERENCES APPLICATION (ID) ON DELETE CASCADE
);

CREATE TABLE ENVGRANTS (
	USER VARCHAR(80) NOT NULL,
	APPLICATION INTEGER NOT NULL,
	ENVIRONMENT VARCHAR(80) NOT NULL,
	GRANTEDFUNCTION VARCHAR(80) NOT NULL,
	CONSTRAINT PK_ENVGRANTS PRIMARY KEY (USER, APPLICATION, ENVIRONMENT, GRANTEDFUNCTION),
	CONSTRAINT FK_ENVGRANTS_APPLICATION_ENVIRONMENT FOREIGN KEY (APPLICATION, ENVIRONMENT) REFERENCES ENVIRONMENT (APPLICATION, NAME) ON DELETE CASCADE
);

CREATE TABLE USERS (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME VARCHAR(80) NOT NULL,
	PASSWORD VARCHAR(256) NOT NULL,
	ADMIN BOOLEAN NOT NULL,
	CONSTRAINT PK_USERS PRIMARY KEY (ID),
	CONSTRAINT UQ_USERS UNIQUE (NAME)
);

CREATE TABLE CONFIGURATION (
	NAME VARCHAR(80) NOT NULL,
	VALUE VARCHAR(500) NOT NULL,
	CONSTRAINT PK_CONFIGURATION PRIMARY KEY (NAME)
);
