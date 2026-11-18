<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tn" value="${chiTiet.thongKe}"/>
<c:set var="tieuDe" value="${tn.tienNghi.maTienNghi} - ${tn.tienNghi.tenTienNghi}"/>
<c:set var="menu" value="tien-nghi"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>

<div class="page-heading">
    <div>
        <div class="eyebrow">CHI TIẾT TIỆN NGHI · <c:out value="${tn.tienNghi.maTienNghi}"/></div>
        <h1><c:out value="${tn.tienNghi.tenTienNghi}"/></h1>
        <p>Xem phòng nào đã có và chưa có tiện nghi này. Gán hoặc gỡ sẽ tự động cập nhật số lượng còn lại trong kho.</p>
    </div>
    <div class="heading-actions">
        <a class="btn btn-ghost" href="${ctx}/tien-nghi?action=form&id=${tn.tienNghi.id}"><i class='bx bx-edit'></i> Sửa</a>
        <a class="btn btn-ghost" href="${ctx}/tien-nghi"><i class='bx bx-arrow-back'></i> Quay lại</a>
    </div>
</div>

<div class="stats-grid">
    <div class="stat-card"><div class="stat-top"><span>Tổng trong kho</span><div class="stat-icon"><i class='bx bx-package'></i></div></div>
        <div class="stat-value">${tn.tong}</div><div class="stat-foot">đơn vị</div></div>
    <div class="stat-card"><div class="stat-top"><span>Đã gắn cho phòng</span><div class="stat-icon"><i class='bx bx-check-circle'></i></div></div>
        <div class="stat-value">${tn.daGan}</div><div class="stat-foot">phòng đang có</div></div>
    <div class="stat-card"><div class="stat-top"><span>Còn lại trong kho</span><div class="stat-icon"><i class='bx bx-archive'></i></div></div>
        <div class="stat-value">${tn.conLai}</div><div class="stat-foot">có thể gắn thêm</div></div>
    <div class="stat-card"><div class="stat-top"><span>Phòng chưa có</span><div class="stat-icon"><i class='bx bx-x-circle'></i></div></div>
        <div class="stat-value">${chiTiet.tongPhongChua}</div><div class="stat-foot">trên toàn khách sạn</div></div>
</div>

<c:if test="${tn.tinhTrang eq 'VUOT_KHO'}">
    <div class="policy-banner" style="margin:0 0 18px">
        <div class="policy-icon"><i class='bx bx-error-circle'></i></div>
        <div class="policy-text"><b>Vượt số lượng kho ${tn.thieu} đơn vị</b>
            <p>Tiện nghi đang gắn cho nhiều phòng hơn số lượng trong kho. Hãy gỡ bớt khỏi phòng hoặc cập nhật lại số lượng kho.</p></div>
    </div>
</c:if>

