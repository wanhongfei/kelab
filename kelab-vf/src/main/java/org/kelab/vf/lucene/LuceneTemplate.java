package org.kelab.vf.lucene;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hongfei.whf on 2016/8/15.
 */
@Data
@Slf4j
public class LuceneTemplate {

	public static final int DEFAULT_MAXX_ROWS = 2000;
	public static final int DEFAULT_MAXX_PAGE = 20;
	public static final int DEFAULT_MAXX_PER_PAGE = 20;

	// 索引路径
	private String indexDir = null;

	// 写索引配置文件
	private IndexWriterConfig cfg = null;

	// 分词器
	private Analyzer analyzer = null;

	// 索引目录
	private Directory directory = null;

	// 写索引
	private IndexWriter writer = null;

	// 追踪写索引
	private TrackingIndexWriter trackingIndexWriter = null;

	// 近实时合并线程
	private ControlledRealTimeReopenThread controlledRealTimeReopenThread = null;

	// 检索管理器
	private SearcherManager searcherManager = null;

//    /**
//     * 测试入口
//     *
//     * @param args
//     */
//    public static void main(String[] args) throws Exception {
//        LuceneTemplate template = new LuceneTemplate();
//        template.deleteAll();
//        for (int i = 0; i < 10; i++) {
//            Document doc = new Document();
//            doc.add(new IntField("id", i, IntField.TYPE_STORED));
//            doc.add(new IntField("user_id", 2, IntField.TYPE_NOT_STORED));
//            doc.add(new IntField("compiler_id", 3, IntField.TYPE_NOT_STORED));
//            doc.add(new IntField("problem_id", 4, IntField.TYPE_NOT_STORED));
//            doc.add(new IntField("status", i, IntField.TYPE_NOT_STORED));
//            doc.add(new IntField("contestId", 6, IntField.TYPE_NOT_STORED));
//            doc.add(new TextField("username", "wwhhff11", Field.Store.NO));
//            template.addDocumentNotCommit(doc);
//        }
//        template.commit();
//
//        IndexSearcher searcher = template.openIndexSearcher();
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("problem_id", 4);
//        params.put("username", "wwhhff");
//        Sort sort = LuceneSortFactory.getSortByField("id", true);
//        List<Document> docs = template.queryByPage(searcher, params, sort, 1, 5);
//        for (Document document : docs) {
//            System.out.println(document.get("id"));
//        }
//        template.releaseIndexSearcher(searcher);
//        template.destory();
//    }

	/**
	 * 初始化
	 *
	 * @return
	 */
	public void init() throws Exception {
		if (indexDir == null) {
			throw new Exception("indexDir is null.");
		}
		if (directory == null) {
			directory = new SimpleFSDirectory(Paths.get(indexDir));
		}
		if (analyzer == null) {
			analyzer = new SimpleAnalyzer();
		}
		if (cfg == null) {
			cfg = new IndexWriterConfig(analyzer);
		}
		if (writer == null) {
			writer = new IndexWriter(directory, cfg);
		}
		searcherManager = new SearcherManager(writer, false, new SearcherFactory());
		trackingIndexWriter = new TrackingIndexWriter(writer);
		controlledRealTimeReopenThread = new ControlledRealTimeReopenThread<IndexSearcher>(
				trackingIndexWriter, searcherManager, 5.0, 0.025);
		controlledRealTimeReopenThread.setDaemon(true);//设为后台进程
		controlledRealTimeReopenThread.start();
	}

