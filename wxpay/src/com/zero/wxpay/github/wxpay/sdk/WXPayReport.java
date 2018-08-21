package com.zero.wxpay.github.wxpay.sdk;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * ���ױ���
 */
public class WXPayReport {

    public static class ReportInfo {

        /**
         * ��������ʹ��int��0Ϊfalse�� 1Ϊtrue��
         */

        // ������Ϣ
        private String version = "v1";
        private String sdk = WXPayConstants.WXPAYSDK_VERSION;
        private String uuid;  // ���׵ı�ʶ
        private long timestamp;   // �ϱ�ʱ��ʱ�������λ��
        private long elapsedTimeMillis; // ��ʱ����λ ����

        // ���������
        private String firstDomain;  // ��1�����������
        private boolean primaryDomain; //�Ƿ�������
        private int firstConnectTimeoutMillis;  // ��1���������õ����ӳ�ʱʱ�䣬��λ ����
        private int firstReadTimeoutMillis;  // ��1���������õĶ�д��ʱʱ�䣬��λ ����
        private int firstHasDnsError;  // ��1�������Ƿ����dns����
        private int firstHasConnectTimeout; // ��1�������Ƿ�������ӳ�ʱ
        private int firstHasReadTimeout; // ��1�������Ƿ�������ӳ�ʱ

        public ReportInfo(String uuid, long timestamp, long elapsedTimeMillis, String firstDomain, boolean primaryDomain, int firstConnectTimeoutMillis, int firstReadTimeoutMillis, boolean firstHasDnsError, boolean firstHasConnectTimeout, boolean firstHasReadTimeout) {
            this.uuid = uuid;
            this.timestamp = timestamp;
            this.elapsedTimeMillis = elapsedTimeMillis;
            this.firstDomain = firstDomain;
            this.primaryDomain = primaryDomain;
            this.firstConnectTimeoutMillis = firstConnectTimeoutMillis;
            this.firstReadTimeoutMillis = firstReadTimeoutMillis;
            this.firstHasDnsError = firstHasDnsError?1:0;
            this.firstHasConnectTimeout = firstHasConnectTimeout?1:0;
            this.firstHasReadTimeout = firstHasReadTimeout?1:0;
         }

        @Override
        public String toString() {
            return "ReportInfo{" +
                    "version='" + version + '\'' +
                    ", sdk='" + sdk + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", timestamp=" + timestamp +
                    ", elapsedTimeMillis=" + elapsedTimeMillis +
                    ", firstDomain='" + firstDomain + '\'' +
                    ", primaryDomain=" + primaryDomain +
                    ", firstConnectTimeoutMillis=" + firstConnectTimeoutMillis +
                    ", firstReadTimeoutMillis=" + firstReadTimeoutMillis +
                    ", firstHasDnsError=" + firstHasDnsError +
                    ", firstHasConnectTimeout=" + firstHasConnectTimeout +
                    ", firstHasReadTimeout=" + firstHasReadTimeout +
                    '}';
        }

        /**
         * ת���� csv ��ʽ
         *
         * @return
         */
        public String toLineString(String key) {
            String separator = ",";
            Object[] objects = new Object[] {
                version, sdk, uuid, timestamp, elapsedTimeMillis,
                    firstDomain, primaryDomain, firstConnectTimeoutMillis, firstReadTimeoutMillis,
                    firstHasDnsError, firstHasConnectTimeout, firstHasReadTimeout
            };
            StringBuffer sb = new StringBuffer();
            for(Object obj: objects) {
                sb.append(obj).append(separator);
            }
            try {
                String sign = WXPayUtil.HMACSHA256(sb.toString(), key);
                sb.append(sign);
                return sb.toString();
            }
            catch (Exception ex) {
                return null;
            }

        }

    }

    private static final String REPORT_URL = "http://report.mch.weixin.qq.com/wxpay/report/default";
    // private static final String REPORT_URL = "http://127.0.0.1:5000/test";


    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 6*1000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 8*1000;

