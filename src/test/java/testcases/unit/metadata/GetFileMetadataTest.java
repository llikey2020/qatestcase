package testcases.unit.metadata;

import common.client.MDClientHolder;
import common.lib.metadata.FileMetadata;
import common.util.MetadataUtil;
import config.Configuration;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import testcases.init.DBInitializer;

@ExtendWith(value = {DBInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetFileMetadataTest {

    private MDClientHolder holder = null;
    private boolean isSuccess = false;

    private String databaseName = MetadataUtil.getDatabaseName(getClass());
    private String tableName = MetadataUtil.getTableName(getClass());
    private String filename = MetadataUtil.getFilename(0);

    private FileMetadata metadata = MetadataUtil.getMockFileMetadata();

    @BeforeAll
    void setup() throws TException {
        holder = MDClientHolder.builder()
                .hostname(Configuration.METADATA_HOSTNAME)
                .port(Configuration.METADATA_PORT)
                .timeout(Configuration.METADATA_TIMEOUT)
                .build();

        holder.getClient().putFileMetadata(databaseName, tableName, filename, metadata);
        isSuccess = true;
    }

    @Test
    void testNormal() throws TException {
        FileMetadata actual
                = holder.getClient().getFileMetadata(databaseName, tableName, filename);
        Assertions.assertEquals(actual, metadata,
                "inserted data is inconsistent with obtained data.");
    }

    @Test
    void testWithInvalidParam_01() throws TException {
        try {
            holder.getClient().getFileMetadata(null, tableName, filename);
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
            holder.getClient().getFileMetadata(databaseName, null, filename);
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
            holder.getClient().getFileMetadata(databaseName, tableName, null);
        }
        catch (TApplicationException ex) {
            Assertions.assertEquals("Metadata.API.InvalidRequestException",
                    MetadataUtil.getErrMsg(ex.getMessage()),
                    "invalid request: please make sure that invalid params have been handle correctly!");
        }
    }

    @Test
    void testGetFileNotExists() throws TException {
        try {
            holder.getClient().getFileMetadata(databaseName + "_BAK",
                    tableName + "_BAK",
                    filename + "_BAK");
        }
        catch (TApplicationException ex) {
            Assertions.assertEquals("Metadata.API.NoObjectionException",
                    MetadataUtil.getErrMsg(ex.getMessage()),
                    "error: try to get file metadata associated with non-exist file.");
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
