<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Đơn đặt phòng"/>
<c:set var="menu" value="don"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-head">
  <h1>Đơn đặt phòng</h1>
  <a class="btn brass" href="${ctx}/dat-phong?action=timPhong">Đặt phòng mới</a>
</div>

<form class="filters panel" method="get" action="${ctx}/dat-phong">
  <label>Mã đơn
    <input type="text" name="maDatPhong" value="<c:out value='${maDatPhong}'/>" placeholder="DP2026001">
  </label>
  <label>Khách hàng (tên hoặc số điện thoại)
    <input type="text" name="tuKhoaKhach" value="<c:out value='${tuKhoaKhach}'/>">
  </label>
  <label>Trạng thái
    <select name="trangThai">
      <option value="">Tất cả</option>
      <option value="CHO_COC" ${trangThai eq 'CHO_COC' ? 'selected' : ''}>Chờ cọc</option>
      <option value="DA_XAC_NHAN" ${trangThai eq 'DA_XAC_NHAN' ? 'selected' : ''}>Đã xác nhận</option>
      <option value="DANG_LUU_TRU" ${trangThai eq 'DANG_LUU_TRU' ? 'selected' : ''}>Đang lưu trú</option>
      <option value="DA_TRA_PHONG" ${trangThai eq 'DA_TRA_PHONG' ? 'selected' : ''}>Đã trả phòng</option>
      <option value="KHACH_HUY" ${trangThai eq 'KHACH_HUY' ? 'selected' : ''}>Khách hủy</option>
      <option value="KHACH_SAN_HUY" ${trangThai eq 'KHACH_SAN_HUY' ? 'selected' : ''}>Khách sạn hủy</option>
      <option value="TU_HUY" ${trangThai eq 'TU_HUY' ? 'selected' : ''}>Tự hủy</option>
    </select>
  </label>
  <label>Nhận phòng từ ngày
    <input type="date" name="tuNgay" value="<c:out value='${tuNgay}'/>">
  </label>
  <label>Đến ngày
    <input type="date" name="denNgay" value="<c:out value='${denNgay}'/>">
  </label>
  <button class="btn primary" type="submit">Tìm đơn</button>
  <a class="btn" href="${ctx}/dat-phong">Xóa bộ lọc</a>
</form>

<div class="table-wrap">
  <c:choose>
    <c:when test="${empty dsDatPhong}">
      <p class="empty">Không có đơn đặt phòng nào khớp với bộ lọc.</p>
    </c:when>
    <c:otherwise>
      <table>
        <thead><tr><th>Mã đơn</th><th>Khách hàng</th><th>Nhận phòng</th><th>Trả phòng</th><th class="num">Số đêm</th><th class="num">Tổng tiền</th><th>Trạng thái</th></tr></thead>
        <tbody>
        <c:forEach var="d" items="${dsDatPhong}">
          <tr>
            <td><a href="${ctx}/dat-phong?action=chiTiet&id=${d.id}"><c:out value="${d.maDatPhong}"/></a></td>
            <td><c:out value="${d.khachHang.hoTen}"/></td>
            <td>${d.thoiGianNhanPhong.toLocalDate()}</td>
            <td>${d.thoiGianTraPhong.toLocalDate()}</td>
            <td class="num">${d.soDem}</td>
            <td class="num"><fmt:formatNumber value="${d.tongTien}" groupingUsed="true"/> ₫</td>
            <td><c:set var="tt" value="${d.trangThai}"/><%@ include file="/WEB-INF/views/layout/badge.jspf" %></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
