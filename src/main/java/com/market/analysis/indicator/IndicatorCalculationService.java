package com.market.analysis.indicator;

import com.market.analysis.model.HistoricalCandle;
import java.util.List;
public interface IndicatorCalculationService {
    double calculateSMA(List <HistoricalCandle> candles, int period);
    double calculateRSI(List <HistoricalCandle> candles, int period);
    double calculateAverageVolume(List <HistoricalCandle> candles, int period);
}