package occupationalTherapies.activities.reaction;

public class ReactionStatistics {
	private long bestTime  = 100000;
	private long worstTime = 0;
	private long numberOfInteractions = 0;
	private double totalReactionTime = 0;
	
	public void addStatistic(long reactionTime){
		if(reactionTime < bestTime)bestTime = reactionTime;
		if(reactionTime > worstTime) worstTime = reactionTime;
		numberOfInteractions++;
		totalReactionTime += reactionTime;
	}
	
	public long getBestTime(){
		return bestTime;
	}
	
	public long getWorstTime(){
		return worstTime;
	}
	
	public double getAverageReactionTime(){
		if(numberOfInteractions == 0)return 0;
		else return totalReactionTime/numberOfInteractions;
	}
}
