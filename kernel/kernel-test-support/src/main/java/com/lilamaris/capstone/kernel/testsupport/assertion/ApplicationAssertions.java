package com.lilamaris.capstone.kernel.testsupport.assertion;

import com.lilamaris.capstone.kernel.core.exception.ApplicationErrorCode;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.catchThrowable;

public class ApplicationAssertions {
    public static ApplicationThrowableAssert assertThatApplicationThrownBy(ThrowableAssert.ThrowingCallable callable) {
        return new ApplicationThrowableAssert(catchThrowable(callable));
    }

    public static final class ApplicationThrowableAssert extends AbstractThrowableAssert<ApplicationThrowableAssert, Throwable> {
        private ApplicationThrowableAssert(Throwable actual) {
            super(actual, ApplicationThrowableAssert.class);
        }

        public ApplicationThrowableAssert hasErrorCode(ApplicationErrorCode errorCode) {
            extracting("errorCode")
                    .isEqualTo(errorCode);
            return this;
        }
    }
}
