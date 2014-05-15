package com.engine.mnsfz.jsoup;

import com.engine.mnsfz.greendao.DaoMaster;
import com.engine.mnsfz.greendao.PersonImage;
import com.engine.mnsfz.util.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USER on 13-7-13.
 */
public class NetFech {
    protected String baseUrl = "http://www.mnsfz.com";
    private Map<ImageType, String> modelMap = new HashMap<ImageType, String>();
    private ImageType mType;

    public ImageType getmType() {
        return mType;
    }

    public void setmType(ImageType mType) {
        this.mType = mType;
    }

    public NetFech(ImageType type) {
        this.mType = type;
        registMap();
    }

    private void registMap() {
        modelMap.put(ImageType.QIAOPI, "/h/qiaopi/");
        modelMap.put(ImageType.QINCUN, "/h/qingchun/");
        modelMap.put(ImageType.SUNMM, "/h/yangguang/");
    }

    public List<Page> getPage() throws IOException {

        LogUtil.d(getClass(),baseUrl+modelMap.get(mType)+"index.html");
        Document doc = Jsoup.connect(baseUrl + modelMap.get(mType) + "index.html").get();

        List<Page> pageList = new ArrayList<Page>();
        Page pageIndex = new Page(1, "index.html");
        pageList.add(pageIndex);
        Elements elements = doc.select(".cShowPage").eq(0).select("a");
        for (int i = 0; i < elements.size() - 1; i++) {
            Element e = elements.get(i);
            Page p = new Page();
            p.setPageIndex(Integer.parseInt(e.text()));
            p.setHref(e.attr("href"));
            LogUtil.d(getClass(), p);
            pageList.add(p);
        }
        return pageList;
    }

    public List<IndexBean> printMainImage(String indexUrl) throws IOException {
        String modelUrl = modelMap.get(mType);
        LogUtil.d(getClass(), "-->" + baseUrl + modelUrl + indexUrl);
        Document doc = Jsoup.connect(baseUrl + modelUrl + indexUrl).get();
        Elements baseElement = doc.select(".p_box .c_inner").eq(1);
        Elements imageLiElement = baseElement.select(".pic li a");
        List<IndexBean> list = new ArrayList<IndexBean>();
        for (Element element : imageLiElement) {
            IndexBean bean = new IndexBean();
            bean.setTitle(element.attr("title"));
            bean.setHref(element.attr("href"));
            Elements img = element.select("img");
            bean.setSrc(img.attr("src"));
            list.add(bean);
        }
        List<PersonImage> l = new ArrayList<PersonImage>() ;
        for (int i = 0; i < list.size(); i++) {
            IndexBean bean = list.get(i) ;
            PersonImage image = new PersonImage() ;
            image.setHref(bean.getHref());
            image.setTitle(bean.getTitle());
            image.setSrc(bean.getSrc());
            l.add(image) ;
        }

        return list;
    }
}
