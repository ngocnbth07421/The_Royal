<%--
  Created by IntelliJ IDEA.
  User: HACHI
  Date: 15/11/2026
  Time: 5:54 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Thêm đánh giá</title>
</head>
<body>
<h2>Thêm đánh giá mới</h2>

<form action="${pageContext.request.contextPath}/quan-ly-danh-gia/add" method="post">
    ID đặt phòng:
    <select name="idDatPhong" required>
        <option value="">-- Chọn mã đặt phòng --</option>
        <c:forEach var="dp" items="${listdat_phong}">
            <option value="${dp.idDatPhong}">${dp.maDatPhong}</option>
        </c:forEach>
    </select><br><br>

    ID khách hàng:
    <select name="idKhachHang" required>
        <option value="">-- Chọn email khách hàng --</option>
        <c:forEach var="kh" items="${listkhach_hang}">
            <option value="${kh.idKhachHang}">${kh.email}</option>
        </c:forEach>
    </select><br><br>
    Điểm đánh giá (1-5): <input type="number" name="diemDanhGia" min="1" max="5" required><br><br>
    Bình luận: <input type="text" name="binhLuan"><br><br>
    Ẩn đánh giá: <input type="checkbox" name="biAn"><br><br>
    Lý do ẩn: <input type="text" name="lyDoAn"><br><br>

    <button type="submit">Lưu</button>
    <a href="${pageContext.request.contextPath}/quan-ly-danh-gia/view">Hủy</a>
</form>
</body>
</html>
