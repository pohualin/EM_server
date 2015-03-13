package com.emmisolutions.emmimanager.persistence.configuration.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import org.springframework.data.jpa.domain.Specifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Base Kryo serializer for Hazelcast
 */
public abstract class BaseKryoStreamSerializer<T> implements StreamSerializer<T>{

    private final boolean compress;

    private final int typeId;

    protected abstract Class<T> getType();

    protected abstract Kryo getKryo();

    public BaseKryoStreamSerializer(boolean compress, int typeId) {
        this.typeId = typeId;
        this.compress = compress;
    }

    @Override
    public int getTypeId(){
        return typeId;
    }

    @Override
    public void write(ObjectDataOutput out, T object) throws IOException {
        Kryo kryo = getKryo();
        if (compress) {
            ByteArrayOutputStream byteArrayOutputStream =
                    new ByteArrayOutputStream(16384);
            DeflaterOutputStream deflaterOutputStream =
                    new DeflaterOutputStream(byteArrayOutputStream);
            Output output = new Output(deflaterOutputStream);
            kryo.writeObject(output, object);
            output.close();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            out.write(bytes);
        } else {
            Output output = new Output((OutputStream) out);
            kryo.writeObject(output, object);
            output.flush();
        }

    }

    @Override
    public T read(ObjectDataInput objectDataInput) throws IOException {
        InputStream in = (InputStream) objectDataInput;
        if (compress) {
            in = new InflaterInputStream(in);
        }
        Input input = new Input(in);
        Kryo kryo = getKryo();
        return kryo.readObject(input, getType());
    }


    @Override
    public void destroy() {

    }
}
