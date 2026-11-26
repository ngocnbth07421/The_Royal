<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Quản lý đặt phòng"/>
<c:set var="menu" value="don"/>
<%@ include file="/layout/header.jspf" %>
<fmt:setLocale value="vi_VN"/>

<div class="page-heading">
    <div>
        <div class="eyebrow">VẬN HÀNH KHÁCH SẠN</div>
        <h1>Quản lý đặt phòng</h1>
        <p>Theo dõi, tra cứu và hủy các đơn đặt phòng của khách sạn.</p>
    </div>
    <div class="heading-actions">
        <span class="date-chip" data-today></span>
        <a class="btn btn-primary" href="${ctx}/dat-phong?action=timPhong"><i class='bx bx-plus'></i> Tạo đặt phòng mới</a>
    </div>
</div>

<form class="panel" method="get" action="${ctx}/dat-phong" style="margin-bottom:18px">
    <div class="filter-bar">
        <div class="field">
            <label for="maDatPhong">Mã đơn</label>
            <input id="maDatPhong" type="text" name="maDatPhong" value="<c:out value='${maDatPhong}'/>" placeholder="DP2026001">
        </div>
        <div class="field">
            <label for="tuKhoaKhach">Khách hàng <span class="field-hint">(tên hoặc số điện thoại)</span></label>
            <input id="tuKhoaKhach" type="text" name="tuKhoaKhach" value="<c:out value='${tuKhoaKhach}'/>">
        </div>
        <div class="field">
            <label for="trangThai">Trạng thái</label>
            <select id="trangThai" name="trangThai">
                <option value="">Tất cả</option>
                <option value="CHO_COC" ${trangThai eq 'CHO_COC' ? 'selected' : ''}>Chờ cọc</option>
                <option value="DA_XAC_NHAN" ${trangThai eq 'DA_XAC_NHAN' ? 'selected' : ''}>Đã xác nhận</option>
                <option value="DANG_LUU_TRU" ${trangThai eq 'DANG_LUU_TRU' ? 'selected' : ''}>Đang lưu trú</option>
                <option value="DA_TRA_PHONG" ${trangThai eq 'DA_TRA_PHONG' ? 'selected' : ''}>Đã trả phòng</option>
                <option value="KHACH_HUY" ${trangThai eq 'KHACH_HUY' ? 'selected' : ''}>Khách hủy</option>
                <option value="KHACH_SAN_HUY" ${trangThai eq 'KHACH_SAN_HUY' ? 'selected' : ''}>Khách sạn hủy</option>
                <option value="TU_HUY" ${trangThai eq 'TU_HUY' ? 'selected' : ''}>Tự hủy</option>
            </select>
        </div>
        <div class="field">
            <label for="tuNgay">Nhận phòng từ ngày</label>
            <input id="tuNgay" type="date" name="tuNgay" value="<c:out value='${tuNgay}'/>">
        </div>
        <div class="field">
            <label for="denNgay">Đến ngày</label>
            <input id="denNgay" type="date" name="denNgay" value="<c:out value='${denNgay}'/>">
        </div>
        <button class="btn btn-success" type="submit"><i class='bx bx-search'></i> Tìm đơn</button>
        <a class="btn btn-ghost" href="${ctx}/dat-phong">Xóa bộ lọc</a>
    </div>
</form>

<div class="table-panel">
    <c:choose>
        <c:when test="${empty dsDatPhong}">
            <div class="empty-box">
                <div class="empty-icon"><i class='bx bx-calendar-x'></i></div>
                <div class="empty-title">Không có đơn đặt phòng nào khớp</div>
                <p class="empty-desc">Thử bỏ bớt điều kiện lọc, hoặc tạo một đơn đặt phòng mới.</p>
                <a class="btn btn-primary" href="${ctx}/dat-phong?action=timPhong"><i class='bx bx-plus'></i> Tạo đặt phòng mới</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-scroll">
                <table>
                    <thead><tr><th>Mã đặt</th><th>Khách hàng</th><th>Nhận phòng</th><th>Trả phòng</th><th class="num">Số đêm</th><th class="num">Tổng tiền</th><th>Trạng thái</th></tr></thead>
                    <tbody>
                    <c:forEach var="d" items="${dsDatPhong}">
                        <tr>
                            <td><a href="${ctx}/dat-phong?action=chiTiet&id=${d.id}"><b><c:out value="${d.maDatPhong}"/></b></a></td>
                            <td><c:out value="${d.khachHang.hoTen}"/></td>
                            <td>${d.thoiGianNhanPhong.toLocalDate()}</td>
                            <td>${d.thoiGianTraPhong.toLocalDate()}</td>
                            <td class="num">${d.soDem}</td>
                            <td class="num"><b><fmt:formatNumber value="${d.tongTien}" groupingUsed="true"/> ₫</b></td>
                            <td><c:set var="tt" value="${d.trangThai}"/><%@ include file="/layout/badge.jspf" %></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="table-footer"><span>Tổng cộng ${dsDatPhong.size()} đơn đặt phòng</span></div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/layout/footer.jspf" %>

