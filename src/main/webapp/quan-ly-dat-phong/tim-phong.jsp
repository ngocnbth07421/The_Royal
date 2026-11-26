<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tieuDe" value="Tìm phòng trống"/>
<c:set var="menu" value="don"/>
<%@ include file="/layout/header.jspf" %>
<fmt:setLocale value="vi_VN"/>

<div class="page-heading">
    <div>
        <div class="eyebrow">VẬN HÀNH KHÁCH SẠN</div>
        <h1>Tìm phòng trống</h1>
        <p>Chọn khoảng ngày lưu trú để xem các phòng còn trống, sau đó chọn phòng để đặt.</p>
    </div>
    <div class="heading-actions">
        <span class="date-chip" data-today></span>
        <a class="btn btn-ghost" href="${ctx}/dat-phong"><i class='bx bx-list-ul'></i> Đơn đặt phòng</a>
    </div>
</div>

<form class="panel" method="get" action="${ctx}/dat-phong" style="margin-bottom:18px">
    <input type="hidden" name="action" value="timPhong">
    <div class="filter-bar">
        <div class="field">
            <label for="nhanPhong">Ngày nhận phòng</label>
            <input id="nhanPhong" type="date" name="nhanPhong" required value="<c:out value='${nhanPhong}'/>">
        </div>
        <div class="field">
            <label for="traPhong">Ngày trả phòng</label>
            <input id="traPhong" type="date" name="traPhong" required value="<c:out value='${traPhong}'/>">
        </div>
        <div class="field" style="flex:0 1 8rem;min-width:8rem">
            <label for="soNguoi">Số người</label>
            <input id="soNguoi" type="number" name="soNguoi" min="1" value="${empty soNguoi ? 1 : soNguoi}">
        </div>
        <div class="field">
            <label for="idHangPhong">Hạng phòng</label>
            <select id="idHangPhong" name="idHangPhong">
                <option value="">Tất cả</option>
                <c:forEach var="h" items="${dsHangPhong}">
                    <option value="${h.id}" ${idHangPhong eq h.id ? 'selected' : ''}><c:out value="${h.maHangPhong}"/></option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label for="idLoaiGiuong">Loại giường</label>
            <select id="idLoaiGiuong" name="idLoaiGiuong">
                <option value="">Tất cả</option>
                <c:forEach var="g" items="${dsLoaiGiuong}">
                    <option value="${g.id}" ${idLoaiGiuong eq g.id ? 'selected' : ''}><c:out value="${g.ten}"/> (tối đa ${g.sucChua} người)</option>
                </c:forEach>
            </select>
        </div>
        <button class="btn btn-success" type="submit"><i class='bx bx-search'></i> Tìm phòng</button>
    </div>
</form>

<c:if test="${daTim}">
    <c:choose>
        <c:when test="${empty dsPhongTrong}">
            <div class="table-panel">
                <div class="empty-box">
                    <div class="empty-icon"><i class='bx bx-hotel'></i></div>
                    <div class="empty-title">Không còn phòng trống trong khoảng ngày này</div>
                    <p class="empty-desc">Thử đổi ngày lưu trú, hạng phòng hoặc loại giường.</p>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <form method="get" action="${ctx}/dat-phong">
                <input type="hidden" name="action" value="formDat">
                <input type="hidden" name="nhanPhong" value="<c:out value='${nhanPhong}'/>">
                <input type="hidden" name="traPhong" value="<c:out value='${traPhong}'/>">

                <div class="table-panel">
                    <div class="table-scroll">
                        <table>
                            <thead><tr><th class="check"></th><th>Phòng</th><th>Hạng</th><th>Loại phòng</th><th>Giường</th><th class="num">Sức chứa</th><th class="num">Giá mỗi đêm</th></tr></thead>
                            <tbody>
                            <c:forEach var="p" items="${dsPhongTrong}">
                                <tr>
                                    <td class="check"><input type="checkbox" name="idPhong" value="${p.id}" aria-label="Chọn phòng ${p.maPhong}"></td>
                                    <td><b><c:out value="${p.maPhong}"/></b></td>
                                    <td><span class="tag-badge tag-quality"><c:out value="${p.loaiPhong.hangPhong.maHangPhong}"/></span></td>
                                    <td><c:out value="${p.loaiPhong.ten}"/></td>
                                    <td><c:out value="${p.loaiPhong.loaiGiuong.ten}"/></td>
                                    <td class="num"><span class="tag-badge tag-capacity">${p.loaiPhong.loaiGiuong.sucChua} người</span></td>
                                    <td class="num"><b><fmt:formatNumber value="${p.loaiPhong.giaMoiDem}" groupingUsed="true"/> ₫</b></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="table-footer"><span>Có ${dsPhongTrong.size()} phòng trống. Giá chưa gồm giảm giá và VAT.</span></div>
                </div>

                <div class="action-bar end">
                    <button class="btn btn-success" type="submit"><i class='bx bx-calendar-check'></i> Đặt các phòng đã chọn</button>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</c:if>

<%@ include file="/layout/footer.jspf" %>

