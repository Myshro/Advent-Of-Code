package Year2023.Day7;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public record Node(int size, Map<String, Node> children, Node parent) {
    private static Pattern filePattern = Pattern.compile("(\\d+) ([\\w.]+)");

    public static Node parseTerminal(String input) {
        Node root = new Node(0, new TreeMap<>(), null);
        input.lines().reduce(root, (node, line) -> node.apply(line), (a, b) -> {return null;});
        return root;
    }

    Node cd(String name) {
        return switch (name) {
            case ".." -> parent;
            case "/" -> parent == null ? this : parent.cd(name);
            default -> children.computeIfAbsent(name, s -> new Node(0, new TreeMap<>(), this));
        };
    }

    Node apply(String line) {
        if (line.startsWith("$ cd ")) {
            return cd(line.substring(5));
        } 
        Matcher fileMatcher = filePattern.matcher(line);    
        if (fileMatcher.matches()) {
            return add(fileMatcher.group(2), fileMatcher.group(1));
        } 
        return this;
    }

    Node add(String name, String size) {
        children.put(name, new Node(Integer.parseInt(size), new TreeMap<>(), this));
        return this;
    }

    Stream<Node> files() {
        return Stream.concat(Stream.of(this), children.values().stream().flatMap(n -> n.files()).filter(n -> n.size == 0));
    }

    public int size() {
        return size > 0 ? size : children.values().stream().mapToInt(n -> n.size()).sum();
    }
}
