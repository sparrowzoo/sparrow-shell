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

package com.sparrow.web.support;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sparrow.protocol.constant.Constant;

public class ValidateCode extends HttpServlet {
    public ValidateCode() {
        super();
    }

    private static final long serialVersionUID = -5813134629255375160L;
    private int imagewidth = 100;
    private int imageheight = 35;
    private int codeCount = 4;
    private int fontHeight;
    private int codeY;
    private int codeWidth;
    char[] codeSequence = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
        'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', '3',
        '4', '5', '6', '7', '8', '9'};

    @Override
    public void init() throws ServletException {
        String strWidth = this.getInitParameter("width");
        String strHeight = this.getInitParameter("height");
        String strCodeCount = this.getInitParameter("codeCount");
        try {
            if (strWidth != null && strWidth.length() != 0) {
                this.imagewidth = Integer.parseInt(strWidth);
            }
            if (strHeight != null && strHeight.length() != 0) {
                this.imageheight = Integer.parseInt(strHeight);
            }
            if (strCodeCount != null && strCodeCount.length() != 0) {
                codeCount = Integer.parseInt(strCodeCount);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        this.codeWidth = this.imagewidth / (codeCount + 1);
        this.fontHeight = this.imageheight - 2;
        this.codeY = this.imageheight - 4;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        BufferedImage buffImg = new BufferedImage(this.imagewidth,
            this.imageheight, BufferedImage.TYPE_INT_RGB);
        Graphics2D gd = buffImg.createGraphics();
        Random random = new Random();
        gd.setColor(Color.white);
        gd.fillRect(0, 0, this.imagewidth, this.imageheight);
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        gd.setFont(font);
        gd.drawRect(0, 0, this.imagewidth - 1, this.imageheight - 1);

        StringBuilder randomCode = new StringBuilder();
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random
                .nextInt(this.codeSequence.length)]);
            red = random.nextInt(254);
            green = random.nextInt(254);
            blue = random.nextInt(254);
            gd.setColor(new Color(red, green, blue));
            gd.drawString(strRand, (i * this.codeWidth) + this.codeWidth / 2,
                codeY);
            randomCode.append(strRand);
        }
        HttpSession session = req.getSession();
        session.setAttribute(Constant.VALIDATE_CODE, randomCode
            .toString());
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpg");
        ServletOutputStream sos = resp.getOutputStream();
        ImageIO.write(buffImg, "jpeg", sos);
        sos.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        if (req.getParameter(Constant.VALIDATE_CODE) != null
            && req.getParameter(Constant.VALIDATE_CODE).equalsIgnoreCase(req.getSession().getAttribute(
            Constant.VALIDATE_CODE)
            .toString().toLowerCase())) {
            resp.getWriter().write("OK");
            return;
        }
        resp.getWriter().write("false");
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
