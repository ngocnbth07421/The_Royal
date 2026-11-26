<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Đơn ${datPhong.maDatPhong}"/>
<c:set var="menu" value="don"/>
<%@ include file="/layout/header.jspf" %>
<fmt:setLocale value="vi_VN"/>

<div class="page-heading">
    <div>
        <div class="eyebrow">CHI TIẾT ĐƠN ĐẶT PHÒNG</div>
        <h1><c:out value="${datPhong.maDatPhong}"/></h1>
        <p><c:set var="tt" value="${datPhong.trangThai}"/><%@ include file="/layout/badge.jspf" %></p>
    </div>
    <div class="heading-actions">
        <a class="btn btn-ghost" href="${ctx}/dat-phong"><i class='bx bx-arrow-back'></i> Về danh sách</a>
    </div>
</div>

<div class="stack">
    <div class="panel">
        <dl class="facts">
            <div><dt>Khách hàng</dt><dd><c:out value="${datPhong.khachHang.hoTen}"/></dd></div>
            <div><dt>Số điện thoại</dt><dd><c:out value="${datPhong.khachHang.soDienThoai}"/></dd></div>
            <div><dt>Nhận phòng</dt><dd>${datPhong.thoiGianNhanPhong.toLocalDate()} lúc ${datPhong.thoiGianNhanPhong.toLocalTime()}</dd></div>
            <div><dt>Trả phòng</dt><dd>${datPhong.thoiGianTraPhong.toLocalDate()} lúc ${datPhong.thoiGianTraPhong.toLocalTime()}</dd></div>
            <div><dt>Số đêm</dt><dd>${datPhong.soDem}</dd></div>
        </dl>
    </div>

    <div>
        <h3 class="section-title" style="margin-top:0">Phòng đã đặt</h3>
        <div class="table-panel">
            <div class="table-scroll">
                <table>
                    <thead><tr><th>Phòng</th><th>Loại phòng</th><th class="num">Người lớn</th><th class="num">Trẻ dưới 6</th><th class="num">Trẻ từ 6</th><th>Trạng thái</th></tr></thead>
                    <tbody>
                    <c:forEach var="ct" items="${datPhong.chiTiet}">
                        <tr>
                            <td><b><c:out value="${ct.phong.maPhong}"/></b></td>
                            <td><c:out value="${ct.phong.loaiPhong.ten}"/></td>
                            <td class="num">${ct.soNguoiLon}</td>
                            <td class="num">${ct.soTreEmDuoi6}</td>
                            <td class="num">${ct.soTreEmTu6Tuoi}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${ct.trangThai eq 'HIEU_LUC'}"><span class="status status-success"><span class="status-dot"></span>Có hiệu lực</span></c:when>
                                    <c:when test="${ct.trangThai eq 'DANG_LUU_TRU'}"><span class="status status-occupied"><span class="status-dot"></span>Đang lưu trú</span></c:when>
                                    <c:when test="${ct.trangThai eq 'DA_HUY'}"><span class="status status-danger"><span class="status-dot"></span>Đã hủy</span></c:when>
                                    <c:otherwise><span class="status status-neutral"><span class="status-dot"></span>Đã trả phòng</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div>
        <h3 class="section-title" style="margin-top:0">Chi phí</h3>
        <div class="pricing-calc-box">
            <div class="calc-row"><span>Tiền phòng</span><span><fmt:formatNumber value="${datPhong.tienPhong}" groupingUsed="true"/> ₫</span></div>
            <div class="calc-row"><span>Giảm giá</span><span>- <fmt:formatNumber value="${datPhong.giamGia}" groupingUsed="true"/> ₫</span></div>
            <div class="calc-row"><span>Phí dịch vụ</span><span><fmt:formatNumber value="${datPhong.phiDichVu}" groupingUsed="true"/> ₫</span></div>
            <div class="calc-row"><span>Thuế VAT</span><span><fmt:formatNumber value="${datPhong.thueVat}" groupingUsed="true"/> ₫</span></div>
            <div class="calc-row total"><span>Tổng tiền</span><strong><fmt:formatNumber value="${datPhong.tongTien}" groupingUsed="true"/> ₫</strong></div>
            <div class="calc-row"><span>Tiền cọc</span><span><fmt:formatNumber value="${datPhong.tienCoc}" groupingUsed="true"/> ₫</span></div>
        </div>
    </div>

    <c:if test="${not empty lichSuHuy}">
        <div class="policy-banner">
            <div class="policy-icon"><i class='bx bx-receipt'></i></div>
            <div class="policy-text">
                <b>Thông tin hủy đơn</b>
                <p>Số tiền hoàn lại: <strong><fmt:formatNumber value="${lichSuHuy.soTienHoan}" groupingUsed="true"/> ₫</strong>.
                    <c:if test="${not empty lichSuHuy.ghiChu}"> Ghi chú: <c:out value="${lichSuHuy.ghiChu}"/></c:if></p>
            </div>
        </div>
    </c:if>

    <c:if test="${coTheHuy}">
        <form class="panel" method="post" action="${ctx}/dat-phong"
              onsubmit="return confirm('Hủy đơn này? Phòng sẽ được nhả cho khách khác đặt.');">
            <input type="hidden" name="action" value="huy">
            <input type="hidden" name="id" value="${datPhong.id}">
            <div class="panel-head">
                <div>
                    <h3>Hủy đơn đặt phòng</h3>
                    <div class="panel-sub">Số tiền hoàn được tính tự động theo thời điểm hủy và số tiền cọc đã thu.</div>
                </div>
            </div>
            <div class="form-grid">
                <div class="form-group">
                    <label for="loaiHuy">Người hủy</label>
                    <select id="loaiHuy" name="loaiHuy">
                        <option value="KHACH_HUY">Khách hủy</option>
                        <option value="KHACH_SAN_HUY">Khách sạn hủy</option>
                    </select>
                </div>
                <div class="form-group full">
                    <label for="ghiChu">Ghi chú <span class="field-hint">(tối đa 255 ký tự)</span></label>
                    <textarea id="ghiChu" name="ghiChu" maxlength="255"></textarea>
                </div>
            </div>
            <div class="action-bar end" style="margin-top:18px">
                <button class="btn btn-danger" type="submit"><i class='bx bx-x-circle'></i> Hủy đơn đặt phòng</button>
            </div>
        </form>
    </c:if>
</div>

<%@ include file="/layout/footer.jspf" %>

