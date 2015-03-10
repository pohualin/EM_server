package com.emmisolutions.emmimanager.persistence.configuration.serializer;

import com.esotericsoftware.kryo.Kryo;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Kryo serializer for Specifications object.
 */
public class SpecificationsKryoStreamSerializer extends BaseKryoStreamSerializer<Specifications> {

    private static final ThreadLocal<Kryo> kryoThreadLocal
            = new ThreadLocal<Kryo>() {

        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(Specifications.class);
            return kryo;
        }
    };

    public SpecificationsKryoStreamSerializer(boolean compress, int typeId) {
        super(compress, typeId);
    }


    @Override
    protected Class<Specifications> getType() {
        return Specifications.class;
    }

    @Override
    protected Kryo getKryo() {
        return kryoThreadLocal.get();
    }
}
