package testcases.unit.metadata;

import common.client.MDClientHolder;
import common.lib.metadata.FileMetadata;
import common.util.MetadataUtil;
import config.Configuration;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutFileMetadataTest {

    private MDClientHolder holder = null;
    private boolean isSuccess = false;

    private String databaseName = MetadataUtil.getDatabaseName(getClass());
    private String tableName = MetadataUtil.getTableName(getClass());
    private String filename = MetadataUtil.getFilename(0);

    private FileMetadata metadata = MetadataUtil.getMockFileMetadata();

    @BeforeAll
    void setup() throws TTransportException {
        holder = MDClientHolder.builder()
                .hostname(Configuration.METADATA_HOSTNAME)
                .port(Configuration.METADATA_PORT)
                .timeout(Configuration.METADATA_TIMEOUT)
                .build();
    }

    // ============== Testcases ==============

    @Test
    void testPing() throws TException {
        holder.getClient().Ping();
    }

    @Test
    void testNormal() throws TException {
        holder.getClient().putFileMetadata(databaseName, tableName, filename, metadata);

        FileMetadata actual
                = holder.getClient().getFileMetadata(databaseName, tableName, filename);
        Assertions.assertEquals(actual, metadata,
                "inserted data is inconsistent with obtained data.");

        isSuccess = true;
    }

    @Test
    void testWithInvalidParam_01() throws TException {
        try {
            holder.getClient().putFileMetadata(null, tableName, filename, metadata);
        }
        catch (TApplicationException ex) {
            Assertions.assertEquals("Metadata.API.InvalidRequestException",
                    MetadataUtil.getErrMsg(ex.getMessage()),
                    "invalid request: please make sure that invalid params have been handle correctly!");
        }
    }

    @Test
    void testWithInvalidParam_02() throws TException {
        try {
            holder.getClient().putFileMetadata(databaseName, null, filename, metadata);
        }
        catch (TApplicationException ex) {
            Assertions.assertEquals("Metadata.API.InvalidRequestException",
                    MetadataUtil.getErrMsg(ex.getMessage()),
                    "invalid request: please make sure that invalid params have been handle correctly!");
        }
    }

    @Test
    void testWithInvalidParam_03() throws TException {
        try {
            holder.getClient().putFileMetadata(databaseName, tableName, null, metadata);
        }
        catch (TApplicationException ex) {
            Assertions.assertEquals("Metadata.API.InvalidRequestException",
                    MetadataUtil.getErrMsg(ex.getMessage()),
                    "invalid request: please make sure that invalid params have been handle correctly!");
        }
    }

    @Test
    void testWithInvalidParam_04() throws TException {
        try {
            holder.getClient().putFileMetadata(databaseName, tableName, filename, null);
        }
        catch (TApplicationException ex) {
            Assertions.assertEquals("Metadata.API.InvalidRequestException",
                    MetadataUtil.getErrMsg(ex.getMessage()),
                    "invalid request: please make sure that invalid params have been handle correctly!");
        }
    }

    @Test
    void testPutFileExists() throws TException {
        FileMetadata metadata2 = MetadataUtil.getMockFileMetadata();
        try {
            holder.getClient().putFileMetadata(databaseName, tableName, filename, metadata2);
        }
        catch (TApplicationException ex) {
            Assertions.assertEquals("Metadata.API.DataAccessException",
                    MetadataUtil.getErrMsg(ex.getMessage()),
                    "error: try to put file metadata associated with an existing file.");
        }
    }

    @AfterAll
    void tearDown() throws TException {
        if (null != holder) {
            if (isSuccess) {
                holder.getClient().deleteFileMetadata(databaseName, tableName, filename);
            }
            holder.close();
        }
    }

}
