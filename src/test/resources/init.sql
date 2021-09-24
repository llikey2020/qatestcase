CREATE DATABASE IF NOT EXISTS metadatadatabase;

USE metadatadatabase;

CREATE TABLE IF NOT EXISTS Metadata
(
    DatabaseName     VARCHAR(200),
    TableName        VARCHAR(200),
    FileName         VARCHAR(200),
    FileFormat       VARCHAR(200),
    RecordCount      BIGINT,
    FileSizeInBytes  BIGINT,
    ColumnSizeID     BINARY(16),
    ValueCountID     BINARY(16),
    NullValueCountID BINARY(16),
    NanValueCountID  BINARY(16),
    LowerBoundID     BINARY(16),
    UpperBoundID     BINARY(16),
    KeyMetadata      VARBINARY(200),
    SplitOffsetID    BINARY(16),
    SortOrderID      INT,
    UNIQUE (DatabaseName, TableName, FileName),
    INDEX (DatabaseName, TableName, FileName)
);

CREATE TABLE IF NOT EXISTS ColumnSizes
(
    ColumnSizeID  BINARY(16),
    ColumnSizeKey INT,
    ColumnSize    BIGINT,
    INDEX (ColumnSizeID)
);

CREATE TABLE IF NOT EXISTS ValueCounts
(
    ValueCountID  BINARY(16),
    ValueCountKey INT,
    ValueCount    BIGINT,
    INDEX (ValueCountID)
);

CREATE TABLE IF NOT EXISTS NullValueCounts
(
    NullValueCountID  BINARY(16),
    NullValueCountKey INT,
    NullValueCount    BIGINT,
    INDEX (NullValueCountID)
);

CREATE TABLE IF NOT EXISTS NanValueCounts
(
    NanValueCountID  BINARY(16),
    NanValueCountKey INT,
    NanValueCount    BIGINT,
    INDEX (NanValueCountID)
);

CREATE TABLE IF NOT EXISTS LowerBounds
(
    LowerBoundID  BINARY(16),
    LowerBoundKey INT,
    LowerBound    VARBINARY(200),
    INDEX (LowerBoundID)
);

CREATE TABLE IF NOT EXISTS UpperBounds
(
    UpperBoundID  BINARY(16),
    UpperBoundKey INT,
    UpperBound    VARBINARY(200),
    INDEX (UpperBoundID)
);

CREATE TABLE IF NOT EXISTS SplitOffsets
(
    SplitOffsetID BINARY(16),
    SplitOffset   BIGINT,
    INDEX (SplitOffsetID)
);