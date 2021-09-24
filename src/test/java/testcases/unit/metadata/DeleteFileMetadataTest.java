package testcases.unit.metadata;

import common.client.MDClientHolder;
import common.lib.metadata.FileMetadata;
import common.util.MetadataUtil;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import testcases.init.DBInitializer;

@ExtendWith(value = {DBInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteFileMetadataTest {

    private MDClientHolder holder = null;
    private boolean isSuccess = false;

    private String databaseName = MetadataUtil.getDatabaseName(getClass());
    private String tableName = MetadataUtil.getTableName(getClass());
    private String filename = MetadataUtil.getFilename(0);

    private FileMetadata metadata = MetadataUtil.getMockFileMetadata();

    @BeforeAll
    void setup() throws TException {
        holder = MDClientHolder.builder().build();

        holder.getClient().putFileMetadata(databaseName, tableName, filename, metadata);
    }

    @Test
    void testNormal() throws TException {
        holder.getClient().deleteFileMetadata(databaseName, tableName, filename);

        try {
            holder.getClient().getFileMetadata(databaseName, tableName, filename);
        }
        catch (TApplicationException ex) {
            Assertions.assertEquals("Metadata.API.DataAccessException",
                    MetadataUtil.getErrMsg(ex.getMessage()),
                    "error: delete failed.");
        }

        isSuccess = true;
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
    void testDeleteFileNotExists() {

    }

    @AfterAll
    void tearDown() throws TException {
        if (null != holder) {
            if (!isSuccess) {
                holder.getClient().deleteFileMetadata(databaseName, tableName, filename);
            }
            holder.close();
        }
    }

}
