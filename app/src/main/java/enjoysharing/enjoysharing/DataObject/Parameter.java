package enjoysharing.enjoysharing.DataObject;

public class Parameter {

    public String Name;
    public Object Value;
    public String Type;

    public Parameter(String Name,Object Value)
    {
        this.Name = Name;
        this.Value = Value;
    }

    public Parameter(String Name,Object Value,String Type)
    {
        this.Name = Name;
        this.Value = Value;
        this.Type = Type;
    }

    public String GetName()
    {
        return this.Name;
    }

    public Object GetValue()
    {
        return this.Value;
    }

}