    private LinkedBlockingQueue<String> reportMsgQueue = null;
    private WXPayConfig config;
    private ExecutorService executorService;

    private volatile static WXPayReport INSTANCE;

    private WXPayReport(final WXPayConfig config) {
        this.config = config;
        reportMsgQueue = new LinkedBlockingQueue<String>(config.getReportQueueMaxSize());

        // ��Ӵ����߳�
        executorService = Executors.newFixedThreadPool(config.getReportWorkerNum(), new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        });

        if (config.shouldAutoReport()) {
            WXPayUtil.getLogger().info("report worker num: {}", config.getReportWorkerNum());
            for (int i = 0; i < config.getReportWorkerNum(); ++i) {
                executorService.execute(new Runnable() {
                    public void run() {
                        while (true) {
                            // ���� take ��ȡ����
                            try {
                                StringBuffer sb = new StringBuffer();
                                String firstMsg = reportMsgQueue.take();
                                WXPayUtil.getLogger().info("get first report msg: {}", firstMsg);
                                String msg = null;
                                sb.append(firstMsg); //������������Ϣ
                                int remainNum = config.getReportBatchSize() - 1;
                                for (int j=0; j<remainNum; ++j) {
                                    WXPayUtil.getLogger().info("try get remain report msg");
                                    // msg = reportMsgQueue.poll();  // ��������
                                    msg = reportMsgQueue.take();
                                    WXPayUtil.getLogger().info("get remain report msg: {}", msg);
                                    if (msg == null) {
                                        break;
                                    }
                                    else {
                                        sb.append("\n");
                                        sb.append(msg);
                                    }
                                }
                                // �ϱ�
                                WXPayReport.httpRequest(sb.toString(), DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS);
                            }
                            catch (Exception ex) {
                                WXPayUtil.getLogger().warn("report fail. reason: {}", ex.getMessage());
                            }
                        }
                    }
                });
            }
        }

    }

    /**
     * ������˫��У�飬���� JDK 1.5�����߰汾��ʹ��
     *
     * @param config
     * @return
     */
    public static WXPayReport getInstance(WXPayConfig config) {
        if (INSTANCE == null) {
            synchronized (WXPayReport.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayReport(config);
                }
            }
        }
        return INSTANCE;
    }

    public void report(String uuid, long elapsedTimeMillis,
                       String firstDomain, boolean primaryDomain, int firstConnectTimeoutMillis, int firstReadTimeoutMillis,
                       boolean firstHasDnsError, boolean firstHasConnectTimeout, boolean firstHasReadTimeout) {
        long currentTimestamp = WXPayUtil.getCurrentTimestamp();
        ReportInfo reportInfo = new ReportInfo(uuid, currentTimestamp, elapsedTimeMillis,
                firstDomain, primaryDomain, firstConnectTimeoutMillis, firstReadTimeoutMillis,
                firstHasDnsError, firstHasConnectTimeout, firstHasReadTimeout);
        String data = reportInfo.toLineString(config.getKey());
        WXPayUtil.getLogger().info("report {}", data);
        if (data != null) {
            reportMsgQueue.offer(data);
        }
    }


    @Deprecated
    private void reportSync(final String data) throws Exception {
        httpRequest(data, DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS);
    }

    @Deprecated
    private void reportAsync(final String data) throws Exception {
        new Thread(new Runnable() {
            public void run() {
                try {
                    httpRequest(data, DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS);
                }
                catch (Exception ex) {
                    WXPayUtil.getLogger().warn("report fail. reason: {}", ex.getMessage());
                }
            }
        }).start();
    }

    /**
     * http ����
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @return
     * @throws Exception
     */
    private static String httpRequest(String data, int connectTimeoutMs, int readTimeoutMs) throws Exception{
        BasicHttpClientConnectionManager connManager;
        connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build(),
                null,
                null,
                null
        );
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        HttpPost httpPost = new HttpPost(REPORT_URL);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT);
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }

}
