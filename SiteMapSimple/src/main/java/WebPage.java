import java.util.Objects;

public class WebPage {
    private String url;

    public WebPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebPage page = (WebPage) o;
        return Objects.equals(url, page.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
