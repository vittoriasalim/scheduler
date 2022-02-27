public class Unit {

    private String Name;
    private int Day;
    private int StartTime;
    private int EndTime;

    public Unit(String Name, int Day, int StartTime, int EndTime) {

        this.Name = Name;
        this.Day = Day;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
    }

    public int getStartTime() { return this.StartTime; }
    public int getEndTime() { return this.EndTime; }
    public int getDay() { return this.Day; }
    
    @Override
    public String toString() { return this.Name; }
}