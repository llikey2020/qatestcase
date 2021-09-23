package testcases.unit.warehouse;

import com.intuit.karate.Runner;
import com.intuit.karate.core.ScenarioResult;
import org.junit.Test;
import testcases.unit.TestBase;

import java.util.stream.Stream;

public class VWHRunner extends TestBase {

    @Test
    public void testParallel() {
        Stream<ScenarioResult> results = Runner.path("classpath:testcases/unit/warehouse")
                .relativeTo(getClass())
                .parallel(5)
                .getScenarioResults();

        assertFail(results);
    }

}
