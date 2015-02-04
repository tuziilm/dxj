package com.tuziilm.dxj.statistics.common;

public interface LogFileHandler<T extends ValidLineEntry> {
    void handleLine(int fIdx, T entry);
}
