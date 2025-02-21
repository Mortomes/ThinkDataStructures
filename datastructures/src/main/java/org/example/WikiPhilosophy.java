package org.example;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class WikiPhilosophy {

    final static List<String> visited = new ArrayList<String>();

    final static Deque<String> toVisit = new ArrayDeque<>();
    final static WikiFetcher wf = new WikiFetcher();

    /**
     * Tests a conjecture about Wikipedia and Philosophy.
     *
     * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
     *
     * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String destination = "https://en.wikipedia.org/wiki/Philosophy";
        String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        testConjecture(destination, source, 20);
    }

    /**
     * Starts from given URL and follows first link until it finds the destination or exceeds the limit.
     *
     * @param destination
     * @param source
     * @throws IOException
     */
    public static void testConjecture(String destination, String source, int limit) throws IOException {
        String nextPage = source;
        int counter = 0;
        while (!visited.contains(nextPage) && counter < limit) {
            System.out.println("Next page = " + nextPage);
            if(nextPage != null && nextPage.equals(destination)) {
                System.out.println("Hurray! Found philosophy in " + counter + " steps");
                break;
            }
            visited.add(nextPage);
            nextPage = findFirstValidLink(nextPage);
            counter++;
        }
        System.out.println("Uh oh! Search stopped after " + counter + " steps");
    }

    private static String findFirstValidLink(String page) throws IOException{
        Elements elements = wf.fetchWikipedia(page);
        for(Element element : elements) {
            WikiNodeIterable iter = new WikiNodeIterable(element);
            for(Node node : iter) {
                if (node instanceof Element elementNode) {
                    Elements links = elementNode.select("a[href]");
                    for(Element link : links) {
                        String relativeLink = link.attr("href");
                        if(relativeLink.startsWith("/wiki/")) {
                            String absoluteLink = "https://en.wikipedia.org" + relativeLink;
                            return absoluteLink;
                        }
                    }
                }
            }
        }
        return null;
    }
}
