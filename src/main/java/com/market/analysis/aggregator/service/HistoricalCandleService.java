package com.market.analysis.aggregator.service;

import com.market.analysis.model.HistoricalCandle;
import java.util.List;

public interface HistoricalCandleService {
    List<HistoricalCandle> fetchCandles( String symbol );
}