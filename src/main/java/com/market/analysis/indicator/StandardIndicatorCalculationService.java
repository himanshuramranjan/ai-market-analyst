package com.market.analysis.indicator;

import com.market.analysis.model.HistoricalCandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StandardIndicatorCalculationService implements IndicatorCalculationService {
    @Override
    public double calculateSMA(List<HistoricalCandle> candles, int period) {
        if(candles.size() < period) {
            return 0;
        }
        return candles.stream().skip(candles.size() - period).mapToDouble(HistoricalCandle::close).average().orElse(0);
    }

    @Override
    public double calculateRSI(List<HistoricalCandle> candles, int period) {
        if(candles.size() <= period) {
            return 50;
        }
        double gains = 0;
        double losses = 0;
        for(int i = candles.size() - period; i < candles.size(); i++) {
            double diff = candles.get(i).close() - candles.get(i - 1).close();
            if(diff > 0) {
                gains += diff;
            } else {
                losses += Math.abs(diff);
            }
        }
        if(losses == 0) {
            return 100;
        }
        double rs = gains / losses;
        return 100 - (100 / (1 + rs));
    }

    @Override
    public double calculateAverageVolume(List<HistoricalCandle> candles, int period) {
        if(candles.size() < period) {
            return 0;
        }
        return candles.stream().skip(candles.size() - period).mapToLong(HistoricalCandle::volume).average().orElse(0);
    }
}