package core;

import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public abstract class BaseTest {

    @BeforeMethod
    public void setLogContext(Method method) {
        String testName = this.getClass().getSimpleName() + "_" + method.getName();
        ThreadContext.put("logFileName", testName);
    }

    @AfterMethod
    public void clearLogContext() {
        ThreadContext.clearAll();
    }
}