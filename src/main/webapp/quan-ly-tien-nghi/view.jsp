<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Tiện nghi phòng"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/layout/header.jspf" %>

<div class="page-heading">
    <div>
        <div class="eyebrow">DỊCH VỤ &amp; TÀI CHÍNH</div>
        <h1>Danh mục tiện nghi</h1>
        <p>Kho danh mục tiện nghi dùng để thiết lập cấu hình cho từng phòng.</p>
    </div>
    <div class="heading-actions">
        <span class="date-chip" data-today></span>
        <a class="btn btn-primary" href="${ctx}/tien-nghi?action=form"><i class='bx bx-plus'></i> Thêm tiện nghi</a>
    </div>
</div>

<form class="toolbar" method="get" action="${ctx}/tien-nghi">
    <div class="toolbar-left">
        <div class="search-wrap">
            <i class='bx bx-search'></i>
            <input class="search-input" type="search" name="tuKhoa" value="<c:out value='${tuKhoa}'/>" placeholder="Tìm theo tên tiện nghi...">
        </div>
        <button class="btn btn-ghost" type="submit">Tìm</button>
        <c:if test="${not empty tuKhoa}"><a class="btn btn-ghost" href="${ctx}/tien-nghi">Xóa bộ lọc</a></c:if>
    </div>
</form>

<div class="table-panel">
    <c:choose>
        <c:when test="${empty dsTienNghi}">
            <div class="empty-box">
                <div class="empty-icon"><i class='bx bx-spa'></i></div>
                <div class="empty-title">Chưa có tiện nghi nào<c:if test="${not empty tuKhoa}"> khớp với “<c:out value="${tuKhoa}"/>”</c:if></div>
                <p class="empty-desc">Thêm tiện nghi để gán cho các phòng khi thiết lập cấu hình.</p>
                <a class="btn btn-primary" href="${ctx}/tien-nghi?action=form"><i class='bx bx-plus'></i> Thêm tiện nghi</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-scroll">
                <table>
                    <thead><tr><th>Mã</th><th>Tên tiện nghi</th><th class="num">Số lượng</th><th>Thao tác</th></tr></thead>
                    <tbody>
                    <c:forEach var="t" items="${dsTienNghi}">
                        <tr>
                            <td>${t.id}</td>
                            <td><b><c:out value="${t.tenTienNghi}"/></b></td>
                            <td class="num">${t.soLuong}</td>
                            <td>
                                <div class="actions">
                                    <a class="btn btn-ghost btn-small" href="${ctx}/tien-nghi?action=form&id=${t.id}"><i class='bx bx-edit'></i> Sửa</a>
                                    <form class="inline-form" method="post" action="${ctx}/tien-nghi" onsubmit="return confirm('Xóa tiện nghi này?');">
                                        <input type="hidden" name="action" value="xoa">
                                        <input type="hidden" name="id" value="${t.id}">
                                        <button class="btn btn-danger btn-small" type="submit"><i class='bx bx-trash'></i> Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="table-footer"><span>Tổng cộng ${dsTienNghi.size()} tiện nghi</span></div>
        </c:otherwise>
    </c:choose>
</div>

<div class="panel" style="margin-top:18px">
    <div class="panel-head">
        <div>
            <h3>Gán tiện nghi cho phòng</h3>
            <div class="panel-sub">Nhập mã phòng (ID) để chọn các tiện nghi được trang bị cho phòng đó.</div>
        </div>
    </div>
    <form class="filter-bar" method="get" action="${ctx}/tien-nghi">
        <input type="hidden" name="action" value="phong">
        <div class="field">
            <label for="idPhong">Mã phòng (ID)</label>
            <input id="idPhong" type="number" name="idPhong" min="1" required>
        </div>
        <button class="btn btn-success" type="submit"><i class='bx bx-slider-alt'></i> Chọn tiện nghi cho phòng</button>
    </form>
</div>

<%@ include file="/layout/footer.jspf" %>

