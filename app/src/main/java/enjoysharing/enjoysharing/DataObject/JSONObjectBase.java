package enjoysharing.enjoysharing.DataObject;

public class JSONObjectBase {

    private Class activityClass;
    private int voiceCommands;
    private String action;

    public Class getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public int getVoiceCommands() {
        return voiceCommands;
    }

    public void setVoiceCommands(int voiceCommands) {
        this.voiceCommands = voiceCommands;
    }

    public String getAction() { return action; }

    public void setAction(String action) { this.action = action; }

    public JSONObjectBase() { }

    public JSONObjectBase(String jsonStr)  // Arriva senza "class "
    {
        try
        {
            String[] params =jsonStr.split(",");
            setActivityClass(Class.forName(params[0]));
            setVoiceCommands(Integer.parseInt(params[1]));
            setAction(params[2]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
