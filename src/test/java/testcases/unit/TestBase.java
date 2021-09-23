package testcases.unit;

import com.intuit.karate.core.ScenarioResult;
import org.junit.jupiter.api.Assertions;

import java.util.stream.Stream;

public abstract class TestBase {

    protected void assertFail(Stream<ScenarioResult> results) {
        results.forEach(result -> {
            if (result.isFailed()) {
                Assertions.fail(result.getFailedStep().getErrorMessage());
            }
        });
    }

}
