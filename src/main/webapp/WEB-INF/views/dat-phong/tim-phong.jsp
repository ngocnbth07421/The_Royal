<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Tìm phòng trống"/>
<c:set var="menu" value="tim-phong"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-head"><h1>Tìm phòng trống</h1></div>

<form class="filters panel" method="get" action="${ctx}/dat-phong">
  <input type="hidden" name="action" value="timPhong">
  <label>Ngày nhận phòng
    <input type="date" name="nhanPhong" required value="<c:out value='${nhanPhong}'/>">
  </label>
  <label>Ngày trả phòng
    <input type="date" name="traPhong" required value="<c:out value='${traPhong}'/>">
  </label>
  <label>Số người
    <input type="number" name="soNguoi" min="1" value="${empty soNguoi ? 1 : soNguoi}">
  </label>
  <label>Hạng phòng
    <select name="idHangPhong">
      <option value="">Tất cả</option>
      <c:forEach var="h" items="${dsHangPhong}">
        <option value="${h.id}" ${idHangPhong eq h.id ? 'selected' : ''}><c:out value="${h.maHangPhong}"/></option>
      </c:forEach>
    </select>
  </label>
  <label>Loại giường
    <select name="idLoaiGiuong">
      <option value="">Tất cả</option>
      <c:forEach var="g" items="${dsLoaiGiuong}">
        <option value="${g.id}" ${idLoaiGiuong eq g.id ? 'selected' : ''}><c:out value="${g.ten}"/> (tối đa ${g.sucChua} người)</option>
      </c:forEach>
    </select>
  </label>
  <button class="btn primary" type="submit">Tìm phòng</button>
</form>

<c:if test="${daTim}">
  <c:choose>
    <c:when test="${empty dsPhongTrong}">
      <p class="empty panel">Không còn phòng trống trong khoảng ngày này. Thử đổi ngày, hạng phòng hoặc loại giường.</p>
    </c:when>
    <c:otherwise>
      <form method="get" action="${ctx}/dat-phong">
        <input type="hidden" name="action" value="formDat">
        <input type="hidden" name="nhanPhong" value="<c:out value='${nhanPhong}'/>">
        <input type="hidden" name="traPhong" value="<c:out value='${traPhong}'/>">

        <p class="hint">Có ${dsPhongTrong.size()} phòng trống. Tick các phòng muốn đặt.</p>
        <div class="table-wrap">
          <table>
            <thead><tr><th></th><th>Phòng</th><th>Hạng</th><th>Loại phòng</th><th>Giường</th><th class="num">Sức chứa</th><th class="num">Giá mỗi đêm</th></tr></thead>
            <tbody>
            <c:forEach var="p" items="${dsPhongTrong}">
              <tr>
                <td><input type="checkbox" name="idPhong" value="${p.id}" aria-label="Chọn phòng ${p.maPhong}"></td>
                <td><c:out value="${p.maPhong}"/></td>
                <td><c:out value="${p.loaiPhong.hangPhong.maHangPhong}"/></td>
                <td><c:out value="${p.loaiPhong.ten}"/></td>
                <td><c:out value="${p.loaiPhong.loaiGiuong.ten}"/></td>
                <td class="num">${p.loaiPhong.loaiGiuong.sucChua}</td>
                <td class="num"><fmt:formatNumber value="${p.loaiPhong.giaMoiDem}" groupingUsed="true"/> ₫</td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
        <div class="sticky-bar">
          <span class="hint">Giá trên chưa gồm giảm giá và VAT.</span>
          <button class="btn brass" type="submit">Đặt các phòng đã chọn</button>
        </div>
      </form>
    </c:otherwise>
  </c:choose>
</c:if>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
