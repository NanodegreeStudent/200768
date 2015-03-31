package com.example.mikhail.santafe;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

public class FullTestSuit extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuit.class)
                .includeAllPackagesUnderHere().build();
    }

    public FullTestSuit() {
        super();
    }
}
