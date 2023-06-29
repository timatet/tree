package com.avl.builder.basic;

import java.util.stream.IntStream;

import com.avl.builder.auxiliary.Subtable;

public class PathTree {

    public Subtable subtable = new Subtable();

    private Integer getIndex(Subtable root, String pathPart) {
        Integer childSubtableIndex = IntStream.range(0, root.subtables.size())
            .filter(i -> root.subtables.get(i).TableName.equals(pathPart))
            .findFirst()
            .orElse(-1);
        return childSubtableIndex;
    }

    public void InsertPath(String path) {

        Subtable root = subtable;
    
        String[] splittedPath = path.split(";");

        for (String pathPart : splittedPath) {
            Integer idx = getIndex(root, pathPart);

            if (idx != -1) {
                root = root.subtables.get(idx);
            } else {
                Subtable newSubtable = new Subtable(pathPart);
                root.subtables.add(newSubtable);
                root = newSubtable;
            }
        }

    }
    
}
