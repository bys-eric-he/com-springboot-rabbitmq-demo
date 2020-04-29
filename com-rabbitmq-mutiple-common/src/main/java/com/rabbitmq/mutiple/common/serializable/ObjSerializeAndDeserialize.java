package com.rabbitmq.mutiple.common.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象的序列化和反序列
 * 实现序列化的必备要求：有实现了Serializable或者Externalizable接口的类的对象才能被序列化为字节序列。
 */
public class ObjSerializeAndDeserialize {

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static byte[] Serialize(Object object) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(object);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object Deserialize(byte[] bytes) {
        Object object = null;
        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            object = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
