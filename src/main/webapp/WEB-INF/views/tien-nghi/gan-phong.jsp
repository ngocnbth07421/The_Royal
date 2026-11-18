<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Tiện nghi của phòng"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-heading">
    <div>
        <div class="eyebrow">QUẢN LÝ TIỆN NGHI VÀ PHÂN LOẠI</div>
        <h1>Tiện nghi của phòng <c:out value="${phong.maPhong}"/></h1>
        <p>Tích chọn tiện nghi phòng này có. Tiện nghi đã hết trong kho sẽ không thể gắn thêm.</p>
    </div>
    <div class="heading-actions">
        <a class="btn btn-ghost" href="${ctx}/tien-nghi"><i class='bx bx-arrow-back'></i> Quay lại</a>
    </div>
</div>

<form class="panel" method="post" action="${ctx}/tien-nghi">
    <input type="hidden" name="action" value="ganPhong">
    <input type="hidden" name="idPhong" value="${phong.id}">

    <c:choose>
        <c:when test="${empty dsThongKe}">
            <div class="empty-box">
                <div class="empty-title">Chưa có tiện nghi nào để gán</div>
                <p class="empty-desc">Hãy thêm tiện nghi vào danh mục trước.</p>
                <a class="btn btn-primary" href="${ctx}/tien-nghi?action=form"><i class='bx bx-plus'></i> Thêm tiện nghi</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="amenities-setup-grid" style="max-height:none">
                <c:forEach var="t" items="${dsThongKe}">
                    <c:set var="dangCo" value="${idDaGan.contains(t.tienNghi.id)}"/>
                    <c:set var="khoaLai" value="${(not dangCo) and t.conLai le 0}"/>
                    <label class="amenity-check-card ${khoaLai ? 'disabled' : ''}">
                        <input type="checkbox" name="idTienNghi" value="${t.tienNghi.id}" ${dangCo ? 'checked' : ''} ${khoaLai ? 'disabled' : ''}>
                        <span class="body">
                            <span><span class="code-tag"><c:out value="${t.tienNghi.maTienNghi}"/></span> <c:out value="${t.tienNghi.tenTienNghi}"/></span>
                            <small>
                                <c:choose>
                                    <c:when test="${khoaLai}">Đã hết trong kho (${t.tong}/${t.tong})</c:when>
                                    <c:when test="${dangCo}">Phòng đang có · kho còn ${t.conLai lt 0 ? 0 : t.conLai}</c:when>
                                    <c:otherwise>Chưa có · kho còn ${t.conLai}</c:otherwise>
                                </c:choose>
                            </small>
                        </span>
                    </label>
                </c:forEach>
            </div>
            <div class="action-bar end" style="margin-top:20px">
                <a class="btn btn-ghost" href="${ctx}/tien-nghi">Hủy</a>
                <button class="btn btn-success" type="submit">Lưu tiện nghi</button>
            </div>
        </c:otherwise>
    </c:choose>
</form>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
