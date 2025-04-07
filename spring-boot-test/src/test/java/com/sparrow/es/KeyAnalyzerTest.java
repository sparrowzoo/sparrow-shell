package com.sparrow.es;

import com.sparrow.lucence.KeyAnalyzer;
import com.sparrow.lucence.LexemeWithBoost;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.util.List;

public class KeyAnalyzerTest {
    static {
        System.setProperty("web.root", "D:\\workspace\\sparrow\\sparrow-shell\\sparrow-test\\target");
    }

    //默认加载路径
    private static final String PATH_HOME = "path.home";

    public static void main(String[] args) {
        KeyAnalyzer keyAnalyzer = new KeyAnalyzer();
        // IndexMetaData indexMetaData=new IndexMetaData();
        // IndexSettings indexSettings=new IndexSettings(null,defaultSetting());

        /**
         * configFile = homeFile.resolve("config");
         * 默认
         * <p>
         * if (PATH_HOME_SETTING.exists(settings)) {
         * homeFile = PathUtils.get(PATH_HOME_SETTING.get(settings)).normalize();
         * } else {
         * throw new IllegalStateException(PATH_HOME_SETTING.getKey() + " is not configured");
         * }
         * path.home 不填会报错
         */
        Environment defaultEnv = new Environment(defaultSetting(), null);
        Configuration defaultConfig = new Configuration(defaultEnv, defaultSetting());

        IKAnalyzer ikAnalyzer = new IKAnalyzer(defaultConfig);
        keyAnalyzer.setAnalyzer(ikAnalyzer);
        List<LexemeWithBoost> list = keyAnalyzer.getKeyList("美家");
        for (LexemeWithBoost lexeme : list) {
            System.out.println(lexeme.getLexemeText());
        }
    }

    private static Settings defaultSetting() {
        String pathHome = System.getProperty("web.root");
        if (null == pathHome || pathHome.trim().isEmpty())
            throw new IllegalArgumentException("load defaultSetting error!" + PATH_HOME + " can't be null!");
        //this.useSmart = settings.get("use_smart", "false").equals("true");
        return Settings.builder().put(PATH_HOME, pathHome + "/classes").put("use_smart", false)
            .build();
    }
}
