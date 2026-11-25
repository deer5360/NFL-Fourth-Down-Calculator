public class FourthDownPlay
{
    private int yardsNeeded, yardsToEndzone, yardsGained;
    private boolean converted;

    public FourthDownPlay(int yN, int yTE, int yG, boolean con)
    {
        yardsNeeded = yN;
        yardsToEndzone = yTE;
        yardsGained = yG;
        converted = con;
    }

    public int getYardsNeeded() {
        return yardsNeeded;
    }

    public int getYardsToEndzone() {
        return yardsToEndzone;
    }

    public int getYardsGained() {
        return yardsGained;
    }

    public boolean isConverted() {
        return converted;
    }
}
