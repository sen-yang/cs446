package sen.sen.numericonsandroid.Models;

public class PowerUp extends DroppedItem{
    private String powerUPName;

    public String getPowerUPName() {
        return powerUPName;
    }

    public void setPowerUPName(String powerUPName) {
        this.powerUPName = powerUPName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPower() {
        return Power;
    }

    public void setPower(int power) {
        Power = power;
    }

    private String Description;
    private int Power;

    public PowerUp(int number, float xPosition, float ySpeed){
        super(number, xPosition, ySpeed);

    }
}
