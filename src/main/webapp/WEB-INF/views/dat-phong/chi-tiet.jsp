<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Đơn ${datPhong.maDatPhong}"/>
<c:set var="menu" value="don"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-head">
  <h1>Đơn <c:out value="${datPhong.maDatPhong}"/>
    <c:set var="tt" value="${datPhong.trangThai}"/><%@ include file="/WEB-INF/views/layout/badge.jspf" %></h1>
  <a class="btn" href="${ctx}/dat-phong">Về danh sách</a>
</div>

<dl class="facts panel">
  <div><dt>Khách hàng</dt><dd><c:out value="${datPhong.khachHang.hoTen}"/></dd></div>
  <div><dt>Số điện thoại</dt><dd><c:out value="${datPhong.khachHang.soDienThoai}"/></dd></div>
  <div><dt>Nhận phòng</dt><dd>${datPhong.thoiGianNhanPhong.toLocalDate()} lúc ${datPhong.thoiGianNhanPhong.toLocalTime()}</dd></div>
  <div><dt>Trả phòng</dt><dd>${datPhong.thoiGianTraPhong.toLocalDate()} lúc ${datPhong.thoiGianTraPhong.toLocalTime()}</dd></div>
  <div><dt>Số đêm</dt><dd>${datPhong.soDem}</dd></div>
</dl>

<h2>Phòng đã đặt</h2>
<div class="table-wrap">
  <table>
    <thead><tr><th>Phòng</th><th>Loại phòng</th><th class="num">Người lớn</th><th class="num">Trẻ dưới 6</th><th class="num">Trẻ từ 6</th><th>Trạng thái</th></tr></thead>
    <tbody>
    <c:forEach var="ct" items="${datPhong.chiTiet}">
      <tr>
        <td><c:out value="${ct.phong.maPhong}"/></td>
        <td><c:out value="${ct.phong.loaiPhong.ten}"/></td>
        <td class="num">${ct.soNguoiLon}</td>
        <td class="num">${ct.soTreEmDuoi6}</td>
        <td class="num">${ct.soTreEmTu6Tuoi}</td>
        <td><c:out value="${ct.trangThai eq 'HIEU_LUC' ? 'Có hiệu lực' : (ct.trangThai eq 'DA_HUY' ? 'Đã hủy' : (ct.trangThai eq 'DANG_LUU_TRU' ? 'Đang lưu trú' : 'Đã trả phòng'))}"/></td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<h2>Chi phí</h2>
<div class="table-wrap money">
  <table>
    <tbody>
      <tr><td>Tiền phòng</td><td><fmt:formatNumber value="${datPhong.tienPhong}" groupingUsed="true"/> ₫</td></tr>
      <tr><td>Giảm giá</td><td>- <fmt:formatNumber value="${datPhong.giamGia}" groupingUsed="true"/> ₫</td></tr>
      <tr><td>Phí dịch vụ</td><td><fmt:formatNumber value="${datPhong.phiDichVu}" groupingUsed="true"/> ₫</td></tr>
      <tr><td>Thuế VAT</td><td><fmt:formatNumber value="${datPhong.thueVat}" groupingUsed="true"/> ₫</td></tr>
      <tr class="total"><td>Tổng tiền</td><td><fmt:formatNumber value="${datPhong.tongTien}" groupingUsed="true"/> ₫</td></tr>
      <tr><td>Tiền cọc</td><td><fmt:formatNumber value="${datPhong.tienCoc}" groupingUsed="true"/> ₫</td></tr>
    </tbody>
  </table>
</div>

<c:if test="${not empty lichSuHuy}">
  <h2>Thông tin hủy</h2>
  <div class="panel">
    <p>Số tiền hoàn lại: <strong><fmt:formatNumber value="${lichSuHuy.soTienHoan}" groupingUsed="true"/> ₫</strong></p>
    <c:if test="${not empty lichSuHuy.ghiChu}"><p>Ghi chú: <c:out value="${lichSuHuy.ghiChu}"/></p></c:if>
  </div>
</c:if>

<c:if test="${coTheHuy}">
  <h2>Hủy đơn</h2>
  <form class="panel form-stack" method="post" action="${ctx}/dat-phong"
        onsubmit="return confirm('Hủy đơn này? Phòng sẽ được nhả cho khách khác đặt.');">
    <input type="hidden" name="action" value="huy">
    <input type="hidden" name="id" value="${datPhong.id}">
    <label>Người hủy
      <select name="loaiHuy">
        <option value="KHACH_HUY">Khách hủy</option>
        <option value="KHACH_SAN_HUY">Khách sạn hủy</option>
      </select>
    </label>
    <label>Ghi chú
      <textarea name="ghiChu" rows="3" maxlength="255"></textarea>
    </label>
    <div class="actions"><button class="btn danger" type="submit">Hủy đơn đặt phòng</button></div>
  </form>
</c:if>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
