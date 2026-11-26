<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Đặt phòng"/>
<c:set var="menu" value="don"/>
<%@ include file="/layout/header.jspf" %>
<fmt:setLocale value="vi_VN"/>

<div class="page-heading">
    <div>
        <div class="eyebrow">VẬN HÀNH KHÁCH SẠN</div>
        <h1>Tạo đặt phòng mới</h1>
        <p>Nhập số người ở từng phòng và chọn khách hàng để hoàn tất đặt phòng.</p>
    </div>
    <div class="heading-actions">
        <a class="btn btn-ghost" href="${ctx}/dat-phong?action=timPhong&nhanPhong=${nhanPhong}&traPhong=${traPhong}"><i class='bx bx-arrow-back'></i> Chọn lại phòng</a>
    </div>
</div>

<form method="post" action="${ctx}/dat-phong" class="stack">
    <input type="hidden" name="action" value="dat">
    <input type="hidden" name="nhanPhong" value="<c:out value='${nhanPhong}'/>">
    <input type="hidden" name="traPhong" value="<c:out value='${traPhong}'/>">

    <div class="panel">
        <dl class="facts">
            <div><dt>Nhận phòng</dt><dd><c:out value="${nhanPhong}"/> lúc 14:00</dd></div>
            <div><dt>Trả phòng</dt><dd><c:out value="${traPhong}"/> lúc 12:00</dd></div>
            <div><dt>Số đêm</dt><dd>${soDem}</dd></div>
        </dl>
        <div class="pricing-calc-box" style="margin-top:16px">
            <div class="calc-row"><span>Tiền phòng tạm tính (${soDem} đêm)</span><span><fmt:formatNumber value="${tienPhongTamTinh}" groupingUsed="true"/> ₫</span></div>
            <div class="calc-row"><span>Giảm giá và VAT</span><span>Tính khi xác nhận đặt phòng</span></div>
        </div>
    </div>

    <div>
        <h3 class="section-title" style="margin-top:0">Số người ở từng phòng</h3>
        <div class="table-panel">
            <div class="table-scroll">
                <table>
                    <thead><tr><th>Phòng</th><th>Loại phòng</th><th class="num">Sức chứa</th><th>Người lớn</th><th>Trẻ dưới 6 tuổi</th><th>Trẻ từ 6 tuổi</th></tr></thead>
                    <tbody>
                    <c:forEach var="p" items="${dsPhongChon}">
                        <c:set var="kNL" value="soNguoiLon_${p.id}"/>
                        <c:set var="kD6" value="soTreEmDuoi6_${p.id}"/>
                        <c:set var="kT6" value="soTreEmTu6Tuoi_${p.id}"/>
                        <tr>
                            <td><b><c:out value="${p.maPhong}"/></b><input type="hidden" name="idPhong" value="${p.id}"></td>
                            <td><c:out value="${p.loaiPhong.ten}"/></td>
                            <td class="num"><span class="tag-badge tag-capacity">${p.loaiPhong.loaiGiuong.sucChua} người</span></td>
                            <td><input class="table-input" type="number" name="${kNL}" min="1" value="${empty param[kNL] ? 1 : param[kNL]}" aria-label="Người lớn phòng ${p.maPhong}"></td>
                            <td><input class="table-input" type="number" name="${kD6}" min="0" max="2" value="${empty param[kD6] ? 0 : param[kD6]}" aria-label="Trẻ dưới 6 tuổi phòng ${p.maPhong}"></td>
                            <td><input class="table-input" type="number" name="${kT6}" min="0" value="${empty param[kT6] ? 0 : param[kT6]}" aria-label="Trẻ từ 6 tuổi phòng ${p.maPhong}"></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="policy-banner">
            <div class="policy-icon"><i class='bx bx-info-circle'></i></div>
            <div class="policy-text">
                <b>Quy định sức chứa</b>
                <p>Người lớn cộng trẻ từ 6 tuổi không được vượt sức chứa của phòng. Trẻ dưới 6 tuổi không tính vào sức chứa (tối đa 2 trẻ mỗi phòng).</p>
            </div>
        </div>
    </div>

    <div class="panel">
        <div class="panel-head"><h3>Khách hàng và mã giảm giá</h3></div>
        <div class="form-grid">
            <div class="form-group">
                <label for="idKhachHang">Khách hàng</label>
                <select id="idKhachHang" name="idKhachHang" required>
                    <option value="">Chọn khách hàng</option>
                    <c:forEach var="k" items="${dsKhachHang}">
                        <option value="${k.id}" ${param.idKhachHang eq k.id ? 'selected' : ''}><c:out value="${k.hoTen}"/> — <c:out value="${k.soDienThoai}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="maGiamGia">Mã giảm giá <span class="field-hint">(nếu có)</span></label>
                <input id="maGiamGia" type="text" name="maGiamGia" value="<c:out value='${param.maGiamGia}'/>">
            </div>
        </div>
    </div>

    <div class="action-bar end">
        <a class="btn btn-ghost" href="${ctx}/dat-phong?action=timPhong&nhanPhong=${nhanPhong}&traPhong=${traPhong}">Hủy</a>
        <button class="btn btn-success" type="submit"><i class='bx bx-check'></i> Xác nhận đặt phòng</button>
    </div>
</form>

<%@ include file="/layout/footer.jspf" %>

