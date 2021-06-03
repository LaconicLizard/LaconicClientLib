package laconiclizard.laconicclientlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/** Utility representing the various Gson-related types. */
public enum GsonType {
    // dev note: ordered topologically
    STRING(String.class) {
        @Override public void put(JsonObject o, String key, Object value) {
            o.addProperty(key, (String) value);
        }
    }, NUMBER(Number.class) {
        @Override public void put(JsonObject o, String key, Object value) {
            o.addProperty(key, (Number) value);
        }
    }, BOOLEAN(Boolean.class) {
        @Override public void put(JsonObject o, String key, Object value) {
            o.addProperty(key, (Boolean) value);
        }
    }, CHARACTER(Character.class) {
        @Override public void put(JsonObject o, String key, Object value) {
            o.addProperty(key, (Character) value);
        }
    }, ELEMENT(Object.class) {
        @Override public void put(JsonObject o, String key, Object value) {
            o.add(key, GSON.toJsonTree(value));
        }
    };

    /* Gson used to recursively serialize objects.  May be freely changed, but not set to null. */
    public static volatile Gson GSON = new GsonBuilder().create();

    // the corresponding Java type of this GsonType
    public final Class<?> javaType;

    GsonType(Class<?> javaType) {
        this.javaType = javaType;
    }

    /**
     * Acquire the GsonType corresponding to the given Java type.
     * Works on primitive types (eg. int.class).
     *
     * @param javaType type of interest
     * @return GsonType corresponding to javaType
     */
    public static GsonType fromType(Class<?> javaType) {
        if (javaType == byte.class || javaType == short.class || javaType == int.class || javaType == long.class
                || javaType == float.class || javaType == double.class) {
            return NUMBER;
        } else if (javaType == boolean.class) {
            return BOOLEAN;
        } else if (javaType == char.class) {
            return CHARACTER;
        }
        for (GsonType gt : GsonType.values()) {
            if (gt.javaType.isAssignableFrom(javaType)) {
                return gt;
            }
        }
        throw new AssertionError("Failed to classify GsonType of " + javaType + " (should be impossible)");
    }

    /**
     * Put an object of this type into the given object.
     *
     * @param o     object to put an entry into
     * @param key   key under which to put the entry
     * @param value value of the entry (must be of this gson type)
     */
    public abstract void put(JsonObject o, String key, Object value);

}
