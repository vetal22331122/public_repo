import java.util.HashSet;
import java.util.Set;

public class WebSite {

    private Set<WebPage> pages = new HashSet<>();

    public WebSite() {
    }

    public boolean addPage(WebPage page) {
        if(page.getUrl().contains("live.skillbox.ru") || page.getUrl().contains("course.skillbox.ru")) {
            return false;
        }
        return this.pages.add(page);
    }
}
