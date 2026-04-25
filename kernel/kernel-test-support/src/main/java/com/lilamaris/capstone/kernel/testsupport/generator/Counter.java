package com.lilamaris.capstone.kernel.testsupport.generator;

public interface Counter<T> {
    T next();

    Class<T> support();
}
