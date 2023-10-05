package com.example.My_Database.Domain.Entity.types;

import lombok.NoArgsConstructor;
import java.io.File;

@NoArgsConstructor
public class TextFileAttr extends Attribute {
    private File file;

    public TextFileAttr(String name, String value) {
        this.name = name;
        this.file = new File(value);
        this.value = new Value<>(value, Types.TEXT_FILE);
    }
    public TextFileAttr(String name) {
        this.name = name;
    }

    @Override
    public Types getType() {
        return Types.TEXT_FILE;
    }

    @Override
    public Boolean validate(String val) {
        if (val == null || val.isEmpty()) {
            return false; // Пустая строка не допускается
        }

        File fileToValidate = new File(val);
        return fileToValidate.exists() && fileToValidate.isFile() && fileToValidate.canRead();
    }

    @Override
    public Value getValue(String val) {
        return new Value<>(val, Types.TEXT_FILE);
    }

    @Override
    public Value getDefault() {
        return new Value<>("", Types.TEXT_FILE);
    }
}