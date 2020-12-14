package com.kdimitrov.edentist.utils;

import graphql.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

public class WorkableLocalDateTimeUtilTest {

    LocalDateTime MONDAY_BEFORE_START = LocalDateTime.of(2020, Month.NOVEMBER, 9, 7, 0);
    LocalDateTime MONDAY_START = LocalDateTime.of(2020, Month.NOVEMBER, 9, 8, 0);
    LocalDateTime FRIDAY_END = LocalDateTime.of(2020, Month.NOVEMBER, 6, 20, 0);
    LocalDateTime SATURDAY = LocalDateTime.of(2020, Month.NOVEMBER, 9, 8, 0);
    LocalDateTime SUNDAY = LocalDateTime.of(2020, Month.NOVEMBER, 9, 8, 0);

    @Test
    public void test() {

        LocalDateTime actual;
        LocalDateTime expected;

        actual = WorkableLocalDateTimeUtil.workable(MONDAY_START);
        expected = MONDAY_START;
        Assert.assertTrue(actual.isEqual(expected));

        actual = WorkableLocalDateTimeUtil.workable(FRIDAY_END);
        expected = MONDAY_START;
        Assert.assertTrue(actual.isEqual(expected));

        actual = WorkableLocalDateTimeUtil.workable(SATURDAY);
        expected = MONDAY_START;
        Assert.assertTrue(actual.isEqual(expected));

        actual = WorkableLocalDateTimeUtil.workable(SUNDAY);
        expected = MONDAY_START;
        Assert.assertTrue(actual.isEqual(expected));

        actual = WorkableLocalDateTimeUtil.workable(MONDAY_BEFORE_START);
        expected = MONDAY_START;
        Assert.assertTrue(actual.isEqual(expected));
    }
}
