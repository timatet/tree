package com.avl.builder.helpers;

import java.util.ArrayList;
import java.util.List;

import com.avl.builder.auxiliary.Node;
import com.avl.builder.basic.AVL;

/* https://stackoverflow.com/a/29704252/19892793 MightyPork */
public class NodePrinter {

    public static <TKey extends Comparable<TKey>, TValue> void print(AVL<TKey, String> avl)
    {
        print(avl.Root);
    }

    public static <TKey extends Comparable<TKey>, TValue> void print(Node<TKey, String> Root)
    {
        List<List<String>> lines = new ArrayList<List<String>>();

        //Node<TKey, String> Root = avl.Root;

        List<Node<TKey, String>> level = new ArrayList<Node<TKey, String>>();
        List<Node<TKey, String>> next = new ArrayList<Node<TKey, String>>();

        level.add(Root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (Node<TKey, String> n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = String.format("(%s, %s)", n.Key, n.Value);
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.Left);
                    next.add(n.Right);

                    if (n.Left != null) nn++;
                    if (n.Right != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            List<Node<TKey, String>> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            for (int j = 0; j < line.size(); j++) {

                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }
    }
    
}
