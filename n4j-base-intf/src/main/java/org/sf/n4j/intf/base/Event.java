package org.sf.n4j.intf.base;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public abstract class Event {

    private String id;
    private Instant time;
    private Object source;
    private Map<String,Object> data;

    public Event(@NonNull Object source,
                 @NonNull Map<String,Object> data){
        this.id = UUID.randomUUID().toString();
        this.time = Instant.now();
        this.source = source;
        this.data = ImmutableMap.copyOf(data);
    }

    public Event(@NonNull Object source){
        this(source,ImmutableMap.of());
    }

    public String getId() {
        return id;
    }

    public Instant getTime() {
        return time;
    }

    public Object getSource() {
        return source;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return getId().equals(event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
