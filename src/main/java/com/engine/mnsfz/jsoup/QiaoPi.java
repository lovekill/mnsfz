package com.engine.mnsfz.jsoup;

import com.engine.mnsfz.util.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 13-7-9.
 */
public class QiaoPi {
    private final String url ="http://www.mnsfz.com";
    private final  String modelUrl = "/h/qiaopi/" ;





    public List<IndexBean> printMainImage(String indexUrl) throws IOException {
        LogUtil.d(getClass(),"-->"+url+modelUrl+indexUrl);
        Document doc = Jsoup.connect(url +modelUrl+ indexUrl).get();
        Elements baseElement=doc.select(".p_box .c_inner").eq(1);
        Elements imageLiElement=baseElement.select(".pic li a");
        List<IndexBean> list = new ArrayList<IndexBean>();
        for (Element element : imageLiElement) {
            IndexBean bean = new IndexBean();
            bean.setTitle(element.attr("title"));
            bean.setHref(element.attr("href"));
            Elements img = element.select("img");
            bean.setSrc(img.attr("src"));
            list.add(bean);
        }
        return list;
    }

    public List<Page> getPage() throws  IOException{
        Document doc = Jsoup.connect(url + "/h/qiaopi/index.html").get();
        List<Page> pageList = new ArrayList<Page>() ;
        Page pageIndex = new Page(1,"index.html");
        pageList.add(pageIndex) ;
        Elements elements = doc.select(".cShowPage").eq(0).select("a") ;
        for (int i=0;i<elements.size()-1;i++){
            Element e =elements.get(i);
            Page p=new Page() ;
            p.setPageIndex(Integer.parseInt(e.text()));
            p.setHref(e.attr("href"));
            LogUtil.d(getClass(),p);
            pageList.add(p);
        }
        return pageList;
    }

}
