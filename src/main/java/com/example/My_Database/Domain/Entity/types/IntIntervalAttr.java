package com.example.My_Database.Domain.Entity.types;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IntIntervalAttr extends Attribute {
    private int from;
    private int to;

    public IntIntervalAttr(String name, String value) {
        this.name = name;
        parseInterval(value);
        this.value = new Value<>(toString());
    }

    public IntIntervalAttr(String name) {
        this.name = name;
        this.from = 0;
        this.to = 0;
    }

    @Override
    public Types getType() {
        return Types.INT_INTERVAL;
    }

    @Override
    public Boolean validate(String val) {
        String[] buf = val.replace(" ", "").split(",");
        if (buf.length != 2) {
            return false;
        }
        try {
            int a = Integer.parseInt(buf[0]);
            int b = Integer.parseInt(buf[1]);
            return a < b;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Value getValue(String val) {
        parseInterval(val);
        return new Value<>(toString());
    }

    @Override
    public Value getDefault() {
        return new Value<>(toString()); // Возвращайте интервал по умолчанию как строку
    }

    private void parseInterval(String value) {
        String[] buf = value.replace(" ", "").split(",");
        if (buf.length == 2) {
            try {
                int a = Integer.parseInt(buf[0]);
                int b = Integer.parseInt(buf[1]);
                if (a < b) {
                    this.from = a;
                    this.to = b;
                }
            } catch (NumberFormatException e) {
                this.from = 0;
                this.to = 0;
            }
        } else {
            this.from = 0;
            this.to = 0;
        }
    }

    @Override
    public String toString() {
        return from + " - " + to;
    }
}
