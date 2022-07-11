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

import com.sparrow.constant.Config;
import com.sparrow.protocol.Size;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.web.QRCodeUtility;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * QRCode
 */
public class QRCode extends HttpServlet {
    private int qrWidth = 300;
    private int qrHeight = 300;

    private int logoWidth = 80;
    private int logoHeight = 80;

    @Override
    public void init() throws ServletException {
        String qrConfigWidth = this.getInitParameter("qr_width");
        String qrConfigHeight = this.getInitParameter("qr_height");

        String logoConfigWidth = this.getInitParameter("logo_width");
        String logoConfigHeight = this.getInitParameter("logo_height");

        try {
            if (qrConfigWidth != null) {
                this.qrWidth = Integer.parseInt(qrConfigWidth);
            }
            if (qrConfigHeight != null) {
                this.qrHeight = Integer.parseInt(qrConfigHeight);
            }

            if (logoConfigWidth != null) {
                this.logoWidth = Integer.parseInt(logoConfigWidth);
            }
            if (logoConfigHeight != null) {
                this.logoHeight = Integer.parseInt(logoConfigHeight);
            }
        } catch (NumberFormatException ignore) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        int currentQRWidth = this.qrWidth;
        int currentQRHeight = this.qrHeight;

        int currentLogoWidth = this.logoWidth;
        int currentLogoHeight = this.logoHeight;
        if (req.getParameter("width") != null) {
            currentQRWidth = Integer.valueOf(req.getParameter("width"));
        }
        if (req.getParameter("height") != null) {
            currentQRHeight = Integer.valueOf(req.getParameter("height"));
        }

        if (req.getParameter("logo_width") != null) {
            currentLogoWidth = Integer.valueOf(req.getParameter("logo_width"));
        }
        if (req.getParameter("logo_height") != null) {
            currentLogoHeight = Integer.valueOf(req.getParameter("logo_height"));
        }

        String code = req.getParameter("code");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpg");
        ServletOutputStream sos = resp.getOutputStream();
        String logo = req.getParameter("logo");
        if (logo != null) {
            logo = ConfigUtility.getValue(Config.RESOURCE_PHYSICAL_PATH) + logo;
        }
        try {
            QRCodeUtility.encode(code, sos, logo, new Size(currentQRWidth, currentQRHeight), new Size(currentLogoWidth, currentLogoHeight));
        } catch (Exception ignore) {

        } finally {
            sos.close();
        }
    }
}
