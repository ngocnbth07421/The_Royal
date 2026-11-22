package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.QuanLyDanhMucService;
import util.FlashMessage;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/thanh-toan")
public class ThanhToanServlet extends HttpServlet {
    private static final String VIEW="/WEB-INF/views/thanh-toan/";
    private final QuanLyDanhMucService service=new QuanLyDanhMucService();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");FlashMessage.nap(req);
        try{String action=req.getParameter("action");if("form".equals(action)){
            req.setAttribute("dsDatPhong",service.danhSachDatPhong());req.getRequestDispatcher(VIEW+"form.jsp").forward(req,resp);
        }else{req.setAttribute("tuKhoa",req.getParameter("tuKhoa"));req.setAttribute("trangThai",req.getParameter("trangThai"));req.setAttribute("dsThanhToan",service.danhSachThanhToan(req.getParameter("tuKhoa"),req.getParameter("trangThai")));req.getRequestDispatcher(VIEW+"danh-sach.jsp").forward(req,resp);}}
        catch(RuntimeException e){FlashMessage.loi(req,msg(e));resp.sendRedirect(req.getContextPath()+"/thanh-toan");}}
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        req.setCharacterEncoding("UTF-8");try{String action=req.getParameter("action");
            if("tao".equals(action)){String the=req.getParameter("idTheThanhVien");service.taoThanhToan(Long.parseLong(req.getParameter("idDatPhong")),the==null||the.isBlank()?null:Long.valueOf(the),req.getParameter("phuongThuc"),new BigDecimal(req.getParameter("tienThanhToan")));FlashMessage.thanhCong(req,"Đã tạo giao dịch ở trạng thái CHO_XU_LY.");}
            else if("trangThai".equals(action)){service.capNhatTrangThaiThanhToan(Long.parseLong(req.getParameter("id")),req.getParameter("trangThai"));FlashMessage.thanhCong(req,"Đã cập nhật trạng thái giao dịch.");}
            else{resp.sendError(400);return;}resp.sendRedirect(req.getContextPath()+"/thanh-toan");
        }catch(RuntimeException e){FlashMessage.loi(req,msg(e));resp.sendRedirect(req.getContextPath()+"/thanh-toan");}}
    private static String msg(RuntimeException e){return e.getMessage()==null?"Dữ liệu không hợp lệ.":e.getMessage();}
}
