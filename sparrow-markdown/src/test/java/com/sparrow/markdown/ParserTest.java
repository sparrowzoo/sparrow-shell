package com.sparrow.markdown;

import com.sparrow.markdown.mark.MarkContext;
import com.sparrow.markdown.parser.MarkParser;
import com.sparrow.markdown.parser.impl.MarkdownParserComposite;

/**
 * Created by harry on 2018/2/8.
 */
public class ParserTest{

    public static void main(String[] args) {
        MarkContext markContext=new MarkContext("**加**\n" +
                "\n" +
                "# sadfdasfs## \n" +
                "sfsdf\n" +
                "\n" +
                "> sfsafs~~fsdf~~ \n" +
                "sfsfs++fasf++\n" +
                "fsaf\n" +
                "afsadf\n" +
                "afsaf\n" +
                "\n" +
                "> fasfsdf\n" +
                "sdfsf\n" +
                "fsad\n" +
                "afsa*dfaf*sdf++dsafs*ad*f\n" +
                "safsadf++\n" +
                "af++sadf++a==fasdf==fsfs[link](http://note.youdao.com/)fsdfsafs++dfsafs++af\n" +
                "\n" +
                "asfsdfsadfafsaf 这里是在北京，#hsafsdfsfasfd\n" +
                "\n" +
                "s\n" +
                "---\n" +
                "\n" +
                "\n" +
                "sdfsadfs\n" +
                "\n" +
                "    sfsfs\n" +
                "    sfsf\n" +
                "    dfsdf\n" +
                "    sss\n" +
                "\n" +
                "sdfsfs\n" +
                "```\n" +
                "sfsfsf\n" +
                "```\n" +
                "\n" +
                "afddd\n" +
                "    \n" +
                "\n" +
                "        这里是北京**f这里是北京f这里是北京f这里是北京f这里是北京f这里是北京\n" +
                "    sfsfafsdf++dsff这里++是北京f这里是北京**f这里是北**京f这里是北京f这里是北京\n" +
                "    fsfsfsadff这里是北京f这里*是北京f*这++里是北京f这里是++北京\n" +
                "    fsafsafsfsfsfsa sdfasf afaf adfsaf f这里是北京f这里是北京**f这里是北京f这里是北京sfs\n" +
                "    \n" +
                "    sfsadf\n" +
                "    afsadf\n" +
                "    safsaf\n" +
                "    sfs\n" +
                "      fsfsaf\n" +
                "fsafssfsdfsfsfsffsafsafsf\n" +
                "1. \n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "sfsf\n" +
                "sfsaf\n" +
                "sfsf\n" +
                "asdf粗**\n" +
                "*SSS*\n" +
                "\n" +
                "\n" +
                "*倾\n" +
                "sfsf\n" +
                "safsadf\n" +
                "sfsafd\n" +
                "sadf斜 \n" +
                "*\n" +
                "\n" +
                "~~删除\n" +
                "safsda\n" +
                "ffas\n" +
                "fdsaf ~~\n" +
                "\n" +
                "++下划线fsfs \n" +
                "sdfsdf sfsf \n" +
                "sssss\n" +
                "sfsfsf\n" +
                "afsdf\n" +
                "sadfsaf++\n" +
                "\n" +
                "==高\n" +
                "亮==\n" +
                "\n" +
                "# h1\n" +
                "\n" +
                "## h2\n" +
                "\n" +
                "### h3\n" +
                "\n" +
                "#### h4 \n" +
                "\n" +
                "##### h5\n" +
                "\n" +
                "###### h6sfs\n" +
                "sfsdf\n" +
                "safsd\n" +
                "fsdfs\n" +
                "fasfas\n" +
                "dfsfsadf\n" +
                "\n" +
                "\n" +
                "横线\n" +
                "---\n" +
                "\n" +
                ">引用\n" +
                "sfsf\n" +
                "sfsafsfd\n" +
                "afsaf\n" +
                "fsd\n" +
                "afsf\n" +
                "asfsdf\n" +
                "sfsaf\n" +
                "\n" +
                "\n" +
                "s\n" +
                "\n" +
                "\n" +
                "- 列表点\n" +
                "\n" +
                "\n" +
                "\n" +
                "- 列表点\n" +
                "\n" +
                "1. 数字 \n" +
                "    1. 1.1\n" +
                "    2. 1.2\n" +
                "1. 数字3\n" +
                "    1. 2.2\n" +
                "234\n" +
                "\n" +
                "\n" +
                "- [ ] 这里\n" +
                "![图标](http://note.youdao.com/favicon.ico\n" +
                ")是复选框\n" +
                " 这里是复选框内容\n" +
                " 这里是什么东西\n" +
                "- [x] 这是啥？\n" +
                "- [超链接](http://note.youdao.com/\n" +
                ")\n" +
                "\n" +
                "![图标](http://note.youdao.com/favicon.ico\n" +
                ")\n" +
                "\n" +
                "\n" +
                "```     print(\"ddd\")\n" +
                "代码段\n" +
                "printf(\"引号是红色\")\n" +
                "\n" +
                "p()有括号认为是方法 \n" +
                "class A {}\n" +
                "```\n" +
                "\n" +
                "\n" +
                "```math\n" +
                "E = mc^2\n" +
                "\n" +
                "E=MC^5\n" +
                "E=mc-5\n" +
                "\n" +
                "\n" +
                "```\n" +
                "数学表达式\n" +
                "\n" +
                "\n" +
                "\n" +
                "title | title2\n" +
                "---|---\n" +
                "r1-1 | r 1-2\n" +
                "r2-1 | r 2-2\n" +
                "\n");
        MarkParser markParser=new MarkdownParserComposite();
        markParser.parse(markContext);
    }
}
