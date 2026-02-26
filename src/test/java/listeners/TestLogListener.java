package listeners;

import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestLogListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getRealClass().getSimpleName()
                + "_" + result.getMethod().getMethodName();
        ThreadContext.put("logFileName", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ThreadContext.clearAll();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ThreadContext.clearAll();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ThreadContext.clearAll();
    }
}