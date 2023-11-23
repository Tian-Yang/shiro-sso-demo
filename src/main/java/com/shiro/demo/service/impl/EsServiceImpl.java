package com.shiro.demo.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.shiro.demo.annotations.DocumentId;
import com.shiro.demo.bean.EsSearchReq;
import com.shiro.demo.entity.TagUser;
import com.shiro.demo.entity.UserTag;
import com.shiro.demo.service.EsBulkCallback;
import com.shiro.demo.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Service
public class EsServiceImpl implements EsService {

    @Resource
    private ElasticsearchClient esClient;

    @Resource
    private Environment env;

    @Override
    public void test() {
        //getDocumentById("1");
        //searchDocuments("1002");
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<TagUser> tagUserList = new ArrayList<>();
        TagUser tagUser1 = new TagUser();
        tagUser1.setUserId("9000001");
        tagUser1.setTagId("5");
        tagUser1.setCreateTime(new Date());

        TagUser tagUser2 = new TagUser();
        tagUser2.setUserId("8000002");
        tagUser2.setTagId("5");
        tagUser2.setCreateTime(new Date());

        TagUser tagUser3 = new TagUser();
        tagUser3.setUserId("8000001");
        tagUser3.setTagId("6");
        tagUser3.setCreateTime(new Date());
        tagUserList.add(tagUser1);
        tagUserList.add(tagUser2);
        tagUserList.add(tagUser3);

        EsBulkCallback esBulkCallback = failList -> {
            bulkAddDocument("", failList);
        };
        //autoBulkAddDocument(buildIndexNameByProfiles("index_tag_calculate"), tagUserList, esBulkCallback);
        try {
            search();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String buildIndexNameByProfiles(String indexName) {
        Assert.notNull(indexName, "index name is null");
        String[] profiles = env.getActiveProfiles();
        Assert.notEmpty(profiles, "profiles is null");
        String profile = profiles[0].toString();
        return indexName + "_" + profile;

    }

    /**
     * 创建索引
     */
    public void createIndex(String indexName, IndexSettings settings) {
        try {
            CreateIndexResponse response = esClient.indices().create
                    (createIndexBuilder -> createIndexBuilder.index(indexName).settings(
                            settings
                    ));
            log.info("response:{}", response);
        } catch (IOException e) {
            log.error("Elasticsearch createIndex error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加文档
     */
    public void addDocument() {
        UserTag userTag = UserTag.builder()
                .tagId("1")
                .userIds("1001,1002,1003")
                .build();

        IndexResponse indexResponse;
        try {
            indexResponse = esClient.index(i -> i.index("test-index")
                    .id(getDocumentId(userTag))
                    .document(userTag));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("response:{}", indexResponse);

        List<UserTag> userTagList = new ArrayList<>();
        EsBulkCallback esBulkCallback = failList -> {
            bulkAddDocument("", failList);
        };

        autoBulkAddDocument("", userTagList, esBulkCallback);


        Predicate<String> isNUll = (a) -> StringUtils.isEmpty(a);
        String s = "1";
        isNUll.test(s);
    }


    /**
     * 查询索引
     *
     * @param id
     */
    public void getDocumentById(String id) {
        try {
            GetResponse<UserTag> response = esClient.get(g -> g
                            .index("test-index")
                            .id(id),
                    UserTag.class
            );
            if (response.found()) {
                UserTag userTag = response.source();
                log.info("userTag:{}", userTag);
            } else {
                log.info("document not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void search() throws IOException {


        Query byName = MatchQuery.of(m -> m
                .field("user_id")
                .query("8000001"))._toQuery();
        List<String> tagIds = new ArrayList<>();
        tagIds.add("10000001_2");
        tagIds.add("10000003_2");

        List<Query> mustQueryList = new ArrayList<>();

        Query rangeByCreateTime = RangeQuery.of(r -> r.field("create_time")
                .lte(JsonData.of("2023-09-15 00:00:00"))
                .gte(JsonData.of("2023-09-14 00:00:00"))
                .format("yyyy-MM-dd HH:mm:ss")
        )._toQuery();
        List<FieldValue> termsList = new ArrayList<>();
        termsList.add(FieldValue.of("10000001_1"));
        termsList.add(FieldValue.of("10000001_2"));

        List<FieldValue> termsList1 = new ArrayList<>();
        termsList1.add(FieldValue.of("10000002_1"));

        mustQueryList.add(QueryBuilders.terms(fn->fn.field("tag_ids").terms(t->t.value(termsList1))));
        mustQueryList.add(QueryBuilders.terms(fn->fn.field("tag_ids").terms(t->t.value(termsList))));
        mustQueryList.add(rangeByCreateTime);

        Function<Query.Builder, ObjectBuilder<Query>> queryFn = fn -> fn.bool(b -> b
                .must(mustQueryList)
        );
        Map<String, Aggregation> aggsMap = new HashMap<>();
        aggsMap.put("group_by_name", Aggregation.of(agg -> agg.terms(term -> term.field("name"))));
        EsSearchReq esSearchReq = EsSearchReq.builder()
                .indexName("index_user_tag_dev")
                .queryFn(queryFn)
                .size(10)
                .build();

        searchDoc(esSearchReq);
        //deleteDoc(esSearchReq);

    }

    public <T> BulkResponse bulkAddDocument(String indexName, List<T> documentList) {
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (T ob : documentList) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id("id")
                            .document(ob)
                    )
            );
        }
        BulkResponse response;
        try {
            response = esClient.bulk(br.build());
        } catch (IOException e) {
            log.error("Elasticsearch bulk error,indexName:{}", indexName);
            response = new BulkResponse.Builder().errors(true).build();
        }
        return response;
    }

    /**
     * 当满足以下条件之一时，ingester将发送批量请求：
     * 操作次数超过最大值maxOperations（默认为1000）
     * 以字节为单位的批量请求大小超过最大值（默认为5 MiB）
     * 自上次请求过期以来的延迟（定期刷新，无默认值）
     *
     * @param indexName
     * @param documentList
     */
    public <T> void autoBulkAddDocument(String indexName, List<T> documentList, EsBulkCallback esBulkCallback) {

        BulkListener<T> listener = new BulkListener<T>() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request, List<T> contexts) {
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, List<T> contexts, BulkResponse response) {
                // The request was accepted, but may contain failed items.
                // The "context" list gives the file name for each bulk item.
                log.debug("Bulk request " + executionId + " completed");
                for (int i = 0; i < contexts.size(); i++) {
                    BulkResponseItem item = response.items().get(i);
                    if (item.error() != null) {
                        // Inspect the failure cause
                        log.error("Failed to index file " + contexts.get(i) + " - " + item.error().reason());
                    }
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, List<T> contexts, Throwable failure) {
                // The request could not be sent
                log.debug("Bulk request " + executionId + " failed", failure);
                esBulkCallback.apply(contexts);

            }
        };

        BulkIngester<T> ingester = BulkIngester.of(b -> b
                .client(esClient)
                //10000条提交一次
                .maxOperations(10000)
                .flushInterval(3, TimeUnit.SECONDS)
                .listener(listener)
        );
        try {
            for (Object ob : documentList) {
                ingester.add(op -> op.index(idx -> idx.index(indexName).document(ob)));
            }
        } finally {
            ingester.close();
        }
    }

    public <T> long countDoc(EsSearchReq<T> esSearchReq) {
        log.info("EsService countDoc,esSearchReq:{}",esSearchReq);
        esSearchReqValidate(esSearchReq);
        CountResponse countResponse;
        try {
            countResponse = esClient.count(s -> s
                    .index(esSearchReq.getIndexName())
                    .query(esSearchReq.getQueryFn()));
        } catch (IOException e) {
            log.error("EsService countDoc error",e);
            throw new RuntimeException(e);
        }
        log.info("EsService countDoc,esSearchReq:{},countResponse:{}",esSearchReq,countResponse);
        return countResponse.count();
    }


    public <T> SearchResponse<T> searchDoc(EsSearchReq<T> esSearchReq) throws IOException {
        log.info("EsService searchDoc esSearchReq:{}", esSearchReq);
        esSearchReqValidate(esSearchReq);
        SearchResponse<T> response = esClient.search(s -> s
                        .index(esSearchReq.getIndexName())
                        .aggregations(Optional.ofNullable(esSearchReq.getAggsMap()).orElse(new HashMap<>()))
                        .size(Optional.ofNullable(esSearchReq.getSize()).orElse(10))
                        .from(Optional.ofNullable(esSearchReq.getFrom()).orElse(0))
                        .query(esSearchReq.getQueryFn()),
                esSearchReq.getClass().getGenericSuperclass()
        );
        if (response.timedOut()) {
            log.error("EsService searchDoc timeout,esSearchReq:{}", esSearchReq);
            throw new RuntimeException("search documents timeout");
        }
        log.info("EsService searchDoc esSearchReq:{},response:{}", esSearchReq, response);
        return response;
    }

    public <T> DeleteByQueryResponse deleteDoc(EsSearchReq<T> esSearchReq) throws IOException {
        log.info("EsService deleteDoc esSearchReq:{}",esSearchReq);
        esSearchReqValidate(esSearchReq);
        DeleteByQueryResponse response = esClient.deleteByQuery(s -> s
                .index(esSearchReq.getIndexName())
                .query(esSearchReq.getQueryFn())
        );
        if(response.timedOut()){
            throw new RuntimeException("EsService delete documents timeout");
        }
        log.info("EsService deleteDoc esSearchReq:{},response:{}",esSearchReq,response);
        return response;
    }

    public <T> ScrollResponse<T> scrollDoc(EsSearchReq<T> esSearchReq) throws IOException {
        log.info("EsService scrollDoc req:{}",esSearchReq);
        Assert.notNull(esSearchReq,"EsService scrollDoc esSearchReq is null");
        Assert.notNull(esSearchReq.getScorllId(),"EsService scrollDoc scrollId is null");
        ScrollResponse<T> response = esClient.scroll(s -> s
                        //滚动有效期，默认1分钟，暂不支持自定义
                        .scroll(new Time.Builder().time("1m").build())
                        .scrollId(esSearchReq.getScorllId())
                , esSearchReq.getClass().getGenericSuperclass()
        );
        if (response.timedOut()) {
            log.error("EsService scrollDoc timeout,req:{}", esSearchReq);
            throw new RuntimeException("EsService scrollDoc documents timeout");
        }
        return response;
    }

    /**
     * 获取文档ID
     *
     * @param t
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private <T> String getDocumentId(T t) {
        String documentId = null;
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(DocumentId.class)) {
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(t);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                documentId = null != value ? String.valueOf(value) : null;
                break;
            }
        }
        return documentId;
    }

    private <T> void esSearchReqValidate(EsSearchReq<T> esSearchReq){
        Assert.notNull(esSearchReq, "EsService esSearchReq is null");
        Assert.notNull(esSearchReq.getIndexName(), "EsService  indexName is null");
        Assert.notNull(esSearchReq.getQueryFn(), "EsService  queryFn is null");
    }


    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        System.out.println(OffsetDateTime.now(ZoneOffset.ofHours(8)).format(formatter).toString());
    }

}
