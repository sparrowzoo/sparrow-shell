package com.sparrow.es;

import com.sparrow.lucence.LexemeWithBoost;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IKParser {
    static {
        System.setProperty("web.root", "D:\\workspace\\sparrow\\sparrow-shell\\sparrow-test\\target");
    }

    public static void main(String[] args) {
        IKParser parser = new IKParser();
        List<Lexeme> lexemes = parser.parseLexeme("美家");
        List<LexemeWithBoost> lexemeWithBoosts = parseLexemes(lexemes);
        for (LexemeWithBoost lexeme : lexemeWithBoosts) {
            System.out.println(lexeme.toString());
        }
    }

    private static Settings defaultSetting = defaultSetting();

    //默认加载路径
    private static final String PATH_HOME = "path.home";

    private static Settings defaultSetting() {
        String pathHome = System.getProperty("web.root");
        if (null == pathHome || pathHome.trim().isEmpty())
            throw new IllegalArgumentException("load defaultSetting error!" + PATH_HOME + " can't be null!");
        return Settings.builder().put(PATH_HOME, pathHome + "/classes").build();
    }

    public synchronized List<Lexeme> parseLexeme(String text) {
        if (!StringUtils.hasText(text))
            return null;
        Environment defaultEnv = new Environment(defaultSetting(), null);
        Configuration defaultConfig = new Configuration(defaultEnv, defaultSetting);
        IKSegmenter ikSegmenter = new IKSegmenter(null, defaultConfig);

        Lexeme textLexeme = new Lexeme(0, 0, text.length(), 0);
        textLexeme.setLexemeText(text);
        StringReader reader = new StringReader(text);
        //重置切词器,该方法内部已经线程同步
        ikSegmenter.reset(reader);
        Lexeme token;
        List<String> words = new ArrayList<>();
        String word = "";
        List<Lexeme> orginWord = new ArrayList<>();
        List<Lexeme> lexemeWord = new ArrayList<>();
        try {
            while ((token = ikSegmenter.next()) != null) {
                word = token.getLexemeText();
                if (StringUtils.hasText(word)) {
                    orginWord.add(token);
                    words.add(word);
                }
            }
            for (Lexeme lexeme : orginWord) {
                lexemeWord.add(lexeme);
            }
            if (lexemeWord.size() == 0)
                lexemeWord.add(textLexeme);

        } catch (IOException e) {
            lexemeWord.add(textLexeme);
        }
        return lexemeWord;
    }

    private static List<LexemeWithBoost> transforList(List<Lexeme> lexemes) {
        List<LexemeWithBoost> semanticList = new ArrayList<LexemeWithBoost>();
        for (Lexeme lexeme : lexemes) {
            com.sparrow.lucence.LexemeWithBoost lexeme1 = new com.sparrow.lucence.LexemeWithBoost(lexeme.getOffset(), lexeme.getBegin(), lexeme.getLength(), lexeme.getLexemeTypeString());
            lexeme1.setLexemeText(lexeme.getLexemeText());
            semanticList.add(lexeme1);
        }
        return semanticList;
    }

    public static Boolean compareTo(LexemeWithBoost current, LexemeWithBoost other) {
        if (current.getEndPosition() >= other.getEndPosition()
            && current.getBeginPosition() <= other.getBeginPosition()
        ) {
            return true;
        } else {
            return false;
        }
    }

    public static List<LexemeWithBoost> parseLexemes(List<Lexeme> lexemes) {
        Map<String, List<LexemeWithBoost>> semanticMap = new HashMap<String, List<LexemeWithBoost>>();
        List<LexemeWithBoost> semanticGroupList = new ArrayList<LexemeWithBoost>();
        if (!CollectionUtils.isEmpty(lexemes)) {
            //实体转换  Lexeme -->   SemanticLexeme
            List<LexemeWithBoost> semanticList = transforList(lexemes);

            //添加 ParentText 和  score
            for (LexemeWithBoost semantic : semanticList) {
                for (LexemeWithBoost other : semanticList) {
                    String parentText = "";
                    if (other.getParent() != null) {
                        parentText = other.getParent().getLexemeText();
                    }
                    if (org.apache.commons.lang3.StringUtils.isEmpty(parentText) && compareTo(semantic, other)) {

                        other.setParent(semantic);
                        if (other.getLength() == 1) {
                            other.setBoost(LexemeWithBoost.SINGLEWORD_SCORE);
                        } else if (other.getParent().getLexemeText().length() == other.getLength()) {
                            other.setBoost(LexemeWithBoost.WHOLEWORD_SCORE);
                        } else {
                            other.setBoost(LexemeWithBoost.NORMALWORD_SCORE);
                        }
                        semanticGroupList.add(other);
                    }
                }
        		/*if(CollectionUtils.isNotEmpty(semanticGroupList)){
        			//semanticGroupList = this.parseWordRel(semantic.getLexemeText(), semanticGroupList);
        			semanticMap.put(semantic.getLexemeText(), semanticGroupList);
        		}*/
            }
        }

        // Collections.sort(semanticGroupList, Comparator.comparing(LexemeWithBoost::getLength).reversed());
        return semanticGroupList;
    }
}
