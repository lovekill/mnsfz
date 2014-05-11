package com.engine.mnsfz.jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IndexImage {
	private final String baseUrl = "http://www.mnsfz.com";
	Pattern p = Pattern.compile(".+=\"(http.+)\".+");
	// ��ȡ�崿ͼƬurl
	Pattern purPattern = Pattern.compile(".+(http.+).+");

	public void getHotPicList() throws IOException {
		Document doc = Jsoup.connect(baseUrl).get();
		Elements newsHeadlines = doc.select(".hotpiclist");
		for (int i = 0; i < newsHeadlines.size(); i++) {
			Element e = newsHeadlines.get(i);

		}
	}

	// 推荐
	public IndexItem getRecommendPic() throws IOException {
		Document doc = Jsoup.connect(baseUrl).get();
		Elements newsHeadlines = doc.select(".reclist");
		IndexItem item = new IndexItem();
		item.setTitle(newsHeadlines.select("strong").text());
		Elements pic = newsHeadlines.select(".pic li a");
		List<IndexBean> list = new ArrayList<IndexBean>();
		for (Element e : pic) {
			IndexBean bean = new IndexBean();
			bean.setHref(e.attr("href"));
			bean.setTitle(e.attr("title"));
			bean.setSrc(e.attr("src"));
			list.add(bean);
		}
		item.setList(list);
		return item;
	}

	// 俏皮
	public IndexItem getQiaopiPic() throws IOException {
		Document doc = Jsoup.connect(baseUrl).get();
		Elements newsHeadlines = doc.select(".c_inner .pic");
		IndexItem item = new IndexItem();
		item.setTitle("��Ƥ˽��");
		Elements pic = newsHeadlines.select("li a");
		List<IndexBean> list = new ArrayList<IndexBean>();
		for (Element e : pic) {
			IndexBean bean = new IndexBean();
			bean.setHref(e.attr("href"));
			bean.setTitle(e.attr("title"));
			bean.setSrc(e.select("img").attr("src"));
			list.add(bean);
		}
		item.setList(list);
		return item;
	}

	//
	public IndexItem getPurertyPic() throws IOException {
		Document doc = Jsoup.connect(baseUrl).get();
		Elements newsHeadlines = doc.select(".imgbox");
		IndexItem item = new IndexItem();
		item.setTitle("�崿˽��");
		Elements pic = newsHeadlines.select(".pic li a");
		List<IndexBean> list = new ArrayList<IndexBean>();
		for (Element e : pic) {
			IndexBean bean = new IndexBean();
			bean.setHref(e.attr("href"));
			Matcher m = purPattern.matcher(e.select("img").attr("style"));
			if (m.matches()) {
				bean.setSrc(m.group(1));
			}
			bean.setTitle(e.attr("title"));
			list.add(bean);
		}
		item.setList(list);
		return item;
	}

	public List<IndexBean> printMainImage() throws IOException {
		Document doc = Jsoup.connect(baseUrl).get();
		Elements newsHeadlines = doc.select(".pic li a");
		List<IndexBean> list = new ArrayList<IndexBean>();
		for (Element element : newsHeadlines) {
			// System.out.println(element.html());
			IndexBean bean = new IndexBean();
			bean.setTitle(element.attr("title"));
			bean.setHref(element.attr("href"));
			Elements img = element.select("img");
			bean.setSrc(img.attr("src"));
			list.add(bean);
		}
		return list;
	}

	public void getDetailUrls(String targetUrl) throws IOException {
		Document doc = Jsoup.connect(baseUrl + targetUrl).get();
		Elements newsHeadlines = doc.select("script");
		for (Element element : newsHeadlines) {
			Matcher m = p.matcher(element.html());
			if (m.matches()) {
				System.out.println(m.group(1));
			} else {
				// Util.print(element.html()) ;
			}
		}
	}

	public void getPageList(String targetUrl) throws IOException {
		Document doc = Jsoup.connect(baseUrl + targetUrl).get();
		Elements newsHeadlines = doc.select(".pageList");
		for (Element element : newsHeadlines) {
		}
	}

}
