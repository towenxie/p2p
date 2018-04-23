package com.download.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Servlet implementation class ServletDownload
 */
@WebServlet(asyncSupported = true, urlPatterns = {"/ServletDownload"})
public class ServletDownload extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletDownload() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获得请求文件名
        String filename = request.getParameter("filename");
        // 设置文件MIME类型
        response.setContentType(getServletContext().getMimeType(filename));
        // 设置Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        // 读取目标文件，通过response将目标文件写到客户端
        // 获取目标文件的绝对路径
        String fullFileName = getServletContext().getRealPath("/download/" + filename);
        // 读取文件
        File file = new File(fullFileName);
        if (!file.exists()) {
            request.setAttribute("message", "您要下载的资源已被删除！！");
            request.getRequestDispatcher("/message.jsp").forward(request, response);
            return;
        }
        // 获取浏览器类型
        String browser = request.getHeader("user-agent");
        // 设置响应头，206支持断点续传
        int http_status = 206;
        if (browser.contains("MSIE"))
            http_status = 200;// 200 响应头，不支持断点续传
        response.reset();
        response.setStatus(http_status);
        // 响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 读文件的起始位置
        int beginIndex = 0;
        // 下载结束位置
        long endIndex = file.length() - 1;
        // 获取Range，下载范围
        String range = request.getHeader("range");
        if (range != null) {
            // 剖解range
            range = range.split("=")[1];
            String[] rs = range.split("-");
            if (isLongValue(rs[0])) {
                beginIndex = Integer.parseInt(rs[0]);
            }
            if (rs.length > 1 && isLongValue(rs[1])) {
                endIndex = Integer.parseInt(rs[1]);
            }
        }
        // 设置响应头
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Range", "bytes " + beginIndex + "-" + endIndex + "/" + file.length());
        // 文件名用ISO08859_1编码
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + new String(filename.getBytes(), "ISO8859_1") + "\"");
        response.setHeader("Content-Length", "" + (endIndex - beginIndex + 1));
        System.out
                .println("download: " + fullFileName + ", bytes " + beginIndex + "-" + endIndex + "/" + file.length());
        OutputStream out = response.getOutputStream();
        RandomAccessFile randomFile = new RandomAccessFile(fullFileName, "r");
        try {
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[128 * 1024];
            int len = 0;
            boolean full = false;
            while ((len = randomFile.read(bytes)) > 0) {
                if (randomFile.getFilePointer() - 1 > endIndex) {
                    len = (int) (len - (randomFile.getFilePointer() - endIndex - 1));
                    full = true;
                }
                out.write(bytes, 0, len);
                if (full)
                    break;
            }
            // 输出
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println("下载中断！！！");
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
        response.flushBuffer();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
    }

    private boolean isLongValue(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return true;
    }
}
