import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SiteMapTexter extends RecursiveTask<StringBuilder> {
    private String startingUrl;
    private int depthLevel;
    private WebSite site;

    public SiteMapTexter(String startingUrl, int depthLevel, WebSite site) {
        this.startingUrl = startingUrl;
        this.depthLevel = depthLevel;
        this.site = site;
    }

    @Override
    protected StringBuilder compute() {
        Document document = null;
        List<SiteMapTexter> mapTexters = new ArrayList<>();
        List<WebPage> webPages = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.depthLevel; i++) {
            result.append("\t");
        }
        result.append(this.startingUrl + "\n");

        try {
            Thread.sleep(150);
            document = Jsoup.connect(this.startingUrl).maxBodySize(0).get();
            Elements foundLinks = document.select("a[href~=((^https:\\/\\/[^\\/]*skillbox.*\\/[^\\?]+)|(^\\/.*\\/))]")
                    .select("a[href~=(^((?!utm).)*$)]")
                    .select("a[href~=(^((?!\\?).)*$)]")
                    .select("a[href~=(^((?!\\#).)*$)]");
            for (Element foundLink : foundLinks) {
                WebPage webPage = new WebPage(foundLink.absUrl("href"));
                if (this.site.addPage(webPage)) {
                    webPages.add(webPage);
                }
            }
        } catch (Exception ex) {
            System.out.println("Дочерние ссылки на странице " + this.startingUrl + " не найдены. Продолжаем.");
        }

        if(webPages.size() > 0) {
            for(WebPage webPage : webPages) {
                SiteMapTexter mapTexter = new SiteMapTexter(webPage.getUrl(),this.depthLevel + 1, this.site);
                mapTexter.fork();
                mapTexters.add(mapTexter);
            }
        }
        for (SiteMapTexter mapTexter : mapTexters) {
            result.append(mapTexter.join());
        }
        return result;
    }
}
