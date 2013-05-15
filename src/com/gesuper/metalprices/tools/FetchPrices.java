package com.gesuper.metalprices.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gesuper.metalprices.R;

import android.util.Log;
import android.widget.RemoteViews;

public class FetchPrices {
	public static final String TAG = "FetchPrices";
	private static String FetchUrl = "http://api.gesuper.com/icbc/precious_metals.php";

    private static int[][] mIds = {
    	{
    		R.id.widget_gold_price,
    		R.id.widget_gold_highest,
    		R.id.widget_gold_lowest,
    	},
    	{
    		R.id.widget_sliver_price,
    		R.id.widget_sliver_highest,
    		R.id.widget_sliver_lowest
    	}
    };
    
	private MetalPrice[] mMetalPrices;
	
	public FetchPrices(){

		mMetalPrices = new MetalPrice[4];
		this.mMetalPrices[0] = new MetalPrice("Gold");
		this.mMetalPrices[1] = new MetalPrice("Sliver");
		this.mMetalPrices[2] = new MetalPrice("Platinum");
		this.mMetalPrices[3] = new MetalPrice("Palladium");
	}
	
	public MetalPrice[] getMetalPrices(){
		return this.mMetalPrices;
	}
	
	public void updatePrices(final RemoteViews mRemoteView){
		Log.d(TAG, "updatePrices");
		try {
			URL url = new URL(FetchUrl);
		    //使用openConnection打开URL对象  
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    //使用Http协议，设置请求方式为GET  
		    conn.setRequestMethod("GET");
		    //设置链接超时异常，5s
		    conn.setConnectTimeout(5 * 1000);
		    //通过输入流获取图片数据  
		    InputStream inStream = conn.getInputStream();
		    
		    parse(inStream);
		    
		    for(int i=0; i < 2; i++){
		   		mRemoteView.setTextViewText(mIds[i][0], mMetalPrices[i].getPrice());
		   		mRemoteView.setTextViewText(mIds[i][1], String.valueOf(mMetalPrices[i].getPriceOfHighest()));
		   		mRemoteView.setTextViewText(mIds[i][2], String.valueOf(mMetalPrices[i].getPriceOfLowest()));
		   	}
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    private void parse(InputStream is) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例  
        DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例  
        Document doc = builder.parse(is);   //解析输入流 得到Document实例
        
        Element rootElement = doc.getDocumentElement();  
        NodeList items = rootElement.getElementsByTagName("metal");
        
        for (int i = 0; i < items.getLength(); i++) {
        	MetalPrice metal = mMetalPrices[i];
        	
            Node item = items.item(i);  
            NodeList properties = item.getChildNodes();  
            for (int j = 0; j < properties.getLength(); j++) {  
                Node property = properties.item(j);  
                String nodeName = property.getNodeName();
                
                if (nodeName.equals("name")) {  
                	metal.setName(property.getFirstChild().getNodeValue());  
                } else if (nodeName.equals("bank_buy")) {  
                	metal.setPriceOfBankBuy(Float.parseFloat(property.getFirstChild().getNodeValue()));  
                }  else if (nodeName.equals("bank_sale")) {  
                	metal.setPriceOfBankSale(Float.parseFloat(property.getFirstChild().getNodeValue()));  
                } else if (nodeName.equals("highest")) {  
                	metal.setPriceOfHighest(Float.parseFloat(property.getFirstChild().getNodeValue()));  
                } else if (nodeName.equals("lowest")) {  
                	metal.setPriceOfLowest(Float.parseFloat(property.getFirstChild().getNodeValue()));  
                } 
            }
        }
    } 
}
