package com.example.My_Database.Domain.Entity.types;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.Interval;

import java.io.Serializable;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
public abstract class Attribute {
    public String name;

    public Value value;

    public String getName() {
        return this.name;
    }

    public abstract Types getType();

    public abstract Boolean validate(String val);

    public abstract Value getValue(String val);

    public abstract Value getDefault();

    public static Attribute getAttribute(String name, Types type, String value) {
        switch (type) {
            case INTEGER:
                return new IntegerAttr(name, value);
            case REAL:
                return new RealAttr(name, value);
            case CHAR:
                return new CharAttr(name, value);
            case STRING:
                return new StringAttr(name, value);
            case INT_INTERVAL:
                return new IntIntervalAttr(name, value);
            case TEXT_FILE:
                return new TextFileAttr(name, value);
            default:
                return null;
        }
    }

    public static Attribute getAttribute(String name, Types type) {
        switch (type) {
            case INTEGER:
                return new IntegerAttr(name, "0");
            case REAL:
                return new RealAttr(name, "0.0");
            case CHAR:
                return new CharAttr(name, "0");
            case STRING:
                return new StringAttr(name, "");
            case INT_INTERVAL:
                return new IntIntervalAttr(name, new Interval(0,1).toString());
            case TEXT_FILE:
                return new TextFileAttr(name, "");
            default:
                return null;
        }
    }


}
