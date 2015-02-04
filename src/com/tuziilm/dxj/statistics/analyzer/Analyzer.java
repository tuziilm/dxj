package com.tuziilm.dxj.statistics.analyzer;

import com.tuziilm.dxj.statistics.common.ChartPvUvData;

import java.util.List;

public interface Analyzer {
    List<ChartPvUvData> analyze() throws Exception;
}
