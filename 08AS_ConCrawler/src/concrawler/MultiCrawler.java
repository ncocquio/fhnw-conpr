package concrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Crawls the web sequentially. This class is already thread safe since it is
 * stateless.
 */
public class MultiCrawler implements Crawler, Callable<Queue<String>> {
    /** Maximal number of visited urls per request. */
    private static final int MAX_VISITS = 20;
    private final String url;
    private final int level;

    public MultiCrawler(String url, int level) {
        this.url = url;
        this.level = level;
    }

    public MultiCrawler() {
        this.url = "";
        this.level = 0;
    }

    /**
     * Crawls the web, starting at startURL and returning a list of visited
     * URLs.
     */
    @Override
    public List<String> crawl(String startURL) {
        /* Contains the already visited urls. */
		final Set<String> urlsVisited = new HashSet<String>();
		/* Contains the urls to visit. */
		final Queue<String> urlsToVisit = new LinkedList<String>();

		final Collection<Callable<Queue<String>>> taskList = new LinkedList<>();

		urlsToVisit.add(startURL);

        ForkJoinPool fj = new ForkJoinPool();

        while ((!urlsToVisit.isEmpty()) && taskList.size() < MAX_VISITS) {
            final String toVisit = urlsToVisit.poll(); // crawl next url

            taskList.add(new MultiCrawler(toVisit,1));
        }

        List<Future<Queue<String>>> results = fj.invokeAll(taskList);

        for (Future<Queue<String>> result : results) {
            try {
                System.out.println("URLs added");
                urlsVisited.addAll(result.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

		return new ArrayList<String>(urlsVisited.stream().limit(20).collect(Collectors.toList()));
    }

    @Override
    public Queue<String> call() throws Exception {
        final Queue<String> urlsToVisit = new LinkedList<String>();

        try {
            final Document doc = Jsoup.parse(Jsoup.connect(this.url)
                    .userAgent("ConCrawler/0.1 Mozilla/5.0")
                    .timeout(3000)
                    .get().html());

            final Elements links = doc.select("a[href]");
            for (final Element link : links) {
                final String linkString = link.absUrl("href");
                if (linkString.startsWith("http") && this.level < 3) {
                    urlsToVisit.add(linkString);
                }
            }
        } catch (final Exception e) {
            System.out.println("Problem reading '" + this.url + "'. Message: " + e.getMessage());
        }

        return urlsToVisit;
    }
}
