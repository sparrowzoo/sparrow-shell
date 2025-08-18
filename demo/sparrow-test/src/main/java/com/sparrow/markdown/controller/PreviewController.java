//package com.sparrow.markdown.controller;
//
//import com.sparrow.markdown.mark.MarkContext;
//import com.sparrow.markdown.parser.MarkParser;
//import com.sparrow.markdown.parser.impl.MarkdownParserComposite;
//import com.sparrow.markdown.vo.MarkdownVO;
//import com.sparrow.mvc.RequestParameters;
//import com.sparrow.utility.StringUtility;
//
//public class PreviewController {
//    @RequestParameters("markdown")
//    public MarkdownVO preview(String markdown){
//        if(StringUtility.isNullOrEmpty(markdown)){
//            return new MarkdownVO("");
//        }
//        MarkContext markContext = new MarkContext(markdown);
//        MarkParser markParser = MarkdownParserComposite.getInstance();
//        markParser.parse(markContext);
//        return new MarkdownVO(markContext.getHtml());
//    }
//}
