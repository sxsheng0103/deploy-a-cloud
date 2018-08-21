package com.zero.wxpay.github.wxpay.sdk;

/**
 * ��������ʵ�����������Զ��л�
 */
public abstract interface IWXPayDomain {
    /**
     * �ϱ���������״��
     * @param domain ������ ���磺api.mch.weixin.qq.com
     * @param elapsedTimeMillis ��ʱ
     * @param ex ���������г��ֵ��쳣��
     *           null��ʾû���쳣
     *           ConnectTimeoutException����ʾ�������������쳣
     *           UnknownHostException�� ��ʾdns�����쳣
     */
    abstract void report(final String domain, long elapsedTimeMillis, final Exception ex);

    /**
     * ��ȡ����
     * @param config ����
     * @return ����
     */
    abstract DomainInfo getDomain(final WXPayConfig config);

    static class DomainInfo{
        public String domain;       //����
        public boolean primaryDomain;     //�������Ƿ�Ϊ������������:api.mch.weixin.qq.comΪ������
        public DomainInfo(String domain, boolean primaryDomain) {
            this.domain = domain;
            this.primaryDomain = primaryDomain;
        }

        @Override
        public String toString() {
            return "DomainInfo{" +
                    "domain='" + domain + '\'' +
                    ", primaryDomain=" + primaryDomain +
                    '}';
        }
    }

}