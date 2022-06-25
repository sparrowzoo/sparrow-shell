/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.xml;

import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.StringUtility;
import java.io.BufferedReader;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStreamReader;

public class XmlValidationModeDetector {

    public static final int VALIDATION_NONE = 0;

    public static final int VALIDATION_AUTO = 1;

    public static final int VALIDATION_DTD = 2;

    public static final int VALIDATION_XSD = 3;

    private static final String DOCTYPE = "DOCTYPE";

    private static final String START_COMMENT = "<!--";

    private static final String END_COMMENT = "-->";

    private boolean inComment;

    public int detectValidationMode(String xmlFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(EnvironmentSupport.getInstance().getFileInputStream(xmlFilePath)));
        try {
            boolean isDtdValidated = false;
            String content;
            while ((content = reader.readLine()) != null) {
                content = consumeCommentTokens(content);
                if (this.inComment || !StringUtility.hasText(content)) {
                    continue;
                }
                if (hasDoctype(content)) {
                    isDtdValidated = true;
                    break;
                }
                if (hasOpeningTag(content)) {
                    // End of meaningful data...
                    break;
                }
            }
            return (isDtdValidated ? VALIDATION_DTD : VALIDATION_XSD);
        } catch (CharConversionException ex) {
            // Choked on some character encoding...
            // Leave the decision up to the caller.
            return VALIDATION_AUTO;
        } finally {
            reader.close();
        }
    }

    private boolean hasDoctype(String content) {
        return content.contains(DOCTYPE);
    }

    private boolean hasOpeningTag(String content) {
        if (this.inComment) {
            return false;
        }
        int openTagIndex = content.indexOf('<');
        return openTagIndex > -1 && (content.length() > openTagIndex + 1) &&
            Character.isLetter(content.charAt(openTagIndex + 1));
    }

    private String consumeCommentTokens(String line) {
        if (!line.contains(START_COMMENT) && !line.contains(END_COMMENT)) {
            return line;
        }
        String currLine = line;
        while ((currLine = consume(currLine)) != null) {
            if (!this.inComment && !currLine.trim().startsWith(START_COMMENT)) {
                return currLine;
            }
        }
        return null;
    }

    private String consume(String line) {
        int index = this.inComment ? endComment(line) : startComment(line);
        return index == -1 ? null : line.substring(index);
    }

    private int startComment(String line) {
        return commentToken(line, START_COMMENT, true);
    }

    private int endComment(String line) {
        return commentToken(line, END_COMMENT, false);
    }

    private int commentToken(String line, String token, boolean inCommentIfPresent) {
        int index = line.indexOf(token);
        if (index > -1) {
            this.inComment = inCommentIfPresent;
        }
        return index == -1 ? index : index + token.length();
    }
}
