public class scoreCalculate {
    int numVertices;
    int numEdges;
    int chromatic;
    int colorUsed;
    int seconds; //takes time in seconds from timeservice. Use this variable in score calculation.
    public scoreCalculate(int numVertices, int numEdges, int chromatic, int colorUsed, TimeService gameTime) {
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        this.chromatic = chromatic;
        this.colorUsed = colorUsed;
        seconds = TimeService.formatTimetoSeconds(gameTime);
    }

    public int calculateScore() {
        int copyV = numVertices;
        int copyE = numEdges;
        double L = 0.4;
        while(copyV + copyE >= 10){
            copyV -= 5;
            copyE -= 5;
            L += 0.2;
        }
        double score = ((double) ((numVertices + numEdges) - (colorUsed - chromatic)) /(numVertices + numEdges)) * 100 * L;
        score=score-(double)(seconds/(L*10));
        if (score < 0) {
            return 0;
        }
        return (int) score;
    }

}
