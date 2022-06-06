package com.sparrow.gson;

import java.io.Serializable;
import java.util.*;

/**
 *
 * 推荐
 * @see <a href = "https://missfresh.feishu.cn/docs/doccnOWsDeU59MVe8IJeBI#" >新推荐服务接口</a>
 */
public class RecoRequest2 implements Serializable {

    /**
     * 设备标示符
     */
    private String deviceId;

    /**
     * 分页开始 index ，默认 0
     */
    private int offset = 0;

    /**
     * 分页请求长度数量，默认 20
     */
    private int limit = 20;

    /**
     * 子级别（比如类目的二级品类）请求数量
     */
    private int sublLimit = 10;

    /**
     * 大区Id
     */
    private int regionId;

    /**
     * 城市Id 配送区域
     */
    private int shippingAreaId;

    /**
     * 商品配送到达类型，0极速达 1非极速达 2全部 3京东
     */
    private int express = EXPRESS_TYPE.ALL.index;

    /**
     * 当前请求是否可推预售品 0 否 1 是
     */
    private int ifPreSale = 0;


    /**
     * 当前微仓是否可推明日送 0 否 1 是
     */
    private int ifNextDay = 0;

    /**
     * 前端类目id，前端一级或二级品类 ID
     */
    private int categoryId = 0;

    /**
     * 后台品类id集合
     */
    private Set<String> backCidSet = new HashSet<>();

    /**
     * 频道 ID
     */
    private int channelId = 0;

    /**
     * 搜索词,用于搜索页补余推荐
     */
    private String query = "";


    /**
     * 用户请求头的user-agent
     */
    private String userAgent;

    private String longitude;           //经度

    private String latitude;            //纬度

    /**
     * 排序规则
     */
    private RecSort sort = RecSort.SMART;

    /**
     * 是否是新用户 （主商城用到）
     */
    private boolean isNewUser;

    /**
     * 用户vip标示
     */
    private boolean isVip;

    /**
     * 召回属性过滤条件的 key-value
     */
    private Map<String, String> filters = new HashMap<>();

    /**
     * 附加信息传送，例如客户端版本信息等等
     */
    private Map<String, String> extensions = new HashMap<>();

    private String eventId;            //活动id，用于优鲜超市活动榜

    private Integer eventType;            //活动类型，用于优鲜超市活动榜， 1是热销榜， 2是品牌榜

    private int groupId = 0;   // 分组 ID

    private List<String> groupList; //调用所有场次

    private String group; //调用指定某个场次

    private List<String> secondCategoryList; //共橙二级品类集合

    public enum RecSort {
        /**
         * 默认不考虑排序
         */
        IGNORE,
        /**
         * 智能排序
         */
        SMART,
        /**
         * 销量降序
         */
        SALES_DESC,
        /**
         * 销量升序
         */
        SALES_ASC,
        /**
         *  价格降序
         */
        PRICE_DESC,
        /**
         * 价格升序
         */
        PRICE_ASC,
        /**
         * 返利降序
         */
        COMMISSION_DESC,
        /**
         * 返利升序
         */
        COMMISSION_ASC,
        /**
         * 评分排序
         */
        RATING_DESC
    }

    /**
     * 配送类型
     */
    public enum EXPRESS_TYPE {
        /**
         * 极速达
         */
        JSD("jsd", 0),
        /**
         * 非极速达
         */
        MARKET("market", 1),
        /**
         * 全部
         */
        ALL("all", 2),
        /**
         * 京东
         */
        JD("jd", 3),
        /**
         * 非京东云超品
         */
        NON_JD_MARKET("non_jd_market", 4);

        private String type;

        private int index;

        EXPRESS_TYPE(String name, int index) {
            this.type = name;
            this.index = index;
        }
    }

}
