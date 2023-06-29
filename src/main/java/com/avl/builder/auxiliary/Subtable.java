package com.avl.builder.auxiliary;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Setter;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonInclude(Include.NON_EMPTY)
public class Subtable {
    
    @Setter
    public String TableName;

    public List<Subtable> subtables;

    public Subtable(String tableName) {
        TableName = tableName;
        subtables = new ArrayList<>();
    }

    public Subtable() {
        TableName = "Root domain";
        subtables = new ArrayList<>();
    }

    public void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(TableName);
        buffer.append('\n');
        for (Subtable subtable : subtables) {
            
            if (subtable.subtables.size() > 0) {
                subtable.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                subtable.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }

}
