package com.sumair.dynamicform.utilities;

import java.util.concurrent.atomic.AtomicInteger;

public class NumberUtils {

    public static final AtomicInteger counter = new AtomicInteger(0);

    public static int getNextNumber(){
        return counter.incrementAndGet();
    }
}
