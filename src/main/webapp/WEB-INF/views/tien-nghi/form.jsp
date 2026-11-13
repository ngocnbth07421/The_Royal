<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="dangSua" value="${not empty tienNghi.id}"/>
<c:set var="tieuDe" value="${dangSua ? 'Sửa tiện nghi' : 'Thêm tiện nghi'}"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-head"><h1><c:out value="${tieuDe}"/></h1></div>

<form class="panel form-stack" method="post" action="${ctx}/tien-nghi">
  <input type="hidden" name="action" value="luu">
  <c:if test="${dangSua}"><input type="hidden" name="id" value="${tienNghi.id}"></c:if>

  <label>Tên tiện nghi
    <input type="text" name="tenTienNghi" maxlength="100" required autofocus value="<c:out value='${tienNghi.tenTienNghi}'/>">
  </label>
  <label>Số lượng
    <input type="number" name="soLuong" min="0" required
           value="${not empty soLuongNhap ? soLuongNhap : (empty tienNghi ? 1 : tienNghi.soLuong)}">
  </label>

  <div class="actions">
    <button class="btn primary" type="submit"><c:out value="${dangSua ? 'Lưu thay đổi' : 'Thêm tiện nghi'}"/></button>
    <a class="btn" href="${ctx}/tien-nghi">Quay lại</a>
  </div>
</form>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
