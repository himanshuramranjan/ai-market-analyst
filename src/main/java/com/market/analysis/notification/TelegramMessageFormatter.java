package com.market.analysis.notification;

import com.market.analysis.model.StockMetrics;
import com.market.analysis.model.TechnicalSignal;
import com.market.analysis.orchestrator.AnalysisResult;
import org.springframework.stereotype.Component;

@Component
public class TelegramMessageFormatter {

    public String format(AnalysisResult result) {

        StockMetrics metrics = result.metrics();

        TechnicalSignal signal = result.signal();

        StringBuilder sb = new StringBuilder();

        sb.append("📈 *").append(metrics.symbol()).append("*").append("\n");

        sb.append(metrics.companyName()).append("\n\n");

        sb.append("💰 *Price:* ₹").append(format(metrics.currentPrice())).append("\n");

        sb.append("🎯 *Recommendation:* ").append(result.recommendation().recommendation()).append("\n\n");

        sb.append("📊 *Technical Snapshot*").append("\n");

        sb.append("Trend: ").append(signal.trendSignal().trend()).append(" (Score: ").append(format(signal.trendSignal().trendScore())).append(")").append("\n");

        sb.append("Momentum: ").append(signal.momentumSignal().momentum()).append(" (RSI: ").append(format(metrics.rsi())).append(")").append("\n");

        sb.append("Confidence: ").append(format(result.scoreCard().confidence())).append("%").append("\n\n");

        sb.append("📌 *Market Levels*").append("\n");

        sb.append("SMA50: ").append(format(metrics.sma50())).append("\n");

        sb.append("SMA200: ").append(format(metrics.sma200())).append("\n");

        sb.append("52W High: ").append(format(metrics.fiftyTwoWeekHigh())).append("\n");

        sb.append("52W Low: ").append(format(metrics.fiftyTwoWeekLow())).append("\n\n");

        sb.append("🧠 *Key Signals*").append("\n");

        result.signal().evaluations().stream().limit(4).forEach(evaluation -> {

            String emoji = switch(evaluation.trend()) {

                case BULLISH -> "✅ ";

                case BEARISH -> "⚠️ ";

                case NEUTRAL -> "➖ ";
            };

            sb.append(emoji).append(evaluation.reason()).append("\n");
        });

        sb.append("\n");

        sb.append("🤖 *AI Commentary*").append("\n");

        sb.append(result.aiCommentary());

        return sb.toString();
    }

    private String format(double value) {

        return String.format("%.2f", value);
    }
}