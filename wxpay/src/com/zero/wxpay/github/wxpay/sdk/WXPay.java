package com.zero.wxpay.github.wxpay.sdk;

import com.github.wxpay.sdk.WXPayConstants.SignType;

import java.util.HashMap;
import java.util.Map;

public class WXPay {

    private WXPayConfig config;
    private SignType signType;
    private boolean autoReport;
    private boolean useSandbox;
    private String notifyUrl;
    private WXPayRequest wxPayRequest;

    public WXPay(final WXPayConfig config) throws Exception {
        this(config, null, true, false);
    }

    public WXPay(final WXPayConfig config, final boolean autoReport) throws Exception {
        this(config, null, autoReport, false);
    }


    public WXPay(final WXPayConfig config, final boolean autoReport, final boolean useSandbox) throws Exception{
        this(config, null, autoReport, useSandbox);
    }

    public WXPay(final WXPayConfig config, final String notifyUrl) throws Exception {
        this(config, notifyUrl, true, false);
    }

    public WXPay(final WXPayConfig config, final String notifyUrl, final boolean autoReport) throws Exception {
        this(config, notifyUrl, autoReport, false);
    }

    public WXPay(final WXPayConfig config, final String notifyUrl, final boolean autoReport, final boolean useSandbox) throws Exception {
        this.config = config;
        this.notifyUrl = notifyUrl;
        this.autoReport = autoReport;
        this.useSandbox = useSandbox;
        if (useSandbox) {
            this.signType = SignType.MD5; // ɳ�价��
        }
        else {
            this.signType = SignType.HMACSHA256;
        }
        this.wxPayRequest = new WXPayRequest(config);
    }

    private void checkWXPayConfig() throws Exception {
        if (this.config == null) {
            throw new Exception("config is null");
        }
        if (this.config.getAppID() == null || this.config.getAppID().trim().length() == 0) {
            throw new Exception("appid in config is empty");
        }
        if (this.config.getMchID() == null || this.config.getMchID().trim().length() == 0) {
            throw new Exception("appid in config is empty");
        }
        if (this.config.getCertStream() == null) {
            throw new Exception("cert stream in config is empty");
        }
        if (this.config.getWXPayDomain() == null){
            throw new Exception("config.getWXPayDomain() is null");
        }

        if (this.config.getHttpConnectTimeoutMs() < 10) {
            throw new Exception("http connect timeout is too small");
        }
        if (this.config.getHttpReadTimeoutMs() < 10) {
            throw new Exception("http read timeout is too small");
        }

    }

