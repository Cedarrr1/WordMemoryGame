package tool;

public class ScoreManager {
    private int score;
    private int hp;
    private final int maxHP;

    public ScoreManager(int initialScore, int initialHp) {
        this.score = initialScore;
        this.hp = initialHp;
        this.maxHP = initialHp;
    }

    public void addScore(int points) {
        score += points;
    }

    public void deductScore(int points) {
        score -= points;
    }

    public void deductHp(int points) {
        hp -= points;
        if (hp < 0) {
            hp = 0; // 确保生命值不会小于0
        }
    }

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public boolean isGameOver() {
        return hp <= 0;
    }
}
