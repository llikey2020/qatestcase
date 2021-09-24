package testcases.init;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public class DBInitializer implements BeforeAllCallback {

    private static boolean started = false;

    private static SqlSessionFactory factory = null;

    static {
        String resource = "mybatis-config.xml";

        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            factory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            started = true;
//            executeScript("init.sql");
        }
    }

    private static void executeScript(String path) throws SQLException, IOException {
        Connection conn = factory.openSession().getConnection();
        ScriptRunner scriptRunner = new ScriptRunner(conn);

        Resources.setCharset(StandardCharsets.UTF_8);
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(path);
            scriptRunner.runScript(reader);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

}
