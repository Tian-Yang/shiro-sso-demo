package com.shiro.demo.util;

/**
  *雪花算法ID生成器
  *Author TianYang
  *Date 2023/12/14 10:00
  */
public class SnowflakeIdGenerator {

    private static final long START_TIMESTAMP = 1609459200000L; // 2021-01-01 00:00:00

    private static final long SEQUENCE_BIT = 12;
    private static final long MACHINE_BIT = 10;

    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);
    private static final long MAX_MACHINE_ID = -1L ^ (-1L << MACHINE_BIT);

    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    private static long lastTimestamp = -1L;
    private static long sequence = 0L;
    private static long machineId;

    public static void init(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException("Machine ID can't be greater than " + MAX_MACHINE_ID + " or less than 0");
        }
        SnowflakeIdGenerator.machineId = machineId;
    }

    public static synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID for " + (lastTimestamp - currentTimestamp) + " milliseconds");
        }

        if (lastTimestamp == currentTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT) | (machineId << MACHINE_LEFT) | sequence;
    }

    private static long waitNextMillis(long lastTimestamp) {
        long currentTimestamp = System.currentTimeMillis();
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }
}
