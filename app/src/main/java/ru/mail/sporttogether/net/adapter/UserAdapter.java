package ru.mail.sporttogether.net.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import ru.mail.sporttogether.net.models.User;

/**
 * Created by said on 01.10.16.
 */

public class UserAdapter extends TypeAdapter<User> {

    @Override
    public void write(JsonWriter out, User value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("token").value(value.getToken());
        out.name("id").value(value.getId());
        out.endObject();
    }

    @Override
    public User read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String id = null;
        String token = null;
        in.beginObject();
        while (in.hasNext()){
            String name = in.nextName();
            switch (name){
                case "id":
                    id = in.nextString();
                    break;
                case "token":
                    token = in.nextString();
                    break;
            }
        }
        in.endObject();

        return new User(token, id);
    }
}
