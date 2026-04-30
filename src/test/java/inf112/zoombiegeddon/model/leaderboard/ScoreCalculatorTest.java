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
}
