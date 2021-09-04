package com.easy.core.entity;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONString;
import org.codehaus.jettison.json.JSONTokener;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class EasyJson {


    private final Map<String, Object> map;
    public static final Object NULL = new Null();

    public EasyJson() {
        this.map = new LinkedHashMap();
    }

    public EasyJson(EasyJson jo, String[] names) {
        this();

        for(int i = 0; i < names.length; ++i) {
            try {
                this.putOnce(names[i], jo.opt(names[i]));
            } catch (Exception var5) {
            }
        }

    }

    public EasyJson(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '{') {
            throw x.syntaxError("A EasyJson text must begin with '{'");
        } else {
            while(true) {
                char c = x.nextClean();
                switch(c) {
                    case '\u0000':
                        throw x.syntaxError("A EasyJson text must end with '}'");
                    case '}':
                        return;
                    default:
                        x.back();
                        String key = x.nextValue().toString();
                        c = x.nextClean();
                        if (c != ':') {
                            throw x.syntaxError("Expected a ':' after a key");
                        }

                        this.putOnce(key, x.nextValue());
                        switch(x.nextClean()) {
                            case ',':
                            case ';':
                                if (x.nextClean() == '}') {
                                    return;
                                }

                                x.back();
                                break;
                            case '}':
                                return;
                            default:
                                throw x.syntaxError("Expected a ',' or '}'");
                        }
                }
            }
        }
    }

    public EasyJson(Map<String, Object> map) {
        this.map = new HashMap();
        if (map != null) {
            Iterator i = map.entrySet().iterator();

            while(i.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)i.next();
                Object value = entry.getValue();
                if (value != null) {
                    this.map.put(entry.getKey(), wrap(value));
                }
            }
        }

    }

    public EasyJson(Object bean) {
        this();
        this.populateMap(bean);
    }

    public EasyJson(Object object, String[] names) {
        this();
        Class c = object.getClass();

        for(int i = 0; i < names.length; ++i) {
            String name = names[i];

            try {
                this.putOpt(name, c.getField(name).get(object));
            } catch (Exception var7) {
            }
        }

    }

    public EasyJson(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public EasyJson(String baseName, Locale locale) throws JSONException {
        this();
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, Thread.currentThread().getContextClassLoader());
        Enumeration keys = bundle.getKeys();

        while(true) {
            Object key;
            do {
                if (!keys.hasMoreElements()) {
                    return;
                }

                key = keys.nextElement();
            } while(key == null);

            String[] path = ((String)key).split("\\.");
            int last = path.length - 1;
            EasyJson target = this;

            for(int i = 0; i < last; ++i) {
                String segment = path[i];
                EasyJson nextTarget = target.optJSONObject(segment);
                if (nextTarget == null) {
                    nextTarget = new EasyJson();
                    target.put(segment, (Object)nextTarget);
                }

                target = nextTarget;
            }

            target.put(path[last], (Object)bundle.getString((String)key));
        }
    }

    public EasyJson accumulate(String key, Object value) throws JSONException {
        testValidity(value);
        Object object = this.opt(key);
        if (object == null) {
            this.put(key, value instanceof EasyJsonArray ? (new EasyJsonArray()).put(value) : value);
        } else if (object instanceof EasyJsonArray) {
            ((EasyJsonArray)object).put(value);
        } else {
            this.put(key, (Object)(new EasyJsonArray()).put(object).put(value));
        }

        return this;
    }

    public EasyJson append(String key, Object value) throws JSONException {
        testValidity(value);
        Object object = this.opt(key);
        if (object == null) {
            this.put(key, (Object)(new EasyJsonArray()).put(value));
        } else {
            if (!(object instanceof EasyJsonArray)) {
                throw new JSONException("EasyJson[" + key + "] is not a EasyJsonArray.");
            }

            this.put(key, (Object)((EasyJsonArray)object).put(value));
        }

        return this;
    }

    public static String doubleToString(double d) {
        if (!Double.isInfinite(d) && !Double.isNaN(d)) {
            String string = Double.toString(d);
            if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
                while(string.endsWith("0")) {
                    string = string.substring(0, string.length() - 1);
                }

                if (string.endsWith(".")) {
                    string = string.substring(0, string.length() - 1);
                }
            }

            return string;
        } else {
            return "null";
        }
    }

    public Object get(String key) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        } else {
            Object object = this.opt(key);
            if (object == null) {
                throw new JSONException("EasyJson[" + quote(key) + "] not found.");
            } else {
                return object;
            }
        }
    }

    public boolean getBoolean(String key) throws JSONException {
        Object object = this.get(key);
        if (!object.equals(Boolean.FALSE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("false"))) {
            if (!object.equals(Boolean.TRUE) && (!(object instanceof String) || !((String)object).equalsIgnoreCase("true"))) {
                throw new JSONException("EasyJson[" + quote(key) + "] is not a Boolean.");
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public double getDouble(String key) throws JSONException {
        Object object = this.get(key);

        try {
            return object instanceof Number ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        } catch (Exception var4) {
            throw new JSONException("EasyJson[" + quote(key) + "] is not a number.");
        }
    }

    public int getInt(String key) throws JSONException {
        Object object = this.get(key);

        try {
            return object instanceof Number ? ((Number)object).intValue() : Integer.parseInt((String)object);
        } catch (Exception var4) {
            throw new JSONException("EasyJson[" + quote(key) + "] is not an int.");
        }
    }

    public EasyJsonArray getJSONArray(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof EasyJsonArray) {
            return (EasyJsonArray)object;
        } else {
            throw new JSONException("EasyJson[" + quote(key) + "] is not a EasyJsonArray.");
        }
    }

    public EasyJson getJSONObject(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof EasyJson) {
            return (EasyJson)object;
        } else {
            throw new JSONException("EasyJson[" + quote(key) + "] is not a EasyJson.");
        }
    }

    public long getLong(String key) throws JSONException {
        Object object = this.get(key);

        try {
            return object instanceof Number ? ((Number)object).longValue() : Long.parseLong((String)object);
        } catch (Exception var4) {
            throw new JSONException("EasyJson[" + quote(key) + "] is not a long.");
        }
    }

    public static String[] getNames(EasyJson jo) {
        int length = jo.length();
        if (length == 0) {
            return null;
        } else {
            Iterator<String> iterator = jo.keys();
            String[] names = new String[length];

            for(int i = 0; iterator.hasNext(); ++i) {
                names[i] = (String)iterator.next();
            }

            return names;
        }
    }

    public static String[] getNames(Object object) {
        if (object == null) {
            return null;
        } else {
            Class klass = object.getClass();
            Field[] fields = klass.getFields();
            int length = fields.length;
            if (length == 0) {
                return null;
            } else {
                String[] names = new String[length];

                for(int i = 0; i < length; ++i) {
                    names[i] = fields[i].getName();
                }

                return names;
            }
        }
    }

    public String getString(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof String) {
            return (String)object;
        } else {
            throw new JSONException("EasyJson[" + quote(key) + "] not a string.");
        }
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public EasyJson increment(String key) throws JSONException {
        Object value = this.opt(key);
        if (value == null) {
            this.put(key, 1);
        } else if (value instanceof Integer) {
            this.put(key, (Integer)value + 1);
        } else if (value instanceof Long) {
            this.put(key, (Long)value + 1L);
        } else if (value instanceof Double) {
            this.put(key, (Double)value + 1.0D);
        } else {
            if (!(value instanceof Float)) {
                throw new JSONException("Unable to increment [" + quote(key) + "].");
            }

            this.put(key, (double)((Float)value + 1.0F));
        }

        return this;
    }

    public boolean isNull(String key) {
        return NULL.equals(this.opt(key));
    }

    public Iterator<String> keys() {
        return this.keySet().iterator();
    }

    public Set<String> keySet() {
        return this.map.keySet();
    }

    public int length() {
        return this.map.size();
    }

    public EasyJsonArray names() {
        EasyJsonArray ja = new EasyJsonArray();
        Iterator keys = this.keys();

        while(keys.hasNext()) {
            ja.put(keys.next());
        }

        return ja.length() == 0 ? null : ja;
    }

    public static String numberToString(Number number) throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        } else {
            testValidity(number);
            String string = number.toString();
            if (string.indexOf(46) > 0 && string.indexOf(101) < 0 && string.indexOf(69) < 0) {
                while(string.endsWith("0")) {
                    string = string.substring(0, string.length() - 1);
                }

                if (string.endsWith(".")) {
                    string = string.substring(0, string.length() - 1);
                }
            }

            return string;
        }
    }

    public Object opt(String key) {
        return key == null ? null : this.map.get(key);
    }

    public boolean optBoolean(String key) {
        return this.optBoolean(key, false);
    }

    public boolean optBoolean(String key, boolean defaultValue) {
        try {
            return this.getBoolean(key);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public double optDouble(String key) {
        return this.optDouble(key, 0.0D / 0.0);
    }

    public double optDouble(String key, double defaultValue) {
        try {
            return this.getDouble(key);
        } catch (Exception var5) {
            return defaultValue;
        }
    }

    public int optInt(String key) {
        return this.optInt(key, 0);
    }

    public int optInt(String key, int defaultValue) {
        try {
            return this.getInt(key);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public EasyJsonArray optJSONArray(String key) {
        Object o = this.opt(key);
        return o instanceof EasyJsonArray ? (EasyJsonArray)o : null;
    }

    public EasyJson optJSONObject(String key) {
        Object object = this.opt(key);
        return object instanceof EasyJson ? (EasyJson)object : null;
    }

    public long optLong(String key) {
        return this.optLong(key, 0L);
    }

    public long optLong(String key, long defaultValue) {
        try {
            return this.getLong(key);
        } catch (Exception var5) {
            return defaultValue;
        }
    }

    public String optString(String key) {
        return this.optString(key, "");
    }

    public String optString(String key, String defaultValue) {
        Object object = this.opt(key);
        return NULL.equals(object) ? defaultValue : object.toString();
    }

    private void populateMap(Object bean) {
        Class klass = bean.getClass();
        boolean includeSuperClass = klass.getClassLoader() != null;
        Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();

        for(int i = 0; i < methods.length; ++i) {
            try {
                Method method = methods[i];
                if (Modifier.isPublic(method.getModifiers())) {
                    String name = method.getName();
                    String key = "";
                    if (name.startsWith("get")) {
                        if (!"getClass".equals(name) && !"getDeclaringClass".equals(name)) {
                            key = name.substring(3);
                        } else {
                            key = "";
                        }
                    } else if (name.startsWith("is")) {
                        key = name.substring(2);
                    }

                    if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        } else if (!Character.isUpperCase(key.charAt(1))) {
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                        }

                        Object result = method.invoke(bean, (Object[])null);
                        if (result != null) {
                            this.map.put(key, wrap(result));
                        }
                    }
                }
            } catch (Exception var10) {
            }
        }

    }

    public EasyJson put(String key, boolean value) throws JSONException {
        this.put(key, (Object)(value ? Boolean.TRUE : Boolean.FALSE));
        return this;
    }

    public EasyJson put(String key, Collection<Object> value) throws JSONException {
        this.put(key, (Object)(new EasyJsonArray(value)));
        return this;
    }

    public EasyJson put(String key, double value) throws JSONException {
        this.put(key, (Object)(new Double(value)));
        return this;
    }

    public EasyJson put(String key, int value) throws JSONException {
        this.put(key, (Object)(new Integer(value)));
        return this;
    }

    public EasyJson put(String key, long value) throws JSONException {
        this.put(key, (Object)(new Long(value)));
        return this;
    }

    public EasyJson put(String key, Map<String, Object> value) throws JSONException {
        this.put(key, (Object)(new EasyJson(value)));
        return this;
    }

    public EasyJson put(String key, Object value) throws JSONException {
        if (key == null) {
            throw new NullPointerException("Null key.");
        } else {
            if (value != null) {
                testValidity(value);
                this.map.put(key, value);
            } else {
                this.remove(key);
            }

            return this;
        }
    }

    public EasyJson putOnce(String key, Object value) throws JSONException {
        if (key != null && value != null) {
            if (this.opt(key) != null) {
                throw new JSONException("Duplicate key \"" + key + "\"");
            }

            this.put(key, value);
        }

        return this;
    }

    public EasyJson putOpt(String key, Object value) throws JSONException {
        if (key != null && value != null) {
            this.put(key, value);
        }

        return this;
    }

    public static String quote(String string) {
        StringWriter sw = new StringWriter();
        synchronized(sw.getBuffer()) {
            String var10000;
            try {
                var10000 = quote(string, sw).toString();
            } catch (IOException var5) {
                return "";
            }

            return var10000;
        }
    }

    public static Writer quote(String string, Writer w) throws IOException {
        if (string != null && string.length() != 0) {
            char c = 0;
            int len = string.length();
            w.write(34);

            for(int i = 0; i < len; ++i) {
                char b = c;
                c = string.charAt(i);
                switch(c) {
                    case '\b':
                        w.write("\\b");
                        continue;
                    case '\t':
                        w.write("\\t");
                        continue;
                    case '\n':
                        w.write("\\n");
                        continue;
                    case '\f':
                        w.write("\\f");
                        continue;
                    case '\r':
                        w.write("\\r");
                        continue;
                    case '"':
                    case '\\':
                        w.write(92);
                        w.write(c);
                        continue;
                    case '/':
                        if (b == '<') {
                            w.write(92);
                        }

                        w.write(c);
                        continue;
                }

                if (c >= ' ' && (c < 128 || c >= 160) && (c < 8192 || c >= 8448)) {
                    w.write(c);
                } else {
                    w.write("\\u");
                    String hhhh = Integer.toHexString(c);
                    w.write("0000", 0, 4 - hhhh.length());
                    w.write(hhhh);
                }
            }

            w.write(34);
            return w;
        } else {
            w.write("\"\"");
            return w;
        }
    }

    public Object remove(String key) {
        return this.map.remove(key);
    }

    public boolean similar(Object other) {
        try {
            if (!(other instanceof EasyJson)) {
                return false;
            } else {
                Set<String> set = this.keySet();
                if (!set.equals(((EasyJson)other).keySet())) {
                    return false;
                } else {
                    Iterator iterator = set.iterator();

                    while(iterator.hasNext()) {
                        String name = (String)iterator.next();
                        Object valueThis = this.get(name);
                        Object valueOther = ((EasyJson)other).get(name);
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
        } catch (Throwable var7) {
            return false;
        }
    }

    public static Object stringToValue(String string) {
        if (string.equals("")) {
            return string;
        } else if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        } else if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        } else if (string.equalsIgnoreCase("null")) {
            return NULL;
        } else {
            char b = string.charAt(0);
            if (b >= '0' && b <= '9' || b == '-') {
                try {
                    if (string.indexOf(46) <= -1 && string.indexOf(101) <= -1 && string.indexOf(69) <= -1) {
                        Long myLong = new Long(string);
                        if (string.equals(myLong.toString())) {
                            if (myLong == (long)myLong.intValue()) {
                                return myLong.intValue();
                            }

                            return myLong;
                        }
                    } else {
                        Double d = Double.valueOf(string);
                        if (!d.isInfinite() && !d.isNaN()) {
                            return d;
                        }
                    }
                } catch (Exception var4) {
                }
            }

            return string;
        }
    }

    public static void testValidity(Object o) throws JSONException {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            } else if (o instanceof Float && (((Float)o).isInfinite() || ((Float)o).isNaN())) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }

    }

    public EasyJsonArray toJSONArray(EasyJsonArray names) throws JSONException {
        if (names != null && names.length() != 0) {
            EasyJsonArray ja = new EasyJsonArray();

            for(int i = 0; i < names.length(); ++i) {
                ja.put(this.opt(names.getString(i)));
            }

            return ja;
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

    public String toString(int indentFactor) {
        StringWriter w = new StringWriter();
        synchronized (w.getBuffer()) {
            return write(w, indentFactor, 0).toString();
        }
    }

    public static String valueToString(Object value) throws JSONException {
        if (value != null && !value.equals((Object)null)) {
            if (value instanceof JSONString) {
                String object;
                try {
                    object = ((JSONString)value).toJSONString();
                } catch (Exception var3) {
                    throw new JSONException(var3);
                }

                if (object instanceof String) {
                    return (String)object;
                } else {
                    throw new JSONException("Bad value from toJSONString: " + object);
                }
            } else if (value instanceof Number) {
                return numberToString((Number)value);
            } else if (!(value instanceof Boolean) && !(value instanceof EasyJson) && !(value instanceof EasyJsonArray)) {
                if (value instanceof Map) {
                    return (new EasyJson((Map)value)).toString();
                } else if (value instanceof Collection) {
                    return (new EasyJsonArray((Collection)value)).toString();
                } else {
                    return value.getClass().isArray() ? (new EasyJsonArray(value)).toString() : quote(value.toString());
                }
            } else {
                return value.toString();
            }
        } else {
            return "null";
        }
    }

    public static Object wrap(Object object) {
        try {
            if (object == null) {
                return NULL;
            } else if (!(object instanceof EasyJson) && !(object instanceof EasyJsonArray) && !NULL.equals(object) && !(object instanceof JSONString) && !(object instanceof Byte) && !(object instanceof Character) && !(object instanceof Short) && !(object instanceof Integer) && !(object instanceof Long) && !(object instanceof Boolean) && !(object instanceof Float) && !(object instanceof Double) && !(object instanceof String)) {
                if (object instanceof Collection) {
                    return new EasyJsonArray((Collection)object);
                } else if (object.getClass().isArray()) {
                    return new EasyJsonArray(object);
                } else if (object instanceof Map) {
                    return new EasyJson((Map)object);
                } else {
                    Package objectPackage = object.getClass().getPackage();
                    String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
                    return !objectPackageName.startsWith("java.") && !objectPackageName.startsWith("javax.") && object.getClass().getClassLoader() != null ? new EasyJson(object) : object.toString();
                }
            } else {
                return object;
            }
        } catch (Exception var3) {
            return null;
        }
    }

    public Writer write(Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }

    static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) {
        try {
            if (value != null && !value.equals((Object) null)) {
                if (value instanceof EasyJson) {
                    ((EasyJson) value).write(writer, indentFactor, indent);
                } else if (value instanceof EasyJsonArray) {
                    ((EasyJsonArray) value).write(writer, indentFactor, indent);
                } else if (value instanceof Map) {
                    (new EasyJson((Map) value)).write(writer, indentFactor, indent);
                } else if (value instanceof Collection) {
                    (new EasyJsonArray((Collection) value)).write(writer, indentFactor, indent);
                } else if (value.getClass().isArray()) {
                    (new EasyJsonArray(value)).write(writer, indentFactor, indent);
                } else if (value instanceof Number) {
                    writer.write(numberToString((Number) value));
                } else if (value instanceof Boolean) {
                    writer.write(value.toString());
                } else if (value instanceof JSONString) {
                    String o;
                    try {
                        o = ((JSONString) value).toJSONString();
                    } catch (Exception var6) {
                        throw new JSONException(var6);
                    }

                    writer.write(o != null ? o.toString() : quote(value.toString()));
                } else {
                    quote(value.toString(), writer);
                }
            } else {
                writer.write("null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }

    static final void indent(Writer writer, int indent) throws IOException {
        for(int i = 0; i < indent; ++i) {
            writer.write(32);
        }

    }

    Writer write(Writer writer, int indentFactor, int indent) {
        try {
            boolean commanate = false;
            int length = this.length();
            Iterator<String> keys = this.keys();
            writer.write(123);
            if (length == 1) {
                Object key = keys.next();
                writer.write(quote(key.toString()));
                writer.write(58);
                if (indentFactor > 0) {
                    writer.write(32);
                }

                writeValue(writer, this.map.get(key), indentFactor, indent);
            } else if (length != 0) {
                for (int newindent = indent + indentFactor; keys.hasNext(); commanate = true) {
                    Object key = keys.next();
                    if (commanate) {
                        writer.write(44);
                    }

                    if (indentFactor > 0) {
                        writer.write(10);
                    }

                    indent(writer, newindent);
                    writer.write(quote(key.toString()));
                    writer.write(58);
                    if (indentFactor > 0) {
                        writer.write(32);
                    }

                    writeValue(writer, this.map.get(key), indentFactor, newindent);
                }

                if (indentFactor > 0) {
                    writer.write(10);
                }

                indent(writer, indent);
            }

            writer.write(125);
            return writer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final class Null {
        private Null() {
        }

        protected final Object clone() {
            return this;
        }

        public boolean equals(Object object) {
            return object == null || object == this;
        }

        public String toString() {
            return "null";
        }
    }
}
