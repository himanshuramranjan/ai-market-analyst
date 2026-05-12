# AI Market Analyst

An intelligent Indian stock market analysis system built using Spring Boot, deterministic technical-analysis engines,
rule-based scoring, AI commentary generation, and Telegram-based delivery.

The system is designed to evaluate whether a stock currently presents a reasonable accumulation, entry, or exit
opportunity using a combination of:

* Technical indicators
* Rule-engine driven signal evaluation
* Weighted scoring
* Confidence estimation
* AI-powered market commentary
* Automated scheduling and notifications

---

# Key Features

## Deterministic Technical Analysis Engine

The core analysis pipeline is fully deterministic and rule-driven.

The system computes and evaluates:

* SMA50
* SMA200
* RSI
* Trend structure
* Momentum quality
* Valuation heuristics
* Pullback quality
* Signal alignment

---

## Rule Engine Architecture

The project uses a modular rule-engine architecture where each rule independently evaluates the stock and contributes
toward:

* Trend analysis
* Momentum analysis
* Valuation analysis

Implemented rules:

* PriceAboveSMA200Rule
* SMA50AboveSMA200Rule
* SMAAlignmentRule
* PriceNearSMA50Rule
* PullbackFromHighRule
* RSIBullishMomentumRule
* RSICoolingRule
* PERatioRule

Each rule produces:

* Directional signal
* Weighted score
* Signal strength
* Human-readable reasoning

---

## Weighted Scoring System

The system combines deterministic signals into:

* Trend score
* Momentum score
* Valuation score
* Composite score
* Confidence score

Scoring emphasizes:

* Trend structure
* Signal agreement
* Directional consistency
* Technical alignment

---

## AI Commentary Layer

The project integrates Groq-hosted LLM models to generate concise market commentary.

The AI layer:

* Uses deterministic analysis as primary context
* Generates concise professional commentary
* Optimized for Telegram/mobile readability
* Avoids hallucinated indicators
* Focuses on practical entry/exit quality

The AI layer is intentionally designed as:

* a synthesis layer
* NOT the primary analysis engine

This architecture significantly improves reliability.

---

## Telegram Notification System

The project can automatically send analysis summaries directly to Telegram.

Features:

* Mobile-friendly formatting
* AI commentary integration
* Signal highlights
* Technical snapshots
* Scheduler-based delivery

---

## Scheduler Support

Production scheduler support included.

Supports:

* Market open analysis
* Market close analysis
* Automated Telegram notifications

Configured using Spring Scheduler.

---

# Technology Stack

| Component      | Technology                 |
|----------------|----------------------------|
| Language       | Java 21                    |
| Framework      | Spring Boot                |
| HTTP Client    | WebClient                  |
| AI Integration | Groq API                   |
| Notifications  | Telegram Bot API           |
| Scheduling     | Spring Scheduler           |
| Build Tool     | Gradle                     |
| Logging        | SLF4J + Logback            |
| Architecture   | Rule Engine + Orchestrator |

---

# High-Level Architecture

```text
Market Data Provider
        ↓
Indicator Calculation
        ↓
Validation Layer
        ↓
Normalization Layer
        ↓
Rule Engine
        ↓
Scoring Engine
        ↓
Interpretation Engine
        ↓
Recommendation Engine
        ↓
AI Commentary Service
        ↓
Telegram Notification
```

---

# Project Structure

## aggregator

Responsible for external market data aggregation.

Contains:

* AlphaVantage integration
* Market data providers
* Historical candle fetching
* Indicator API calls
* Mock provider support

---

## ai

Contains the complete AI integration layer.

### client

* GroqClient
* GroqResponse

Responsible for LLM communication.

### model

* AIAnalysisContext
* AICommentaryResponse

AI request/response models.

### prompt

* PromptBuilder

Builds deterministic-context prompts.

### service

* AICommentaryService

Coordinates AI commentary generation.

---

## config

Application-wide configuration.

Includes:

* WebClient configuration
* Property binding
* Application configuration

---

## constants

Contains configurable thresholds and scoring constants.

---

## indicator

Responsible for technical indicator calculations.

Includes:

* SMA calculations
* RSI calculations
* Indicator abstractions

---

## interpretation

Responsible for converting raw signals into practical narrative interpretation.

Includes:

* trend interpretation
* momentum interpretation
* entry-quality assessment

---

## model

Core domain models.

Includes:

* StockMetrics
* TechnicalSignal
* TrendSignal
* MomentumSignal
* ValuationSignal
* RecommendationResult
* ScoreCard
* HistoricalCandle

---

## normalizer

Responsible for metric normalization before analysis.

---

## notification

Telegram notification layer.

Includes:

* TelegramNotifier
* TelegramMessageFormatter

---

## orchestrator

Core orchestration layer.

Coordinates the full analysis pipeline.

Main responsibilities:

* fetch market data
* validate metrics
* normalize metrics
* execute rule engine
* calculate scores
* generate interpretation
* generate recommendations
* trigger AI commentary

---

## recommendation

Responsible for generating final recommendation outcomes.

Examples:

* Strong Buy
* Moderate Buy
* Hold
* Avoid

---

## report

Console reporting support.

Used mainly during development and debugging.

---

## rules

Core deterministic rule engine.

Contains:

* individual analysis rules
* rule evaluation models
* weighted directional scoring

### engine

Contains:

* RuleEngine
* StandardRuleEngine

Responsible for:

* executing rules
* directional aggregation
* signal construction

---

## scheduler

Contains production scheduler jobs.

Supports:

* market open analysis
* market close analysis
* automated Telegram delivery

---

## scoring

Responsible for:

* weighted score calculation
* composite scoring
* confidence calculation

