package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String url = "http://en.wikipedia.org/wiki/Java_(programming_language)";
        WikiFetcher fetcher = new WikiFetcher();
        Elements paragraphs = fetcher.fetchWikipedia(url);
        for(Element paragraph : paragraphs) {
            recursiveDFS(paragraph);
        }
    }

    private static void recursiveDFS(Node node) {
        if(node instanceof TextNode) {
            System.out.println(node);
        }
        for(Node child : node.childNodes()) {
            iterativeDFS(child);
        }
    }

    private static void iterativeDFS(Node root) {
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(root);

        while(!stack.isEmpty()) {
            Node node = stack.pop();
            if(node instanceof TextNode) {
                System.out.println(node);
            }
            List<Node> children = new ArrayList<>(node.childNodes());
            Collections.reverse(children);
            for(Node child : children) {
                stack.push(child);
            }
        }
    }

}