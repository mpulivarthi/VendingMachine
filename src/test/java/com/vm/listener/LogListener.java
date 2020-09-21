package com.vm.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class LogListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("Test Started : \t"+ iTestResult.getName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("Test Successful : \t"+ iTestResult.getName());
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("Test Failed : \t"+iTestResult.getName());

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("Test Skipped : \t"+iTestResult.getName());

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but within success percentage : \t"+iTestResult.getName());

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("Suite Started : \t"+ iTestContext.getName());

    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("Suite Finished : \t"+ iTestContext.getName());

    }
}
