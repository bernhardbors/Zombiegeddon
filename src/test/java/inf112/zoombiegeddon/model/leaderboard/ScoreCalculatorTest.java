package inf112.zoombiegeddon.model.leaderboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreCalculatorTest {

    @Test
    void calculateLevelScoreReturnsCorrectScoreForNormalValues() {
        ScoreCalculator calculator = new ScoreCalculator();
        ScoreData data = new ScoreData(10, 3, 50.0f);

        int score = calculator.calculateLevelScore(data);

        assertEquals(5000, score);
    }

    @Test
    void calculateLevelScoreReturnsZeroTimeBonusWhenTimeIsVeryHigh() {
        ScoreCalculator calculator = new ScoreCalculator();
        ScoreData data = new ScoreData(5, 2, 500.0f);

        int score = calculator.calculateLevelScore(data);

        assertEquals(1500, score);
    }

    @Test
    void calculateLevelScoreHandlesZeroItemsZeroLivesAndZeroTime() {
        ScoreCalculator calculator = new ScoreCalculator();
        ScoreData data = new ScoreData(0, 0, 0.0f);

        int score = calculator.calculateLevelScore(data);

        assertEquals(3000, score);
    }

    @Test
    void calculateLevelScoreHandlesMixedValuesCorrectly() {
        ScoreCalculator calculator = new ScoreCalculator();
        ScoreData data = new ScoreData(7, 1, 123.4f);

        int score = calculator.calculateLevelScore(data);

        assertEquals(2966, score);
    }

    @Test
    void calculateLevelScoreTimeBonusIsZeroAtExactBoundary() {
        ScoreCalculator calculator = new ScoreCalculator();
        // timeUsed = 300.0f → (int)(300.0 * 10) = 3000 → 3000 - 3000 = 0 → Math.max(0, 0) = 0
        ScoreData data = new ScoreData(0, 0, 300.0f);
        assertEquals(0, calculator.calculateLevelScore(data));
    }

    @Test
    void calculateLevelScoreTimeBonusIsOneJustBelowBoundary() {
        ScoreCalculator calculator = new ScoreCalculator();
        // timeUsed = 299.9f → (int)(2999.0) = 2999 → 3000 - 2999 = 1
        ScoreData data = new ScoreData(0, 0, 299.9f);
        assertEquals(1, calculator.calculateLevelScore(data));
    }

    @Test
    void calculateLevelScoreEachItemCountsAsExactlyOneHundred() {
        ScoreCalculator calculator = new ScoreCalculator();
        assertEquals(100, calculator.calculateLevelScore(new ScoreData(1, 0, 300.0f)));
        assertEquals(500, calculator.calculateLevelScore(new ScoreData(5, 0, 300.0f)));
    }

    @Test
    void calculateLevelScoreEachLifeCountsAsExactlyFiveHundred() {
        ScoreCalculator calculator = new ScoreCalculator();
        assertEquals(500, calculator.calculateLevelScore(new ScoreData(0, 1, 300.0f)));
        assertEquals(1500, calculator.calculateLevelScore(new ScoreData(0, 3, 300.0f)));
    }
}