	/**
	 * 销毁
	 */
	public void destory() {
		try {
			writer.commit();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 获得Searcher
	 *
	 * @return
	 */
	public IndexSearcher openIndexSearcher() throws Exception {
		searcherManager.maybeRefresh();
		return searcherManager.acquire();
	}

	/**
	 * 释放Searcher
	 *
	 * @param searcher
	 * @return
	 */
	public Boolean releaseIndexSearcher(@NonNull IndexSearcher searcher) throws Exception {
		try {
			searcherManager.release(searcher);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 向索引增加文档
	 *
	 * @param document
	 * @return
	 */
	public Boolean addDocumentWithCommit(@NonNull Document document) {
		try {
			writer.addDocument(document);
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 向索引增加文档
	 *
	 * @param document
	 * @return
	 */
	public Boolean addDocumentNotCommit(@NonNull Document document) {
		try {
			writer.addDocument(document);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 向索引增加文档
	 *
	 * @param documents
	 * @return
	 */
	public Boolean addDocumentWithCommit(@NonNull List<Document> documents) {
		try {
			writer.addDocuments(documents);
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 向索引增加文档
	 *
	 * @param documents
	 * @return
	 */
	public Boolean addDocumentNotCommit(@NonNull List<Document> documents) {
		try {
			writer.addDocuments(documents);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 提交变更
	 *
	 * @return
	 */
	public Boolean commit() {
		try {
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 清空
	 *
	 * @return
	 */
	public Boolean deleteAll() {
		try {
			writer.deleteAll();
			writer.commit();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 查询
	 *
	 * @param searcher
	 * @param params
	 * @param rows
	 * @return
	 * @throws IOException
	 */
	public List<Document> query(@NonNull IndexSearcher searcher,
	                            @NonNull Map<String, Object> params,
	                            Integer rows) throws IOException {
		if (rows == null) {
			rows = DEFAULT_MAXX_ROWS;
		}
		Query query = getMulitConditionQuery(params);
		TopDocs topDocs = searcher.search(query, rows);
		ScoreDoc[] hits = topDocs.scoreDocs;
		List<Document> list = new ArrayList<Document>();
		for (int i = 0; i < hits.length; i++) {
			ScoreDoc hit = hits[i];
			Document hitDoc = searcher.doc(hit.doc);
			list.add(hitDoc);
		}
		return list;
	}

	/**
	 * 分页查询
	 *
	 * @param searcher
	 * @param params
	 * @param sort
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public List<Document> queryByPage(@NonNull IndexSearcher searcher,
	                                  @NonNull Map<String, Object> params,
	                                  @NonNull Sort sort,
	                                  @NonNull Integer page,
	                                  @NonNull Integer rows)
			throws Exception {
		if (page <= 0) {
			page = 1;
		}
		if (page > DEFAULT_MAXX_PAGE) {
			page = DEFAULT_MAXX_PAGE;
		}
		if (rows > DEFAULT_MAXX_PER_PAGE) {
			rows = DEFAULT_MAXX_PER_PAGE;
		}
		Query query = getMulitConditionQuery(params);
		TopDocs topDocs = searcher.search(query, DEFAULT_MAXX_ROWS, sort);
		ScoreDoc[] hits = topDocs.scoreDocs;
		Integer start = (page - 1) * rows;
		if (start > topDocs.totalHits) {
			throw new Exception("totalHits is less than start");
		}
		Integer end = Math.min(start + rows, topDocs.totalHits);
		List<Document> list = new ArrayList<Document>();
		for (int i = start; i < end; i++) {
			ScoreDoc hit = hits[i];
			Document hitDoc = searcher.doc(hit.doc);
			list.add(hitDoc);
		}
		return list;
	}

	/**
	 * 获取多条件查询
	 *
	 * @param params
	 * @return
	 */
	private BooleanQuery getMulitConditionQuery(@NonNull Map<String, Object> params) {
		BooleanQuery booleanQuery = new BooleanQuery();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			Object value = entry.getValue();
			Query query = null;
			if (value.getClass() == String.class) {
				query = new TermQuery(new Term(entry.getKey(), value.toString()));
			} else if (value.getClass() == Integer.class) {
				query = NumericRangeQuery.newIntRange(entry.getKey(), (Integer) value, (Integer) value, true,
						true);
			}
			booleanQuery.add(query, BooleanClause.Occur.MUST);
		}
		return booleanQuery;
	}
}
