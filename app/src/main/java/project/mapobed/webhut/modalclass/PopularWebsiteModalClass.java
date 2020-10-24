package project.mapobed.webhut.modalclass;

public class PopularWebsiteModalClass {
    private String web_name,web_link;
    private int web_img;

    public PopularWebsiteModalClass(String web_name,String web_link, int web_img) {
        this.web_name = web_name;
        this.web_link=web_link;
        this.web_img = web_img;
    }

    public String getWeb_link() {
        return web_link;
    }

    public void setWeb_link(String web_link) {
        this.web_link = web_link;
    }

    public String getWeb_name() {
        return web_name;
    }

    public void setWeb_name(String web_name) {
        this.web_name = web_name;
    }

    public int getWeb_img() {
        return web_img;
    }

    public void setWeb_img(int web_img) {
        this.web_img = web_img;
    }
}
