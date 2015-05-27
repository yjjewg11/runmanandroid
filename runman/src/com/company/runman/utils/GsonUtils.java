package com.company.runman.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/5/22.
 */
public class GsonUtils {
     Gson GSON=null;
    public class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
        private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        public JsonElement serialize(Timestamp ts, Type t, JsonSerializationContext jsc) {
            String dfString = format.format(new Date(ts.getTime()));
            return new JsonPrimitive(dfString);
        }
        public Timestamp deserialize(JsonElement json, Type t, JsonDeserializationContext jsc) throws JsonParseException {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }

            try {
                Date date = format.parse(json.getAsString());
                return new Timestamp(date.getTime());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public GsonUtils() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gsonBuilder.registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter());
         GSON = gsonBuilder.create();
    }

    public Gson getGson(){
        return  GSON;
    };
    public String toJson(Object o){
       return  GSON.toJson(o);
   }
    public Object stringToObject(String str,Class clazz){
        return  GSON.fromJson(str,clazz);
    }
}
