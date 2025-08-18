package com.sparrow.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializeUtil {
    
    private static Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

    public static final int BUFFER_SIZE = 2048;
    public static final int MAX_BUFFER_SIZE = 10485760;

    private static Kryo getKryo(){
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    }

    public static byte[] serialize(Object t) throws Exception{
        Kryo kryo = null;
        Output output = null;
        try{
            kryo =getKryo();
            output = new Output(BUFFER_SIZE, MAX_BUFFER_SIZE);
            kryo.writeClassAndObject(output, t);
            return output.toBytes();
            
        }catch(Exception e){
            logger.error("异常", e);
            throw e;
        }finally{
            if(output != null){
                try {
                    output.close();
                    output = null;
                } catch (Exception e) {
                    logger.error("异常", e);
                }
            }
        }
    }
    
    public static Object deserialize(byte[] bytes) throws Exception{
        Kryo kryo = null;
        Input input = null;
        try{
            kryo =getKryo();
            input = new Input(bytes);
            Object t = kryo.readClassAndObject(input);
            return t;
        }catch(Exception e){
            logger.error("异常", e);
            throw e;
        }finally{
            if(input != null){
                try {
                    input.close();
                    input = null;
                } catch (Exception e) {
                    logger.error("异常", e);
                }
            }
        }
    }
}