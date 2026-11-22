package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.QuanLyDanhMucService;
import util.FlashMessage;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/loai-phong")
public class LoaiPhongServlet extends HttpServlet {
    private static final String VIEW="/WEB-INF/views/loai-phong/";
    private final QuanLyDanhMucService service=new QuanLyDanhMucService();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8"); FlashMessage.nap(req);
        try {
            String action=req.getParameter("action");
            if("form".equals(action)){
                Long id=longOrNull(req.getParameter("id"));
                req.setAttribute("loaiPhong",id==null?null:service.layLoaiPhong(id));
                req.setAttribute("dsHangPhong",service.danhSachHangPhong());req.setAttribute("dsLoaiGiuong",service.danhSachLoaiGiuong());
                req.getRequestDispatcher(VIEW+"form.jsp").forward(req,resp);
            }else{
                String kw=req.getParameter("tuKhoa");req.setAttribute("tuKhoa",kw);req.setAttribute("dsLoaiPhong",service.danhSachLoaiPhong(kw));
                req.getRequestDispatcher(VIEW+"danh-sach.jsp").forward(req,resp);
            }
        }catch(RuntimeException e){FlashMessage.loi(req,msg(e));resp.sendRedirect(req.getContextPath()+"/loai-phong");}
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");String action=req.getParameter("action");
        try{
            if("xoa".equals(action)){service.xoaLoaiPhong(requiredLong(req,"id"));FlashMessage.thanhCong(req,"Đã xóa loại phòng.");}
            else if("luu".equals(action)){
                String gia=req.getParameter("giaMoiDem");
                service.luuLoaiPhong(longOrNull(req.getParameter("id")),requiredLong(req,"idHangPhong"),requiredLong(req,"idLoaiGiuong"),req.getParameter("ten"),new BigDecimal(gia),req.getParameter("moTa"));
                FlashMessage.thanhCong(req,"Đã lưu loại phòng.");
            }else resp.sendError(400,"Thao tác không hợp lệ");
            resp.sendRedirect(req.getContextPath()+"/loai-phong");
        }catch(RuntimeException e){req.setAttribute("loi",msg(e));req.setAttribute("dsHangPhong",service.danhSachHangPhong());req.setAttribute("dsLoaiGiuong",service.danhSachLoaiGiuong());req.getRequestDispatcher(VIEW+"form.jsp").forward(req,resp);}
    }
    private static Long longOrNull(String s){return s==null||s.isBlank()?null:Long.valueOf(s);}
    private static Long requiredLong(HttpServletRequest r,String k){Long v=longOrNull(r.getParameter(k));if(v==null)throw new IllegalArgumentException("Thiếu "+k);return v;}
    private static String msg(RuntimeException e){return e.getMessage()==null?"Dữ liệu không hợp lệ.":e.getMessage();}
}
