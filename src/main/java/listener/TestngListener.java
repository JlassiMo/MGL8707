package listener;

import annotation.DependentStep;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import xray.Xray;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class TestngListener implements ITestListener, ISuiteListener, IMethodInterceptor, IInvokedMethodListener {

    private boolean isPreviousMethodPassed = true;
    private String previousMethod = null;

    public TestngListener() {
        setEnvironmentVariables();
    }

    @Override
    public void onStart(@NotNull ITestContext context) {
        log.info("Execution started: {}", context.getStartDate());
    }

    @Override
    public void onTestStart(@NotNull ITestResult result) {
        log.info("Starting: {}", result.getMethod().getMethodName());
        Xray xrayAnnotation = result.getTestClass().getRealClass().getAnnotation(Xray.class);
        if (xrayAnnotation != null) {
            result.setAttribute("requirement", xrayAnnotation.requirement());
            result.setAttribute("test", xrayAnnotation.test());
            result.setAttribute("labels", xrayAnnotation.labels());
        }
    }

    @Override
    public void onTestSuccess(@NotNull ITestResult result) {
        log.info("Success: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(@NotNull ITestResult result) {
        log.error("Failed: {}", result.getThrowable().getMessage());
        result.setStatus(ITestResult.FAILURE);
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        // Sort the methods lexicographically based on their method name.
        Collections.sort(methods, Comparator.comparing(method -> method.getMethod().getMethodName()));

        return methods;
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // Get the next method that will be invoked
        Method nextMethod = method.getTestMethod().getConstructorOrMethod().getMethod();

        if (nextMethod.getAnnotation(DependentStep.class) != null && !isPreviousMethodPassed) {
            log.warn("Skipping [{}] because it depends on a previous method [{}].", method.getTestMethod().getMethodName(), previousMethod);
            throw new SkipException("Skipping test method due to failed dependency");
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        previousMethod = method.getTestMethod().getMethodName();
        isPreviousMethodPassed = testResult.isSuccess();
    }

    private void setEnvironmentVariables() {
        String environment = System.getProperty("testEnvironment");

        if (environment != null) {
            log.info("Test Environment: [{}]", environment);
            System.setProperty("bnc.data.manager", String.format("data/%s/data-manager.json", environment));
        }
    }
}
