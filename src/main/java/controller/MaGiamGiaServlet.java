package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.QuanLyDanhMucService;
import util.FlashMessage;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/ma-giam-gia")
public class MaGiamGiaServlet extends HttpServlet {
    private static final String VIEW="/WEB-INF/views/ma-giam-gia/";
    private static final DateTimeFormatter F=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final QuanLyDanhMucService service=new QuanLyDanhMucService();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");FlashMessage.nap(req);
        try{String action=req.getParameter("action");if("form".equals(action)){
            String id=req.getParameter("id");req.setAttribute("maGiamGia",id==null?null:service.layMaGiamGia(Long.parseLong(id)));
            req.getRequestDispatcher(VIEW+"form.jsp").forward(req,resp);
        }else{req.setAttribute("tuKhoa",req.getParameter("tuKhoa"));req.setAttribute("dsMaGiamGia",service.danhSachMaGiamGia(req.getParameter("tuKhoa")));req.getRequestDispatcher(VIEW+"danh-sach.jsp").forward(req,resp);}}
        catch(RuntimeException e){FlashMessage.loi(req,msg(e));resp.sendRedirect(req.getContextPath()+"/ma-giam-gia");}}
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");try{String action=req.getParameter("action");
            if("xoa".equals(action)){service.xoaMaGiamGia(Long.parseLong(req.getParameter("id")));FlashMessage.thanhCong(req,"Đã xóa mã giảm giá.");}
            else if("luu".equals(action)){service.luuMaGiamGia(blankLong(req.getParameter("id")),req.getParameter("maGiamGia"),req.getParameter("loaiGiamGia"),new BigDecimal(req.getParameter("giaTri")),LocalDateTime.parse(req.getParameter("batDauLuc"),F),LocalDateTime.parse(req.getParameter("ketThucLuc"),F),req.getParameter("trangThai"));FlashMessage.thanhCong(req,"Đã lưu mã giảm giá.");}
            else{resp.sendError(400);return;}resp.sendRedirect(req.getContextPath()+"/ma-giam-gia");
        }catch(RuntimeException e){req.setAttribute("loi",msg(e));req.getRequestDispatcher(VIEW+"form.jsp").forward(req,resp);}}
    private static Long blankLong(String s){return s==null||s.isBlank()?null:Long.valueOf(s);}
    private static String msg(RuntimeException e){return e.getMessage()==null?"Dữ liệu không hợp lệ.":e.getMessage();}
}
