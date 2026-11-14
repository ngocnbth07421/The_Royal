<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="dangSua" value="${not empty tienNghi.id}"/>
<c:set var="tieuDe" value="${dangSua ? 'Sửa tiện nghi' : 'Thêm tiện nghi'}"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-heading">
    <div>
        <div class="eyebrow">DỊCH VỤ &amp; TÀI CHÍNH</div>
        <h1><c:out value="${tieuDe}"/></h1>
        <p>Tên tiện nghi không được trùng với tiện nghi đã có.</p>
    </div>
    <div class="heading-actions">
        <a class="btn btn-ghost" href="${ctx}/tien-nghi"><i class='bx bx-arrow-back'></i> Quay lại</a>
    </div>
</div>

<form class="panel" method="post" action="${ctx}/tien-nghi" style="max-width:680px">
    <input type="hidden" name="action" value="luu">
    <c:if test="${dangSua}"><input type="hidden" name="id" value="${tienNghi.id}"></c:if>

    <div class="form-grid">
        <div class="form-group full">
            <label for="tenTienNghi">Tên tiện nghi <span class="field-hint">(tối đa 100 ký tự)</span></label>
            <input id="tenTienNghi" type="text" name="tenTienNghi" maxlength="100" required autofocus value="<c:out value='${tienNghi.tenTienNghi}'/>">
        </div>
        <div class="form-group">
            <label for="soLuong">Số lượng</label>
            <input id="soLuong" type="number" name="soLuong" min="0" required
                   value="${not empty soLuongNhap ? soLuongNhap : (empty tienNghi ? 1 : tienNghi.soLuong)}">
        </div>
    </div>

    <div class="action-bar end" style="margin-top:22px">
        <a class="btn btn-ghost" href="${ctx}/tien-nghi">Hủy</a>
        <button class="btn btn-success" type="submit"><c:out value="${dangSua ? 'Lưu thay đổi' : 'Thêm tiện nghi'}"/></button>
    </div>
</form>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