---

## validator

Responsible for validating incoming market data.

Detects:

* missing metrics
* invalid values
* inconsistent technical structures

---

# Analysis Pipeline

## Step 1 — Market Data Fetching

The system fetches:

* price data
* historical candles
* indicator values

using provider abstractions.

---

## Step 2 — Validation

Incoming metrics are validated before analysis.

---

## Step 3 — Normalization

Metrics are normalized into a consistent analysis structure.

---

## Step 4 — Rule Engine Execution

Each rule independently evaluates the stock.

Example:

* price above SMA200
* RSI momentum quality
* SMA alignment
* pullback quality

---

## Step 5 — Signal Generation

Signals are aggregated into:

* TrendSignal
* MomentumSignal
* ValuationSignal

---

## Step 6 — Scoring

Weighted scoring produces:

* composite score
* confidence score

---

## Step 7 — Interpretation

Human-readable interpretation generation.

---

## Step 8 — Recommendation

Final recommendation generation.

---

## Step 9 — AI Commentary

LLM-based contextual synthesis.

---

## Step 10 — Notification Delivery

Telegram notification delivery.

---

# Configuration

## application.yml

Example:

```yaml
market:
  analysis:

    rules:
      trend:
        sma200-weight: 0.65
        sma50-weight: 0.35

      momentum:
        rsi-threshold-strong: 60
        rsi-threshold-weak: 40

      valuation:
        pe-cheap-threshold: 18
        pe-expensive-threshold: 35

    scoring:
      trend-weight: 0.55
      momentum-weight: 0.30
      valuation-weight: 0.15
```

---

# Environment Variables

Sensitive values should NOT be committed.

Use environment variables for:

```text
ALPHA_VANTAGE_API_KEY
GROQ_API_KEY
TELEGRAM_BOT_TOKEN
TELEGRAM_CHAT_ID
```

Example:

```yaml
api-key: ${GROQ_API_KEY}
```

---

# Running Locally

## Development Profile

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

---

## Production Profile

```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

---

# Telegram Setup

## Create Bot

Use Telegram BotFather.

Get:

* Bot token
* Chat ID

Configure using environment variables.

---

# Scheduler

Scheduler runs only under:

```text
prod profile
```

Configured using:

```yaml
scheduler:
  cron: "0 15 18 * * MON-FRI"
```

---

# AI Commentary Philosophy

The project intentionally follows:

```text
Deterministic Analysis First
AI Commentary Second
```

The AI layer:

* does NOT compute indicators
* does NOT make trading decisions
* does NOT override deterministic analysis

Instead, AI:

* synthesizes
* contextualizes
* compresses commentary

This significantly reduces hallucination risk.

---

# Design Principles

## Deterministic-first Architecture

The system prioritizes:

* explainability
* reproducibility
* signal transparency

before AI augmentation.

---

## Modular Rule Engine

Rules are isolated and independently extensible.

Adding a new rule requires:

* implementing AnalysisRule
* registering as Spring component

---

## Directional Signal Aggregation

Signals contribute directionally:

* bullish
* bearish
* neutral

instead of simplistic optimistic averaging.

---

## Resilient AI Integration

AI failures do NOT break:

* analysis pipeline
* scheduler
* Telegram delivery

The system gracefully degrades.

---

# Exception Handling

The project uses custom domain-specific exceptions to improve:

* error classification
* API failure handling
* debugging clarity
* graceful degradation
* production observability

Custom exceptions:

## ApiException

Used when external providers fail.

Examples:

* AlphaVantage failures
* Groq API failures
* invalid HTTP responses
* provider throttling

```java
package com.market.analysis.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## MarketDataException

Used when market data is incomplete or invalid.

Examples:

* missing candles
* insufficient historical data
* invalid indicator values
* malformed responses

```java
package com.market.analysis.exception;

public class MarketDataException extends RuntimeException {

    public MarketDataException(String message) {
        super(message);
    }

    public MarketDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## AICommentaryException

Used when AI commentary generation fails.

Examples:

* Groq API unavailable
* invalid AI response
* timeout issues
* malformed commentary payloads

```java
package com.market.analysis.exception;

public class AICommentaryException extends RuntimeException {

    public AICommentaryException(String message) {
        super(message);
    }

    public AICommentaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## NotificationException

Used when notification delivery fails.

Examples:

* Telegram API failures
* invalid chat ID
* bot authorization issues
* network failures during message delivery

```java
package com.market.analysis.exception;

public class NotificationException extends RuntimeException {

    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## Design Philosophy

The application is intentionally designed to:

* fail gracefully
* isolate external API failures
* continue deterministic analysis even if AI fails
* preserve scheduler stability
* avoid crashing the full analysis pipeline

Example:

* Groq failure should NOT stop Telegram delivery
* AI failure should NOT stop technical analysis
* individual stock failure should NOT stop portfolio analysis

---

# Future Improvements

Potential future enhancements:

* sector-relative valuation
* market regime detection
* portfolio-level intelligence
* duplicate notification suppression
* persistent historical analysis
* backtesting engine
* multi-provider aggregation
* support/resistance detection
* volume profile analysis
* portfolio risk scoring
* watchlist intelligence
* market breadth indicators

---

# Example Output

```text
📈 RELIANCE

Price: ₹1435.70
Recommendation: Moderate Buy

Trend: NEUTRAL
Momentum: STRONG
Confidence: 45%

AI Commentary:
Momentum remains constructive, though
medium-term trend alignment is still weak.
Current structure favors gradual accumulation
rather than aggressive positioning.
```

---

# Disclaimer

This project is intended for educational and research purposes only.

It should NOT be considered financial advice.

Always perform independent research before making investment decisions.
