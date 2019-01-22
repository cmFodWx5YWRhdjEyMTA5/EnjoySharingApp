package enjoysharing.enjoysharing.DataObject;

import java.util.ArrayList;
import java.util.List;

public class ParameterCollection {

    public List<Parameter> ParameterList;

    public ParameterCollection() {
        ParameterList = new ArrayList<Parameter>();
    }

    public void setParameterList(List<Parameter> params) {
        this.ParameterList = params;
    }

    public void Add(Parameter param) {
        ParameterList.add(param);
    }

    public void Add(String Name, Object Value) {
        ParameterList.add(new Parameter(Name, Value));
    }

    public void Add(String Name, Object Value, String Type) {
        ParameterList.add(new Parameter(Name, Value, Type));
    }

    public void Update(String Name, Object Value) {
        Drop(Name);
        Add(Name, Value);
    }

    public void Drop(String Name) {
        if (Contains(Name))
            ParameterList.remove(IndexOfName(Name));
    }

    public int IndexOfName(String Name) {
        int ret = -1;
        for (int i = 0; i < ParameterList.size(); i++)
            if (ParameterList.get(i).Name.equals(Name))
                ret = i;
        return ret;
    }

    public boolean Contains(String Name) {
        boolean IsContain = false;
        for (Parameter param : ParameterList)
            if (param.Name.equals(Name))
                IsContain = true;
        return IsContain;
    }

    public Object Get(String Name) {
        for (Parameter param : ParameterList)
            if (param.Name.equals(Name))
                return param.Value;
        return null;
    }

    public Parameter GetParameter(String Name) {
        for (Parameter param : ParameterList)
            if (param.Name.equals(Name))
                return param;
        return null;
    }

    public Object[] GetParametersListObject() {
        Object[] parameters = new Object[ParameterList.size()];
        for (int i = 0; i < ParameterList.size(); i++)
            parameters[i] = ((Parameter) ParameterList.get(i)).Value;
        return parameters;
    }

    public List<Parameter> GetParametersList() {
        return ParameterList;
    }

    public int Size() {
        return ParameterList.size();
    }
}
