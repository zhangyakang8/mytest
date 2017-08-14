package com.itheima.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Response;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJ {
/*
 * 添加
 */
	@Test
	public void testAdd() throws Exception, IOException{
		//底下两段代码已经连接上solr服务器了
		String baseURL="http://localhost:8080/solr";
		SolrServer server=new HttpSolrServer(baseURL);
		SolrInputDocument doc=new SolrInputDocument();
		doc.setField("id", 20);
		doc.setField("name", "周杰伦");
		server.add(doc);
		server.commit();
	}
	/*
	 * 删除
	 */
	@Test
	public void testDelete() throws Exception, IOException{
		//底下两段代码已经连接上solr服务器了
		String baseURL="http://localhost:8080/solr";
		SolrServer server=new HttpSolrServer(baseURL);
		SolrInputDocument doc=new SolrInputDocument();
		//server.deleteById("10");
		//通过条件删除
		server.deleteByQuery("name:林");
		server.commit();
	}
	//简单查询
	@Test
	public void testQuery() throws Exception{
		//底下两段代码已经连接上solr服务器了
		String baseURL="http://localhost:8080/solr";
		SolrServer server=new HttpSolrServer(baseURL);
		//条件对象
		SolrQuery params=new SolrQuery();
		//条件 关键词  查询所有
		params.set("q", "*:*");
		//执行查询
		QueryResponse response=server.query(params);
		//结果集
	    SolrDocumentList docs = response.getResults();
		//总条数
	    long found = docs.getNumFound();
	    System.out.println("总条数是:"+found);
	    for (SolrDocument doc : docs) {
			System.out.println("id"+doc.get("id"));
			System.out.println("name"+doc.get("name"));
		}
	}
	@Test
	public void testSoleSearcher() throws Exception{
		//底下两段代码已经连接上solr服务器了
		String baseURL="http://localhost:8080/solr";
		SolrServer server=new HttpSolrServer(baseURL);
		//子类查询
		SolrQuery params=new SolrQuery();
		//关键字查询
		params.setQuery("钻石");
		//过滤条件
		params.set("fq", "product_price:[7 TO 10}");
		//排序
		params.setSort("product_price",ORDER.desc);
		//分页
		params.setStart(0);
		params.setRows(10);
		//查询指定域
		params.set("fl", "id,product_name");
		//默认查询的域
		params.set("df", "product_name");
		//打开高亮开关
		params.setHighlight(true);
		//设置要高亮的域
		params.addHighlightField("product_name");
		//设置要高亮的前缀
		params.setHighlightSimplePre("<span style='color:red'>");
		//设置要高亮的后缀
		params.setHighlightSimplePost("</span>");
		//执行查询
		QueryResponse queryResponse=server.query(params);
		//取高亮
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		//结果集
		SolrDocumentList docs=queryResponse.getResults();
		//总条数
		long numFound = docs.getNumFound();
		System.out.println("总条数为:"+numFound);
		for (SolrDocument doc : docs) {
			System.out.println("Id:"+doc.get("id"));
			System.out.println("名称:"+doc.get("product_name"));
			
			Map<String, List<String>> map = highlighting.get(doc.get("id"));
			List<String> list = map.get("product_name");
			System.out.println("高亮的名称:"+list.get(0));
		}
	}
}
