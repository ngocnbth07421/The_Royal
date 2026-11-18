<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Tiện nghi phòng"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<c:set var="tongKho" value="0"/><c:set var="tongGan" value="0"/><c:set var="soVuot" value="0"/>
<c:forEach var="t" items="${dsTienNghi}">
    <c:set var="tongKho" value="${tongKho + t.tong}"/>
    <c:set var="tongGan" value="${tongGan + t.daGan}"/>
    <c:if test="${t.conLai lt 0}"><c:set var="soVuot" value="${soVuot + 1}"/></c:if>
</c:forEach>

<div class="page-heading">
    <div>
        <div class="eyebrow">DỊCH VỤ &amp; TÀI CHÍNH</div>
        <h1>Danh mục tiện nghi</h1>
        <p>Mỗi tiện nghi có mã riêng, số lượng trong kho và danh sách phòng đang sử dụng.</p>
    </div>
    <div class="heading-actions">
        <span class="date-chip" data-today></span>
        <a class="btn btn-primary" href="${ctx}/tien-nghi?action=form"><i class='bx bx-plus'></i> Thêm tiện nghi</a>
    </div>
</div>

<div class="stats-grid">
    <div class="stat-card"><div class="stat-top"><span>Loại tiện nghi</span><div class="stat-icon"><i class='bx bx-spa'></i></div></div>
        <div class="stat-value">${dsTienNghi.size()}</div><div class="stat-foot">trong danh mục</div></div>
    <div class="stat-card"><div class="stat-top"><span>Tổng trong kho</span><div class="stat-icon"><i class='bx bx-package'></i></div></div>
        <div class="stat-value">${tongKho}</div><div class="stat-foot">đơn vị tiện nghi</div></div>
    <div class="stat-card"><div class="stat-top"><span>Đã gắn cho phòng</span><div class="stat-icon"><i class='bx bx-hotel'></i></div></div>
        <div class="stat-value">${tongGan}</div><div class="stat-foot">lượt gắn vào phòng</div></div>
    <div class="stat-card"><div class="stat-top"><span>Cần xử lý</span><div class="stat-icon"><i class='bx bx-error-circle'></i></div></div>
        <div class="stat-value">${soVuot}</div><div class="stat-foot">tiện nghi vượt số lượng kho</div></div>
</div>

<c:if test="${soVuot gt 0}">
    <div class="policy-banner" style="margin:0 0 18px">
        <div class="policy-icon"><i class='bx bx-error-circle'></i></div>
        <div class="policy-text">
            <b>Có ${soVuot} tiện nghi đang gắn cho nhiều phòng hơn số lượng trong kho</b>
            <p>Hãy bấm “Sửa” để nhập lại đúng số lượng trong kho, hoặc mở “Chi tiết” để gỡ bớt khỏi các phòng.</p>
        </div>
    </div>
</c:if>

<form class="toolbar" method="get" action="${ctx}/tien-nghi">
    <div class="toolbar-left">
        <div class="search-wrap">
            <i class='bx bx-search'></i>
            <input class="search-input" type="search" name="tuKhoa" value="<c:out value='${tuKhoa}'/>" placeholder="Tìm theo mã (TN001) hoặc tên...">
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
                    <thead><tr><th>Mã</th><th>Tên tiện nghi</th><th class="num">Trong kho</th><th class="num">Đã gắn phòng</th><th class="num">Còn lại</th><th>Tình trạng kho</th><th>Thao tác</th></tr></thead>
                    <tbody>
                    <c:forEach var="t" items="${dsTienNghi}">
                        <tr>
                            <td><span class="code-tag"><c:out value="${t.tienNghi.maTienNghi}"/></span></td>
                            <td><b><c:out value="${t.tienNghi.tenTienNghi}"/></b></td>
                            <td class="num">${t.tong}</td>
                            <td class="num">${t.daGan}</td>
                            <td class="num"><b>${t.conLai}</b></td>
                            <td>
                                <c:choose>
                                    <c:when test="${t.tinhTrang eq 'VUOT_KHO'}"><span class="status status-danger"><span class="status-dot"></span>Vượt kho (thiếu ${t.thieu})</span></c:when>
                                    <c:when test="${t.tinhTrang eq 'HET_KHO'}"><span class="status status-warning"><span class="status-dot"></span>Hết kho</span></c:when>
                                    <c:otherwise><span class="status status-success"><span class="status-dot"></span>Còn hàng</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="actions">
                                    <a class="btn btn-primary btn-small" href="${ctx}/tien-nghi?action=chiTiet&id=${t.tienNghi.id}"><i class='bx bx-show'></i> Chi tiết</a>
                                    <a class="btn btn-ghost btn-small" href="${ctx}/tien-nghi?action=form&id=${t.tienNghi.id}"><i class='bx bx-edit'></i> Sửa</a>
                                    <form class="inline-form" method="post" action="${ctx}/tien-nghi" onsubmit="return confirm('Xóa tiện nghi này?');">
                                        <input type="hidden" name="action" value="xoa">
                                        <input type="hidden" name="id" value="${t.tienNghi.id}">
                                        <button class="btn btn-danger btn-small" type="submit"><i class='bx bx-trash'></i> Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="table-footer"><span>Tổng cộng ${dsTienNghi.size()} loại tiện nghi. “Còn lại” = số lượng trong kho trừ số phòng đang có tiện nghi đó.</span></div>
        </c:otherwise>
    </c:choose>
</div>

<div class="panel" style="margin-top:18px">
    <div class="panel-head">
        <div>
            <h3>Xem và gán tiện nghi theo phòng</h3>
            <div class="panel-sub">Nhập mã phòng (ID) để xem phòng đó đang có và chưa có những tiện nghi nào.</div>
        </div>
    </div>
    <form class="filter-bar" method="get" action="${ctx}/tien-nghi">
        <input type="hidden" name="action" value="phong">
        <div class="field">
            <label for="idPhong">Mã phòng (ID)</label>
            <input id="idPhong" type="number" name="idPhong" min="1" required>
        </div>
        <button class="btn btn-success" type="submit"><i class='bx bx-slider-alt'></i> Mở phòng</button>
    </form>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
