package com.engine.mnsfz.jsoup;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupMain {

	

	public static void main(String[] args) {
		IndexImage index = new IndexImage() ;
		try {
			index.getQiaopiPic();
//			List<IndexBean> list= index.getModelIndexImage() ;
//			index.getPageList(list.get(5).getHref()) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
