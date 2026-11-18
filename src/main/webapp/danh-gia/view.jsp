<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Quản lý đánh giá</title>
</head>
<body>
<h2>Danh Sách đánh giá</h2>
<a href="${pageContext.request.contextPath}/danh-gia/add">+ Thêm đánh giá</a>

<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>ID đặt phòng</th>
        <th>ID khách hàng</th>
        <th>Điểm</th>
        <th>Bình luận</th>
        <th>Bị ẩn</th>
        <th>Lý do ẩn</th>
        <th>Hành động</th>
    </tr>
    <c:forEach var="dg" items="${listdanh_gia}">
    <tr>
        <td>${dg.idDanhGia}</td>
        <td>
            <c:forEach var="dp" items="${listdat_phong}">
                <c:if test="${dp.idDatPhong == dg.idDatPhong}">${dp.maDatPhong}</c:if>
            </c:forEach>
        </td>
        <td>
            <c:forEach var="kh" items="${listkhach_hang}">
                <c:if test="${kh.idKhachHang == dg.idKhachHang}">${kh.email}</c:if>
            </c:forEach>
        </td>
        <td align="center">${dg.diemDanhGia} </td>
        <td>${dg.binhLuan}</td>
        <td align="center">${dg.biAn ? 'Đang ẩn' : 'Hiển thị'}</td>
        <td>${dg.lyDoAn}</td>
        <td>
            <a href="${pageContext.request.contextPath}/danh-gia/detail?id=${dg.idDanhGia}">Xem</a> |
            <a href="${pageContext.request.contextPath}/danh-gia/update?id=${dg.idDanhGia}">Sửa</a> |
            <a href="${pageContext.request.contextPath}/danh-gia/delete?id=${dg.idDanhGia}"
               onclick="return confirm('Bạn có chắc chắn muốn xóa đánh giá này?');">Xóa</a>
        </td>
    </tr>
</c:forEach>
    <c:if test="${empty listdanh_gia}">
        <tr><td colspan="8" align="center">Chưa có dữ liệu đánh giá nào.</td></tr>
    </c:if>
</table>
</body>
</html>
