package AndroidGobbler;

public class Utility {
	
	//TODO: Remove all the stuff I'm no longer using
	
	public static int scoreBoard2(Board b, Team team) {
		int score = 0;
		for (int i = 0; i < 10; ++i) {
			score += Math.pow(2, b.scoreBoardBlack[i] - 1) + Math.max(0, 1000 * (b.scoreBoardBlack[i] - 3));
		}
		for (int j = 0; j < 10; ++j) {
			score -= Math.pow(2, b.scoreBoardRed[j] - 1) + Math.max(0, 1000 * (b.scoreBoardRed[j] - 3));
		}
		if (team == Team.RED) {
			return score * -1;
		} else {
			return score;
		}
	}
}
