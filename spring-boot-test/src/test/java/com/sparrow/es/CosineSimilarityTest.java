package com.sparrow.es;

import com.sparrow.lucence.KeyAnalyzer;
import com.sparrow.similarity.CosineSimilarity;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @author: zh_harry@163.com
 * @date: 2019/6/24 17:41
 * @description:
 */
public class CosineSimilarityTest {
    public static void main(String[] args) {
        Environment defaultEnv = new Environment(defaultSetting(),null);
        Configuration defaultConfig = new Configuration(defaultEnv, defaultSetting());
        KeyAnalyzer keyAnalyzer = new KeyAnalyzer();
        IKAnalyzer ikAnalyzer = new IKAnalyzer(defaultConfig);
        keyAnalyzer.setAnalyzer(ikAnalyzer);
        CosineSimilarity cosineSimilarity=new CosineSimilarity();
        cosineSimilarity.setAnalyzer(keyAnalyzer);
        System.out.println(cosineSimilarity.getSimilarity("情人节快乐","快乐情人节"));
        System.out.println(cosineSimilarity.getSimilarity("芝麻球","芝麻球（冻）"));
        System.out.println(cosineSimilarity.getSimilarity("我爱北京,北京朝阳","我爱北京天安门"));
    }


    //默认加载路径
    private static final String PATH_HOME = "path.home";
    private static Settings defaultSetting() {
        return Settings.builder().build();
    }
}
