package org.underpressureapps.unconflicto;

import java.util.HashMap;

public class User {
    public String uni_code;
    public String name;

    public HashMap<String, Boolean> topics;

    public User() {

    }

    public boolean canAccessTopic(String topic) { return topics != null && topics.containsKey(topic) && topics.get(topic); }
}
