<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Chi tiết đánh giá</title>
</head>
<body>
<h2>Chi tiết đánh giá #${dg.idDanhGia}</h2>
<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>ID Đánh giá</th>
        <td>${dg.idDanhGia}</td>
    </tr>
    <tr>
        <th>Mã đặt phòng</th>
        <td>
            <c:forEach var="dp" items="${listdat_phong}">
                <c:if test="${dp.idDatPhong == dg.idDatPhong}">${dp.maDatPhong}</c:if>
            </c:forEach>
        </td>
    </tr>
    <tr>
        <th>Email khách hàng</th>
        <td>
            <c:forEach var="kh" items="${listkhach_hang}">
                <c:if test="${kh.idKhachHang == dg.idKhachHang}">${kh.email}</c:if>
            </c:forEach>
        </td>
    </tr>
    <tr>
        <th>Điểm đánh giá</th>
        <td>${dg.diemDanhGia} / 5</td>
    </tr>
    <tr>
        <th>Bình luận</th>
        <td>${dg.binhLuan}</td>
    </tr>
    <tr>
        <th>Trạng thái hiển thị</th>
        <td>${dg.biAn ? 'Đang ẩn' : 'Hiển thị công khai'}</td>
    </tr>
    <tr>
        <th>Lý do ẩn</th>
        <td>${empty dg.lyDoAn ? 'Không có' : dg.lyDoAn}</td>
    </tr>
</table>
<br>
<a href="${pageContext.request.contextPath}/quan-ly-danh-gia/update?id=${dg.idDanhGia}">Sửa đánh giá này</a> |
<a href="${pageContext.request.contextPath}/quan-ly-danh-gia/view">Quay lại danh sách</a>
</body>
</html>
