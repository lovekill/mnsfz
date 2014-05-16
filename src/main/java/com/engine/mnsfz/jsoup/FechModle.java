package com.engine.mnsfz.jsoup;

import com.engine.mnsfz.DaoManager;
import com.engine.mnsfz.greendao.ModelBean;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by USER on 13-7-12.
 */
public class FechModle {
    Pattern p = Pattern.compile("arrayImg\\[\\d\\]=\".+") ;
    protected String baseUrl = "http://www.mnsfz.com";
    private Map<ImageType, String> modelMap = new HashMap<ImageType, String>();
    public FechModle() {
        modelMap.put(ImageType.QIAOPI, "/h/qiaopi/");
        modelMap.put(ImageType.QINCUN, "/h/qingchun/");
        modelMap.put(ImageType.SUNMM, "/h/yangguang/");
    }
    private void getModelImage(List<IndexBean> list,String urls) throws IOException {
        LogUtil.d(getClass(),baseUrl+urls);
        Document doc = Jsoup.connect(baseUrl + urls).get() ;

        Matcher m= p.matcher(doc.html()) ;
        if(m.find()){
            String content =m.group().replaceAll("arrayImg\\[\\d\\]=","").replaceAll("\"","").replaceAll("big","pic") ;

            LogUtil.d(getClass(),"-->"+content);
           String[] imges =content.split(";") ;
            for (int i=0;i<imges.length-1;i++){
                IndexBean bean=new IndexBean();
                bean.setSrc(imges[i]);
                list.add(bean) ;
            }
        }else {
            LogUtil.d(getClass(),"not matcher");
        }

    }



   private   List<String> getPageUrl(String urls) throws  IOException{
       //从URL中提取前面 /h/qiaopi/
       String pre = getPre(urls) ;
       LogUtil.d(getClass(),"getPageUrl-->"+baseUrl+urls );
        Document doc = Jsoup.connect(baseUrl+urls).get();
        Elements e = doc.select(".pageList a") ;
        List<String> urlList= new ArrayList<String>() ;
        Element element= e.get(e.size()-2) ;
        urlList.add(pre+element.attr("href")) ;
//        urlList.add(e.get(e.size()-2).attr("href")) ;
//        for(int i=1;i<e.size();i++){
//            urlList.add(qiaopiUrl+e.get(i).attr("href")) ;
//        }
        return  urlList;
    }

    private  String getPre(String url){
        for(ImageType t:modelMap.keySet()){
            if(url.contains(modelMap.get(t))){
                return modelMap.get(t) ;
            }
        }
       return  "" ;
    }

    public List<IndexBean> getModeList(String urls ,String title) throws IOException{
        List<String> sList = getPageUrl(urls) ;
        List<IndexBean> beanList = new ArrayList<IndexBean>() ;
        for(String url:sList){
            LogUtil.d(getClass(),"getModeList-->"+url);
            getModelImage(beanList, url);
        }
        List<ModelBean> modelBeans = new ArrayList<ModelBean>() ;
        for (int i = 0; i < beanList.size(); i++) {
            ModelBean modelBean = new ModelBean() ;
           modelBean.setSrc(beanList.get(i).getSrc());
            modelBean.setTime(System.currentTimeMillis());
            modelBean.setTitle(title);
            modelBeans.add(modelBean) ;
        }
        DaoManager.getDaoSession().getModelBeanDao().insertOrReplaceInTx(modelBeans);
        return  beanList ;
    }
}