    /**
     * �� Map ����� appid��mch_id��nonce_str��sign_type��sign <br>
     * �ú����������̻�������ͳһ�µ��Ƚӿڣ��������ں��������ȯ�ӿ�
     *
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
        reqData.put("appid", config.getAppID());
        reqData.put("mch_id", config.getMchID());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        if (SignType.MD5.equals(this.signType)) {
            reqData.put("sign_type", WXPayConstants.MD5);
        }
        else if (SignType.HMACSHA256.equals(this.signType)) {
            reqData.put("sign_type", WXPayConstants.HMACSHA256);
        }
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), this.signType));
        return reqData;
    }

    /**
     * �ж�xml���ݵ�sign�Ƿ���Ч���������sign�ֶΣ����򷵻�false��
     *
     * @param reqData ��wxpay post����������
     * @return ǩ���Ƿ���Ч
     * @throws Exception
     */
    public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
        // �������ݵ�ǩ����ʽ�������и�����ǩ����ʽ��һ�µ�
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
    }

    /**
     * �ж�֧�����֪ͨ�е�sign�Ƿ���Ч
     *
     * @param reqData ��wxpay post����������
     * @return ǩ���Ƿ���Ч
     * @throws Exception
     */
    public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
        String signTypeInData = reqData.get(WXPayConstants.FIELD_SIGN_TYPE);
        SignType signType;
        if (signTypeInData == null) {
            signType = SignType.MD5;
        }
        else {
            signTypeInData = signTypeInData.trim();
            if (signTypeInData.length() == 0) {
                signType = SignType.MD5;
            }
            else if (WXPayConstants.MD5.equals(signTypeInData)) {
                signType = SignType.MD5;
            }
            else if (WXPayConstants.HMACSHA256.equals(signTypeInData)) {
                signType = SignType.HMACSHA256;
            }
            else {
                throw new Exception(String.format("Unsupported sign_type: %s", signTypeInData));
            }
        }
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), signType);
    }


    /**
     * ����Ҫ֤�������
     * @param urlSuffix String
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ��ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ��ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public String requestWithoutCert(String urlSuffix, Map<String, String> reqData,
                                     int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = WXPayUtil.mapToXml(reqData);

        String resp = this.wxPayRequest.requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, autoReport);
        return resp;
    }


    /**
     * ��Ҫ֤�������
     * @param urlSuffix String
     * @param reqData ��wxpay post����������  Map
     * @param connectTimeoutMs ��ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ��ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public String requestWithCert(String urlSuffix, Map<String, String> reqData,
                                  int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String msgUUID= reqData.get("nonce_str");
        String reqBody = WXPayUtil.mapToXml(reqData);

        String resp = this.wxPayRequest.requestWithCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, this.autoReport);
        return resp;
    }

    /**
     * ���� HTTPS API�������ݣ�ת����Map����return_codeΪSUCCESSʱ����֤ǩ����
     * @param xmlStr API���ص�XML��ʽ����
     * @return Map��������
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr) throws Exception {
        String RETURN_CODE = "return_code";
        String return_code;
        Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        }
        else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals(WXPayConstants.FAIL)) {
            return respData;
        }
        else if (return_code.equals(WXPayConstants.SUCCESS)) {
           if (this.isResponseSignatureValid(respData)) {
               return respData;
           }
           else {
               throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
           }
        }
        else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }

    /**
     * ���ã��ύˢ��֧��<br>
     * ������ˢ��֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> microPay(Map<String, String> reqData) throws Exception {
        return this.microPay(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã��ύˢ��֧��<br>
     * ������ˢ��֧��
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> microPay(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_MICROPAY_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.MICROPAY_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * �ύˢ��֧���������POS�����������ɹ�
     * �������Ի��ƣ����60s
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> microPayWithPos(Map<String, String> reqData) throws Exception {
        return this.microPayWithPos(reqData, this.config.getHttpConnectTimeoutMs());
    }

    /**
     * �ύˢ��֧���������POS�����������ɹ�
     * �������Ի��ƣ����60s
     * @param reqData
     * @param connectTimeoutMs
     * @return
     * @throws Exception
     */
    public Map<String, String> microPayWithPos(Map<String, String> reqData, int connectTimeoutMs) throws Exception {
        int remainingTimeMs = 60*1000;
        long startTimestampMs = 0;
        Map<String, String> lastResult = null;
        Exception lastException = null;

        while (true) {
            startTimestampMs = WXPayUtil.getCurrentTimestampMs();
            int readTimeoutMs = remainingTimeMs - connectTimeoutMs;
            if (readTimeoutMs > 1000) {
                try {
                    lastResult = this.microPay(reqData, connectTimeoutMs, readTimeoutMs);
                    String returnCode = lastResult.get("return_code");
                    if (returnCode.equals("SUCCESS")) {
                        String resultCode = lastResult.get("result_code");
                        String errCode = lastResult.get("err_code");
                        if (resultCode.equals("SUCCESS")) {
                            break;
                        }
                        else {
                            // �������룬��֧�����δ֪���������ύˢ��֧��
                            if (errCode.equals("SYSTEMERROR") || errCode.equals("BANKERROR") || errCode.equals("USERPAYING")) {
                                remainingTimeMs = remainingTimeMs - (int)(WXPayUtil.getCurrentTimestampMs() - startTimestampMs);
                                if (remainingTimeMs <= 100) {
                                    break;
                                }
                                else {
                                    WXPayUtil.getLogger().info("microPayWithPos: try micropay again");
                                    if (remainingTimeMs > 5*1000) {
                                        Thread.sleep(5*1000);
                                    }
                                    else {
                                        Thread.sleep(1*1000);
                                    }
                                    continue;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                    else {
                        break;
                    }
                }
                catch (Exception ex) {
                    lastResult = null;
                    lastException = ex;
                }
            }
            else {
                break;
            }
        }

        if (lastResult == null) {
            throw lastException;
        }
        else {
            return lastResult;
        }
    }



    /**
     * ���ã�ͳһ�µ�<br>
     * ������������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
        return this.unifiedOrder(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã�ͳһ�µ�<br>
     * ������������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> unifiedOrder(Map<String, String> reqData,  int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
        }
        if(this.notifyUrl != null) {
            reqData.put("notify_url", this.notifyUrl);
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


    /**
     * ���ã���ѯ����<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> orderQuery(Map<String, String> reqData) throws Exception {
        return this.orderQuery(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã���ѯ����<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post���������� int
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> orderQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_ORDERQUERY_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.ORDERQUERY_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


    /**
     * ���ã���������<br>
     * ������ˢ��֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> reverse(Map<String, String> reqData) throws Exception {
        return this.reverse(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã���������<br>
     * ������ˢ��֧��<br>
     * ��������Ҫ֤��
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> reverse(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_REVERSE_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.REVERSE_URL_SUFFIX;
        }
        String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


    /**
     * ���ã��رն���<br>
     * ������������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> closeOrder(Map<String, String> reqData) throws Exception {
        return this.closeOrder(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã��رն���<br>
     * ������������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> closeOrder(Map<String, String> reqData,  int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_CLOSEORDER_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.CLOSEORDER_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


    /**
     * ���ã������˿�<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> refund(Map<String, String> reqData) throws Exception {
        return this.refund(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã������˿�<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��<br>
     * ��������Ҫ֤��
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> refund(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_REFUND_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.REFUND_URL_SUFFIX;
        }
        String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


    /**
     * ���ã��˿��ѯ<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> refundQuery(Map<String, String> reqData) throws Exception {
        return this.refundQuery(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã��˿��ѯ<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> refundQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_REFUNDQUERY_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.REFUNDQUERY_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


    /**
     * ���ã����˵����أ��ɹ�ʱ���ض��˵����ݣ�ʧ��ʱ����XML��ʽ���ݣ�<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> downloadBill(Map<String, String> reqData) throws Exception {
        return this.downloadBill(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã����˵�����<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��<br>
     * �����������Ƿ�ɹ�������Map�����ɹ������ص�Map�к���return_code��return_msg��data��
     *      ����return_codeΪ`SUCCESS`��dataΪ���˵����ݡ�
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return ������װ��API��������
     * @throws Exception
     */
    public Map<String, String> downloadBill(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_DOWNLOADBILL_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.DOWNLOADBILL_URL_SUFFIX;
        }
        String respStr = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs).trim();
        Map<String, String> ret;
        // ���ִ��󣬷���XML����
        if (respStr.indexOf("<") == 0) {
            ret = WXPayUtil.xmlToMap(respStr);
        }
        else {
            // ��������csv����
            ret = new HashMap<String, String>();
            ret.put("return_code", WXPayConstants.SUCCESS);
            ret.put("return_msg", "ok");
            ret.put("data", respStr);
        }
        return ret;
    }


    /**
     * ���ã����ױ���<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> report(Map<String, String> reqData) throws Exception {
        return this.report(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã����ױ���<br>
     * ������ˢ��֧����������֧����ɨ��֧����APP֧��
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> report(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_REPORT_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.REPORT_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return WXPayUtil.xmlToMap(respXml);
    }


    /**
     * ���ã�ת��������<br>
     * ������ˢ��֧����ɨ��֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> shortUrl(Map<String, String> reqData) throws Exception {
        return this.shortUrl(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã�ת��������<br>
     * ������ˢ��֧����ɨ��֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> shortUrl(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_SHORTURL_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.SHORTURL_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


    /**
     * ���ã���Ȩ���ѯOPENID�ӿ�<br>
     * ������ˢ��֧��
     * @param reqData ��wxpay post����������
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> authCodeToOpenid(Map<String, String> reqData) throws Exception {
        return this.authCodeToOpenid(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * ���ã���Ȩ���ѯOPENID�ӿ�<br>
     * ������ˢ��֧��
     * @param reqData ��wxpay post����������
     * @param connectTimeoutMs ���ӳ�ʱʱ�䣬��λ�Ǻ���
     * @param readTimeoutMs ����ʱʱ�䣬��λ�Ǻ���
     * @return API��������
     * @throws Exception
     */
    public Map<String, String> authCodeToOpenid(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_AUTHCODETOOPENID_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.AUTHCODETOOPENID_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


} // end class
