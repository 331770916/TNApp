DROP TABLE IF EXISTS USER;

DROP TABLE IF EXISTS STOCK;

DROP TABLE IF EXISTS NEWS_INFO;

CREATE TABLE USER
(
   ID                   INTEGER NOT NULL    PRIMARY KEY AUTOINCREMENT,
   USERNAME             VARCHAR(50),
   NICKNAME             VARCHAR(50),
   MOBILE_NUM             VARCHAR(11),
   IS_ENCRYPT              TINYINT,
   USER_TYPE             TINYINT,
   REGISTER_ACCOUNT        VARCHAR(50),
   TRADE_ACCOUNT         VARCHAR(50),
   REGISTER_TIME           DATETIME,
   PROFILE_PIC           VARCHAR(100),
   IS_LOGIN	       VARCHAR(10),
   CERTIFICATION		VARCHAR(10),
   PLUGINS		VARCHAR(10),
   REFRESH_TIME		VARCHAR(10),
   IS_INIT_UNREGISTER_DATA TINYINT,
   FLAG1                VARCHAR(10),
   FLAG2                VARCHAR(10),
   FLAG3                VARCHAR(10),
   FLAG4                VARCHAR(10),
   FLAG5                VARCHAR(10)
);

CREATE TABLE STOCK
(
   ID                   INTEGER NOT NULL   PRIMARY KEY AUTOINCREMENT,
   STOCK_FLAG           INT,
   STOCK_CODE            VARCHAR(50),
   STOCK_NAME            VARCHAR(50),
   CREATE_TIME           DATETIME,
   FLAG1                VARCHAR(10),
   FLAG2                VARCHAR(10),
   FLAG3                VARCHAR(10),
   FLAG4                VARCHAR(10),
   FLAG5                VARCHAR(10)
);


CREATE TABLE NEWS_INFO
(
   ID                   INTEGER NOT NULL   PRIMARY KEY AUTOINCREMENT,
   CONTENT              BLOB,
   NEWS_FLAG	INT,
   NEWS_ID		INT,
   STOCK_CODE	VARCHAR(10),
   FLAG1                VARCHAR(10),
   FLAG2                VARCHAR(10)
);