<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Đặt phòng"/>
<c:set var="menu" value="tim-phong"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-head"><h1>Đặt phòng</h1></div>

<form method="post" action="${ctx}/dat-phong">
  <input type="hidden" name="action" value="dat">
  <input type="hidden" name="nhanPhong" value="<c:out value='${nhanPhong}'/>">
  <input type="hidden" name="traPhong" value="<c:out value='${traPhong}'/>">

  <dl class="facts panel" style="margin-bottom:1.25rem">
    <div><dt>Nhận phòng</dt><dd><c:out value="${nhanPhong}"/> lúc 14:00</dd></div>
    <div><dt>Trả phòng</dt><dd><c:out value="${traPhong}"/> lúc 12:00</dd></div>
    <div><dt>Số đêm</dt><dd>${soDem}</dd></div>
    <div><dt>Tiền phòng tạm tính</dt><dd><fmt:formatNumber value="${tienPhongTamTinh}" groupingUsed="true"/> ₫</dd></div>
  </dl>

  <h2>Số người ở từng phòng</h2>
  <div class="table-wrap">
    <table>
      <thead><tr><th>Phòng</th><th>Loại phòng</th><th class="num">Sức chứa</th><th>Người lớn</th><th>Trẻ dưới 6 tuổi</th><th>Trẻ từ 6 tuổi</th></tr></thead>
      <tbody>
      <c:forEach var="p" items="${dsPhongChon}">
        <c:set var="kNL" value="soNguoiLon_${p.id}"/>
        <c:set var="kD6" value="soTreEmDuoi6_${p.id}"/>
        <c:set var="kT6" value="soTreEmTu6Tuoi_${p.id}"/>
        <tr>
          <td><c:out value="${p.maPhong}"/><input type="hidden" name="idPhong" value="${p.id}"></td>
          <td><c:out value="${p.loaiPhong.ten}"/></td>
          <td class="num">${p.loaiPhong.loaiGiuong.sucChua}</td>
          <td><input type="number" name="${kNL}" min="1" value="${empty param[kNL] ? 1 : param[kNL]}" aria-label="Người lớn phòng ${p.maPhong}"></td>
          <td><input type="number" name="${kD6}" min="0" max="2" value="${empty param[kD6] ? 0 : param[kD6]}" aria-label="Trẻ dưới 6 tuổi phòng ${p.maPhong}"></td>
          <td><input type="number" name="${kT6}" min="0" value="${empty param[kT6] ? 0 : param[kT6]}" aria-label="Trẻ từ 6 tuổi phòng ${p.maPhong}"></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
  <p class="hint">Người lớn cộng trẻ từ 6 tuổi không được vượt sức chứa của phòng. Trẻ dưới 6 tuổi không tính vào sức chứa.</p>

  <h2>Khách hàng và mã giảm giá</h2>
  <div class="panel form-stack">
    <label>Khách hàng
      <select name="idKhachHang" required>
        <option value="">Chọn khách hàng</option>
        <c:forEach var="k" items="${dsKhachHang}">
          <option value="${k.id}" ${param.idKhachHang eq k.id ? 'selected' : ''}><c:out value="${k.hoTen}"/> - <c:out value="${k.soDienThoai}"/></option>
        </c:forEach>
      </select>
    </label>
    <label>Mã giảm giá (nếu có)
      <input type="text" name="maGiamGia" value="<c:out value='${param.maGiamGia}'/>">
    </label>
  </div>

  <div class="sticky-bar">
    <a class="btn" href="${ctx}/dat-phong?action=timPhong&nhanPhong=${nhanPhong}&traPhong=${traPhong}">Chọn lại phòng</a>
    <button class="btn brass" type="submit">Xác nhận đặt phòng</button>
  </div>
</form>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
