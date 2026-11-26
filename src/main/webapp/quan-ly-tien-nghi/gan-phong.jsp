<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Tiện nghi của phòng"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/layout/header.jspf" %>

<div class="page-heading">
    <div>
        <div class="eyebrow">QUẢN LÝ TIỆN NGHI VÀ PHÂN LOẠI</div>
        <h1>Tiện nghi của phòng <c:out value="${phong.maPhong}"/></h1>
        <p>Tích chọn những tiện nghi được trang bị cho phòng này, rồi lưu lại.</p>
    </div>
    <div class="heading-actions">
        <a class="btn btn-ghost" href="${ctx}/tien-nghi"><i class='bx bx-arrow-back'></i> Quay lại</a>
    </div>
</div>

<form class="panel" method="post" action="${ctx}/tien-nghi">
    <input type="hidden" name="action" value="ganPhong">
    <input type="hidden" name="idPhong" value="${phong.id}">

    <c:choose>
        <c:when test="${empty dsTatCaTienNghi}">
            <div class="empty-box">
                <div class="empty-title">Chưa có tiện nghi nào để gán</div>
                <p class="empty-desc">Hãy thêm tiện nghi vào danh mục trước.</p>
                <a class="btn btn-primary" href="${ctx}/tien-nghi?action=form"><i class='bx bx-plus'></i> Thêm tiện nghi</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="amenities-setup-grid" style="max-height:none">
                <c:forEach var="t" items="${dsTatCaTienNghi}">
                    <label class="amenity-check-card">
                        <input type="checkbox" name="idTienNghi" value="${t.id}" ${idDaGan.contains(t.id) ? 'checked' : ''}>
                        <span><c:out value="${t.tenTienNghi}"/></span>
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

<%@ include file="/layout/footer.jspf" %>

