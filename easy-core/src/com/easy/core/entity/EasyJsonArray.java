package com.easy.core.entity;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONTokener;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class EasyJsonArray {

    private final ArrayList<Object> myArrayList;

    public EasyJsonArray() {
        this.myArrayList = new ArrayList();
    }

    public EasyJsonArray(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A EasyJsonArray text must start with '['");
        } else if (x.nextClean() != ']') {
            x.back();

            while(true) {
                if (x.nextClean() == ',') {
                    x.back();
                    this.myArrayList.add(EasyJson.NULL);
                } else {
                    x.back();
                    this.myArrayList.add(x.nextValue());
                }

                switch(x.nextClean()) {
                    case ',':
                        if (x.nextClean() == ']') {
                            return;
                        }

                        x.back();
                        break;
                    case ']':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }

    public EasyJsonArray(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public EasyJsonArray(Collection<Object> collection) {
        this.myArrayList = new ArrayList();
        if (collection != null) {
            Iterator iter = collection.iterator();

            while(iter.hasNext()) {
                this.myArrayList.add(EasyJson.wrap(iter.next()));
            }
        }

    }

    public EasyJsonArray(Object array) throws JSONException {
        this();
        if (!array.getClass().isArray()) {
            throw new JSONException("EasyJsonArray initial value should be a string or collection or array.");
        } else {
            int length = Array.getLength(array);

            for(int i = 0; i < length; ++i) {
                this.put(EasyJson.wrap(Array.get(array, i)));
            }

        }
    }

    public Object get(int index) {
        Object object = opt(index);
        if (object == null) {
            return null;
        } else {
            return object;
        }
    }

    public boolean getBoolean(int index) throws JSONException {
        Object object = get(index);
        if (!object.equals(Boolean.FALSE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("false"))) {
            if (!object.equals(Boolean.TRUE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("true"))) {
                throw new JSONException("EasyJsonArray[" + index + "] is not a boolean.");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public double getDouble(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        } catch (Exception var4) {
            throw new JSONException("EasyJsonArray[" + index + "] is not a number.");
        }
    }

    public int getInt(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).intValue() : Integer.parseInt((String)object);
        } catch (Exception var4) {
            throw new JSONException("EasyJsonArray[" + index + "] is not a number.");
        }
    }

    public EasyJsonArray getJSONArray(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof EasyJsonArray) {
            return (EasyJsonArray)object;
        } else {
            throw new JSONException("EasyJsonArray[" + index + "] is not a EasyJsonArray.");
        }
    }

    public EasyJson getJSONObject(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof EasyJson) {
            return (EasyJson)object;
        } else {
            throw new JSONException("EasyJsonArray[" + index + "] is not a EasyJson.");
        }
    }

    public long getLong(int index) throws JSONException {
        Object object = this.get(index);

        try {
            return object instanceof Number ? ((Number)object).longValue() : Long.parseLong((String)object);
        } catch (Exception var4) {
            throw new JSONException("EasyJsonArray[" + index + "] is not a number.");
        }
    }

    public String getString(int index) throws JSONException {
        Object object = this.get(index);
        if (object instanceof String) {
            return (String)object;
        } else {
            throw new JSONException("EasyJsonArray[" + index + "] not a string.");
        }
    }

    public boolean isNull(int index) {
        return EasyJson.NULL.equals(this.opt(index));
    }

    public String join(String separator) throws JSONException {
        int len = this.length();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < len; ++i) {
            if (i > 0) {
                sb.append(separator);
            }

            sb.append(EasyJson.valueToString(this.myArrayList.get(i)));
        }

        return sb.toString();
    }

    public int length() {
        return this.myArrayList.size();
    }

    public Object opt(int index) {
        return index >= 0 && index < this.length() ? this.myArrayList.get(index) : null;
    }

    public boolean optBoolean(int index) {
        return this.optBoolean(index, false);
    }

    public boolean optBoolean(int index, boolean defaultValue) {
        try {
            return this.getBoolean(index);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public double optDouble(int index) {
        return this.optDouble(index, 0.0D / 0.0);
    }

    public double optDouble(int index, double defaultValue) {
        try {
            return this.getDouble(index);
        } catch (Exception var5) {
            return defaultValue;
        }
    }

    public int optInt(int index) {
        return this.optInt(index, 0);
    }

    public int optInt(int index, int defaultValue) {
        try {
            return this.getInt(index);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public EasyJsonArray optJSONArray(int index) {
        Object o = this.opt(index);
        return o instanceof EasyJsonArray ? (EasyJsonArray)o : null;
    }

    public EasyJson optJSONObject(int index) {
        Object o = this.opt(index);
        return o instanceof EasyJson ? (EasyJson)o : null;
    }

    public long optLong(int index) {
        return this.optLong(index, 0L);
    }

    public long optLong(int index, long defaultValue) {
        try {
            return this.getLong(index);
        } catch (Exception var5) {
            return defaultValue;
        }
    }

    public String optString(int index) {
        return this.optString(index, "");
    }

    public String optString(int index, String defaultValue) {
        Object object = this.opt(index);
        return EasyJson.NULL.equals(object) ? defaultValue : object.toString();
    }

    public EasyJsonArray put(boolean value) {
        this.put((Object)(value ? Boolean.TRUE : Boolean.FALSE));
        return this;
    }

    public EasyJsonArray put(Collection<Object> value) {
        this.put((Object)(new EasyJsonArray(value)));
        return this;
    }

    public EasyJsonArray put(double value) throws JSONException {
        Double d = new Double(value);
        EasyJson.testValidity(d);
        this.put((Object)d);
        return this;
    }

    public EasyJsonArray put(int value) {
        this.put((Object)(new Integer(value)));
        return this;
    }

    public EasyJsonArray put(long value) {
        this.put((Object)(new Long(value)));
        return this;
    }

    public EasyJsonArray put(Map<String, Object> value) {
        this.put((Object)(new EasyJson(value)));
        return this;
    }

    public EasyJsonArray put(Object value) {
        this.myArrayList.add(value);
        return this;
    }

    public EasyJsonArray put(int index, boolean value) throws JSONException {
        this.put(index, (Object)(value ? Boolean.TRUE : Boolean.FALSE));
        return this;
    }

    public EasyJsonArray put(int index, Collection<Object> value) throws JSONException {
        this.put(index, (Object)(new EasyJsonArray(value)));
        return this;
    }

    public EasyJsonArray put(int index, double value) throws JSONException {
        this.put(index, (Object)(new Double(value)));
        return this;
    }

    public EasyJsonArray put(int index, int value) throws JSONException {
        this.put(index, (Object)(new Integer(value)));
        return this;
    }

    public EasyJsonArray put(int index, long value) throws JSONException {
        this.put(index, (Object)(new Long(value)));
        return this;
    }

    public EasyJsonArray put(int index, Map<String, Object> value) throws JSONException {
        this.put(index, (Object)(new EasyJson(value)));
        return this;
    }

    public EasyJsonArray put(int index, Object value) throws JSONException {
        EasyJson.testValidity(value);
        if (index < 0) {
            throw new JSONException("EasyJsonArray[" + index + "] not found.");
        } else {
            if (index < this.length()) {
                this.myArrayList.set(index, value);
            } else {
                while(index != this.length()) {
                    this.put(EasyJson.NULL);
                }

                this.put(value);
            }

            return this;
        }
    }

    public Object remove(int index) {
        return index >= 0 && index < this.length() ? this.myArrayList.remove(index) : null;
    }

    public boolean similar(Object other) {
        if (!(other instanceof EasyJsonArray)) {
            return false;
        } else {
            int len = this.length();
            if (len != ((EasyJsonArray)other).length()) {
                return false;
            } else {
                for(int i = 0; i < len; ++i) {
                    Object valueThis = get(i);
                    Object valueOther = ((EasyJsonArray)other).get(i);
                    if (valueThis instanceof EasyJson) {
                        if (!((EasyJson)valueThis).similar(valueOther)) {
                            return false;
                        }
                    } else if (valueThis instanceof EasyJsonArray) {
                        if (!((EasyJsonArray)valueThis).similar(valueOther)) {
                            return false;
                        }
                    } else if (!valueThis.equals(valueOther)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public EasyJson toJSONObject(EasyJsonArray names) throws JSONException {
        if (names != null && names.length() != 0 && this.length() != 0) {
            EasyJson jo = new EasyJson();

            for(int i = 0; i < names.length(); ++i) {
                jo.put(names.getString(i), this.opt(i));
            }

            return jo;
        } else {
            return null;
        }
    }

    public String toString() {
        try {
            return this.toString(0);
        } catch (Exception var2) {
            return null;
        }
    }

    public String toString(int indentFactor) throws JSONException {
        StringWriter sw = new StringWriter();
        synchronized(sw.getBuffer()) {
            return this.write(sw, indentFactor, 0).toString();
        }
    }

    public Writer write(Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }

    Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        try {
            boolean commanate = false;
            int length = this.length();
            writer.write(91);
            if (length == 1) {
                EasyJson.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
            } else if (length != 0) {
                int newindent = indent + indentFactor;

                for(int i = 0; i < length; ++i) {
                    if (commanate) {
                        writer.write(44);
                    }

                    if (indentFactor > 0) {
                        writer.write(10);
                    }

                    EasyJson.indent(writer, newindent);
                    EasyJson.writeValue(writer, this.myArrayList.get(i), indentFactor, newindent);
                    commanate = true;
                }

                if (indentFactor > 0) {
                    writer.write(10);
                }

                EasyJson.indent(writer, indent);
            }

            writer.write(93);
            return writer;
        } catch (IOException var8) {
            throw new JSONException(var8);
        }
    }
}