<div class="two-col">
    <!-- ============ Phòng ĐÃ có ============ -->
    <form class="panel panel-tight" method="post" action="${ctx}/tien-nghi" onsubmit="return confirm('Gỡ tiện nghi này khỏi các phòng đã chọn?');">
        <input type="hidden" name="action" value="ganNhieuPhong">
        <input type="hidden" name="mode" value="go">
        <input type="hidden" name="idTienNghi" value="${tn.tienNghi.id}">
        <div class="panel-top">
            <h3>Phòng đã có <span class="status status-success">${chiTiet.phongCo.size()}</span></h3>
        </div>
        <div class="list-scroll">
            <c:choose>
                <c:when test="${empty chiTiet.phongCo}"><p class="empty">Chưa có phòng nào có tiện nghi này.</p></c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th class="check"><input type="checkbox" data-check-all=".chk-co" aria-label="Chọn tất cả"></th><th>Phòng</th><th>Trạng thái phòng</th></tr></thead>
                        <tbody>
                        <c:forEach var="p" items="${chiTiet.phongCo}">
                            <tr>
                                <td class="check"><input class="chk-co" type="checkbox" name="idPhong" value="${p.id}" aria-label="Chọn phòng ${p.maPhong}"></td>
                                <td><b><c:out value="${p.maPhong}"/></b></td>
                                <td><c:out value="${p.trangThai eq 'SAN_SANG' ? 'Sẵn sàng' : (p.trangThai eq 'DANG_O' ? 'Đang ở' : (p.trangThai eq 'CAN_DON' ? 'Cần dọn' : 'Bảo trì'))}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="panel-foot">
            <span>Gỡ sẽ trả lại 1 đơn vị về kho cho mỗi phòng.</span>
            <button class="btn btn-danger btn-small" type="submit" ${empty chiTiet.phongCo ? 'disabled' : ''}><i class='bx bx-minus-circle'></i> Gỡ khỏi phòng đã chọn</button>
        </div>
    </form>

    <!-- ============ Phòng CHƯA có ============ -->
    <div class="panel panel-tight">
        <div class="panel-top">
            <h3>Phòng chưa có <span class="status status-neutral">${chiTiet.tongPhongChua}</span></h3>
            <form class="filter-bar" method="get" action="${ctx}/tien-nghi" style="gap:8px">
                <input type="hidden" name="action" value="chiTiet">
                <input type="hidden" name="id" value="${tn.tienNghi.id}">
                <div class="search-wrap"><i class='bx bx-search'></i>
                    <input class="search-input" style="width:170px" type="search" name="q" value="<c:out value='${chiTiet.tuKhoa}'/>" placeholder="Mã phòng...">
                </div>
                <button class="btn btn-ghost btn-small" type="submit">Tìm</button>
            </form>
        </div>
        <form method="post" action="${ctx}/tien-nghi">
            <input type="hidden" name="action" value="ganNhieuPhong">
            <input type="hidden" name="mode" value="gan">
            <input type="hidden" name="idTienNghi" value="${tn.tienNghi.id}">
            <div class="list-scroll">
                <c:choose>
                    <c:when test="${empty chiTiet.phongChua}"><p class="empty">Không có phòng nào khớp.</p></c:when>
                    <c:otherwise>
                        <table>
                            <thead><tr><th class="check"><input type="checkbox" data-check-all=".chk-chua" aria-label="Chọn tất cả"></th><th>Phòng</th><th>Trạng thái phòng</th></tr></thead>
                            <tbody>
                            <c:forEach var="p" items="${chiTiet.phongChua}">
                                <tr>
                                    <td class="check"><input class="chk-chua" type="checkbox" name="idPhong" value="${p.id}" aria-label="Chọn phòng ${p.maPhong}"></td>
                                    <td><b><c:out value="${p.maPhong}"/></b></td>
                                    <td><c:out value="${p.trangThai eq 'SAN_SANG' ? 'Sẵn sàng' : (p.trangThai eq 'DANG_O' ? 'Đang ở' : (p.trangThai eq 'CAN_DON' ? 'Cần dọn' : 'Bảo trì'))}"/></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="panel-foot">
                <span>
                    <c:choose>
                        <c:when test="${chiTiet.biCat}">Hiển thị ${chiTiet.phongChua.size()}/${chiTiet.tongPhongChua} phòng, hãy tìm theo mã phòng để thu hẹp.</c:when>
                        <c:otherwise>Kho còn <b>${tn.conLai lt 0 ? 0 : tn.conLai}</b> đơn vị.</c:otherwise>
                    </c:choose>
                </span>
                <button class="btn btn-success btn-small" type="submit" ${(empty chiTiet.phongChua or tn.conLai le 0) ? 'disabled' : ''}><i class='bx bx-plus-circle'></i> Gán vào phòng đã chọn</button>
            </div>
        </form>
    </div>
</div>

<script>
    document.querySelectorAll('[data-check-all]').forEach(function (box) {
        box.addEventListener('change', function () {
            document.querySelectorAll(box.getAttribute('data-check-all')).forEach(function (c) { c.checked = box.checked; });
        });
    });
</script>

<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
