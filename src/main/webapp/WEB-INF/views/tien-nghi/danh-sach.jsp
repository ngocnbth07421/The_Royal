<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Tiện nghi"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-head">
  <h1>Tiện nghi</h1>
  <a class="btn brass" href="${ctx}/tien-nghi?action=form">Thêm tiện nghi</a>
</div>

<form class="toolbar" method="get" action="${ctx}/tien-nghi">
  <label>Tìm tiện nghi
    <input type="search" name="tuKhoa" value="<c:out value='${tuKhoa}'/>" placeholder="Ví dụ: Wi-Fi">
  </label>
  <button class="btn" type="submit">Tìm</button>
  <c:if test="${not empty tuKhoa}"><a class="btn" href="${ctx}/tien-nghi">Xóa bộ lọc</a></c:if>
</form>

<div class="table-wrap">
  <c:choose>
    <c:when test="${empty dsTienNghi}">
      <p class="empty">Chưa có tiện nghi nào<c:if test="${not empty tuKhoa}"> khớp với “<c:out value="${tuKhoa}"/>”</c:if>. Bấm “Thêm tiện nghi” để tạo mới.</p>
    </c:when>
    <c:otherwise>
      <table>
        <thead><tr><th>Mã</th><th>Tên tiện nghi</th><th class="num">Số lượng</th><th>Thao tác</th></tr></thead>
        <tbody>
        <c:forEach var="t" items="${dsTienNghi}">
          <tr>
            <td>${t.id}</td>
            <td><c:out value="${t.tenTienNghi}"/></td>
            <td class="num">${t.soLuong}</td>
            <td class="row-actions">
              <a class="btn small" href="${ctx}/tien-nghi?action=form&id=${t.id}">Sửa</a>
              <form class="inline" method="post" action="${ctx}/tien-nghi" onsubmit="return confirm('Xóa tiện nghi này?');">
                <input type="hidden" name="action" value="xoa">
                <input type="hidden" name="id" value="${t.id}">
                <button class="btn small danger" type="submit">Xóa</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</div>

<h2>Gán tiện nghi cho phòng</h2>
<form class="toolbar" method="get" action="${ctx}/tien-nghi">
  <input type="hidden" name="action" value="phong">
  <label>Mã phòng (ID)
    <input type="number" name="idPhong" min="1" required>
  </label>
  <button class="btn primary" type="submit">Chọn tiện nghi cho phòng</button>
</form>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
