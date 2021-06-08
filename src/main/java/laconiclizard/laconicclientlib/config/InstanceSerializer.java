package laconiclizard.laconicclientlib.config;

import me.shedaniel.autoconfig.ConfigData;

/**
 * A ConfigSerializer that allows in-memory editing of an existing "backer" config instance.
 * When this serializer "serializes" it writes into the backer instance.
 * When this serializer "deserializes" it returns a copy of the backer instance.
 *
 * @param <T> type of config to de/serialize
 */
public class InstanceSerializer<T extends ConfigData> implements CustomSerializer<T> {

    public final Class<T> clazz;
    protected final Object lock = new Object();
    protected T backer = null;

    /**
     * Create a new InstanceSerializer.
     *
     * @param clazz type of class to de/serialize
     */
    public InstanceSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Set the config instance that backs this serializer.
     * Null values are permitted, but will cause an IllegalStateException if de/serialization is attempted
     * while the backer is null.
     *
     * @param backer new backer for this serializer
     */
    public void setBacker(T backer) {
        synchronized (lock) {
            this.backer = backer;
        }
    }

    @Override public void serialize(T t) {
        synchronized (lock) {
            if (backer == null) {
                throw new IllegalStateException("No dest set when attempting to serialize " + t);
            } else {
                ConfigSig.getSig(clazz).copyInto(backer, t);
            }
        }
    }

    @Override public T deserialize() {
        synchronized (lock) {
            if (backer == null) {
                throw new IllegalStateException("No dest set when attempting to deserialize");
            }
            ConfigSig<T, ?> sig = ConfigSig.getSig(clazz);
            return sig.copyInto(sig.createDefault(), backer);
        }
    }

    @Override public T createDefault() {
        synchronized (lock) {
            return ConfigSig.getSig(clazz).createDefault();
        }
    }

}
